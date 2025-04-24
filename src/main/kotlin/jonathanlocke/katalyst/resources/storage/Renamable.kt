package jonathanlocke.katalyst.resources.storage

interface Renamable<T> {
    fun rename(target: T)
}