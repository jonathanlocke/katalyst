package jonathanlocke.katalyst.nucleus.values

import jonathanlocke.katalyst.nucleus.mixins.Mixin
import kotlin.reflect.KClass

interface ValueMixin<Value : Any> : Mixin<Value> {
    fun value(type: KClass<Value>, value: Value? = null): Value = mixinAttach(type) { value!! }
}