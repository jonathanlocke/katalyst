package jonathanlocke.katalyst.data.values.version

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ThrowOnError.Companion.throwOnError

/**
 * Represents a [*semantic version*](https://semver.org), such as "6.3" or "1.2.1"
 *
 * **Creation**
 *
 * - [version] - Creates a version from the given string, throwing an exception if the string can't be parsed
 * - [Version.parseVersion] - Parses the given text into a version, reporting any problems to the given handler
 *
 * **Comparison**
 *
 * - [Version.isNewerThan]
 * - [Version.isNewerThanOrEqualTo]
 * - [Version.isOlderThan]
 * - [Version.isOlderThanOrEqualTo]
 * - [Version.isCompatibleWith]
 * - [Version.newer]
 * - [Version.older]

 * **String Representation**
 *
 * - [Version.toString]
 *
 * @property major The major version number
 * @property minor The minor version number
 * @property patch The patch version number
 *
 * @see [*Semantic Versioning*](https://semver.org)
 */
data class Version(val major: Int, val minor: Int, val patch: Int = 0) {

    fun version(text: String): Version = parseVersion(text)!!

    fun isCompatibleWith(that: Version): Boolean = major == that.major && minor == that.minor
    fun isNewerThan(that: Version): Boolean = asInt() > that.asInt()
    fun isNewerThanOrEqualTo(that: Version): Boolean = equals(that) || isNewerThan(that)
    fun isOlderThan(that: Version): Boolean = !equals(that) && !isNewerThan(that)
    fun isOlderThanOrEqualTo(that: Version): Boolean = equals(that) || isOlderThan(that)
    fun newer(that: Version): Version = if (isNewerThan(that)) this else that
    fun older(that: Version): Version? = if (isOlderThan(that)) this else that

    override fun toString(): String = "$major.$minor.$patch"

    companion object {

        private val pattern =
            Regex("(?x) (?<major> \\d+) (\\. (?<minor> \\d+)) (\\. (?<patch> \\d+))?", RegexOption.IGNORE_CASE)

        /**
         * Parses the given text into a version, reporting any problems to the given handler
         *
         * @return The given text, of the form [major].[minor](.[revision])?(-release)?, parsed as a [Version] object,
         * or null if the text is not of that form.
         */
        fun parseVersion(text: String, problemHandler: ProblemHandler = throwOnError): Version? {

            // If the text matches the version pattern,
            val match = pattern.matchEntire(text)
            if (match != null) {
                val major = match.groups["major"]!!.value.toInt()
                val minor = match.groups["minor"]!!.value.toInt()
                val patch = match.groups["patch"]!!.value.toInt()
                return Version(major, minor, patch)
            }

            return problemHandler.error("Couldn't parse version '$text'").let { null }
        }
    }

    private fun asInt(): Int = major * 10_000 + minor * 100 + patch
}
