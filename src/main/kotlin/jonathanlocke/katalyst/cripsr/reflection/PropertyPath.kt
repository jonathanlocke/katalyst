package jonathanlocke.katalyst.cripsr.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class PropertyPath(val type: KClass<*>) : ArrayList<String>() {

    companion object {

        const val SEPARATOR = "."

        fun rootPropertyPath(type: KClass<*>): PropertyPath = PropertyPath(type)

        fun propertyPath(type: KClass<*>, text: String): PropertyPath = PropertyPath(type).apply {
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

    fun value(instance: Any): Any? = property()?.getter?.call(instance)

    fun value(instance: Any, value: Any?) {
        property()?.setter?.call(instance, value)
    }

    /**
     * Returns the property at this path in the given type.
     * @param type The type to search for the property.
     * @return The property at this path in the given type, or null if it does not exist.
     */
    fun property(): KMutableProperty<*>? {

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