package jonathanlocke.katalyst.sequencer.serialization

/**
 * Visitor pattern for limiting serialization impact for DOS attacks and similar problems in input data
 *
 * Things to limit using serialization state:
 *
 *  - field length
 *  - number of fields
 *  - length of lists
 */
interface SerializationLimiter