package jonathanlocke.katalyst.mixins.store

import jonathanlocke.katalyst.mixins.Mixin

/**
 * Store of values associated with mixins via [MixinReference]s.
 *
 * @see Mixin
 * @see MixinReference
 */
internal object MixinStore {

    private val referenceToState = HashMap<MixinReference, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> attach(reference: MixinReference, valueFactory: () -> T): T {
        synchronized(referenceToState) {
            return if (referenceToState.containsKey(reference)) {
                referenceToState[reference] as T
            } else valueFactory.invoke().also {
                referenceToState[reference] = it
            }
        }
    }

    fun detach(reference: MixinReference) {
        synchronized(referenceToState) {
            referenceToState.remove(reference)
        }
    }
}
