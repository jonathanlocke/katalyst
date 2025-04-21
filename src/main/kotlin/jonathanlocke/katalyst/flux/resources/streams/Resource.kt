package jonathanlocke.katalyst.flux.resources.streams

import jonathanlocke.katalyst.flux.resources.creation.ResourceObject
import jonathanlocke.katalyst.flux.resources.storage.Copyable
import jonathanlocke.katalyst.flux.resources.storage.Renamable
import jonathanlocke.katalyst.flux.resources.streams.reading.Readable
import jonathanlocke.katalyst.flux.resources.streams.reading.StreamReadable
import jonathanlocke.katalyst.flux.resources.streams.writing.StreamWritable
import jonathanlocke.katalyst.flux.resources.streams.writing.Writable

interface Resource :
    ResourceObject,
    Renamable<Resource>,
    Copyable,
    Readable,
    Writable,
    StreamReadable,
    StreamWritable
