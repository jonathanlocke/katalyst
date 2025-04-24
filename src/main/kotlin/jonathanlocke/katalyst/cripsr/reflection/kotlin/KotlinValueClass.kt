package jonathanlocke.katalyst.cripsr.reflection.kotlin

import jonathanlocke.katalyst.cripsr.reflection.Property
import jonathanlocke.katalyst.cripsr.reflection.Property.Companion.newProperty
import jonathanlocke.katalyst.cripsr.reflection.ValueClass
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties

class KotlinValueClass<T : Any>(private val kClass: KClass<T>) : ValueClass<T> {

    @Suppress("UNCHECKED_CAST")
    override fun memberProperties() = kClass.memberProperties.map { newProperty(it as KMutableProperty<Any>) }
    override fun property(name: String): Property<*>? = memberProperties().find { it.name == name }
    override fun supertypes(): List<KotlinValueClass<*>> =
        kClass.supertypes.mapNotNull { it.classifier as? KClass<*> }.map { KotlinValueClass(it) }

    override fun createInstance(): T = kClass.createInstance()
    override val simpleName: String = kClass.simpleName!!

    override fun superProperties(): List<Property<*>> = supertypes().flatMap { it.memberProperties() }
    override fun declaredProperties(): List<Property<*>> =
        memberProperties().filter { property: Property<*> -> property !in superProperties() }.toList()
}