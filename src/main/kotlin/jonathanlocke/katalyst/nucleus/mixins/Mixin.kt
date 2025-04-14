package jonathanlocke.katalyst.nucleus.mixins

interface Mixin {

    fun <T : Any> mixinAttach(valueFactory: () -> T): T {
        return MixinStore.attach(mixinStateReference(), valueFactory)
    }

    fun <T : Any> mixinDetach() {
        MixinStore.detach(mixinStateReference())
    }

    private fun mixinStateReference() = MixinReference(this::class, this)
}