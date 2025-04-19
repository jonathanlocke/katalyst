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
package jonathanlocke.katalyst.cripsr.kivakit.property

import com.telenav.kivakit.annotations.code.quality.TypeQuality
import java.util.function.Supplier

/**
 * A property with a getter and/or setter.
 *
 *
 * **Access**
 *
 *
 *  * [.get]
 *  * [.set]
 *
 *
 *
 * **Properties**
 *
 *
 *  * [.field]
 *  * [.getter]
 *  * [.getter]
 *  * [.isOptional]
 *  * [.member]
 *  * [.method]
 *  * [.name]
 *  * [.setter]
 *  * [.setter]
 *  * [.parentType]
 *
 *
 * @author jonathanl (shibo)
 */
@Suppress("unused")
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE_EXTENSIBLE, testing = UNTESTED, documentation = DOCUMENTED)
class Property(name: String?, getter: Getter?, setter: Setter?) : Named, Comparable<Property> {
    /** The property getter  */
    private var getter: Getter?

    /** The name of the property  */
    private val name: String?

    /** Any property setter  */
    private var setter: Setter?

    /**
     * Constructs a property with the given name, getter and setter
     *
     * @param name The name of the property
     * @param getter The property getter
     * @param setter Any property setter
     */
    init {
        ensure(name != null)
        ensure(getter != null || setter != null)
        ensure(getter == null || setter == null || getter.type().equals(setter.type()))

        this.name = name
        this.getter = getter
        this.setter = setter
    }

    /**
     * {@inheritDoc}
     */
    override fun compareTo(that: Property): Int {
        return name!!.compareTo(that.name!!)
    }

    /**
     * {@inheritDoc}
     */
    override fun equals(`object`: Any?): Boolean {
        if (`object` is Property) {
            return name == `object`.name
        }
        return false
    }

    /**
     * Returns any field for this property, or null if there is none
     */
    fun field(): Field? {
        val getter: Getter = getter()
        if (getter is FieldGetter) {
            return (getter as FieldGetter).field()
        }
        val setter: Setter = setter()
        if (setter is FieldSetter) {
            return (setter as FieldSetter).field()
        }
        return null
    }

    /**
     * Retrieves this property from the given object
     *
     * @param object The object to get from
     * @return The object retrieved or a String if something went wrong
     */
    fun get(`object`: Any?): Any? {
        if (getter != null && `object` != null) {
            return getter.get(`object`)
        }
        return null
    }

    /**
     * Returns the getter for this property
     */
    fun getter(): Getter? {
        return getter
    }

    /**
     * Changes the getter for this property
     */
    fun getter(getter: Getter?) {
        this.getter = getter
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        return hashMany(name)
    }

    val isOptional: Boolean
        /**
         * Returns true if this property is optional because it was annotated with [OptionalProperty]
         */
        get() = setter.hasAnnotation(OptionalProperty::class.java)

    /**
     * Returns the field or method that is getting this property
     */
    fun member(): Member? {
        val method: Method = method() ?: return field()
        return method
    }

    /**
     * Returns the method that gets this property
     */
    fun method(): Method? {
        val getter: Getter = getter()
        if (getter is MethodGetter) {
            return (getter as MethodGetter).method()
        }
        val setter: Setter = setter()
        if (setter is MethodSetter) {
            return (setter as MethodSetter).method()
        }
        return null
    }

    /**
     * Returns the name of this property
     */
    override fun name(): String? {
        return name
    }

    /**
     * Returns the type for which this property is defined
     */
    fun parentType(): Type<*>? {
        if (getter != null) {
            return getter.type()
        }
        if (setter != null) {
            return setter.type()
        }
        return null
    }

    /**
     * Sets this property on the given object using the value supplied by the given source
     *
     * @param object The object to set the property on
     * @param source The source of the value
     * @return ReflectionProblem if anything went wrong, otherwise null
     */
    fun <T> set(`object`: Any?, source: Supplier<T>): ReflectionProblem {
        val value: T? = source.get()

        if (value == null && !isOptional) {
            return ReflectionProblem("Required property was not populated: $this")
        }

        return if (setter != null) {
            setter.set(`object`, value)
        } else {
            ReflectionProblem("Setter not found: $this")
        }
    }

    /**
     * Returns any setter for this property
     */
    fun setter(): Setter? {
        return setter
    }

    /**
     * Changes the setter for this property
     */
    fun setter(setter: Setter?) {
        this.setter = setter
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        return "[Property name = " + name() + ", type = " + parentType().simpleName() + "]"
    }
}
