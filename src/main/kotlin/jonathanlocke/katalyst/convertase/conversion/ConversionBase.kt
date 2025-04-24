package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.cripsr.reflection.ValueClass

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
    override val from: ValueClass<From>,
    override val to: ValueClass<To>
) :
    Conversion<From, To> {

    /**
     * Registers this conversion with the default [ConversionRegistry]
     */
    override fun register(conversionRegistry: ConversionRegistry) = conversionRegistry.register(from, to, this)
}