package jonathanlocke.katalyst.cripsr.kivakit

import com.telenav.kivakit.annotations.code.quality.TypeQuality
import java.lang.reflect.Modifier

/**
 * Defines methods common to [Field]s and [Method]s.
 *
 *
 * **Annotations**
 *
 *
 *  * [.annotation]
 *  * [.hasAnnotation]
 *
 *
 *
 * **Modifiers**
 *
 *
 *  * [.isFinal]
 *  * [.isPrivate]
 *  * [.isProtected]
 *  * [.isPublic]
 *  * [.isStatic]
 *  * [.isSynthetic]
 *  * [.modifiers]
 *
 *
 *
 * **Properties**
 *
 *
 *  * [.arrayElementType]
 *  * [.genericTypeParameters]
 *  * [.name]
 *  * [.parentType]
 *
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE, testing = TESTING_NOT_NEEDED, documentation = DOCUMENTED)
abstract class Member {
    /**
     * Gets any annotation of the given type from this member
     */
    abstract fun <T : Annotation?> annotation(annotationClass: Class<T>): T

    /**
     * Gets this member's element type, if it is an array. The element type for arrays is the component type, and for
     * methods, it is the return type.
     *
     * @return The element type for this array, or null if this member is not an array
     */
    abstract fun <T> arrayElementType(): ObjectList<Type<T>>?

    /**
     * Gets this member's type parameters, if any. The member's type for arrays is the component type, and for methods,
     * it is the return type.
     *
     * @return The generic type parameters for this member, or null if this member is not parameterized
     */
    abstract fun <T> genericTypeParameters(): ObjectList<Type<T>>

    /**
     * Returns true if this field has an annotation of the given type
     */
    fun hasAnnotation(type: Class<out Annotation>): Boolean {
        return annotation(type) != null
    }

    val isFinal: Boolean
        /**
         * Returns true if this is a final member
         */
        get() = Modifier.isFinal(modifiers())

    val isPrivate: Boolean
        /**
         * Returns true if this is a private member
         */
        get() = Modifier.isPrivate(modifiers())

    val isProtected: Boolean
        /**
         * Returns true if this is a protected member
         */
        get() = Modifier.isProtected(modifiers())

    val isPublic: Boolean
        /**
         * Returns true if this is a public member
         */
        get() = Modifier.isPublic(modifiers())

    val isStatic: Boolean
        /**
         * Returns true if this is a static member
         */
        get() = Modifier.isStatic(modifiers())

    /**
     * Returns true if this is a synthetic method
     */
    abstract val isSynthetic: Boolean

    /**
     * Returns any modifiers for the member
     */
    abstract fun modifiers(): Int

    /**
     * Returns the name of this member
     */
    abstract fun name(): String

    /**
     * Returns the type to which this member belongs
     */
    abstract fun parentType(): Type<*>?

    /**
     * The type of this member. If the member is a method, the return type is used.
     *
     * @return The type
     */
    abstract fun type(): Type<*>?
}
