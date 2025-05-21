package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeList
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor.Visibility.Public
import jonathanlocke.katalyst.reflection.properties.walker.filters.DefaultPropertyExplorationFilter
import jonathanlocke.katalyst.reflection.properties.walker.sorters.SortByPropertyPath
import jonathanlocke.katalyst.status.StatusHandlerMixin
import java.util.stream.Collectors

class PropertyWalker<Value : Any>(
    private val rootValue: Value,
) : StatusHandlerMixin {
    class Settings {

        private var explorationFilter: PropertyExplorationFilter = DefaultPropertyExplorationFilter()

        private var visitFilter: PropertyVisitFilter = PropertyVisitFilter { property -> true }

        private var sorter: PropertyComparator? = SortByPropertyPath()

        private var resolvers: MutableList<PropertyResolver> = mutableListOf()

        fun withFilter(filter: PropertyExplorationFilter): Settings {
            val copy = copy()
            copy.explorationFilter = filter
            return copy
        }

        fun canExplore(property: Property<*>): Boolean {
            return explorationFilter.test(property)
        }

        fun canVisit(property: Property<*>): Boolean {
            return visitFilter.test(property)
        }

        fun withVisitFilter(visitFilter: PropertyVisitFilter): Settings {
            val copy = copy()
            copy.visitFilter = visitFilter
            return copy
        }

        fun sorter(): PropertyComparator? {
            return sorter
        }

        fun resolvers(): List<PropertyResolver> {
            return resolvers
        }

        fun withSorter(sorter: PropertyComparator?): Settings {
            val copy = copy()
            copy.sorter = sorter
            return copy
        }

        fun withResolver(resolver: PropertyResolver): Settings {
            val copy = copy()
            copy.resolvers.add(resolver)
            return copy
        }

        private fun copy(): Settings {
            val copy = Settings()
            copy.explorationFilter = this.explorationFilter
            copy.visitFilter = this.visitFilter
            copy.sorter = this.sorter
            copy.resolvers = this.resolvers
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
        val rootPath = handleStatusOf(valueType(rootValue::class)).rootPropertyPath()

        // collect all the properties under the root value,
        val properties: MutableList<Property<*>> = safeList()
        walk(Walk(rootValue, rootValue, rootPath, settings, properties::add))

        // Return the properties in sorted order.
        return properties.stream().sorted(settings.sorter()).collect(Collectors.toList())
    }

    /**
     * Implementation of recursive property walking
     *
     * @param walk The state of the walk
     */
    private fun walk(walk: Walk<Value>) {

        // Get the value we are at in the traversal,
        val value = walk.value
        if (value != null) {

            // and for each declared property of that value,
            handleStatusOf(valueType(value::class))
                .declaredPropertyAccessors()
                .stream()
                .filter { it.isInstance() }
                .forEach { accessor ->

                    // if the property can be accessed,
                    if (accessor.canGet(value)) {

                        // create a property object for the property of value with the given name,
                        var property = handleStatusOf(Property(value, walk.child(accessor.name), accessor))

                        // go through each property resolver,
                        walk.settings.resolvers().forEach { resolver ->

                            // and if it can resolve the property to something else,
                            if (resolver.canResolve(property)) {

                                // then do that.
                                property = resolver.resolve(property)
                            }
                        }

                        // Visit the property,
                        walk.visitProperty(property) {

                            // recursing into it if it's not null.
                            walk(it)
                        }
                    }
                }
        }
    }

    companion object {
        val AllProperties: PropertyExplorationFilter = PropertyExplorationFilter { property -> true }
        val PublicProperties: PropertyExplorationFilter =
            AllProperties.and { property -> property.accessor.visibility === Public }
    }
}