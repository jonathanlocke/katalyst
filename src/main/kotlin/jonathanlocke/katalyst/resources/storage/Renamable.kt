package jonathanlocke.katalyst.resources.storage

import jonathanlocke.katalyst.resources.Resource

interface Renamable {
    fun rename(target: Resource)
}