package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.kotlin.KotlinValueType
import jonathanlocke.katalyst.reflection.properties.Property
import kotlin.reflect.KClass

interface ValueType<T : Any> {

    companion object {

        fun <T : Any> propertyClass(kClass: KClass<T>) = KotlinValueType(kClass)
    }

    val simpleName: String
    fun createInstance(): T
    fun superProperties(): List<Property<*>>
    fun declaredProperties(): List<Property<*>>
    fun memberProperties(): List<Property<*>>
    fun property(name: String): Property<*>?
    fun supertypes(): List<ValueType<*>>
}