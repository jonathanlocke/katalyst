package jonathanlocke.katalyst.nucleus.mixins

import java.util.*
import kotlin.reflect.KClass

internal class MixinReference(val type: KClass<*>, val owner: Any) {

    override fun toString(): String = "${System.identityHashCode(owner)}:${type.simpleName}"

    override fun equals(other: Any?): Boolean {
        if (other is MixinReference) {
            return this.type == other.type && this.owner === other.owner
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(type, owner)
}