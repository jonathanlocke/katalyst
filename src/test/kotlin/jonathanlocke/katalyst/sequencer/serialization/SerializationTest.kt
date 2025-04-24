package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.cripsr.reflection.ValueClass.Companion.valueClass
import jonathanlocke.katalyst.sequencer.serialization.properties.PropertiesSerialization
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SerializationTest {

    class Y {
        var y: Int? = null

        override fun equals(other: Any?): Boolean = other === this || (other is Y && y == other.y)
        override fun hashCode(): Int = y ?: 0
    }

    class X {
        var x: Int? = null
        var y: Y = Y()

        override fun equals(other: Any?): Boolean = other === this || (other is X && x == other.x && y == other.y)
        override fun hashCode(): Int = 31 * (x ?: 0) + y.hashCode()
    }

    @Test
    fun testSuccess() {

        // Create a test value with null fields,
        val value = X()
        value.x = 10
        value.y.y = 20

        // serialize the value, deserialize it, and ensure they are equal.
        val serialization = PropertiesSerialization<X>()
        val text = serialization.serialize(value)
        val deserialized = serialization.deserialize(valueClass(X::class), text)
        assertEquals(value, deserialized)
    }
}
