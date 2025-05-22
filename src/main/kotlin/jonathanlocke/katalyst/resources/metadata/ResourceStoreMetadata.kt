package jonathanlocke.katalyst.resources.metadata

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes

enum class ResourceStoreMetadataValue {
    HasSize,
    HasUsableSpace,
    HasFreeSpace,
}

data class ResourceStoreMetadata(

    val values: Set<ResourceStoreMetadataValue>,
    val size: Bytes = bytes(0),
    val usable: Bytes = bytes(0),
    val free: Bytes = bytes(0),
) {
    fun has(value: ResourceStoreMetadataValue) = value in values
}
