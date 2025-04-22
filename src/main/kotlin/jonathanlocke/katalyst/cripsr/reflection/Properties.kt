package jonathanlocke.katalyst.cripsr.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Properties {
    companion object {
        fun KProperty<*>.kClass(): KClass<*> = returnType.classifier as KClass<*>
    }
}