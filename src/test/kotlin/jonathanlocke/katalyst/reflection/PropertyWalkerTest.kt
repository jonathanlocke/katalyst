package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.properties.PropertyPath
import jonathanlocke.katalyst.reflection.properties.walker.PropertyWalker
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PropertyWalkerTest {

    class Y {
        val y = 10
    }

    class X {
        val x = 5
        val y = Y()
    }

    @Test
    fun testSuccess() {
        val paths = mutableListOf<PropertyPath>()
        val values = mutableListOf<Any?>()
        PropertyWalker(X()).walk { property ->
            paths.add(property.path)
            values.add(property.value)
        }
        assertEquals("X:x", paths[0].toString())
        assertEquals("X:y", paths[1].toString())
        assertEquals("X:y.y", paths[2].toString())
        assertEquals(5, values[0])
        assertEquals(10, values[2])
    }
}
