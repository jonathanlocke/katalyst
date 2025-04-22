package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.cripsr.reflection.PropertyWalker
import jonathanlocke.katalyst.nucleus.problems.ProblemList
import jonathanlocke.katalyst.sequencer.serialization.properties.PropertiesSerialization
import org.junit.jupiter.api.Test

class SerializationTest {

    class Y {
        var y: Int? = null
    }

    class X {
        var x: Int? = null
        var y: Y? = null
    }

    @Test
    fun testSuccess() {

        val x = X()
        x.x = 5
        x.y = Y()
        x.y?.y = 10

        val serialization = PropertiesSerialization<X>()
        val problems = ProblemList()
        val text = serialization.serializer().serialize(problems, x)
        println("\n\nSerialized:\n\n$text")
        val z = serialization.deserializer(X::class).deserialize(problems, text)
        println("\n\nDeserialized:\n\n")
        PropertyWalker(z).walk { property, type, path, value ->
            println("$path = $value")
        }
    }
}
