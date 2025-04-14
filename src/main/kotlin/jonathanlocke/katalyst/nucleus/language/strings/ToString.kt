package jonathanlocke.katalyst.nucleus.language.strings

interface ToString<T> {
    fun toString(strategy: StringFormattingStrategy<T>): String
}