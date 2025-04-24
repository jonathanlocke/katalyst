package jonathanlocke.katalyst.cripsr.reflection

import jonathanlocke.katalyst.cripsr.reflection.kotlin.KotlinProperty
import kotlin.reflect.KMutableProperty

interface Property<T : Any> {

    enum class Visibility { PUBLIC, PRIVATE, PROTECTED }
    companion object {

        fun <T : Any> newProperty(property: KMutableProperty<T>) = KotlinProperty(property)
    }

    val visibility: Visibility
    val name: String
    val valueClass: PropertyClass<T>
    fun get(instance: Any): T?
    fun set(instance: Any, value: T?)
    fun canGet(instance: Any): Boolean
}