package jonathanlocke.katalyst.reflection.properties.kotlin

import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.Property.Visibility
import jonathanlocke.katalyst.reflection.properties.Property.Visibility.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility

class KotlinProperty<T : Any>(val property: KProperty<T>) : Property<T> {

    override val visibility: Visibility = when (property.visibility) {
        KVisibility.PUBLIC -> PUBLIC
        KVisibility.PRIVATE -> PRIVATE
        KVisibility.PROTECTED -> PROTECTED
        else -> PUBLIC
    }

    override val name = property.name

    @Suppress("UNCHECKED_CAST")
    override val type = valueType(property.returnType.classifier as KClass<T>)
    override fun get(instance: Any): T? = property.getter.call(instance)
    override fun set(instance: Any, value: T?) {
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