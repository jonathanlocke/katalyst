package jonathanlocke.katalyst.cripsr.reflection

import jonathanlocke.katalyst.cripsr.reflection.kotlin.KotlinPropertyClass
import kotlin.reflect.KClass

interface PropertyClass<T : Any> {

    companion object {

        fun <T : Any> valueClass(kClass: KClass<T>) = KotlinPropertyClass(kClass)
    }

    val simpleName: String
    fun createInstance(): T
    fun superProperties(): List<Property<*>>
    fun declaredProperties(): List<Property<*>>
    fun memberProperties(): List<Property<*>>
    fun property(name: String): Property<*>?
    fun supertypes(): List<PropertyClass<*>>
}