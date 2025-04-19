package jonathanlocke.katalyst.cripsr.reflection

class PropertyPath : ArrayList<String>() {

    override fun toString(): String = this.joinToString(".")

    fun copy(): PropertyPath {
        val copy = PropertyPath()
        copy.addAll(this)
        return copy
    }

    operator fun plus(element: String): PropertyPath = copy().apply { add(element) }
}