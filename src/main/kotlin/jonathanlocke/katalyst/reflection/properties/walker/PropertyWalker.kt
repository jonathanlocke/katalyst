package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeList
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor.Visibility.Public
import jonathanlocke.katalyst.reflection.properties.sorters.SortByPropertyPath
import jonathanlocke.katalyst.reflection.properties.walker.filters.DefaultPropertyExplorationFilter
import java.util.stream.Collectors

class PropertyWalker<Value : Any>(
    private val rootValue: Value,
) {
    class Settings {
        private var explorationFilter: PropertyExplorationFilter = DefaultPropertyExplorationFilter()

        private var visitFilter: PropertyVisitFilter = PropertyVisitFilter { property -> true }

        private var sorter: PropertyComparator? = SortByPropertyPath()

        fun withFilter(filter: PropertyExplorationFilter): Settings {
            val copy = copy()
            copy.explorationFilter = filter
            return copy
        }

        fun canExplore(property: Property<*>?): Boolean {
            return explorationFilter.test(property)
        }

        fun canVisit(property: Property<*>?): Boolean {
            return visitFilter.test(property)
        }

        fun withVisitFilter(visitFilter: PropertyVisitFilter): Settings {
            val copy = copy()
            copy.visitFilter = visitFilter
            return copy
        }

        fun getSorter(): PropertyComparator? {
            return sorter
        }

        fun withSorter(sorter: PropertyComparator?): Settings {
            val copy = copy()
            copy.sorter = sorter
            return copy
        }

        private fun copy(): Settings {
            val copy = Settings()
            copy.explorationFilter = this.explorationFilter
            copy.visitFilter = this.visitFilter
            copy.sorter = this.sorter
            return copy
        }
    }

    fun walk(visitor: PropertyVisitor) {
        walk(Settings()).forEach { visitor.atProperty(it) }
    }

    fun walk(): MutableList<Property<*>> {
        return walk(Settings())
    }

    /**
     * Walks the properties of the root value, calling the given PropertyVisitor for each property.
     *
     * @param settings The settings to use for the walk
     * @param visitor The visitor to call for each property
     */
    fun walk(settings: Settings, visitor: PropertyVisitor) {
        walk(settings).forEach { visitor.atProperty(it) }
    }

    /**
     * Walks the properties of the root value, returning a list of information about the properties encountered.
     *
     * @param settings The settings to use for the walk
     * @return List of property information
     */
    fun walk(settings: Settings): MutableList<Property<*>> {
        // Starting at the root property path,

        val rootPath = valueType(rootValue::class).rootPropertyPath()

        // collect all the properties under the root value,
        val properties: MutableList<Property<*>> = safeList()
        walk(Walk(rootValue, rootValue, rootPath, settings, properties::add))

        // Return the properties in sorted order.
        return properties.stream().sorted(settings.getSorter()).collect(Collectors.toList())
    }

    /**
     * Implementation of recursive property walking
     *
     * @param walk The state of the walk
     */
    private fun walk(walk: Walk<Value>) {

        // Get the value we are at in the traversal,
        val value = walk.getValue()
        if (value != null) {

            // and for each declared property of that value,
            valueType(value::class)
                .declaredPropertyAccessors()
                .stream()
                .filter { it.isInstance() }
                .forEach { accessor ->

                    // if the property can be accessed,
                    if (accessor.canGet(value)) {

                        // create a property object for the property of value with the given name,
                        val property = Property(value, walk.child(accessor.name), accessor)

                        // and if the property can be explored,
                        if (walk.canExplore(property)) {

                            // and it can be visited,
                            if (walk.canVisit(property)) {
                                // call the visitor for the property,

                                walk.visit(property)
                            }

                            // and if the property has a value,
                            if (property.value != null) {

                                // then walk the property recursively.
                                walk(walk.recurseIntoProperty(property))
                            }
                        }
                    }
                }
        }
    }

    companion object {

        val AllProperties: PropertyExplorationFilter = PropertyExplorationFilter { property -> true }
        val PublicProperties: PropertyExplorationFilter? = AllProperties.and(
            { property -> property.accessor.visibility === Public })
    }
}