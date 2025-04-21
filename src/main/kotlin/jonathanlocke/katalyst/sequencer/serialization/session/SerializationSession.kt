/** ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////// */ //
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
/** ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// */
package jonathanlocke.katalyst.sequencer.serialization.session

import com.telenav.kivakit.core.code.UncheckedVoidCode
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

/**
 * A high-level abstraction for serialization. This interface allows the serialization of a sequence of
 * [SerializableObject]s using an [ObjectSerializer] during a bracketed session associated with an
 * [InputStream] or [OutputStream]. This design provides ease-of-use while ensuring that the stream is
 * always assigned a version and the input or output stream is managed correctly.
 *
 *
 * **Creating a Session**
 *
 *
 *
 * The method [SerializationSessionFactory.newSession] can be called to obtain a
 * [SerializationSession] instance. Providers of serialization will provide a [SerializationSessionFactory]
 * that produces *thread-safe* [SerializationSession]s. Alternatively a session can be created by
 * constructing an implementation instance.
 *
 *
 *
 * **Opening a Session**
 *
 *
 *
 * A serialization session is initiated by calling:
 *
 *
 *  * [.open]
 *  * [.open]
 *  * [.open]
 *  * [.open]
 *  * [.open]
 *  * [.open]
 *
 *
 *
 *
 * When a [SessionType.CLIENT_SOCKET_SERIALIZATION_SESSION] or
 * [SessionType.SERVER_SOCKET_SERIALIZATION_SESSION] session is opened, handshaking and
 * exchange for version information will take place.
 *
 *
 *
 * **Reading and Writing**
 *
 *
 *
 * A session will remember any input or output stream that was given to it when the session was opened, so that
 * when read and write methods are called:
 *
 *
 *  * [.read] - Read a [SerializableObject] from input
 *  * [.read] - Read an object of the given type from input
 * <LI>[.readList]</LI>
 *  * [.readResource]
 *  * [.write] - Write an object to output
 *  * [.write] - Write a [SerializableObject] to output
 *  * [.writeList]
 *  * [.writeResource]
 *
 *
 *
 *
 * objects will be read and written to the streams passed to open().
 *
 *
 *
 * **Closing a Session**
 *
 *
 *  * [.close]
 *  * [.onClose]
 *
 *
 *
 * **Properties**
 *
 *
 *  * [.isActive]
 *  * [.isReading]
 *  * [.isWriting]
 *  * [.maximumFlushTime]
 *
 *
 *
 * **Example**
 *
 * <pre>
 * var session = new KryoSerializationSession();
 * var version = session.open(SessionType.RESOURCE, input, output);
 * session.write(new SerializedObject&lt;&gt;("hello"));
 * session.close();
</pre> *
 *
 * @author jonathanl (shibo)
 * @see SerializationSessionFactory
 *
 * @see SerializableObject
 *
 * @see Version
 */
@Suppress("unused")
interface SerializationSession : Closeable, Flushable<Duration?>, Versioned, Repeater, TryTrait {
    /**
     * The type of serialization session. This determines the order of
     */
    enum class SessionType {
        /** This session is interacting with a client socket  */
        CLIENT_SOCKET_SERIALIZATION_SESSION,

        /** This session is interacting with a server socket  */
        SERVER_SOCKET_SERIALIZATION_SESSION,

        /** This session is serializing to a resource  */
        RESOURCE_SERIALIZATION_SESSION
    }

    /**
     * {@inheritDoc}
     */
    override fun close() {
        onClose()
    }

    val isActive: Boolean
        /**
         * Returns true if this session is reading or writing
         */
        get() = this.isReading || this.isWriting

    /**
     * Returns true if data is being read
     */
    val isReading: Boolean

    /**
     * Returns true if data is being written
     */
    val isWriting: Boolean

    /**
     * {@inheritDoc}
     */
    public override fun maximumFlushTime(): Duration {
        return FOREVER
    }

    /**
     * Ends a serialization session, flushing any pending output.
     */
    fun onClose()

