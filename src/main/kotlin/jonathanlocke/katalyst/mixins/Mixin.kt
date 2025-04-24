package jonathanlocke.katalyst.mixins

import jonathanlocke.katalyst.mixins.store.MixinReference
import jonathanlocke.katalyst.mixins.store.MixinStore
import jonathanlocke.katalyst.reflection.ValueType

/**
 * Interface that enables the creation of mixins (stateful traits).
 *
 * - [mixinValue] - Retrieves or creates a value associated with *this* object
 * - [mixinValueDetach] - Detaches the value associated with *this* object
 *
 * @see MixinReference
 * @see MixinStore
 */
interface Mixin<T : Any> {

    /**
     * Retrieves or creates a value associated with *this* object
     * @param type The type of the value
     * @param valueFactory A factory function to create the value if it does not exist
     * @return The value associated with *this* object
     */
    fun mixinValue(type: ValueType<T>, valueFactory: () -> T): T {
        return MixinStore.attach(mixinStateReference(type), valueFactory)
    }

    /**
     * Detaches the value associated with *this* object
     * @param type The type of the value
     */
    fun mixinValueDetach(type: ValueType<T>) {
        MixinStore.detach(mixinStateReference(type))
    }

    private fun mixinStateReference(type: ValueType<T>) = MixinReference(this, type)
}