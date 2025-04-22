package jonathanlocke.katalyst.cripsr.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class PropertyPath : ArrayList<String>() {

    companion object {

        const val SEPARATOR = "."

        val rootPropertyPath: PropertyPath = PropertyPath()

        fun propertyPath(text: String): PropertyPath = PropertyPath().apply { addAll(text.split(SEPARATOR)) }
    }

    override fun toString(): String = this.joinToString(SEPARATOR)

    fun parent(): PropertyPath {
        val copy = PropertyPath()
        copy.addAll(this.dropLast(1))
        return copy
    }

    fun copy(): PropertyPath {
        val copy = PropertyPath()
        copy.addAll(this)
        return copy
    }

    /**
     * Returns the property at this path in the given type.
     * @param type The type to search for the property.
     * @return The property at this path in the given type, or null if it does not exist.
     */
    fun property(type: KClass<*>): KMutableProperty<*>? {

        var at: KClass<*> = type
        var property: KMutableProperty<*>? = null

        // For each element in the path,
        for (element in this) {

            // find the property with the element's name,
            property = at.memberProperties.filterIsInstance<KMutableProperty<*>>().find { it.name == element }

            // and if it is not found,
            if (property == null) {

                // give up,
                return null
            }

            // otherwise advance to the type of the property for the next element.
            at = property.returnType.classifier as KClass<*>
        }

        return property
    }

    operator fun plus(element: String): PropertyPath = copy().apply { add(element) }
}