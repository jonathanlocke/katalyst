package jonathanlocke.katalyst.cripsr.reflection

class PropertyPath(val type: ValueClass<*>) : ArrayList<String>() {

    companion object {

        const val SEPARATOR = "."

        fun rootPropertyPath(type: ValueClass<*>): PropertyPath = PropertyPath(type)

        fun propertyPath(type: ValueClass<*>, text: String): PropertyPath = PropertyPath(type).apply {
            addAll(text.split(SEPARATOR))
        }
    }

    fun pathString(): String = this.joinToString(SEPARATOR)

    override fun toString(): String = type.simpleName + ":" + pathString()

    fun parent(): PropertyPath {
        val copy = PropertyPath(type)
        copy.addAll(this.dropLast(1))
        return copy
    }

    fun copy(): PropertyPath {
        val copy = PropertyPath(type)
        copy.addAll(this)
        return copy
    }

    /**
     * Returns the property at this path in the given type.
     * @param type The type to search for the property.
     * @return The property at this path in the given type, or null if it does not exist.
     */
    fun property(): Property<*>? {

        var at: ValueClass<*> = type
        var property: Property<*>? = null

        // For each element in the path,
        for (element in this) {

            // find the property with the element's name,
            property = at.property(element)

            // and if it is not found,
            if (property == null) {

                // give up,
                return null
            }

            // otherwise advance to the type of the property for the next element.
            at = property.valueClass
        }

        return property
    }

    operator fun plus(element: String): PropertyPath = copy().apply { add(element) }
}