package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueInstance
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.accessors.PropertyResolvingAccessor
import jonathanlocke.katalyst.status.StatusHandlerMixin
import kotlin.reflect.KClass

class Property<Value : Any>(
    val parentValue: Any,
    val path: PropertyPath,
    val accessor: PropertyAccessor<Value>,
) : StatusHandlerMixin {

    private var value: Value? = null

    fun isChildOf(property: Property<*>): Boolean = path.isChildOf(property.path)
    fun isParentOf(property: Property<*>): Boolean = path.isParentOf(property.path)

    val name get() = path.last()

    fun <T : Any> resolveAs(value: ValueInstance<T>): Property<T> {
        return handleStatusOf(Property(parentValue, path, handleStatusOf(PropertyResolvingAccessor(accessor, value))))
    }

    fun <AnnotationInstance : Annotation> getAnnotation(type: KClass<AnnotationInstance>): AnnotationInstance? =
        accessor.annotation(type)

    val packageName = parentValueType.packageName
    val parentValueType: ValueType<*> get() = valueType(parentValue::class)
    val type: ValueType<Value> get() = this.accessor.type()

    fun get(): Value? = tryValue("Cannot get property: $path") {
        if (value == null) {
            value = this.accessor.get(parentValue)
        }
        value
    }

    fun set(value: Value) = tryBoolean("Cannot set property: $path = $value") {
        accessor.set(parentValue, value)
        true
    }

    fun fullPath() = this.packageName + ":" + this

    override fun toString() = path.toPathString() + " = " + this.value
}
