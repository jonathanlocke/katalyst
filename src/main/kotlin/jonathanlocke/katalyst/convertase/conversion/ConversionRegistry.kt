package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter
import jonathanlocke.katalyst.nucleus.language.collections.SafeDataStructure.Companion.safeMutableMultiMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import jonathanlocke.katalyst.cripsr.reflection.ValueClass
import jonathanlocke.katalyst.cripsr.reflection.ValueClass.Companion.valueClass
import jonathanlocke.katalyst.nucleus.language.collections.maps.MultiMap

/**
 * Registry of [Conversion]s.
 *
 *  **Methods**
 *
 *  - [register] - Registers a conversion with the registry
 *
 *  **Companions**
 *
 *  - [defaultConversionRegistry] - The default conversion registry populated with useful conversions
 *  - [registerAllConversions] - Registers all [Conversion] properties of the given class' companion object
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
 *             text, listener -> text.toByteOrNull() ?:
 *                 listener.error("Invalid Byte value $text")
 *         }
 *
 *     [...]
 * ```
 *
 * @see Conversion
 * @see ConversionBase
 */
open class ConversionRegistry() {

    /** Conversions keyed by the From type of the conversion */
    private val from = safeMutableMultiMap<KClass<*>, Conversion<*, *>>()

    /** Conversions keyed by the To type of the conversion */
    private val to = safeMutableMultiMap<KClass<*>, Conversion<*, *>>()

    companion object {

        /** The singleton registry instance */
        @JvmStatic
        val defaultConversionRegistry = DefaultConversionRegistry()
    }

    override fun toString(): String {
        return synchronized(this) {
            "Conversions:\n\n" + from.entries().joinToString("\n") { (fromType, conversionsList) ->
                "${fromType.simpleName.orEmpty()} => ${
                    conversionsList.joinToString(", ") { it ->
                        it.forwardConverter().to.simpleName.orEmpty()
                    }
                }\n"
            } + to.entries().joinToString("\n") { (toType, conversionsList) ->
                "${toType.simpleName} => ${
                    conversionsList.joinToString(",") { it ->
                        it.from.simpleName.orEmpty()
                    }
                }"
            }
        }
    }

    /**
     * Merges two conversion registries by combining their conversions
     */
    operator fun plus(other: ConversionRegistry): ConversionRegistry {
        val merged = ConversionRegistry()
        synchronized(this) {
            synchronized(other) {
                merged.from.putAll(this.from)
                merged.from.putAll(other.from)
                merged.from.putAll(this.to)
                merged.from.putAll(other.to)
            }
        }
        return merged
    }

    /**
     * Registers a conversion with the registry
     * @param fromClass The source type of the conversion
     * @param toClass The target type of the conversion
     * @param conversion The conversion to register
     */
    fun register(fromClass: ValueClass<*>, toClass: ValueClass<*>, conversion: Conversion<*, *>) {
        synchronized(this) {
            from.put(fromClass, conversion)
            to.put(toClass, conversion)
        }
    }

    /**
     * True if the registry contains a conversion from the given type to another type
     */
    fun hasConversionFrom(fromType: ValueClass<*>): Boolean = synchronized(this) {
        from.containsKey(fromType)
    }

    /**
     * True if the registry contains a conversion to the given type from another type
     */
    fun hasConversionTo(toType: ValueClass<*>): Boolean = synchronized(this) {
        to.containsKey(toType)
    }

    /**
     * All the conversions that convert *from* the given type to another type
     */
    fun from(fromType: ValueClass<*>): List<Conversion<*, *>> = synchronized(this) {
        from[fromType] ?: emptyList()
    }

    /**
     * All the conversions that convert *to* the given type to another type
     */
    fun to(toType: ValueClass<*>): List<Conversion<*, *>> = synchronized(this) {
        to[toType] ?: emptyList()
    }

    /**
     * Registers all [Conversion] and [StringToValueConverter] properties of the given companion object as conversions
     * in this conversion registry
     * @param companionObject The value object having a companion object with conversion properties to register
     */
    @Suppress("UNCHECKED_CAST")
    fun registerAllConversions(companionObject: Any) {

        // For each property of the companion object,
        valueClass(companionObject::class).memberProperties().forEach { property ->

            when (property.valueClass) {

                // register it if it is a Conversion,
                valueClass(Conversion::class) -> {
                    val conversion = property.get(companionObject) as Conversion<*, *>
                    conversion.register(this)
                }

                // or if it is a StringToValueConverter,
                valueClass(StringToValueConverter::class) -> {
                    val conversion = property.get(companionObject) as StringToValueConverter<*>
                    conversion.register(this)
                }
            }
        }
    }
}
