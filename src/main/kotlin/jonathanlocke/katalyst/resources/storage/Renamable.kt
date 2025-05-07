package jonathanlocke.katalyst.resources.storage

interface Renamable<Target> {
    fun rename(target: Target)
}