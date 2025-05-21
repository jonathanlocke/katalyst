package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.ValueInstance.Companion.valueInstance
import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyPath
import jonathanlocke.katalyst.reflection.properties.walker.PropertyResolver
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

    class Z {
        val x = 3
        val y = "Y"
    }

    @Test
    fun testResolver() {

        val y = Y()
        val settings = PropertyWalker.Settings().withResolver(object : PropertyResolver {
            override fun canResolve(property: Property<*>): Boolean {
                return property.path.toPathString() == "y"
            }

            override fun resolve(property: Property<*>): Property<*> {
                return property.resolveAs(valueInstance(Y::class, y))
            }

        })
        val properties = PropertyWalker(Z()).walk(settings)
        assertEquals(3, properties[0].value)
        assertEquals(y, properties[1].value)
        assertEquals(10, properties[2].value)
    }

    @Test
    fun test() {
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
