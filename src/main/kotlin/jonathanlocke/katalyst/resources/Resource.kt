package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.streaming.Streamable
import jonathanlocke.katalyst.resources.streaming.WriteMode

class Resource(location: ResourceLocation) : ResourceNode(location), Streamable {
    override fun openForReading() = store.resource(location).openForReading()
    override fun openForWriting(mode: WriteMode) = store.resource(location).openForWriting(mode)
}
