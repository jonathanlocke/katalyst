package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyPath

data class PropertyInformation(val parent: Any?, val path: PropertyPath, val property: Property<*>, val value: Any?)
