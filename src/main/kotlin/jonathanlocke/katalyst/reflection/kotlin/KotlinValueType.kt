package jonathanlocke.katalyst.reflection.kotlin

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor
import jonathanlocke.katalyst.reflection.properties.PropertyAccessor.Companion.propertyAccessor
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

class KotlinValueType<Value : Any>(
    override val valueClass: KClass<Value>,
    override val parameterClass: KClass<*>?,
) : ValueType<Value> {

    @Suppress("UNCHECKED_CAST")
    override fun memberPropertyAccessors(): List<PropertyAccessor<*>> =
        valueClass.memberProperties.map { propertyAccessor(it as KProperty<Any>) }

    override fun property(name: String): PropertyAccessor<*>? = memberPropertyAccessors().find { it.name == name }
    override fun supertypes(): List<KotlinValueType<*>> =
        valueClass.supertypes.mapNotNull { it.classifier as? KClass<*> }.map { KotlinValueType(it, null) }

    override fun createInstance(): Value = valueClass.createInstance()
    override val simpleName: String = valueClass.simpleName!!
    override val qualifiedName: String = valueClass.qualifiedName!!
    override val packageName: String = valueClass.java.`package`.name
    override fun isInstanceOf(type: KClass<*>) = valueClass.isSubclassOf(type)

    override fun superPropertyAccessors(): List<PropertyAccessor<*>> =
        supertypes().flatMap { it.memberPropertyAccessors() }

    override fun declaredPropertyAccessors(): List<PropertyAccessor<*>> =
        memberPropertyAccessors().filter { property: PropertyAccessor<*> -> property !in superPropertyAccessors() }
            .toList()

    override fun equals(other: Any?) =
        other is KotlinValueType<*> && valueClass == other.valueClass && parameterClass == other.parameterClass

    override fun hashCode(): Int = Objects.hash(valueClass, parameterClass)
    override fun toString(): String = "${valueClass.simpleName}${parameterClass?.let { "<$it>" } ?: ""}"
}