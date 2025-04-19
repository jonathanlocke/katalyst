/** ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////// */ //
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
/**///////////////////////////////////////////////////////////////////////////////////////////////////////////////////// */
package jonathanlocke.katalyst.cripsr.kivakit

import com.telenav.kivakit.core.collections.list.ObjectList
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 * Provides access to a field of a particular object, or of any object.
 *
 *
 * **Field Access**
 *
 *
 *  * [.get] - Reads this field's value on any object passed to the constructor
 *  * [.get] - Reads this field's value on the given object
 *  * [.makeAccessible] - Makes the field accessible even if it is private
 *  * [.object] - Sets the object to access when calling [.get] and [.set]
 *  * [.set] - Writes the given value to this field of any object passed to the constructor
 *  * [.set] - Writes the given value to this field of the given object
 *  * [.type] - Returns the type of this field
 *
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
 *  * [.isPrimitive]
 *  * [.isPrivate]
 *  * [.isProtected]
 *  * [.isPublic]
 *  * [.isStatic]
 *  * [.isSynthetic]
 *  * [.isTransient]
 *  * [.isVolatile]
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
 * @see ReflectionProblem
 */
@Suppress("unused")
class Field : Member {
    /** The underlying reflection field  */
    private val field: java.lang.reflect.Field

    /** The object where the field should be accessed  */
    private var parentObject: Any?

    /** The type where this field is defined  */
    private val parentType: Type<*>

    /**
     * Constructs a field of a particular object
     */
    protected constructor(parentObject: Any, field: java.lang.reflect.Field) {
        this.parentType = Type.Companion.type<Any>(parentObject)
        this.parentObject = parentObject
        this.field = field
    }

    /**
     * Constructs a field of any object
     */
    constructor(parentType: Type<*>, field: java.lang.reflect.Field) {
        this.parentType = parentType
        this.parentObject = null
        this.field = field
    }

    /**
     * {@inheritDoc}
     */
    override fun <T : Annotation?> annotation(annotationClass: Class<T>): T {
        return field.getAnnotation(annotationClass)
    }

    /**
     * {@inheritDoc}
     */
    override fun <T> arrayElementType(): ObjectList<Type<T>>? {
        if (field.type.isArray) {
            val list: ObjectList<Type<T>> = ObjectList<Type<T>>()
            list.add(typeForClass(field.type.componentType as Class<T>))
            return list
        }

        return null
    }

    /**
     * {@inheritDoc}
     */
    override fun equals(`object`: Any?): Boolean {
        if (`object` is Field) {
            return this.parentObject === `object`.parentObject && field == `object`.field
        }
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun <T> genericTypeParameters(): ObjectList<Type<T>> {
        if (field.genericType is ParameterizedType) {
            val list: ObjectList<Type<T>> = ObjectList<Type<T>>()
            for (at in genericType.getActualTypeArguments()) {
                if (at is Class<*>) {
                    list.add(typeForClass(at as Class<T>))
                }
            }
            return list
        }
        return list()
    }

    /**
     * Gets the value of this field the given object
     *
     * @return The value or an instance of [ReflectionProblem] if something went wrong
     */
    fun get(`object`: Any): Any {
        try {
            val problem = makeAccessible() ?: return field[ensureNotNull(`object`)]
            return problem
        } catch (e: Exception) {
            return ReflectionProblem(e, "Cannot get $this")
        }
    }

    /**
     * Gets the value of this field
     *
     * @return The value or an instance of [ReflectionProblem] if something went wrong
     */
    fun get(): Any {
        if (parentObject == null) {
            return ReflectionProblem("No object to get from")
        }
        return get(parentObject!!)
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        return hashMany(System.identityHashCode(parentObject), field)
    }

    val isPrimitive: Boolean
        /**
         * Returns true if this is a primitive field
         */
        get() = field.getType().isPrimitive()

    override val isSynthetic: Boolean
        /**
         * Returns true if this is a synthetic field
         */
        get() = field.isSynthetic()

    val isTransient: Boolean
        /**
         * Returns true if this is a transient field
         */
        get() = Modifier.isTransient(modifiers())

    val isVolatile: Boolean
        /**
         * Returns true if this is a synchronized method
         */
        get() = Modifier.isVolatile(modifiers())

    /**
     * Makes this field accessible via reflection
     *
     * @return Any [ReflectionProblem] that occurred, or null if the operation succeeded
     */
    fun makeAccessible(): ReflectionProblem? {
        try {
            field.isAccessible = true
            return null
        } catch (e: Exception) {
            return ReflectionProblem("Cannot access $this")
        }
    }

    override fun modifiers(): Int {
        return field.modifiers
    }

    /**
     * {@inheritDoc}
     */
    override fun name(): String {
        return field.name
    }

    /**
     * Sets the object for this field
     *
     * @param object The object
     */
    fun `object`(`object`: Any?) {
        this.parentObject = `object`
    }

    /**
     * {@inheritDoc}
     */
    override fun parentType(): Type<*> {
        return parentType
    }

    /**
     * Sets the value of this field on the given object
     *
     * @return The value or an instance of [ReflectionProblem] if something went wrong
     */
    fun set(`object`: Any, value: Any?): ReflectionProblem? {
        try {
            val problem = makeAccessible()
            if (problem == null) {
                field[ensureNotNull(`object`)] = value
                return null
            }
            return problem
        } catch (e: Exception) {
            return ReflectionProblem(e, "Cannot get $this")
        }
    }

    /**
     * Gets the value of this field
     *
     * @return The value or an instance of [ReflectionProblem] if something went wrong
     */
    fun set(value: Any?): Any? {
        if (parentObject == null) {
            return ReflectionProblem("No object to set to")
        }
        return set(parentObject!!, value)
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        if (parentObject == null) {
            return field.name
        }
        return simpleName(parentObject!!.javaClass) + "." + field.name + " = " + parentObject
    }

    /**
     * Returns the type of this field
     */
    override fun type(): Type<*> {
        return typeForClass(field.type)
    }
}