    /**
     * Returns opens the given socket for reading and writing. Version handshaking is performed automatically for
     * [SessionType.SERVER_SOCKET_SERIALIZATION_SESSION]s and
     * [SessionType.CLIENT_SOCKET_SERIALIZATION_SESSION]s with the version of the connected endpoint returned to
     * the caller.
     */
    fun open(
        socket: Socket,
        sessionType: SessionType?,
        version: Version?,
        reporter: ProgressReporter?
    ): Version? {
        try {
            trace("Opening socket")
            return open(
                ProgressiveInputStream(socket.getInputStream(), reporter),
                ProgressiveOutputStream(socket.getOutputStream(), reporter), sessionType,
                version
            )
        } catch (e: Exception) {
            fatal(e, "Socket connection failed")
            return null
        }
    }

    /**
     * Opens this session for reading
     *
     * @return The version or an exception is thrown
     */
    fun open(input: InputStream?, sessionType: SessionType?): Version? {
        return open(input, null, sessionType, null)
    }

    /**
     * Opens this session for reading from a resource
     *
     * @param input The input stream,
     * @return The version of the stream, or a runtime exception
     */
    fun open(input: InputStream?): Version? {
        return open(input, null, SessionType.RESOURCE_SERIALIZATION_SESSION, null)
    }

    /**
     * Opens this session for writing
     */
    fun open(output: OutputStream?, sessionType: SessionType?, version: Version?) {
        open(null, output, sessionType, version)
    }

    /**
     * Opens this session for writing to a resource
     */
    fun open(output: OutputStream?, version: Version?) {
        open(output, SessionType.RESOURCE_SERIALIZATION_SESSION, version)
    }

    /**
     * Opens this session for reading and/or writing. Retains the given input and output streams for future use, and
     * performs version any socket handshaking per the [SessionType] parameter.
     *
     * @return The resource, client or server version, or an exception
     */
    fun open(input: InputStream?, output: OutputStream?, sessionType: SessionType?, version: Version?): Version?

    /**
     * Returns a serializable object
     */
    fun <T> read(): SerializableObject<T?>?

    /**
     * Reads an object of the given type from the input, discarding any version. If the object read from input is not of
     * the given type, an [IllegalStateException] will be thrown. This method can be used to read primitives, for
     * example: *read(Integer.class)*.
     *
     * @param type The type to read
     * @return The object
     */
    fun <T> read(type: Class<T?>): T? {
        val `object`: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
            read<Any?>().`object`()
        if (type.isAssignableFrom(`object`.getClass())) {
            return `object`
        }
        throw IllegalStateException("Expected object of type " + type + ", not " + `object`.getClass())
    }

    /**
     * Reads a list of elements written by the [.writeList] method
     *
     * @param type The element type
     * @return The list
     */
    fun <Element> readList(type: Class<Element?>): ObjectList<Element?> {
        val size = read<Int>(Int::class.java)!!
        val list: ObjectList<Element?> = ObjectList<Element?>()
        for (i in 0..<size) {
            list.add(read<T?>(type))
        }
        return list
    }

    /**
     * Runs the given code while the given resource is open for reading
     *
     * @param resource The resource to read from
     * @param code The code to run
     */
    fun readResource(resource: Resource, code: UncheckedVoidCode?) {
        try {
            resource.openForReading().use { input ->
                open(input, SessionType.RESOURCE_SERIALIZATION_SESSION)
                tryCatchThrow(code, "Error while reading from: $")
                close()
            }
        } catch (e: IOException) {
            problem(e, "Auto-close failure")
        }
    }

    /**
     * Writes the given object to output without version information
     */
    fun <T> write(`object`: T?) {
        ensureFalse(`object` is VersionedObject, "Use SerializableObject instead of VersionedObject")

        write(SerializableObject(`object`))
    }

    /**
     * Writes the given [SerializableObject] to output
     */
    fun <T> write(`object`: SerializableObject<T?>?)

    /**
     * Writes the given collection of elements as a list
     *
     * @param list The list to write
     */
    fun <Element> writeList(list: MutableCollection<Element?>) {
        write<Int?>(list.size)
        for (element in list) {
            write<Element?>(element)
        }
    }

    /**
     * Runs the given code while the given resource is open for writing
     *
     * @param resource The resource to write to
     * @param version The output version
     * @param code The code to run
     */
    fun writeResource(resource: WritableResource, version: Version?, code: UncheckedVoidCode?) {
        try {
            resource.openForWriting().use { output ->
                open(output, SessionType.RESOURCE_SERIALIZATION_SESSION, version)
                tryCatchThrow(code, "Error while writing to: $")
                close()
            }
        } catch (e: IOException) {
            problem(e, "Auto-close failure")
        }
    }
}
