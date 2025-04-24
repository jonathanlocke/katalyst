package jonathanlocke.katalyst.resources.creation

import jonathanlocke.katalyst.resources.streams.Resource

interface ResourceFactory {
    fun newResource(): Resource
}
