package jonathanlocke.katalyst.convertase

/**
 * A conversion supplies a "to" converter and a "from" converter
 *
 * - [to]
 * - [from]
 *
 * @param From The original type
 * @param To The desired type
 * @see Converter
 */
interface Conversion<From : Any, To : Any> {

    fun to(): Converter<From, To>
    fun from(): Converter<To, From>
}
