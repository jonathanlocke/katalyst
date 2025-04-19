package jonathanlocke.katalyst.flux.resources.uri

import jonathanlocke.katalyst.flux.resources.streams.Resource

interface ResourceFactory {
    fun newResource(): Resource
}
