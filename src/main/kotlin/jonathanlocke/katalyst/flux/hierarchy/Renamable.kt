package jonathanlocke.katalyst.flux.hierarchy

interface Renamable<T> {
    fun rename(target: T)
}