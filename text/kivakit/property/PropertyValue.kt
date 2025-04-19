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
 * Yields a value for a given property
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE, testing = TESTING_NOT_NEEDED, documentation = DOCUMENTED)
interface PropertyValue {
    /**
     * Returns a value for the given property
     */
    fun propertyValue(property: Property?): Any?
}
