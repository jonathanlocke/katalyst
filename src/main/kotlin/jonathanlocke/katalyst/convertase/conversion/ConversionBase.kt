package jonathanlocke.katalyst.convertase.conversion

import kotlin.reflect.KClass

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
abstract class ConversionBase<From : Any, To : Any>(override val from: KClass<From>, override val to: KClass<To>) :
    Conversion<From, To> {

    /**
     * Registers this conversion with the default [ConversionRegistry]
     */
    override fun register(conversionRegistry: ConversionRegistry) = conversionRegistry.register(from, to, this)
}