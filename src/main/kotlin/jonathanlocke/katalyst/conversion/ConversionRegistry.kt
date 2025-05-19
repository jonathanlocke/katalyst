package jonathanlocke.katalyst.conversion

import jonathanlocke.katalyst.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.conversion.converters.strings.StringConversion
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter
import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeList
import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeMultiMap
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueTypeString
import jonathanlocke.katalyst.status.StatusHandler
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
 *             text, statusHandler -> text.toByteOrNull() ?:
 *                 statusHandler.error("Invalid Byte value $text")
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
    private val from = safeMultiMap<ValueType<*>, Conversion<*, *>>(newSafeList = { safeList() })

    /** Conversions keyed by the To type of the conversion */
    private val to = safeMultiMap<ValueType<*>, Conversion<*, *>>(newSafeList = { safeList() })

    companion object {

        /** The singleton registry instance */
        @JvmStatic
        val defaultConversionRegistry = DefaultConversionRegistry()
    }

    override fun toString(): String {
        return synchronized(this) {
            "Conversions:\n\n" + from.entries().joinToString("\n") { (fromType, conversionsList) ->
                "${fromType.simpleName} => ${
                    conversionsList.joinToString(", ") { it ->
                        it.forwardConverter().to.simpleName
                    }
                }\n"
            } + to.entries().joinToString("\n") { (toType, conversionsList) ->
                "${toType.simpleName} => ${
                    conversionsList.joinToString(",") { it ->
                        it.from.simpleName
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
    fun register(fromClass: ValueType<*>, toClass: ValueType<*>, conversion: Conversion<*, *>) {
        synchronized(this) {
            from.put(fromClass, conversion)
            to.put(toClass, conversion)
        }
    }

    /**
     * True if the registry contains a conversion from the given type to another type
     */
    fun hasConversionFrom(fromType: ValueType<*>): Boolean = synchronized(this) {
        from.containsKey(fromType)
    }

    /**
     * True if the registry contains a conversion to the given type from another type
     */
    fun hasConversionTo(toType: ValueType<*>): Boolean = synchronized(this) {
        to.containsKey(toType)
    }

    /**
     * Converts the given value from the given type to the given type
     */
    fun <From : Any, To : Any> convert(
        fromType: ValueType<From>, toType: ValueType<To>, from: From?, statusHandler: StatusHandler,
    ): To? {
        val conversion = conversion(fromType, toType)
        if (conversion == null) {
            statusHandler.error("Could not find conversion from $fromType to $toType")
            return null
        }
        val result = conversion.forwardConverter().convert(from, statusHandler)
        if (result == null) {
            statusHandler.error("Could not convert ${from?.toString() ?: "null"} from $fromType to $toType")
        }
        return result
    }

    fun <Value : Any> stringConversion(valueType: ValueType<Value>): StringConversion<Value> {
        return conversion(valueTypeString, valueType) as StringConversion<Value>
    }

    /**
     * Returns any conversion for the given types, or throws an exception if no conversion exists
     */
    @Suppress("UNCHECKED_CAST")
    fun <From : Any, To : Any> conversion(
        fromType: ValueType<From>, toType: ValueType<To>,
    ): Conversion<From, To>? {
        synchronized(this) {
            val conversion = from[fromType]?.stream()?.filter { it.to.equals(toType) }?.findFirst()?.orElse(null)
            return conversion as? Conversion<From, To>
        }
    }

    /**
     * All the conversions that convert *from* the given type to another type
     */
    fun from(fromType: ValueType<*>): List<Conversion<*, *>> = synchronized(this) {
        from[fromType] ?: emptyList()
    }

    /**
     * All the conversions that convert *to* the given type to another type
     */
    fun to(toType: ValueType<*>): List<Conversion<*, *>> = synchronized(this) {
        to[toType] ?: emptyList()
    }

    /**
     * Registers all [Conversion] and [StringToValueConverter] properties of the given companion object as conversions
     * in this conversion registry
     * @param companionObject The value object having a companion object with conversion properties to register
     */
    @Suppress("UNCHECKED_CAST")
    fun registerAllConversions(companionObject: Any) {

        require(companionObject::class.java.enclosingClass?.kotlin?.companionObjectInstance == companionObject)
        { "Not a companion object: $companionObject" }

        // For each property of the companion object,
        valueType(companionObject::class).memberPropertyAccessors().forEach { property ->

            when (property.type()) {

                // register it if it is a Conversion,
                valueType(Conversion::class) -> {
                    val conversion = property.get(companionObject) as Conversion<*, *>
                    conversion.register(this)
                }

                // or if it is a StringToValueConverter,
                valueType(StringToValueConverter::class) -> {
                    val conversion = property.get(companionObject) as StringToValueConverter<*>
                    conversion.register(this)
                }
            }
        }
    }
}
