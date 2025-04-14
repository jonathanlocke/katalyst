package jonathanlocke.katalyst.flux.storage

interface Renamable<T> {
    fun rename(target: T)
}