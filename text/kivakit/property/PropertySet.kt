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

/**
 * Base class for property filters. Supports Java Beans and KivaKit style accessor naming conventions with the enum
 * [PropertyNamingConvention] and filtering of properties and fields with [PropertyMemberSelector]. The
 * constructor takes a style and a set of explicit includes (nothing is included by default). Static fields and
 * properties are never included. Any field or method tagged with [ExcludeProperty] will be excluded regardless of
 * the inclusions specified.
 *
 *
 * Subclasses can utilize the following tests to implement filters that don't follow the pattern implemented by this
 * base class.
 *
 *
 * **Getters and Setters**
 *
 *  * [.isGetter] - True if the method is a getter under the given naming convention
 *  * [.isSetter] - True if the method is a setter under the given naming convention
 *
 *
 *
 * **Include Tests**
 *
 *  * [.isIncluded] - True if the method is included under the given set of
 * [PropertyMemberSelector]s
 *  * [.isIncluded] - True if the field is included under the given set of
 * [PropertyMemberSelector]s
 *
 *
 *
 * **KivaKit Annotation Tests**
 *
 *  * [.isIncludedByAnnotation] - True if the method is annotated with [IncludeProperty]
 *  * [.isIncludedByAnnotation] - True if the field is annotated with [IncludeProperty]
 *  * [.isExcludedByAnnotation] - True if the method is annotated with [ExcludeProperty]
 *  * [.isExcludedByAnnotation] - True if the field is annotated with [ExcludeProperty]
 *
 *
 * @author jonathanl (shibo)
 */
