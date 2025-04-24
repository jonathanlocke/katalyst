package jonathanlocke.katalyst.resources.streams

import jonathanlocke.katalyst.resources.creation.ResourceObject
import jonathanlocke.katalyst.resources.storage.Copyable
import jonathanlocke.katalyst.resources.storage.Renamable
import jonathanlocke.katalyst.resources.streams.reading.Readable
import jonathanlocke.katalyst.resources.streams.reading.StreamReadable
import jonathanlocke.katalyst.resources.streams.writing.StreamWritable
import jonathanlocke.katalyst.resources.streams.writing.Writable

interface Resource :
    ResourceObject,
    Renamable<Resource>,
    Copyable,
    Readable,
    Writable,
    StreamReadable,
    StreamWritable
