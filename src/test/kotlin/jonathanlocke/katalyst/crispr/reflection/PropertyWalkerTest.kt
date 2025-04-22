package jonathanlocke.katalyst.crispr.reflection

import jonathanlocke.katalyst.cripsr.reflection.PropertyWalker
import org.junit.jupiter.api.Test

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
        PropertyWalker(X()).walk { property, type, path, value ->
            println("$path = $value")
        }
    }
}

