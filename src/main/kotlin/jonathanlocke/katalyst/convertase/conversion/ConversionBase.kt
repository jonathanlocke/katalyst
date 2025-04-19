package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.conversionRegistry
import kotlin.reflect.KClass

/**
 * Base class for implementing a [Conversion].
 *
 *  - [register] - Registers this conversion with the [ConversionRegistry]]
 *
 * @param From Source type
 * @param To Destination type
 *
 * @see Conversion
 */
abstract class ConversionBase<From : Any, To : Any>(val from: KClass<From>, val to: KClass<To>) : Conversion<From, To> {

    /**
     * Registers this conversion with the [ConversionRegistry]
     */
    override fun register() = conversionRegistry.register(from, to, this)
}