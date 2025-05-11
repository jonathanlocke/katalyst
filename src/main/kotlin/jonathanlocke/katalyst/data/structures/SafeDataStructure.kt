package jonathanlocke.katalyst.data.structures

import jonathanlocke.katalyst.data.structures.lists.SafeList
import jonathanlocke.katalyst.data.structures.maps.SafeMap
import jonathanlocke.katalyst.data.structures.maps.SafeMultiMap
import jonathanlocke.katalyst.data.structures.sets.SafeSet
import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers.Companion.throwOnError

abstract class SafeDataStructure(
    open val metadata: SafetyMetadata,
    open val problemHandler: ProblemHandler
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
            problemHandler.warning(withContext("Exceeded ${metadata.warningSize} values"))

            // but only once.
            warned = true
        }

        // If the projected number of entries exceeds the maximum size,
        if (size + values > metadata.maximumSize.asLong()) {

            // then fail hard.
            problemHandler.fail(
                withContext(
                    "Adding $values values would exceed the maximum of ${metadata.maximumSize}"
                )
            )
        }
    }

    companion object {

        val globalEstimatedSize: Count by lazy { count(32) }
        val globalWarningSize: Count by lazy { count(100_000) }
        val globalMaximumSize: Count by lazy { count(1_100_000) }
        

        /**
         * Creates a [SafeList] with the given metadata
         *
         * @param name The name of the collection
         * @param estimatedSize The initial size of the collection
         * @param warningSize The size at which the warning A problem handler to report problems to will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeList A function that creates a new mutable list with the given estimated size
         * @param problemHandler The problem handler to use
         */
        fun <Element : Any> safeList(
            name: String = "Unknown",
            estimatedSize: Count = globalEstimatedSize,
            warningSize: Count = globalWarningSize,
            maximumSize: Count = globalMaximumSize,
            newUnsafeList: (Count) -> MutableList<Element> = { size -> ArrayList(size.asInt()) },
            problemHandler: ProblemHandler = throwOnError
        ): SafeList<Element> {
            val metadata = SafetyMetadata(name, estimatedSize, warningSize, maximumSize)
            return SafeList(metadata, problemHandler, newUnsafeList.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeSet] with the given metadata
         *
         * @param name The name of the collection
         * @param estimatedSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeSet A function that creates a new mutable set with the given estimated size
         * @param problemHandler The problem handler to use
         */
        fun <Member : Any> safeSet(
            name: String = "Unknown",
            estimatedSize: Count = globalEstimatedSize,
            warningSize: Count = globalWarningSize,
            maximumSize: Count = globalMaximumSize,
            newUnsafeSet: (Count) -> MutableSet<Member> = { size -> HashSet(size.asInt()) },
            problemHandler: ProblemHandler = throwOnError
        ): SafeSet<Member> {
            val metadata = SafetyMetadata(name, estimatedSize, warningSize, maximumSize)
            return SafeSet(metadata, problemHandler, newUnsafeSet.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeMap] with the given metadata
         *
         * @param name The name of the collection
         * @param estimatedSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeMap A function that creates a new mutable map with the given estimated size
         * @param problemHandler The problem handler to use
         */
        fun <Key : Any, Value : Any> safeMap(
            name: String = "Unknown",
            estimatedSize: Count = globalEstimatedSize,
            warningSize: Count = globalWarningSize,
            maximumSize: Count = globalMaximumSize,
            newUnsafeMap: (Count) -> MutableMap<Key, Value> = { size -> HashMap(size.asInt()) },
            problemHandler: ProblemHandler = throwOnError
        ): SafeMap<Key, Value> {
            val metadata = SafetyMetadata(name, estimatedSize, warningSize, maximumSize)
            return SafeMap(metadata, problemHandler, newUnsafeMap.invoke(estimatedSize))
        }

        /**
         * Creates a [SafeMultiMap] with the given metadata
         *
         * @param name The name of the collection
         * @param estimatedSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeMap A function that creates a new mutable map with the given estimated size
         * @param newSafeList A function that creates a new mutable list with the given estimated size
         * @param problemHandler The problem handler to use
         */
        fun <Key : Any, Value : Any> safeMultiMap(
            name: String = "Unknown",
            estimatedSize: Count = globalEstimatedSize,
            warningSize: Count = globalWarningSize,
            maximumSize: Count = globalMaximumSize,
            newUnsafeMap: (Count) -> MutableMap<Key, Value> = { size -> HashMap(size.asInt()) },
            newSafeList: () -> SafeList<Value>,
            problemHandler: ProblemHandler = throwOnError
        ): SafeMultiMap<Key, Value> {
            val metadata = SafetyMetadata(name, estimatedSize, warningSize, maximumSize)
            return SafeMultiMap(metadata, problemHandler, newSafeList)
        }
    }

    private fun withContext(text: String): String = "${this::class.simpleName} ($metadata.name): $text"
}
