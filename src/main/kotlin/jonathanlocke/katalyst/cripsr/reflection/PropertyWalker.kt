package jonathanlocke.katalyst.cripsr.reflection

import java.util.concurrent.ConcurrentHashMap
import java.util.function.Predicate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility.PUBLIC
import kotlin.reflect.full.memberProperties

class PropertyWalker(val value: Any) {

    fun interface PropertyVisitor {
        fun atProperty(property: KProperty<*>, type: KClass<*>, path: PropertyPath, value: Any?)
    }

    fun interface PropertyFilter : Predicate<KProperty.Getter<Any?>> {
        infix fun and(other: Predicate<KProperty.Getter<Any?>>): PropertyFilter =
            PropertyFilter { this.test(it) && other.test(it) }

        infix fun or(other: Predicate<KProperty.Getter<Any?>>): PropertyFilter =
            PropertyFilter { this.test(it) || other.test(it) }
    }

    companion object {

        val ALL_PROPERTIES = PropertyFilter { true }
        val PUBLIC_PROPERTIES = ALL_PROPERTIES and PropertyFilter { it.visibility == PUBLIC }

        private val inaccessibleGetters = ConcurrentHashMap<KProperty.Getter<*>, Boolean>()
    }

    fun walk(filter: PropertyFilter = PUBLIC_PROPERTIES, recursive: Boolean = true, visitor: PropertyVisitor) =
        walk(PropertyPath(), filter, recursive, visitor)

    private fun walk(path: PropertyPath, filter: PropertyFilter, recursive: Boolean, visitor: PropertyVisitor) {
        for (property in value::class.declaredProperties()) {
            if (canAccess(value, property)) {
                val propertyValue = propertyValue(property, value)
                val propertyPath = path + property.name
                if (filter.test(property.getter)) {
                    val type = property.returnType.classifier as? KClass<*> ?: Any::class
                    visitor.atProperty(property, type, propertyPath, propertyValue)
                }
                if (recursive && propertyValue != null) {
                    PropertyWalker(propertyValue).walk(propertyPath, filter, recursive, visitor)
                }
            }
        }
    }

    fun <T : Any> KClass<T>.declaredProperties(): List<KProperty1<T, *>> {
        val superProperties =
            this.supertypes.mapNotNull { it.classifier as? KClass<*> }.flatMap { it.memberProperties }.toSet()
        return memberProperties.toList().filter { it !in superProperties }
    }

    private fun canAccess(instance: Any, property: KProperty<*>): Boolean {
        if (!isInternal(property)) {
            if (!inaccessibleGetters.contains(property.getter)) {
                try {
                    propertyValue(property, instance)
                    return true
                } catch (e: Exception) {
                    inaccessibleGetters[property.getter] = true
                }
            }
        }
        return false
    }

    private fun isInternal(property: KProperty<*>): Boolean = property.name.find { it in "$@" } != null

    private fun propertyValue(property: KProperty<*>, instance: Any): Any? = property.getter.call(instance)
}
