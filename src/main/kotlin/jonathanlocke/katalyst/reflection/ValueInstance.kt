package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import java.io.Serializable
import kotlin.reflect.KClass

class ValueInstance<Value : Any>(
    private val type: ValueType<Value>,
    private val value: Value,
) : Serializable {

    companion object {
        fun <Value : Any> valueInstance(valueType: KClass<Value>, value: Value): ValueInstance<Value> {
            return valueType(valueType).valueInstance(value)
        }
    }

    fun getType(): ValueType<Value> {
        return type
    }

    fun getValue(): Value {
        return value
    }
}