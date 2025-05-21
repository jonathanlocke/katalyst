package jonathanlocke.katalyst.reflection

import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.PropertyPath
import jonathanlocke.katalyst.reflection.properties.PropertyPath.Companion.propertyPath
import kotlin.test.Test
import kotlin.test.assertEquals

class PropertyPathTest {

    class X {
        val x = 5
    }

    @Test
    fun test() {
        assert(path().toPathString() == "x")
        assert(path().toQualifiedString() == "jonathanlocke.katalyst.reflection.PropertyPathTest.X:x")
    }

    private fun path(): PropertyPath = propertyPath(valueType(X::class), "x")
    private fun emptyPath(): PropertyPath = propertyPath(valueType(X::class), "")

    @Test
    fun testEqualsHashCode() {
        assertEquals(path(), path())
        assertEquals(path().hashCode(), path().hashCode())
        assertEquals(emptyPath(), emptyPath())
        assertEquals(emptyPath().hashCode(), emptyPath().hashCode())
    }
}