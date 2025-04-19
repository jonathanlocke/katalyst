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
package jonathanlocke.katalyst.cripsr.kivakit.filters.field

/**
 * A filter that includes all non-static fields
 *
 * @author jonathanl (shibo)
 */
open class AllFields
/**
 * Constructs a property filter that matches all fields with the given naming convention
 */
    : PropertySet(ANY_NAMING_CONVENTION) {
    /**
     * {@inheritDoc}
     */
    override fun includeAsGetter(method: Method?): Boolean {
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun includeAsSetter(method: Method?): Boolean {
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun includeField(field: Field): Boolean {
        return !field.isStatic()
    }
}
