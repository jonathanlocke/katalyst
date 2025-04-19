package jonathanlocke.katalyst.nucleus.language.predicates

class Predicates {

    companion object {

        infix fun <T> ((T) -> Boolean).and(other: (T) -> Boolean): (T) -> Boolean =
            { t -> this(t) && other(t) }

        infix fun <T> ((T) -> Boolean).or(other: (T) -> Boolean): (T) -> Boolean =
            { t -> this(t) || other(t) }

        fun <T> ((T) -> Boolean).not(): (T) -> Boolean =
            { t -> !this(t) }
    }
}