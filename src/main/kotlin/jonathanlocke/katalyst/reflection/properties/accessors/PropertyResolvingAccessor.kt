package jonathanlocke.katalyst.reflection.properties.accessors

import jonathanlocke.katalyst.reflection.ValueInstance
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor
import kotlin.reflect.KClass

/**
 * A property accessor that delegates all calls to another accessor except for {@link PropertyAccessor::get}, which
 * returns a hardwired value.
 */
class PropertyResolvingAccessor<Id : Any, Value : Any>(
    private val propertyAccessor: PropertyAccessor<Id>,
    private val value: ValueInstance<Value>,
) : PropertyAccessor<Value> {

    override val name: String get() = propertyAccessor.name
    override val visibility: PropertyAccessor.Visibility get() = propertyAccessor.visibility
    override fun isInstance(): Boolean = propertyAccessor.isInstance()
    override fun set(instance: Any, value: Value?): Unit = throw UnsupportedOperationException()
    override fun canGet(instance: Any): Boolean = propertyAccessor.canGet(instance)
    override fun <AnnotationInstance : Annotation> annotation(type: KClass<AnnotationInstance>) =
        propertyAccessor.annotation(type)

    override fun <AnnotationInstance : Annotation> annotations(type: KClass<AnnotationInstance>) =
        propertyAccessor.annotations(type)

    override fun type(): ValueType<Value> = value.type
    override fun get(instance: Any): Value? = value.value
}