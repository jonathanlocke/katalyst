package jonathanlocke.katalyst.flux.resources.metadata

import jonathanlocke.katalyst.flux.resources.proximity.ResourceProximity
import jonathanlocke.katalyst.flux.resources.uri.ResourceScheme
import jonathanlocke.katalyst.nucleus.values.Bytes
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
    val deletable: Boolean
    val path: Path
    val uri: URI
    val scheme: ResourceScheme
    val proximity: ResourceProximity
}