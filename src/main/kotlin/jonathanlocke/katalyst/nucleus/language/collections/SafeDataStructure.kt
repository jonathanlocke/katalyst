package jonathanlocke.katalyst.nucleus.language.collections

import jonathanlocke.katalyst.nucleus.language.collections.lists.SafeMutableList
import jonathanlocke.katalyst.nucleus.language.collections.lists.SafeMutableSet
import jonathanlocke.katalyst.nucleus.language.collections.maps.SafeMutableMap
import jonathanlocke.katalyst.nucleus.language.collections.maps.SafeMutableMultiMap
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.listeners.Throw
import jonathanlocke.katalyst.nucleus.values.count.Count
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.count

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

    abstract fun size(): Count

    protected fun ensureSafeToAdd(entries: Int) {

        // If the projected number of entries exceeds the warning size,
        if (size() + entries <= metadata.warningSize && !warned) {

            // then issue a warning,
            problemListener.warning(withContext("exceeded ${metadata.warningSize} entries"))

            // but only once.
            warned = true
        }

        // If the projected number of entries exceeds the maximum size,
        if (size() + entries > metadata.maximumSize) {

            // then fail hard.
            problemListener.fail("Maximum size exceeded")
        }
    }

    companion object {

        var globalEstimatedSize: Count = count(32)
        var globalWarningSize: Count = count(100_000)
        var globalMaximumSize: Count = count(1_100_000)

        /**
         * Creates a [SafeMutableList] with the given metadata
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
        ): SafeMutableList<T> {
            val metadata = SafetyMetadata(
                name,
                estimatedSize,
                warningSize.max(globalWarningSize),
                maximumSize.max(globalMaximumSize),
            )
            return SafeMutableList(metadata, problemListener, newUnsafeMutableList.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeMutableSet] with the given metadata
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
        ): SafeMutableSet<T> {
            val metadata = SafetyMetadata(
                name,
                estimatedSize,
                warningSize.max(globalWarningSize),
                maximumSize.max(globalMaximumSize),
            )
            return SafeMutableSet(metadata, problemListener, newUnsafeMutableSet.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeMutableMap] with the given metadata
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
        ): SafeMutableMap<Key, Value> {
            val metadata = SafetyMetadata(
                name,
                estimatedSize,
                warningSize.max(globalWarningSize),
                maximumSize.max(globalMaximumSize),
            )
            return SafeMutableMap(metadata, problemListener, newUnsafeMutableMap.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeMutableMultiMap] with the given metadata
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
        ): SafeMutableMultiMap<Key, Value> {
            val metadata = SafetyMetadata(
                name,
                estimatedSize,
                warningSize.max(globalWarningSize),
                maximumSize.max(globalMaximumSize),
            )
            return SafeMutableMultiMap(metadata, problemListener, newUnsafeMutableList)
        }
    }

    private fun withContext(text: String): String = "${this::class.simpleName} ($metadata.name): $text"
}
