package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import java.util.function.Predicate

class PropertyWalker<Value : Any>(val rootValue: Value) {

    companion object {

        /**
         * Filter that matches all properties
         */
        val ALL_PROPERTIES = Predicate<Property<*>> { true }

        /**
         * Filter that matches all public properties
         */
        val PUBLIC_PROPERTIES = ALL_PROPERTIES.and { it.visibility == Property.Visibility.PUBLIC }
    }

    internal class Walk(
        val at: Any,
        val path: PropertyPath,
        val filter: Predicate<Property<*>>,
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
    fun walk(filter: Predicate<Property<*>> = PUBLIC_PROPERTIES, recursive: Boolean = true, visitor: PropertyVisitor) =
        walk(Walk(rootValue, PropertyPath(valueType(rootValue::class)), filter, recursive), visitor)

    /**
     * Implementation of recursive property walking
     *
     * @param walk The state of the walk
     */
    private fun walk(walk: Walk, propertyVisitor: PropertyVisitor) {

        // For each declared property in the current value,
        for (property in valueType(walk.at::class).declaredProperties()) {

            // if the property can be accessed,
            if (property.canGet(walk.at)) {

                // get the value of the property,
                val propertyValue = property.get(walk.at)

                // extend the property path with the property name,
                val propertyPath = walk.path + property.name

                // and if the property passes the filter,
                if (walk.filter.test(property)) {

                    // call the visitor.
                    propertyVisitor.atProperty(propertyPath, property, propertyValue)
                }

                // If we are walking recursively and the property has a value,
                if (walk.recursive && propertyValue != null) {

                    // then recurse into the value.
                    walk(walk.at(propertyValue, propertyPath), propertyVisitor)
                }
            }
        }
    }
}
