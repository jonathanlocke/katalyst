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
/**///////////////////////////////////////////////////////////////////////////////////////////////////////////////////// */
package jonathanlocke.katalyst.cripsr.kivakit

import com.telenav.kivakit.annotations.code.quality.TypeQuality
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 * Holds a class and method name when a proper Java reflection [java.lang.reflect.Method] cannot be determined, as
 * in the case of the limited and poorly designed [StackTraceElement] class.
 *
 *
 * **Access**
 *
 *
 *  * [.invoke]
 *
 *
 *
 * **Annotations**
 *
 *
 *  * [.annotation]
 *  * [.hasAnnotation]
 *
 *
 *
 * **Modifiers**
 *
 *
 *  * [.isFinal]
 *  * [.isNative]
 *  * [.isPrivate]
 *  * [.isProtected]
 *  * [.isPublic]
 *  * [.isStatic]
 *  * [.isSynchronized]
 *  * [.isSynthetic]
 *  * [.modifiers]
 *
 *
 *
 * **Properties**
 *
 *
 *  * [.arrayElementType]
 *  * [.genericTypeParameters]
 *  * [.name]
 *  * [.parameterTypes]
 *  * [.parentType]
 *  * [.returnType]
 *
 *
 * @author jonathanl (shibo)
 */
@Suppress("unused")
@UmlClassDiagram(diagram = DiagramReflection::class)
@TypeQuality(stability = STABLE_EXTENSIBLE, testing = UNTESTED, documentation = DOCUMENTED)
class Method : Member {
    /** The underlying reflection method  */
    private var method: java.lang.reflect.Method? = null

    /** The name of this method  */
    private val name: String

    /** The method type  */
    private val type: Type<*>

    constructor(type: Type<*>?, method: java.lang.reflect.Method) {
        this.type = ensureNotNull(type)
        this.method = ensureNotNull(method)
        name = method.name
    }

    protected constructor(type: Type<*>?, name: String?) {
        this.type = ensureNotNull(type)
        this.name = ensureNotNull(name)
    }

    /**
     * {@inheritDoc}
     */
    override fun <T : Annotation?> annotation(annotationClass: Class<T>): T {
        return method!!.getAnnotation(annotationClass)
    }

    /**
     * {@inheritDoc}
     */
    override fun <T> arrayElementType(): ObjectList<Type<T>>? {
        if (method!!.returnType.isArray) {
            val list: ObjectList<Type<T>> = ObjectList<Type<T>>()
            list.add(typeForClass(method!!.returnType.componentType as Class<T>))
            return list
        }

        return null
    }

    /**
     * {@inheritDoc}
     */
    override fun <T> genericTypeParameters(): ObjectList<Type<T>> {
        val list: ObjectList<Type<T>> = ObjectList<Type<T>>()
        val genericType = method!!.genericReturnType as ParameterizedType
        for (at in genericType.actualTypeArguments) {
            if (at is Class<*>) {
                list.add(typeForClass(at as Class<T>))
            }
        }
        return list
    }

    /**
     * Invokes this method on the given target object
     *
     * @param target The object on which to call the method
     * @param parameters The parameters to pass
     * @return The object returned by the method, or an instance of [ReflectionProblem] if the operation failed
     */
    fun invoke(target: Any?, vararg parameters: Any?): Any {
        try {
            val problem = makeAccessible() ?: return method!!.invoke(ensureNotNull(target), parameters)
            return problem
        } catch (e: Exception) {
            return ReflectionProblem(e, "Cannot get $this")
        }
    }

    val isNative: Boolean
        /**
         * Returns true if this is a native method
         */
        get() = Modifier.isNative(modifiers())

    val isSynchronized: Boolean
        /**
         * Returns true if this is a synchronized method
         */
        get() = Modifier.isSynchronized(modifiers())

    override val isSynthetic: Boolean
        /**
         * Returns true if this is a synthetic method
         */
        get() = method!!.isSynthetic

    /**
     * Makes this field accessible via reflection
     *
     * @return Any [ReflectionProblem] that occurred, or null if the operation succeeded
     */
    fun makeAccessible(): ReflectionProblem? {
        try {
            method!!.isAccessible = true
            return null
        } catch (e: Exception) {
            return ReflectionProblem("Cannot access $this")
        }
    }

    /**
     * Returns the underlying reflection method
     */
    fun method(): java.lang.reflect.Method? {
        return method
    }

    override fun modifiers(): Int {
        return method!!.modifiers
    }

    /**
     * {@inheritDoc}
     */
    override fun name(): String {
        return name
    }

    /**
     * Returns the parameter types for this method
     */
    fun parameterTypes(): Array<Class<*>> {
        return method!!.parameterTypes
    }

    /**
     * {@inheritDoc}
     */
    override fun parentType(): Type<*> {
        return type
    }

    /**
     * Returns the return type for this method
     */
    fun returnType(): Class<*> {
        return method!!.returnType
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        return type.toString() + "." + name() + "(" + list(method!!.parameterTypes).join() + ")"
    }

    override fun type(): Type<*> {
        return type
    }

    companion object {
        /**
         * Returns a [Method] instance for the given stack frame
         */
        fun method(stackFrame: StackTraceElement): Method? {
            try {
                val type = Class.forName(stackFrame.className)
                return Method(typeForClass(type), stackFrame.methodName)
            } catch (ignored: Exception) {
            }
            return null
        }
    }
}
