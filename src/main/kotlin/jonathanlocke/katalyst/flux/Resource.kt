package jonathanlocke.katalyst.flux

import jonathanlocke.katalyst.flux.hierarchy.Renamable
import jonathanlocke.katalyst.flux.io.Copyable
import jonathanlocke.katalyst.flux.io.StreamReadable
import jonathanlocke.katalyst.flux.io.StreamWritable
import jonathanlocke.katalyst.flux.io.Writable

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