package jonathanlocke.katalyst.flux.resources.storage

interface Renamable<T> {
    fun rename(target: T)
}