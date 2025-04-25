package jonathanlocke.katalyst.data.structures

import jonathanlocke.katalyst.data.structures.lists.SafeList
import jonathanlocke.katalyst.data.structures.maps.SafeMap
import jonathanlocke.katalyst.data.structures.maps.SafeMultiMap
import jonathanlocke.katalyst.data.structures.sets.SafeSet
import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.Throw

abstract class SafeDataStructure(
    open val metadata: SafetyMetadata,
    open val problemListener: ProblemListener
) {
    open class SafetyMetadata(
        val name: String,
        val estimatedSize: Count,
        val warningSize: Count,
        val maximumSize: Count
    )

    var warned = false

    abstract val size: Int

    protected fun ensureSafeToAdd(values: Int) {

        // If the projected number of entries exceeds the warning size,
        if (size + values > metadata.warningSize.asLong() && !warned) {

            // then issue a warning,
            problemListener.warning(withContext("Exceeded ${metadata.warningSize} values"))

            // but only once.
            warned = true
        }

        // If the projected number of entries exceeds the maximum size,
        if (size + values > metadata.maximumSize.asLong()) {

            // then fail hard.
            problemListener.fail(
                withContext(
                    "Adding $values values would exceed the maximum of ${metadata.maximumSize}"
                )
            )
        }
    }

    companion object {

        var globalEstimatedSize: Count = count(32)
        var globalWarningSize: Count = count(100_000)
        var globalMaximumSize: Count = count(1_100_000)

        /**
         * Creates a [SafeList] with the given metadata
         *
         * @param name The name of the collection
         * @param estimatedSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeMutableList A function that creates a new mutable list with the given estimated size
         * @param problemListener The problem listener to use
         */
        fun <T : Any> safeMutableList(
            name: String = "Unknown",
            estimatedSize: Count = globalEstimatedSize,
            warningSize: Count = count(1_000_000),
            maximumSize: Count = globalMaximumSize,
            newUnsafeMutableList: (Count) -> MutableList<T> = { size -> ArrayList(size.asInt()) },
            problemListener: ProblemListener = Throw()
        ): SafeList<T> {
            val metadata = SafetyMetadata(name, estimatedSize, warningSize, maximumSize)
            return SafeList(metadata, problemListener, newUnsafeMutableList.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeSet] with the given metadata
         *
         * @param name The name of the collection
         * @param estimatedSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeMutableSet A function that creates a new mutable set with the given estimated size
         * @param problemListener The problem listener to use
         */
        fun <T : Any> safeMutableSet(
            name: String = "Unknown",
            estimatedSize: Count = globalEstimatedSize,
            warningSize: Count = count(1_000_000),
            maximumSize: Count = globalMaximumSize,
            newUnsafeMutableSet: (Count) -> MutableSet<T> = { size -> HashSet(size.asInt()) },
            problemListener: ProblemListener = Throw()
        ): SafeSet<T> {
            val metadata = SafetyMetadata(name, estimatedSize, warningSize, maximumSize)
            return SafeSet(metadata, problemListener, newUnsafeMutableSet.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeMap] with the given metadata
         *
         * @param name The name of the collection
         * @param estimatedSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeMutableMap A function that creates a new mutable map with the given estimated size
         * @param problemListener The problem listener to use
         */
        fun <Key : Any, Value : Any> safeMutableMap(
            name: String = "Unknown",
            estimatedSize: Count = globalEstimatedSize,
            warningSize: Count = count(1_000_000),
            maximumSize: Count = globalMaximumSize,
            newUnsafeMutableMap: (Count) -> MutableMap<Key, Value> = { size -> HashMap(size.asInt()) },
            problemListener: ProblemListener = Throw()
        ): SafeMap<Key, Value> {
            val metadata = SafetyMetadata(name, estimatedSize, warningSize, maximumSize)
            return SafeMap(metadata, problemListener, newUnsafeMutableMap.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeMultiMap] with the given metadata
         *
         * @param name The name of the collection
         * @param estimatedSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeMutableMap A function that creates a new mutable map with the given estimated size
         * @param newUnsafeMutableList A function that creates a new mutable list with the given estimated size
         * @param problemListener The problem listener to use
         */
        fun <Key : Any, Value : Any> safeMutableMultiMap(
            name: String = "Unknown",
            estimatedSize: Count = globalEstimatedSize,
            warningSize: Count = count(1_000_000),
            maximumSize: Count = globalMaximumSize,
            newUnsafeMutableMap: (Count) -> MutableMap<Key, Value> = { size -> HashMap(size.asInt()) },
            newUnsafeMutableList: (Count) -> MutableList<Value> = { size -> ArrayList(size.asInt()) },
            problemListener: ProblemListener = Throw()
        ): SafeMultiMap<Key, Value> {
            val metadata = SafetyMetadata(name, estimatedSize, warningSize, maximumSize)
            return SafeMultiMap(metadata, problemListener, newUnsafeMutableList)
        }
    }

    private fun withContext(text: String): String = "${this::class.simpleName} ($metadata.name): $text"
}
