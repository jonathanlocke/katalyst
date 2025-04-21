package jonathanlocke.katalyst.nucleus.mixins

import kotlin.reflect.KClass

interface Mixin<T : Any> {

    fun mixinAttach(type: KClass<T>, valueFactory: () -> T): T {
        return MixinStore.attach(mixinStateReference(type), valueFactory)
    }

    fun mixinDetach(type: KClass<T>) {
        MixinStore.detach(mixinStateReference(type))
    }

    private fun mixinStateReference(type: KClass<T>) = MixinReference(this, type)
}