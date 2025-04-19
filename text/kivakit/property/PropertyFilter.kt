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

/**
 * A filter that determines how a [Property] field or method can be accessed.
 *
 * @author jonathanl (shibo)
 */
@Suppress("unused")
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE, testing = UNTESTED, documentation = DOCUMENTED)
interface PropertyFilter {
    /** True if the given method should be included as a property getter  */
    fun includeAsGetter(method: Method): Boolean

    /** True if the given method should be included as a property setter  */
    fun includeAsSetter(method: Method): Boolean

    /** True if the given field should be included  */
    fun includeField(field: Field): Boolean

    /** The name of the field  */
    fun nameForField(field: Field): String

    /** A non-camelcase name of the method  */
    fun nameForMethod(method: Method): String

    companion object {
        /**
         * Returns the specified members as a property filter, which selects only these members. Both Java Beans and KivaKit
         * style properties are included.
         */
        fun allProperties(vararg included: PropertyMemberSelector?): PropertyFilter {
            return jonathanlocke.katalyst.cripsr.kivakit.property.PropertySet(ANY_NAMING_CONVENTION, included)
        }

        /**
         * Returns the specified members as a property filter, which selects only these members. Only JavaBeans properties
         * are included.
         */
        fun beansProperties(vararg included: PropertyMemberSelector?): PropertyFilter {
            return jonathanlocke.katalyst.cripsr.kivakit.property.PropertySet(JAVA_BEANS_NAMING, included)
        }

        /**
         * Returns the specified members as a property filter, which selects only these members. Only KivaKit style
         * properties are included.
         */
        fun kivakitProperties(vararg included: PropertyMemberSelector?): PropertyFilter {
            return jonathanlocke.katalyst.cripsr.kivakit.property.PropertySet(KIVAKIT_PROPERTY_NAMING, included)
        }
    }
}
