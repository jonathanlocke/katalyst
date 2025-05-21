package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueInstance
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.accessors.PropertyResolvingAccessor
import kotlin.reflect.KClass

class Property<Value : Any>(
    val parentValue: Any,
    val path: PropertyPath,
    val accessor: PropertyAccessor<Value>,
) {
    private var propertyValue: Value? = null

    fun isChildOf(property: Property<*>): Boolean = path.isChildOf(property.path)
    fun isParentOf(property: Property<*>): Boolean = path.isParentOf(property.path)

    val name get() = path.last()

    fun <T : Any> resolveAs(value: ValueInstance<T>): Property<T> {
        return Property(parentValue, path, PropertyResolvingAccessor(accessor, value))
    }

    fun <AnnotationInstance : Annotation> getAnnotation(type: KClass<AnnotationInstance>): AnnotationInstance? =
        accessor.annotation(type)

    val packageName = parentValueType.packageName
    val parentValueType: ValueType<*> get() = valueType(parentValue::class)
    val type: ValueType<Value> get() = this.accessor.type()

    val value: Value?
        get() {
            if (propertyValue == null) {
                propertyValue = this.accessor.get(parentValue)
            }
            return propertyValue
        }

    fun fullPath() = this.packageName + ":" + this

    override fun toString() = path.pathString() + " = " + this.value
}
