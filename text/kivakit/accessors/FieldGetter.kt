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
 * Gets the value of a field
 *
 * @param field The field to access
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE, testing = UNTESTED, documentation = DOCUMENTED)
class FieldGetter(field: Field) : Getter {
    /**
     * {@inheritDoc}
     */
    override fun <T : Annotation?> annotation(annotationType: Class<T>?): T {
        return field.annotation(annotationType)
    }

    /**
     * Returns the field to access
     */
    override fun field(): Field {
        return field
    }

    /**
     * Gets the value of this field in the given object
     *
     * @param object The object to get from
     * @return The value of this field in the given object, or an instance of [ReflectionProblem] if the operation
     * failed
     */
    override fun get(`object`: Any?): Any {
        try {
            val problem: Unit = field.makeAccessible() ?: return field.get(`object`)
            return problem
        } catch (e: Exception) {
            return ReflectionProblem(e, "Cannot get $this")
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun name(): String {
        return field.name()
    }

    override fun toString(): String {
        return "[FieldGetter name = " + name() + ", type = " + type() + "]"
    }

    /**
     * {@inheritDoc}
     */
    override fun type(): Type<*> {
        return field.parentType()
    }

    val field: Field = field
}
