package jonathanlocke.katalyst.resources.metadata

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import java.time.Instant

data class ResourceMetadata(
    val size: Bytes,
    val createdAtUtc: Instant,
    val lastModifiedAtUtc: Instant,
    val lastAccessedAtUtc: Instant,
)