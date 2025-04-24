package jonathanlocke.katalyst.cripsr.reflection

import jonathanlocke.katalyst.cripsr.reflection.kotlin.KotlinValueClass
import kotlin.reflect.KClass

interface ValueClass<T : Any> {

    companion object {

        fun <T : Any> valueClass(kClass: KClass<T>) = KotlinValueClass(kClass)
    }

    val simpleName: String
    fun createInstance(): T
    fun superProperties(): List<Property<*>>
    fun declaredProperties(): List<Property<*>>
    fun memberProperties(): List<Property<*>>
    fun property(name: String): Property<*>?
    fun supertypes(): List<ValueClass<*>>
}