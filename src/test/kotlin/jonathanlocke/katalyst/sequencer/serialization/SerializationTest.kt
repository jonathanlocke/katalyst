package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.cripsr.reflection.PropertyWalker
import jonathanlocke.katalyst.nucleus.problems.ProblemList
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

        val debug = false

        // Create a test value with null fields,
        val value = X()
        value.x = 10
        value.y.y = 20

        // serialize the value to text,
        val serialization = PropertiesSerialization<X>()
        val problems = ProblemList()
        val text = serialization.serializer().serialize(problems, value)

        // show the serialized text,
        if (debug) println("\n\nSerialized:\n\n$text")

        // then deserialize the text back to a value,
        val deserialized = serialization.deserializer(X::class).deserialize(problems, text)

        // and ensure we got the same value back,
        assertEquals(value, deserialized)

        // show that value,
        if (debug) println("\n\nDeserialized:\n\n")

        // and show the resulting deserialized value,
        if (debug) PropertyWalker(deserialized).walk { property, type, path, value ->
            if (defaultConversionRegistry.hasConversionTo(type)) {
                println("$path=$value")
            }
        }
    }
}
