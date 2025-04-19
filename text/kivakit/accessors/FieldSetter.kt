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
package jonathanlocke.katalyst.cripsr.kivakit.accessors

import com.telenav.kivakit.annotations.code.quality.TypeQuality

/**
 * Set the value of a field
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE, testing = UNTESTED, documentation = DOCUMENTED)
class FieldSetter(field: Field) : Setter {
    /** The field to access  */
    private val field: Field = field

    /**
     * {@inheritDoc}
     */
    override fun <T : Annotation?> annotation(annotationType: Class<T>?): T {
        return field.annotation(annotationType)
    }

    fun field(): Field {
        return field
    }

    /**
     * {@inheritDoc}
     */
    override fun name(): String {
        return field.name()
    }

    /**
     * Sets the value of this field in the given object
     *
     * @param object The object to set to
     * @param value The value to set
     * @return Any reflection problem, or null if the operation succeeded
     */
    override fun set(`object`: Any?, value: Any): ReflectionProblem? {
        try {
            val problem: Unit = field.makeAccessible() ?: return field.set(`object`, value)
            return problem
        } catch (e: Exception) {
            return ReflectionProblem(e, "Cannot set $this to $value")
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        return "[FieldSetter name = " + name() + ", type = " + type() + "]"
    }

    /**
     * {@inheritDoc}
     */
    override fun type(): Type<*> {
        return field.parentType()
    }
}
