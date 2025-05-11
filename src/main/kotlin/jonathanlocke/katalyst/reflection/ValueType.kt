package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.kotlin.KotlinValueType
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor
import kotlin.reflect.KClass

interface ValueType<Value : Any> {

    companion object {

        fun <Value : Any> valueType(valueClass: KClass<Value>) =
            KotlinValueType(valueClass, null)

        fun <Value : Any> valueType(kClass: KClass<Value>, parameterClass: KClass<*>) =
            KotlinValueType(kClass, parameterClass)
    }

    val simpleName: String
    val qualifiedName: String
    val packageName: String
    val valueClass: KClass<Value>
    val parameterClass: KClass<*>?
    fun isInstanceOf(type: KClass<*>): Boolean
    fun createInstance(): Value
    fun superProperties(): List<PropertyAccessor<*>>
    fun declaredProperties(): List<PropertyAccessor<*>>
    fun memberProperties(): List<PropertyAccessor<*>>
    fun property(name: String): PropertyAccessor<*>?
    fun supertypes(): List<ValueType<*>>
}