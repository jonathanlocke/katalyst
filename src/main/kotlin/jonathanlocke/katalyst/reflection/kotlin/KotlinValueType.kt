package jonathanlocke.katalyst.reflection.kotlin

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.properties.Property
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties

class KotlinValueType<T : Any>(private val kClass: KClass<T>) : ValueType<T> {

    @Suppress("UNCHECKED_CAST")
    override fun memberProperties(): List<Property<*>> =
        kClass.memberProperties.map { Property.Companion.property(it as KProperty<Any>) }

    override fun property(name: String): Property<*>? = memberProperties().find { it.name == name }
    override fun supertypes(): List<KotlinValueType<*>> =
        kClass.supertypes.mapNotNull { it.classifier as? KClass<*> }.map { KotlinValueType(it) }

    override fun createInstance(): T = kClass.createInstance()
    override val simpleName: String = kClass.simpleName!!

    override fun superProperties(): List<Property<*>> = supertypes().flatMap { it.memberProperties() }
    override fun declaredProperties(): List<Property<*>> =
        memberProperties().filter { property: Property<*> -> property !in superProperties() }.toList()

    override fun equals(other: Any?) = other is KotlinValueType<*> && kClass == other.kClass
    override fun hashCode(): Int = kClass.hashCode()
    override fun toString(): String = "KotlinValueType(${kClass.simpleName})"
}