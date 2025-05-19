package jonathanlocke.katalyst.data.structures

import jonathanlocke.katalyst.data.structures.lists.SafeList
import jonathanlocke.katalyst.data.structures.maps.SafeMap
import jonathanlocke.katalyst.data.structures.maps.SafeMultiMap
import jonathanlocke.katalyst.data.structures.sets.SafeSet
import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlers.Companion.throwOnError

abstract class SafeDataStructure(
    open val metadata: SafetyMetadata,
    open val statusHandler: StatusHandler,
) {
    open class SafetyMetadata(
        val name: String,
        val initialSize: Count,
        val warningSize: Count,
        val maximumSize: Count,
    )

    var warned = false

    abstract val size: Int

    protected fun ensureSafeToAdd(values: Int) {

        // If the projected number of entries exceeds the warning size,
        if (size + values > metadata.warningSize.asLong() && !warned) {

            // then issue a warning,
            statusHandler.warning(withContext("Exceeded ${metadata.warningSize} values"))

            // but only once.
            warned = true
        }

        // If the projected number of entries exceeds the maximum size,
        if (size + values > metadata.maximumSize.asLong()) {

            // then fail hard.
            statusHandler.fail(
                withContext(
                    "Adding $values values would exceed the maximum of ${metadata.maximumSize}"
                )
            )
        }
    }

    companion object {

        val globalInitialSize: Count by lazy { count(32) }
        val globalWarningSize: Count by lazy { count(100_000) }
        val globalMaximumSize: Count by lazy { count(1_100_000) }


        /**
         * Creates a [SafeList] with the given metadata
         *
         * @param name The name of the collection
         * @param initialSize The initial size of the collection
         * @param warningSize The size at which a warning should be issued
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeList A function that creates a new mutable list with the given estimated size
         * @param statusHandler The status handler to use
         */
        fun <Element : Any> safeList(
            name: String = "Unknown",
            initialSize: Count = globalInitialSize,
            warningSize: Count = globalWarningSize,
            maximumSize: Count = globalMaximumSize,
            newUnsafeList: (Count) -> MutableList<Element> = { size -> ArrayList(size.asInt()) },
            statusHandler: StatusHandler = throwOnError,
        ): SafeList<Element> {
            val metadata = SafetyMetadata(name, initialSize, warningSize, maximumSize)
            return SafeList(metadata, statusHandler, newUnsafeList.invoke(initialSize))
        }

        /**
         * Creates a [SafeSet] with the given metadata
         *
         * @param name The name of the collection
         * @param initialSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeSet A function that creates a new mutable set with the given estimated size
         * @param statusHandler The status handler to use
         */
        fun <Member : Any> safeSet(
            name: String = "Unknown",
            initialSize: Count = globalInitialSize,
            warningSize: Count = globalWarningSize,
            maximumSize: Count = globalMaximumSize,
            newUnsafeSet: (Count) -> MutableSet<Member> = { size -> HashSet(size.asInt()) },
            statusHandler: StatusHandler = throwOnError,
        ): SafeSet<Member> {
            val metadata = SafetyMetadata(name, initialSize, warningSize, maximumSize)
            return SafeSet(metadata, statusHandler, newUnsafeSet.invoke(initialSize))
        }

        /**
         * Creates a [SafeMap] with the given metadata
         *
         * @param name The name of the collection
         * @param initialSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeMap A function that creates a new mutable map with the given estimated size
         * @param statusHandler The status handler to use
         */
        fun <Key : Any, Value : Any> safeMap(
            name: String = "Unknown",
            initialSize: Count = globalInitialSize,
            warningSize: Count = globalWarningSize,
            maximumSize: Count = globalMaximumSize,
            newUnsafeMap: (Count) -> MutableMap<Key, Value> = { size -> HashMap(size.asInt()) },
            statusHandler: StatusHandler = throwOnError,
        ): SafeMap<Key, Value> {
            val metadata = SafetyMetadata(name, initialSize, warningSize, maximumSize)
            return SafeMap(metadata, statusHandler, newUnsafeMap.invoke(initialSize))
        }

        /**
         * Creates a [SafeMultiMap] with the given metadata
         *
         * @param name The name of the collection
         * @param initialSize The initial size of the collection
         * @param warningSize The size at which the warning listener will be called
         * @param maximumSize The maximum size of the collection
         * @param newUnsafeMap A function that creates a new mutable map with the given estimated size
         * @param newSafeList A function that creates a new mutable list with the given estimated size
         * @param statusHandler The status handler to use
         */
        fun <Key : Any, Value : Any> safeMultiMap(
            name: String = "Unknown",
            initialSize: Count = globalInitialSize,
            warningSize: Count = globalWarningSize,
            maximumSize: Count = globalMaximumSize,
            newUnsafeMap: (Count) -> MutableMap<Key, Value> = { size -> HashMap(size.asInt()) },
            newSafeList: () -> SafeList<Value>,
            statusHandler: StatusHandler = throwOnError,
        ): SafeMultiMap<Key, Value> {
            val metadata = SafetyMetadata(name, initialSize, warningSize, maximumSize)
            return SafeMultiMap(metadata, statusHandler, newSafeList)
        }
    }

    private fun withContext(text: String): String = "${this::class.simpleName} ($metadata.name): $text"
}
