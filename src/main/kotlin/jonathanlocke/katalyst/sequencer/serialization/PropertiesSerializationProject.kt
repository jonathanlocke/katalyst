package jonathanlocke.katalyst.sequencer.serialization

import com.telenav.kivakit.annotations.code.quality.TypeQuality

/**
 * This class defines a KivaKit [Project]. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call [Project.resolveProject], or use
 * [ProjectTrait.project].
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE, testing = UNTESTED, documentation = DOCUMENTED)
class PropertiesSerializationProject : BaseKivaKitProject() {
    /**
     * {@inheritDoc}
     */
    public override fun onInitialize() {
        // Register .properties object serializer
        require(ObjectSerializerRegistry::class.java, { ObjectSerializerRegistry() })
            .add(PROPERTIES, listenTo(PropertiesObjectSerializer()))
    }
}
