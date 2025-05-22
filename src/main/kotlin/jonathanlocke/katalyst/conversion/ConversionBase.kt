package jonathanlocke.katalyst.conversion

import jonathanlocke.katalyst.reflection.ValueType

/**
 * Base class for implementing a [Conversion].
 *
 *  **Methods**
 *
 * - [register] - Registers this conversion with the [ConversionRegistry]
 *
 * @param From Source type
 * @param To Destination type
 *
 * @see Conversion
 */
abstract class ConversionBase<From : Any, To : Any>(
    override val from: ValueType<From>,
    override val to: ValueType<To>,
) :
    Conversion<From, To> {

    /**
     * Registers this conversion with the default [ConversionRegistry]
     */
    override fun register(conversionRegistry: ConversionRegistry) = conversionRegistry.register(from, to, this)
}
