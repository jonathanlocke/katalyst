package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.properties.kotlin.KotlinPropertyAccessor
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

interface PropertyAccessor<Value : Any> {

    enum class Visibility { PUBLIC, PRIVATE, PROTECTED }

    companion object {

        fun <Value : Any> property(property: KProperty<Value>) = KotlinPropertyAccessor(property)
    }

    fun <AnnotationInstance : Annotation> annotations(type: KClass<AnnotationInstance>): List<AnnotationInstance>
    fun <AnnotationInstance : Annotation> annotation(type: KClass<AnnotationInstance>): AnnotationInstance? =
        annotations(type).firstOrNull()

    val visibility: Visibility
    val name: String
    fun type(): ValueType<Value>
    fun get(instance: Any): Value?
    fun set(instance: Any, value: Value?)
    fun canGet(instance: Any): Boolean
}