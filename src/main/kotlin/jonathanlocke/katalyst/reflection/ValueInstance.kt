package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import java.io.Serializable
import kotlin.reflect.KClass

class ValueInstance<Value : Any>(
    val type: ValueType<Value>,
    val value: Value,
) : Serializable {

    companion object {
        fun <Value : Any> valueInstance(valueType: KClass<Value>, value: Value): ValueInstance<Value> {
            return valueType(valueType).valueInstance(value)
        }
    }
}