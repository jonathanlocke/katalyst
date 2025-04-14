package jonathanlocke.katalyst.flux

import jonathanlocke.katalyst.flux.io.StreamReadable
import jonathanlocke.katalyst.flux.io.StreamWritable
import jonathanlocke.katalyst.flux.io.Writable
import jonathanlocke.katalyst.flux.storage.Copyable
import jonathanlocke.katalyst.flux.storage.Renamable

interface Resource :
    ResourceObject,
    /*    IAccessControlled,
        ICapable<ResourceCapability>,
        IVersioned,*/
    Renamable<Resource>,
    Copyable,
    Readable,
    StreamReadable,
    StreamWritable,
    Writable