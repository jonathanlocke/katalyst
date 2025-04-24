package jonathanlocke.katalyst.resources.metadata

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.resources.creation.ResourceScheme
import jonathanlocke.katalyst.resources.proximity.ResourceProximity
import java.net.URI
import java.nio.file.Path
import java.time.Instant

interface ResourceMetadata {
    val filename: Filename
    val exists: Boolean
    val size: Bytes
    val createdAtUtc: Instant
    val lastModifiedAtUtc: Instant
    val lastAccessedAtUtc: Instant
    val path: Path
    val uri: URI
    val scheme: ResourceScheme
    val proximity: ResourceProximity
    val isReadable: Boolean
    val isWritable: Boolean
}