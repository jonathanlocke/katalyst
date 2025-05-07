package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.storage.ResourceStoreNode
import jonathanlocke.katalyst.resources.streams.Streamable

interface Resource : ResourceStoreNode, Streamable
