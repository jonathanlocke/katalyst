package jonathanlocke.katalyst.nucleus.mixins

import jonathanlocke.katalyst.cripsr.reflection.ValueClass
import jonathanlocke.katalyst.nucleus.mixins.store.MixinReference
import jonathanlocke.katalyst.nucleus.mixins.store.MixinStore

/**
 * Interface that enables the creation of mixins (stateful traits).
 *
 * - [mixinValue] - Retrieves or creates a value associated with *this* object
 * - [mixinValueDetach] - Detaches the value associated with *this* object
 *
 * @see jonathanlocke.katalyst.nucleus.mixins.store.MixinReference
 * @see jonathanlocke.katalyst.nucleus.mixins.store.MixinStore
 */
interface Mixin<T : Any> {

    /**
     * Retrieves or creates a value associated with *this* object
     * @param type The type of the value
     * @param valueFactory A factory function to create the value if it does not exist
     * @return The value associated with *this* object
     */
    fun mixinValue(type: ValueClass<T>, valueFactory: () -> T): T {
        return MixinStore.attach(mixinStateReference(type), valueFactory)
    }

    /**
     * Detaches the value associated with *this* object
     * @param type The type of the value
     */
    fun mixinValueDetach(type: ValueClass<T>) {
        MixinStore.detach(mixinStateReference(type))
    }

    private fun mixinStateReference(type: ValueClass<T>) = MixinReference(this, type)
}