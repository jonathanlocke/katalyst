package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.status.StatusHandlerMixin
import kotlin.reflect.KClass

interface PropertyAccessor<Value : Any> : StatusHandlerMixin {

    enum class Visibility { Public, Private, Protected }

    fun <AnnotationInstance : Annotation> annotations(type: KClass<AnnotationInstance>): List<AnnotationInstance>
    fun <AnnotationInstance : Annotation> annotation(type: KClass<AnnotationInstance>): AnnotationInstance?

    val visibility: Visibility
    val name: String
    fun type(): ValueType<Value>
    fun isInstance(): Boolean
    fun get(instance: Any): Value?
    fun set(instance: Any, value: Value?)
    fun canGet(instance: Any): Boolean
}