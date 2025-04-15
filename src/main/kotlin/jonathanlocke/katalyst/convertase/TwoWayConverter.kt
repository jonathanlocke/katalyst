package jonathanlocke.katalyst.convertase

/**
 * The superinterface, [Converter], converts values from type [From] to type [To] while this interface
 * adds the ability to convert in the reverse direction from type [To] to type [From].
 *
 * **Reverse Conversions**
 *
 * - [unconvert]
 *
 * @param From The original type
 * @param To The desired type
 * @see Converter
 */
interface TwoWayConverter<From : Any, To : Any> : Converter<From, To> {

    /**
     * Converts from the destination type back to the original type
     */
    fun unconvert(to: To?): From?
}
