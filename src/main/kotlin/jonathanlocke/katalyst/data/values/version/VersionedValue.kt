package jonathanlocke.katalyst.data.values.version

/**
 * An value of a particular version, used in serialization
 *
 * @property value The value
 * @property version The version of the value
 *
 * @see Versioned
 */
class VersionedValue<Value>(val value: Value, override val version: Version) : Versioned
