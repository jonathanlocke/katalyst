package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyPath
import java.util.function.Consumer

class Walk<Value : Any>(
    val root: Value,
    val value: Any?,
    val path: PropertyPath,
    val settings: PropertyWalker.Settings,
    private val visitor: PropertyVisitor,
) {
    private val visited = HashSet<Any?>()

    fun child(name: String) = path.plus(name)

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

    fun visitProperty(
        property: Property<out Any>,
        recurse: Consumer<Walk<Value>>,
    ) {
        // If the property can be explored,
        if (canExplore(property)) {

            // and it can be visited,
            if (canVisit(property)) {

                // call the visitor for the property,
                visit(property)
            }

            // and if the property has a value,
            if (property.value != null) {

                // then walk the property recursively.
                recurse.accept(walkChild(property))
            }
        }
    }

    private fun canExplore(property: Property<*>) = settings.canExplore(property)
    private fun canVisit(property: Property<*>) = settings.canVisit(property)
    private fun walkChild(property: Property<*>): Walk<Value> =
        Walk(root, property.value, path.plus(property.name), settings, visitor)
}