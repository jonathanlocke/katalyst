package jonathanlocke.katalyst.reflection.properties.kotlin

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor.Visibility
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor.Visibility.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotations

class KotlinPropertyAccessor<Value : Any>(val property: KProperty<Value>) : PropertyAccessor<Value> {

    override val visibility: Visibility = when (property.visibility) {
        KVisibility.PUBLIC -> PUBLIC
        KVisibility.PRIVATE -> PRIVATE
        KVisibility.PROTECTED -> PROTECTED
        else -> PUBLIC
    }

    override val name = property.name

    override fun <AnnotationInstance : Annotation> annotations(type: KClass<AnnotationInstance>):
            List<AnnotationInstance> = property.findAnnotations(type)

    @Suppress("UNCHECKED_CAST")
    override fun type(): ValueType<Value> {
        val propertyType = property.returnType
        val valueClass = propertyType.classifier as KClass<Value>
        return if (propertyType.arguments.isEmpty()) {
            valueType(valueClass)
        } else {
            val parameterClass = property.returnType.arguments.first().type?.classifier as? KClass<*>
            valueType(valueClass, parameterClass!!)
        }
    }

    override fun get(instance: Any): Value? = property.getter.call(instance)
    override fun set(instance: Any, value: Value?) {
        if (property is KMutableProperty<*>) {
            property.setter.call(instance, value)
        }
    }

    companion object {
        private val inaccessibleGetters = ConcurrentHashMap<KProperty.Getter<*>, Boolean>()
    }

    override fun canGet(instance: Any): Boolean {

        // If the property isn't internal,
        if (!isInternal(property)) {

            // and we haven't already determined that the getter is inaccessible,
            if (!inaccessibleGetters.contains(property.getter)) {

                try {

                    // then try to get the value,
                    get(instance)
                    return true

                } catch (e: Exception) {

                    // and if we fail, record that the getter is inaccessible.
                    inaccessibleGetters[property.getter] = true
                }
            }
        }
        return false
    }

    private fun isInternal(property: KProperty<*>): Boolean = property.name.find { it in "$@" } != null
}