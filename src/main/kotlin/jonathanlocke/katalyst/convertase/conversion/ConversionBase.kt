package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.cripsr.reflection.PropertyClass

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
    override val from: PropertyClass<From>,
    override val to: PropertyClass<To>
) :
    Conversion<From, To> {

    /**
     * Registers this conversion with the default [ConversionRegistry]
     */
    override fun register(conversionRegistry: ConversionRegistry) = conversionRegistry.register(from, to, this)
}