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
 * Interface to a setter, either for a field or a method.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE, testing = TESTING_NOT_NEEDED, documentation = DOCUMENTED)
interface Setter : Named {
    /**
     * Gets the annotation of the given type for this getter
     */
    fun <T : Annotation?> annotation(annotationType: Class<T>?): T

    /**
     * True if this setter has an annotation of the given type
     */
    fun hasAnnotation(annotation: Class<out Annotation>?): Boolean {
        return annotation(annotation) != null
    }

    /**
     * @param object The object whose property should be set
     * @param value The property value
     * @return [ReflectionProblem] if something went wrong, or null if the operation succeeded
     */
    fun set(`object`: Any?, value: Any): ReflectionProblem?

    /**
     * Gets the type of value that this setter sets
     */
    fun type(): Type<*>?
}
