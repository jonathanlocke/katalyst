package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.properties.Property

interface PropertyResolver {

    fun canResolve(property: Property<*>): Boolean
    fun resolve(property: Property<*>): Property<*>
}
