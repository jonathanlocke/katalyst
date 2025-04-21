package jonathanlocke.katalyst.sequencer.serialization

import com.telenav.kivakit.annotations.code.quality.TypeQuality
import java.io.InputStream
import java.io.OutputStream

/**
 * Reads a [SerializableObject] from a *.properties* file. Writing is not supported at this time.
 *
 * @author jonathanl (shibo)
 * @see ObjectSerializer
 *
 * @see Resource
 *
 * @see Version
 *
 * @see InstanceIdentifier
 */
@TypeQuality(stability = STABLE_EXTENSIBLE, testing = UNTESTED, documentation = DOCUMENTED)
class PropertiesObjectSerializer @JvmOverloads constructor(reporter: ProgressReporter? = nullProgressReporter()) :
    ObjectSerializer {
    /** The progress reporter to call while serializing  */
    private val reporter: ProgressReporter?

    /**
     * Creates a properties file object serializer
     *
     * @param reporter The progress reporter to update as lines are read
     */
    /**
     * Creates a properties file object serializer
     */
    init {
        this.reporter = reporter
    }

    /**
     * {@inheritDoc}
     */
    public override fun progressReporter(): ProgressReporter? {
        return reporter
    }

    /**
     * {@inheritDoc}
     */
    public override fun <T> readObject(
        input: InputStream,
        path: StringPath,
        type: Class<T?>?,
        vararg metadata: ObjectMetadata
    ): SerializableObject<T?>? {
        // Load properties from the given resource,
        var type = type
        val properties: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
            loadPropertyMap(this, input)
        try {
            // and if a type wasn't explicitly specified,
            if (type == null) {
                // then load it using the class name specified by the 'class' property.
                ensure(
                    arrayContains(metadata, METADATA_OBJECT_TYPE), "Must specify either an explicit type or " +
                            "ObjectMetadata.TYPE to read the type from the input"
                )
                var typeName: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
                    properties.get("class")
                if (typeName == null) {
                    typeName = properties.get("type")
                }
                ensureNotNull(typeName, "Cannot find 'class' or 'type' property in: $", path)
                type = ensureNotNull(classForName(typeName), "Unable to load class $, specified in $", typeName, path)
            }

            // Next, read any version
            var version: Version? = null
            if (arrayContains(metadata, METADATA_OBJECT_VERSION)) {
                version = parseVersion(this, properties.get("version"))
            }

            // get any instance identifier,
            var instance: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? = singleton()
            if (arrayContains(metadata, METADATA_OBJECT_INSTANCE)) {
                val enumName: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
                    properties.getOrDefault("instance", singleton().name())
                instance = instanceIdentifierForEnumName(this, enumName)
            }

            // convert the property map to an object,
            val `object`: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
                ObjectConverter(this, type).convert(properties)
            if (`object` != null) {
                // and return the deserialized object.
                return SerializableObject(`object`, version, instance)
            } else {
                return fail("Unable to convert properties to an object of type $: $", type, path)
            }
        } catch (e: Exception) {
            return fail(e, "Unable to load properties for type $: $", type, path)
        }
    }

    /**
     * {@inheritDoc}
     */
    public override fun <T> writeObject(
        output: OutputStream,
        path: StringPath,
        `object`: SerializableObject<T?>,
        vararg metadata: ObjectMetadata
    ) {
        unsupported()
    }
}
