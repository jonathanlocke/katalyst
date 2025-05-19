package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyPath

class Walk<Value : Any>(
    private val root: Value,
    private val value: Any?,
    private val path: PropertyPath,
    private var settings: PropertyWalker.Settings,
    private val visitor: PropertyVisitor,
) {
    private val visited = HashSet<Any?>()

    fun visit(property: Property<*>): Boolean {

        // If we haven't already visited this property,
        if (!visited.contains(property)) {

            // call the visitor,
            visitor.atProperty(property)

            // and mark it as visited.
            visited.add(property)
            return true
        }

        // We already visited this property!
        return false
    }

    fun getValue(): Any? {
        return value
    }

    fun canExplore(property: Property<*>): Boolean {
        return settings.canExplore(property)
    }

    fun canVisit(property: Property<*>): Boolean {
        return settings.canVisit(property)
    }

    fun child(name: String): PropertyPath {
        return path.plus(name)
    }

    /**
     * Returns a new Walk object for the child property with the given value and name
     */
    fun recurseIntoProperty(property: Property<*>): Walk<Value> {
        return Walk(root, property.value, path.plus(property.name), settings, visitor)
    }
}