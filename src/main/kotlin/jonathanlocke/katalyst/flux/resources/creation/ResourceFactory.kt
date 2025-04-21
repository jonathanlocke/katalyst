package jonathanlocke.katalyst.flux.resources.creation

import jonathanlocke.katalyst.flux.resources.streams.Resource

interface ResourceFactory {
    fun newResource(): Resource
}
