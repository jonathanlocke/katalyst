package jonathanlocke.katalyst.mixins.store

import jonathanlocke.katalyst.reflection.ValueType
import java.util.*

/**
 * A key used to retrieve a value from a [MixinStore].
 *
 * @property owner The object that owns the value
 * @property type The type of the value
 */
internal class MixinReference(val owner: Any, val type: ValueType<*>) {

    override fun toString(): String = "${System.identityHashCode(owner)}:${type.simpleName}"

    override fun equals(other: Any?): Boolean =
        (other is MixinReference && this.type == other.type && this.owner === other.owner)

    override fun hashCode(): Int = Objects.hash(type, owner)
}
