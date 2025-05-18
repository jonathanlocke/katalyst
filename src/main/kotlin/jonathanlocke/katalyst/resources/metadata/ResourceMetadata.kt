package jonathanlocke.katalyst.resources.metadata

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import java.time.Instant
import java.time.Instant.EPOCH

enum class ResourceMetadataValue {
    HasSize,
    HasCreatedAt,
    HasLastModifiedAt,
    HasLastAccessedAt,
}

data class ResourceMetadata(
    val values: Set<ResourceMetadataValue>,
    val size: Bytes = bytes(0),
    val createdAtUtc: Instant = EPOCH,
    val lastModifiedAtUtc: Instant = EPOCH,
    val lastAccessedAtUtc: Instant = EPOCH,
) {
    fun has(value: ResourceMetadataValue) = value in values
}