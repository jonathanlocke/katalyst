package jonathanlocke.katalyst.nucleus.mixins.store

import jonathanlocke.katalyst.nucleus.mixins.Mixin

/**
 * Store of values associated with mixins via [MixinReference]s.
 *
 * @see Mixin
 * @see MixinReference
 */
internal class MixinStore {

    companion object {

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
}
