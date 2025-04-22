package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.registerConversions
import jonathanlocke.katalyst.nucleus.language.collections.maps.MultiMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance

/**
 * Registry of [Conversion]s.
 *
 *  **Methods**
 *
 *  - [register] - Registers a conversion with the registry
 *
 *  **Companions**
 *
 *  - [defaultConversionRegistry] - The singleton registry instance
 *  - [registerConversions] - Registers all [Conversion] properties of the given class' companion object
 *                            by forcing each conversion property to be created. When the conversion is
 *                            created, it is registered with the registry.
 *
 *  **Example**
 *
 *  ```
 *  class StringToNumber {
 *
 *     init {
 *         registerConversions(this::class)
 *     }
 *
 *     companion object {
 *
 *         val byteConverter = stringToValueConverter(Byte::class) {
 *             text, reporter -> text.toByteOrNull() ?:
 *                 reporter.error("Invalid Byte value $text")
 *         }
 *
 *     [...]
 * ```
 *
 * @see Conversion
 * @see ConversionBase
 */
class ConversionRegistry {

    /** The registry of conversions keyed by the From type of the forward conversion */
    private val conversions = MultiMap<KClass<*>, Conversion<*, *>>()

    /**
     * Registers a conversion with the registry
     * @param from The source type of the conversion
     * @param to The target type of the conversion
     * @param conversion The conversion to register
     */
    fun register(from: KClass<*>, to: KClass<*>, conversion: Conversion<*, *>) {
        synchronized(conversions) {
            conversions.put(from, conversion)
            conversions.put(to, conversion)
        }
    }

    /**
     * Gets all conversions registered for the given type
     */
    operator fun get(type: KClass<*>): List<Conversion<*, *>> = synchronized(conversions) {
        conversions[type] ?: emptyList()
    }

    /**
     * Checks if conversion to the given type is possible
     */
    fun canConvertTo(type: KClass<*>): Boolean {
        synchronized(conversions) {
            return conversions[type]?.isNotEmpty() ?: false
        }
    }

    companion object {

        /** The singleton registry instance */
        @JvmStatic
        val defaultConversionRegistry = ConversionRegistry()

        /**
         * Registers all [Conversion] properties of the given class' companion object
         */
        fun registerConversions(type: KClass<*>) {

            // If there is a companion object,
            type.companionObject?.let { companion ->

                // and a companion object instance,
                type.companionObjectInstance?.let { instance ->

                    // for each member of the companion object,
                    companion.members.forEach {

                        // that is a property,
                        if (it is KProperty1<*, *>) {

                            // if it is a Conversion property,
                            if (it.returnType.classifier == Conversion::class) {

                                // register it by getting the value of the property.
                                @Suppress("UNCHECKED_CAST")
                                (it as KProperty1<Any, *>).get(instance)
                            }
                        }
                    }
                }
            }
        }
    }
}
