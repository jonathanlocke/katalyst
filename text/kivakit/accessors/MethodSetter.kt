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
 * Set the value of a property by calling its setter method
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE, testing = UNTESTED, documentation = DOCUMENTED)
class MethodSetter(method: Method) : Setter {
    /** The method to call to set a value  */
    private val method: Method = method

    /**
     * {@inheritDoc}
     */
    override fun <T : Annotation?> annotation(annotationType: Class<T>?): T {
        return method.annotation(annotationType)
    }

    fun method(): Method {
        return method
    }

    /**
     * {@inheritDoc}
     */
    override fun name(): String {
        return method.name()
    }

    /**
     * {@inheritDoc}
     */
    override fun set(`object`: Any?, value: Any): ReflectionProblem? {
        try {
            val problem: Unit = method.makeAccessible()
            if (problem == null) {
                method.invoke(`object`, value)
                return null
            }
            return problem
        } catch (e: Exception) {
            return ReflectionProblem(e, "Cannot get $this")
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        return "[MethodSetter name = " + name() + ", type = " + type() + "]"
    }

    /**
     * {@inheritDoc}
     */
    override fun type(): Type<*> {
        return typeForClass(method.parameterTypes().get(0))
    }
}
