package jonathanlocke.katalyst.resources.metadata

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes

data class ResourceStoreMetadata(
    val size: Bytes,
    val usable: Bytes,
    val free: Bytes,
)