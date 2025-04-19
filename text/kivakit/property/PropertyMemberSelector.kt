package jonathanlocke.katalyst.cripsr.kivakit.property

import com.telenav.kivakit.annotations.code.quality.TypeQuality

/**
 * Different kinds of property methods and fields.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE_EXTENSIBLE, testing = UNTESTED, documentation = DOCUMENTED)
enum class PropertyMemberSelector {
    /** Include all fields and methods as properties  */
    ALL_FIELDS_AND_METHODS,

    /** Include properties with public getters and setters  */
    PUBLIC_METHODS,

    /** Include properties with non-public getters and setters  */
    NON_PUBLIC_METHODS,

    /** Include fields and methods marked with [IncludeProperty]  */
    KIVAKIT_INCLUDED_FIELDS_AND_METHODS,

    /** Include fields marked with [IncludeProperty]  */
    KIVAKIT_INCLUDED_FIELDS,

    /** Include fields marked with @KivaKitPropertyConverter  */
    KIVAKIT_CONVERTED_MEMBERS
}
