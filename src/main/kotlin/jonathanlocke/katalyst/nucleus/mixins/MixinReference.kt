package jonathanlocke.katalyst.nucleus.mixins

import java.util.*
import kotlin.reflect.KClass

internal class MixinReference(val owner: Any, val type: KClass<*>) {

    override fun toString(): String = "${System.identityHashCode(owner)}:${type.simpleName}"

    override fun equals(other: Any?): Boolean =
        (other is MixinReference && this.type == other.type && this.owner === other.owner)

    override fun hashCode(): Int = Objects.hash(type, owner)
}
