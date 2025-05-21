package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.serialization.formats.properties.PropertiesSerialization
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationTest {

    class Y {
        var y: Int? = null

        override fun equals(other: Any?) = if (other is Y) {
            y == other.y
        } else false

        override fun hashCode() = Objects.hash(y)
    }

    class X {
        var x: Int? = null
        var y: Y = Y()

        override fun equals(other: Any?) = if (other is X) {
            x == other.x && y == other.y
        } else false

        override fun hashCode() = Objects.hash(x, y)
    }

    // todo: this test is currently failing due to a regression likely to do with property walker
    @Test
    fun testSuccess() {

        // Create a test value with null fields,
        val value = X()
        value.x = 10
        value.y.y = 20

        // serialize the value, deserialize it, and ensure they are equal.
        val serialization = PropertiesSerialization<X>()
        val text = serialization.serialize(value)
        val deserialized = serialization.deserialize(valueType(X::class), text)
        assertEquals(value, deserialized)
    }
}