open class PropertySet(
    /** The property naming convention  */
    @field:UmlAggregation private val convention: jonathanlocke.katalyst.cripsr.kivakit.property.PropertyNamingConvention,
    vararg selection: jonathanlocke.katalyst.cripsr.kivakit.property.PropertyMemberSelector?
) : jonathanlocke.katalyst.cripsr.kivakit.property.PropertyFilter {
    /** The set of selectors that describe what kind of properties to include  */
    @UmlAggregation
    private val selection: Set<jonathanlocke.katalyst.cripsr.kivakit.property.PropertyMemberSelector> =
        java.util.Set.of(*selection)

    /**
     * {@inheritDoc}
     */
    override fun equals(`object`: Any?): Boolean {
        if (`object` is PropertySet) {
            return javaClass == `object`.javaClass
        }
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    /**
     * {@inheritDoc}
     */
    override fun includeAsGetter(method: Method): Boolean {
        return isIncluded(method) && isGetter(method)
    }

    /**
     * {@inheritDoc}
     */
    override fun includeAsSetter(method: Method): Boolean {
        return isIncluded(method) && isSetter(method)
    }

    /**
     * {@inheritDoc}
     */
    override fun includeField(field: Field): Boolean {
        return isIncluded(field)
    }

    /**
     * Returns the set of selectors for this filter
     */
    fun included(): Set<jonathanlocke.katalyst.cripsr.kivakit.property.PropertyMemberSelector> {
        return selection
    }

    /**
     * {@inheritDoc}
     */
    override fun nameForField(field: Field): String {
        // If an explicit name was provided,
        val includeAnnotation: Unit =
            field.annotation(jonathanlocke.katalyst.cripsr.kivakit.property.IncludeProperty::class.java)
        if (includeAnnotation != null) {
            val name = includeAnnotation.`as`()
            if ("" != name) {
                // use that name
                return name
            }
        }
        return field.name()
    }

    /**
     * {@inheritDoc}
     */
    override fun nameForMethod(method: Method): String {
        // If an explicit name was provided,
        val includeAnnotation: Unit =
            method.annotation(jonathanlocke.katalyst.cripsr.kivakit.property.IncludeProperty::class.java)
        if (includeAnnotation != null) {
            val name = includeAnnotation.`as`()
            if ("" != name) {
                // use that name
                return name
            }
        }

        // Otherwise, use beans naming conventions, if applicable
        val name: Unit = method.name()
        if (name.startsWith("is") && name.length() >= 3 && isUpperCase(name.charAt(2))) {
            return decapitalize(name.substring(2))
        }
        if ((name.startsWith("get") || name.startsWith("set")) && name.length() >= 4) {
            return decapitalize(name.substring(3))
        }
        return name
    }

    /**
     * Returns true if the method is marked with [ExcludeProperty]
     */
    protected fun isExcludedByAnnotation(method: Method): Boolean {
        return method.annotation(jonathanlocke.katalyst.cripsr.kivakit.property.ExcludeProperty::class.java) != null
    }

    /**
     * Returns true if the field is marked with [ExcludeProperty]
     */
    protected fun isExcludedByAnnotation(field: Field): Boolean {
        return field.annotation(jonathanlocke.katalyst.cripsr.kivakit.property.ExcludeProperty::class.java) != null
    }

    /**
     * Returns true if the method is a getter in the filter's [PropertyNamingConvention]
     */
    protected fun isGetter(method: Method): Boolean {
        // If the method takes no parameters, and it's not static,
        if (method.parameterTypes().length === 0) {
            // then determine if it's a getter in the given style
            return when (convention) {
                jonathanlocke.katalyst.cripsr.kivakit.property.PropertyNamingConvention.JAVA_BEANS_NAMING -> isJavaBeansGetterMethod(
                    method
                )

                jonathanlocke.katalyst.cripsr.kivakit.property.PropertyNamingConvention.KIVAKIT_PROPERTY_NAMING -> isKivaKitGetterMethod(
                    method
                )

                jonathanlocke.katalyst.cripsr.kivakit.property.PropertyNamingConvention.ANY_NAMING_CONVENTION -> isJavaBeansGetterMethod(
                    method
                ) || isKivaKitGetterMethod(
                    method
                )

                else -> false
            }
        }
        return false
    }

    /**
     * Returns true if the field is included under the set of inclusions
     */
    protected fun isIncluded(field: Field): Boolean {
        return !field.isStatic() && !isExcludedByAnnotation(field) && isIncludedByAnnotation(field)
    }

    /**
     * Returns true if the field is included under the set of inclusions
     */
    protected fun isIncluded(method: Method): Boolean {
        return !method.isStatic() && !isExcludedByAnnotation(method) && isIncludedByAnnotation(method)
    }

    /**
     * Returns true if the method is marked with [IncludeProperty]
     */
    protected fun isIncludedByAnnotation(method: Method): Boolean {
        if (!method.isSynthetic() && !method.isStatic()) {
            if (selection.contains(ALL_FIELDS_AND_METHODS)) {
                return true
            }

            if (selection.contains(KIVAKIT_INCLUDED_FIELDS_AND_METHODS)
                && method.annotation(jonathanlocke.katalyst.cripsr.kivakit.property.IncludeProperty::class.java) != null
            ) {
                return true
            }

            if (selection.contains(PUBLIC_METHODS) && method.isPublic()) {
                return true
            }

            return selection.contains(NON_PUBLIC_METHODS)
        }
        return false
    }

    /**
     * Returns true if the field is marked with [IncludeProperty]
     */
    protected fun isIncludedByAnnotation(field: Field): Boolean {
        if (!field.isSynthetic() && !field.isStatic()) {
            if (selection.contains(ALL_FIELDS_AND_METHODS)) {
                return true
            }
            return field.annotation(jonathanlocke.katalyst.cripsr.kivakit.property.IncludeProperty::class.java) != null &&
                    selection.contains(KIVAKIT_INCLUDED_FIELDS)
        }
        return false
    }

    /**
     * Returns true if the method is a getter in the filter's [PropertyNamingConvention]
     */
    protected fun isSetter(method: Method): Boolean {
        // If the method takes one parameter, and it's not static,
        if (method.parameterTypes().length === 1) {
            return convention == KIVAKIT_PROPERTY_NAMING ||
                    method.name().startsWith("set")
        }
        return false
    }

    private fun isJavaBeansGetterMethod(method: Method): Boolean {
        val name: Unit = method.name()
        if ("getClass" != name) {
            if (name.startsWith("get") && name.matches("get[A-Z].*")
                || name.startsWith("is") && name.matches("is[A-Z].*")
            ) {
                return name.matches("is[A-Z].*")
            }
        }
        return false
    }

    private fun isKivaKitGetterMethod(method: Method): Boolean {
        return method.returnType() !== Void::class.java && method.parameterTypes().length === 0
    }
}
