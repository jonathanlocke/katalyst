package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.properties.kotlin.KotlinProperty
import kotlin.reflect.KProperty

interface Property<Value : Any> {

    enum class Visibility { PUBLIC, PRIVATE, PROTECTED }
    companion object {

        fun <Value : Any> property(property: KProperty<Value>) = KotlinProperty(property)
    }

    val visibility: Visibility
    val name: String
    fun type(): ValueType<Value>
    fun get(instance: Any): Value?
    fun set(instance: Any, value: Value?)
    fun canGet(instance: Any): Boolean
}