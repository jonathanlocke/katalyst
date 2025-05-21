package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.properties.PropertyAccessor
import jonathanlocke.katalyst.reflection.properties.PropertyPath
import jonathanlocke.katalyst.reflection.types.KotlinValueType
import kotlin.reflect.KClass

interface ValueType<Value : Any> {

    companion object {

        val valueTypeString = valueType(String::class)

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
    fun rootPropertyPath() = PropertyPath(this)
    fun valueInstance(value: Value): ValueInstance<Value> = ValueInstance(this, value)
    fun isInstanceOf(type: KClass<*>): Boolean
    fun createInstance(): Value
    fun superPropertyAccessors(): List<PropertyAccessor<*>>
    fun declaredPropertyAccessors(): List<PropertyAccessor<*>>
    fun memberPropertyAccessors(): List<PropertyAccessor<*>>
    fun property(name: String): PropertyAccessor<*>?
    fun supertypes(): List<ValueType<*>>
}