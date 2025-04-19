package jonathanlocke.katalyst.flux.resources.streams

import jonathanlocke.katalyst.flux.resources.storage.Copyable
import jonathanlocke.katalyst.flux.resources.streams.io.StreamReadable
import jonathanlocke.katalyst.flux.resources.streams.io.StreamWritable
import jonathanlocke.katalyst.flux.resources.streams.io.Writable
import jonathanlocke.katalyst.flux.resources.uri.ResourceObject


interface Resource :
    ResourceObject,
    /*    IAccessControlled,
        ICapable<ResourceCapability>,
        IVersioned,*/
    jonathanlocke.katalyst.flux.resources.storage.Renamable<Resource>,
    Copyable,
    Readable,
    StreamReadable,
    StreamWritable,
    Writable