package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.properties.kotlin.KotlinProperty
import kotlin.reflect.KProperty

interface Property<T : Any> {

    enum class Visibility { PUBLIC, PRIVATE, PROTECTED }
    companion object {

        fun <T : Any> property(property: KProperty<T>) = KotlinProperty(property)
    }

    val visibility: Visibility
    val name: String
    val type: ValueType<T>
    fun get(instance: Any): T?
    fun set(instance: Any, value: T?)
    fun canGet(instance: Any): Boolean
}