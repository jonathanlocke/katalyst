package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeList
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor.Visibility.PUBLIC
import jonathanlocke.katalyst.reflection.properties.PropertyPath
import jonathanlocke.katalyst.reflection.properties.walker.PropertyWalker.WalkFlag.RECURSE_BREADTH_FIRST
import jonathanlocke.katalyst.reflection.properties.walker.PropertyWalker.WalkFlag.RECURSE_DEPTH_FIRST
import jonathanlocke.katalyst.reflection.properties.walker.comparators.SortByName

class PropertyWalker(val rootValue: Any) {

    enum class WalkFlag { RECURSE_DEPTH_FIRST, RECURSE_BREADTH_FIRST }

    companion object {

        /**
         * Filter that matches all properties
         */
        val ALL_PROPERTIES = PropertyFilter { true }

        /**
         * Filter that matches all public properties
         */
        val PUBLIC_PROPERTIES = ALL_PROPERTIES.and { it.property.visibility == PUBLIC }
    }

    internal class Walk(
        val parent: Any,
        val path: PropertyPath,
        val filter: PropertyFilter,
        val sorter: PropertyComparator,
        val visitor: PropertyVisitor,
        private val flags: List<WalkFlag>,
    ) {
        fun recurse(parent: Any, path: PropertyPath): Walk = Walk(parent, path, filter, sorter, visitor, flags)

        fun hasFlag(flag: WalkFlag): Boolean = flags.contains(flag)
    }

    /**
     * Walks the properties of the root value, calling the given [PropertyVisitor] for each property.
     *
     * @param filter A filter to limit the properties that are visited
     * @param sorter A property sorter to order the properties
     * @param flags Flags that control how the walk is performed
     * @param visitor The visitor to call for each property
     */
    fun walk(
        filter: PropertyFilter = PUBLIC_PROPERTIES,
        sorter: PropertyComparator = SortByName(),
        vararg flags: WalkFlag,
        visitor: PropertyVisitor
    ) {
        walk(filter, sorter, *flags).forEach { visitor.atProperty(it) }
    }

    /**
     * Walks the properties of the root value, returning a list of information about the properties encountered.
     *
     * @param filter A filter to limit the properties that are visited
     * @param sorter A property sorter to order the properties
     * @param flags Flags that control how the walk is performed
     * @return List of property information
     */
    fun walk(
        filter: PropertyFilter = PUBLIC_PROPERTIES,
        sorter: PropertyComparator = SortByName(),
        vararg flags: WalkFlag
    ): List<Property> {
        val rootPath = PropertyPath(valueType(rootValue::class))
        val properties = safeList<Property>()
        walk(Walk(rootValue, rootPath, filter, sorter, properties::add, flags.toList()))
        return properties.sortedWith(sorter)
    }

    /**
     * Implementation of recursive property walking
     *
     * @param walk The state of the walk
     */
    private fun walk(walk: Walk) {

        // For each declared property in the current value,
        val parent = walk.parent
        val recursiveWalks = safeList<Walk>()
        for (property in valueType(parent::class).declaredProperties()) {

            // if the property can be accessed,
            if (property.canGet(parent)) {

                // get the value of the property,
                val propertyValue = property.get(parent)

                // extend the property path with the property name,
                val propertyPath = walk.path + property.name

                // and if the property passes the filter,
                val propertyInformation = Property(parent, propertyPath, property, propertyValue)
                if (walk.filter.test(propertyInformation)) {

                    // collect the property information
                    walk.visitor.atProperty(propertyInformation)

                    // and if the property has a value,
                    if (propertyValue != null) {

                        // and we are doing depth-first recursion,
                        val recursiveWalk = walk.recurse(propertyValue, propertyPath)
                        if (walk.hasFlag(RECURSE_DEPTH_FIRST)) {

                            // then recurse into the value.
                            walk(recursiveWalk)
                        }

                        // or if we are doing breadth-first recursion,
                        if (walk.hasFlag(RECURSE_BREADTH_FIRST)) {

                            // save the walk for after we finish the properties at this level.
                            recursiveWalks.add(recursiveWalk)
                        }
                    }
                }
            }
        }

        // If we are doing breadth-first recursion,
        if (walk.hasFlag(RECURSE_BREADTH_FIRST)) {

            // do any recursive walks now.
            recursiveWalks.forEach { walk(it) }
        }
    }
}
