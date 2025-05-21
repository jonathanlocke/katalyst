package jonathanlocke.katalyst.reflection.properties.accessors

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor.Visibility
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor.Visibility.*
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility.*
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.jvm.javaField

open class KotlinPropertyAccessor<Value : Any>(
    val property: KProperty<Value>,
) : PropertyAccessor<Value> {

    override val visibility: Visibility = when (property.visibility) {
        PUBLIC -> Public
        PRIVATE -> Private
        PROTECTED -> Protected
        else -> Public
    }

    override val name = property.name

    override fun <AnnotationInstance : Annotation> annotations(type: KClass<AnnotationInstance>) =
        property.findAnnotations(type)

    override fun <AnnotationInstance : Annotation> annotation(type: KClass<AnnotationInstance>) =
        annotations(type).firstOrNull()

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

    override fun isInstance(): Boolean {
        val declaringClass = property.javaField?.declaringClass
        val isStatic = Modifier.isStatic(property.javaField?.modifiers ?: 0)
        return !(isStatic || declaringClass?.enclosingClass == null)
    }

    override fun get(instance: Any): Value? =
        tryValue("Cannot get property: $this") { property.getter.call(instance) }

    override fun set(instance: Any, value: Value?): Unit =
        tryUnit("Cannot set property: $this = $value") {
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