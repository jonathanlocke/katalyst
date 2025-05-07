package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.Property.Visibility.PUBLIC
import jonathanlocke.katalyst.reflection.properties.PropertyPath

class PropertyWalker(val rootValue: Any) {

    companion object {

        /**
         * Filter that matches all properties
         */
        val ALL_PROPERTIES = PropertyFilter { true }

        /**
         * Filter that matches all public properties
         */
        val PUBLIC_PROPERTIES = ALL_PROPERTIES.and { it.property.visibility == PUBLIC } as PropertyFilter
    }

    internal class Walk(
        val parent: Any,
        val path: PropertyPath,
        val filter: PropertyFilter,
        val recursive: Boolean
    ) {
        fun at(at: Any, path: PropertyPath): Walk =
            Walk(at, path, filter, recursive)
    }

    /**
     * Walks the properties of the root value recursively, calling the given [PropertyVisitor] for each property.
     *
     * @param filter A filter to limit the properties that are visited
     * @param recursive Whether or not to walk the properties recursively
     * @param visitor The visitor to call for each property
     */
    fun walk(
        filter: PropertyFilter = PUBLIC_PROPERTIES, recursive: Boolean = true, visitor:
        PropertyVisitor
    ) =
        walk(Walk(rootValue, PropertyPath(valueType(rootValue::class)), filter, recursive), visitor)

    /**
     * Implementation of recursive property walking
     *
     * @param walk The state of the walk
     */
    private fun walk(walk: Walk, propertyVisitor: PropertyVisitor) {

        // For each declared property in the current value,
        val parent = walk.parent
        for (property in valueType(parent::class).declaredProperties()) {

            // if the property can be accessed,
            if (property.canGet(parent)) {

                // get the value of the property,
                val propertyValue = property.get(parent)

                // extend the property path with the property name,
                val propertyPath = walk.path + property.name

                // and if the property passes the filter,
                val propertyInformation = PropertyInformation(parent, propertyPath, property, propertyValue)
                if (walk.filter.test(propertyInformation)) {

                    // call the visitor,
                    propertyVisitor.atProperty(propertyInformation)
                    
                    // and if we are walking recursively and the property has a value,
                    if (walk.recursive && propertyValue != null) {

                        // then recurse into the value.
                        walk(walk.at(propertyValue, propertyPath), propertyVisitor)
                    }
                }
            }
        }
    }
}
