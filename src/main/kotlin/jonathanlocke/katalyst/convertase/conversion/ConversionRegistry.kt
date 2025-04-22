package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter
import jonathanlocke.katalyst.nucleus.language.collections.maps.MultiMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

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
open class ConversionRegistry {

    /** Conversions keyed by the From type of the conversion */
    private val from = MultiMap<KClass<*>, Conversion<*, *>>()

    /** Conversions keyed by the To type of the conversion */
    private val to = MultiMap<KClass<*>, Conversion<*, *>>()

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
    fun register(fromClass: KClass<*>, toClass: KClass<*>, conversion: Conversion<*, *>) {
        synchronized(this) {
            from.put(fromClass, conversion)
            to.put(toClass, conversion)
        }
    }

    /**
     * True if the registry contains a conversion from the given type to another type
     */
    fun hasConversionFrom(fromType: KClass<*>): Boolean = synchronized(this) {
        from.containsKey(fromType)
    }

    /**
     * True if the registry contains a conversion to the given type from another type
     */
    fun hasConversionTo(toType: KClass<*>): Boolean = synchronized(this) {
        to.containsKey(toType)
    }

    /**
     * All the conversions that convert *from* the given type to another type
     */
    fun from(fromType: KClass<*>): List<Conversion<*, *>> = synchronized(this) {
        from[fromType] ?: emptyList()
    }

    /**
     * All the conversions that convert *to* the given type to another type
     */
    fun to(toType: KClass<*>): List<Conversion<*, *>> = synchronized(this) {
        to[toType] ?: emptyList()
    }

    /**
     * Registers all [Conversion] properties of the companion object
     * @param companionType The companion object type containing the conversions to register
     */
    @Suppress("UNCHECKED_CAST")
    fun registerAllConversions(companionType: KClass<*>) {

        // Make sure that the argument is a companion type,
        require(companionType.isCompanion) { "Type must be a companion object class, was: ${companionType.qualifiedName}" }

        // For each member of the companion object,
        companionType.members.forEach {

            // that is a property,
            if (it is KProperty1<*, *>) {

                when {

                    // register it if it is a Conversion,
                    it.returnType.classifier == Conversion::class -> {
                        val conversion = (it as KProperty1<Any, *>).get(it) as Conversion<*, *>
                        conversion.register(this)
                    }

                    // or if it is a StringToValueConverter,
                    it.returnType.classifier == StringToValueConverter::class -> {
                        val companion = companionType.objectInstance
                        if (companion != null) {
                            val converter = (it as KProperty1<Any, *>).get(companion) as? StringToValueConverter<*>
                            converter?.register(this)
                        }
                    }
                }
            }
        }
    }
}
