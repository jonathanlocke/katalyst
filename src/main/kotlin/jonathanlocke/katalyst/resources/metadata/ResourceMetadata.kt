package jonathanlocke.katalyst.resources.metadata

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import java.time.Instant

interface ResourceMetadata {
    val size: Bytes
    val createdAtUtc: Instant
    val lastModifiedAtUtc: Instant
    val lastAccessedAtUtc: Instant
}