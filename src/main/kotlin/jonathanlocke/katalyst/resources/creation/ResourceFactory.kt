package jonathanlocke.katalyst.resources.creation

import jonathanlocke.katalyst.resources.Resource

interface ResourceFactory {
    fun newResource(): Resource
}
