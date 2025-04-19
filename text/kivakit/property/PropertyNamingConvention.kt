package jonathanlocke.katalyst.cripsr.kivakit.property

import com.telenav.kivakit.annotations.code.quality.TypeQuality

/**
 * The naming convention for getters and setters
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE_EXTENSIBLE, testing = TESTING_NOT_NEEDED, documentation = DOCUMENTED)
enum class PropertyNamingConvention {
    /** The x() and x(Value) naming convention used in KivaKit  */
    KIVAKIT_PROPERTY_NAMING,

    /** The getX() and setX(Value) naming convention that is used in Java Beans  */
    JAVA_BEANS_NAMING,

    /** Both KivaKit and Java Beans naming conventions are acceptable  */
    ANY_NAMING_CONVENTION
}
