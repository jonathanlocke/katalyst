package jonathanlocke.katalyst.flux.resources.streams.io

interface Readable {
    fun reader(): ResourceReader
}