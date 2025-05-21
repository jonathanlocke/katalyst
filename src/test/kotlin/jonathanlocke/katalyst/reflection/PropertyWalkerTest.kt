package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.logging.LoggerMixin
import jonathanlocke.katalyst.reflection.ValueInstance.Companion.valueInstance
import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyPath
import jonathanlocke.katalyst.reflection.properties.walker.PropertyResolver
import jonathanlocke.katalyst.reflection.properties.walker.PropertyWalker
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PropertyWalkerTest : LoggerMixin {

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
        val properties = handleStatusOf(PropertyWalker(Z())).walk(settings)
        assertEquals(3, properties[0].get())
        assertEquals(y, properties[1].get())
        assertEquals(10, properties[2].get())
    }

    @Test
    fun test() {
        val paths = mutableListOf<PropertyPath>()
        val values = mutableListOf<Any?>()
        handleStatusOf(PropertyWalker(X())).walk { property ->
            paths.add(property.path)
            values.add(property.get())
        }
        assertEquals("jonathanlocke.katalyst.reflection.PropertyWalkerTest.X:x", paths[0].toString())
        assertEquals("jonathanlocke.katalyst.reflection.PropertyWalkerTest.X:y", paths[1].toString())
        assertEquals("jonathanlocke.katalyst.reflection.PropertyWalkerTest.X:y.y", paths[2].toString())
        assertEquals(5, values[0])
        assertEquals(10, values[2])
    }

}
