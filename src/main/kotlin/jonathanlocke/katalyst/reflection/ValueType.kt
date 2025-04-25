package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.kotlin.KotlinValueType
import jonathanlocke.katalyst.reflection.properties.Property
import kotlin.reflect.KClass

interface ValueType<Value : Any> {

    companion object {

        fun <Value : Any> valueType(kClass: KClass<Value>) = KotlinValueType(kClass)
    }

    val simpleName: String
    fun createInstance(): Value
    fun superProperties(): List<Property<*>>
    fun declaredProperties(): List<Property<*>>
    fun memberProperties(): List<Property<*>>
    fun property(name: String): Property<*>?
    fun supertypes(): List<ValueType<*>>
}