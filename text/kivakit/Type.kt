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
package jonathanlocke.katalyst.cripsr.kivakit

import com.telenav.kivakit.core.collections.list.ObjectList
import jonathanlocke.katalyst.cripsr.reflection.reflection.property.Property
import jonathanlocke.katalyst.cripsr.reflection.reflection.property.PropertyFilter
import java.lang.reflect.Constructor
import java.util.*

/**
 * Reflects on a class and retains a set of [Property] objects that can be used to efficiently set property
 * values.
 *
 *
 * **Creation**
 *
 *
 *  * [.type]
 *  * [.typeForClass]
 *  * [.typeForName]
 *  * [.constructor]
 *  * [.newInstance]
 *  * [.newInstance]
 *
 *
 *
 * **Hierarchy**
 *
 *
 *  * [.interfaces]
 *  * [.is]
 *  * [.isDescendantOf]
 *  * [.isInOrUnder]
 *  * [.superClass]
 *  * [.superClasses]
 *  * [.superInterfaces]
 *  * [.superTypes]
 *
 *
 *
 * **Properties**
 *
 *
 *  * [.arrayElementType]
 *  * [.asJavaType]
 *  * [.declaresToString]
 *  * [.enumValues]
 *  * [.fullyQualifiedName]
 *  * [.name]
 *  * [.packagePath]
 *  * [.simpleName]
 *  * [.simpleNameWithoutSyntheticSuffix]
 *
 *
 *
 * **Annotations**
 *
 *
 *  * [.annotation]
 *  * [.annotations]
 *  * [.hasAnnotation]
 *
 *
 *
 * **Beans Properties**
 *
 *
 *  * [.properties]
 *  * [.properties]
 *  * [.properties]
 *  * [.property]
 *
 *
 *
 * **Fields**
 *
 *
 *  * [.allFields]
 *  * [.field]
 *  * [.fields]
 *  * [.reachableFields]
 *  * [.reachableObjects]
 *  * [.reachableObjects]
 *  * [.reachableObjectsImplementing]
 *
 *
 *
 * **Methods**
 *
 *
 *  * [.allMethods]
 *  * [.method]
 *
 *
 *
 * **Modifiers**
 *
 *
 *  * [.isPrimitive]
 *  * [.isArray]
 *  * [.isEnum]
 *  * [.isInterface]
 *  * [.isSystem]
 *
 *
 * @author jonathanl (shibo)
 */
@Suppress("unused")
class Type<T>
private constructor(
    /** The underlying class  */
    private val type: Class<T>?
) {
    /** True if the type defines a toString() method  */
    private var hasToString: Boolean? = null

    /** Properties stored by name  */
    private val propertiesForFilter: MutableMap<PropertyFilter, MutableMap<String, Property>?> =
        IdentityHashMap<PropertyFilter, MutableMap<String, Property>?>()

    /**
     * Returns all fields of this type
     */
    fun allFields(): ObjectList<Field> {
        val fields: ObjectList<Field> = ObjectList<Field>()
        var current: Type<*>? = this
        while (!current!!.`is`(Any::class.java)) {
            if (current.type != null) {
                for (field in current.type!!.declaredFields) {
                    fields.add(Field(this, field))
                }
            }
            current = current.superClass()
        }
        return fields
    }

    /**
     * Returns all methods of this type, including inherited methods
     */
    fun allMethods(): ObjectList<Method> {
        val methods: ObjectList<Method> = ObjectList<Method>()
        var current: Type<*>? = this
        while (!current!!.`is`(Any::class.java)) {
            if (current.type != null) {
                for (method in current.type!!.declaredMethods) {
                    methods.add(Method(this, method))
                }
            }
            current = current.superClass()
        }
        return methods
    }

    /**
     * Returns the annotation of the given type on this type
     */
    fun <A : Annotation?> annotation(annotationType: Class<A>): A {
        return type!!.getAnnotation(annotationType)
    }

    /**
     * Returns all annotations of the given type on this type
     */
    fun <A : Annotation?> annotations(annotationType: Class<A>): Array<A> {
        return type!!.getAnnotationsByType(annotationType)
    }

    /**
     * Returns the array element type for this type if it is an array
     */
    fun arrayElementType(): Type<*>? {
        if (isArray) {
            return type<Any>(type!!.componentType)
        }
        return null
    }

    /**
     * Returns the underlying Java type
     */
    fun asJavaType(): Class<T>? {
        return type
    }

    /**
     * Returns the constructor with the given parameters
     *
     * @param parameters The constructor parameter types
     * @return The constructor
     */
    fun constructor(vararg parameters: Class<*>?): Constructor<T> {
        return try {
            type!!.getConstructor(*parameters)
        } catch (e: Exception) {
            illegalArgument("Unable to find constructor")
        }
    }

    /**
     * Returns true if this type directly declares a toString() method
     */
    fun declaresToString(): Boolean {
        if (hasToString == null) {
            hasToString = try {
                type!!.getDeclaredMethod("toString") != null
            } catch (e: Exception) {
                java.lang.Boolean.FALSE
            }
        }
        return hasToString!!
    }

    /**
     * Returns the enum values for this type if it is an enum
     */
    fun enumValues(): Set<Enum<*>> {
        val values = HashSet<Enum<*>>()
        for (value in type!!.enumConstants) {
            values.add(value as Enum<*>)
        }
        return values
    }

    /**
     * {@inheritDoc}
     */
    override fun equals(`object`: Any?): Boolean {
        if (`object` is Type<*>) {
            return type == `object`.type
        }
        return false
    }

    /**
     * Returns the field [Property] of the given name in this type
     */
    fun field(name: String?): Property? {
        val iterator: Unit = properties(NamedField(name)).iterator()
        if (iterator.hasNext()) {
            return iterator.next()
        }
        return null
    }

    /**
     * Returns the matching named fields
     *
     * @param name The name of the field
     */
    fun fields(name: String?): ObjectList<Field> {
        return allFields().matching { field -> field.name().equals(name) }
    }

    /**
     * Returns the list of all fields in this type that match the given matcher
     *
     * @param matcher The matcher to match against
     */
    fun fields(matcher: Matcher<Field?>?): ObjectList<Field> {
        return allFields().matching(matcher)
    }

    /**
     * Returns the fully qualified class name for this type
     */
    fun fullyQualifiedName(): String {
        return type!!.name
    }

    /**
     * Returns true if this type declares the given annotation
     */
    fun <A : Annotation?> hasAnnotation(annotationType: Class<A?>): Boolean {
        return annotation(annotationType) != null
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        return type.hashCode()
    }

    /**
     * Returns a list of any interfaces that this type implements
     */
    fun interfaces(): ObjectList<Type<*>> {
        val interfaces: ObjectList<Type<*>> = ObjectList<Type<*>>()
        for (at in type!!.interfaces) {
            interfaces.add(typeForClass<T>(at))
        }
        return interfaces
    }

    /**
     * Returns true if this type is the same as the given type
     */
    fun `is`(type: Class<*>): Boolean {
        return this.type == type
    }

    val isArray: Boolean
        /**
         * Returns true if this type is an array
         */
        get() = type!!.isArray

    /**
     * Returns true if this [Type] descends from the given class
     *
     * @param that The class to test
     * @return True if this [Type] descends from the given class
     */
    fun isDescendantOf(that: Class<*>): Boolean {
        return that.isAssignableFrom(type)
    }

    val isEnum: Boolean
        /**
         * Returns true if this type is an enum
         */
        get() = Enum::class.java.isAssignableFrom(type)

    /**
     * Returns true if this type is in or below the given package
     *
     * @param reference The package reference
     */
    fun isInOrUnder(reference: PackageReference?): Boolean {
        return packagePath().startsWith(reference)
    }

    val isInterface: Boolean
        /**
         * Returns true if this is a primitive type
         */
        get() = type!!.isInterface

    val isPrimitive: Boolean
        /**
         * Returns true if this is a primitive type
         */
        get() = type!!.isPrimitive

    val isSystem: Boolean
        /**
         * Returns true if this is a system type, meaning that it is under the package java or javax
         */
        get() = name().startsWith("java.") || name().startsWith("javax.")

    /**
     * Returns any method in this type with the given name
     */
    fun method(methodName: String): Method {
        return try {
            Method(
                this,
                type!!.getMethod(methodName)
            )
        } catch (e: Exception) {
            illegalArgument("Unable to get method $.$", type, methodName)
        }
    }

    /**
     * Returns the simple name of this type
     */
    override fun name(): String {
        return Classes.simpleName(type)
    }

    /**
     * Creates an instance of this type using any no-argument constructor
     */
    fun newInstance(): T {
        try {
            val constructor = type!!.getConstructor()
            constructor.isAccessible = true
            return constructor.newInstance()
        } catch (e: Exception) {
            return illegalState(e, "Cannot instantiate $this")
        }
    }

    /**
     * Creates an instance of this type using any constructor that accepts the given parameters
     */
    fun newInstance(vararg parameters: Any): T {
        try {
            val types: Array<Class<*>> = arrayOfNulls(parameters.size)
            var i = 0
            for (at in parameters) {
                types[i++] = at.javaClass
            }
            return type!!.getConstructor(*types).newInstance(*parameters)
        } catch (e: Exception) {
            return illegalArgument("Couldn't construct " + type + "(" + parameters.contentToString() + ")", e)
        }
    }

    /**
     * Returns the package reference to the package that contains this type
     */
    fun packagePath(): PackageReference {
        return PackageReference.packageReference(type)
    }

    /**
     * Returns set of properties matching the given filter, where setter methods are preferred to direct field access
     */
    fun properties(filter: PropertyFilter): ObjectList<Property> {
        // If we haven't reflected on the properties yet
        var properties: MutableMap<String, Property>? = propertiesForFilter[filter]
        if (properties == null) {
            // create a new set of properties
            properties = StringMap()

            // and add a property getter/setter for each declared field in this type and all super classes
            for (field in allFields()) {
                if (filter.includeField(field)) {
                    val fieldName: Unit = filter.nameForField(field)
                    properties!!.put(
                        "field-$fieldName", Property(
                            fieldName, FieldGetter(field),
                            FieldSetter(field)
                        )
                    )
                }
            }

            // then add setter properties, overriding any fields
            for (method in allMethods()) {
                // this is necessary because the Java compiler creates duplicate method objects for
                // methods inheriting from a generic interface, and the extra synthetic method will
                // not carry any annotation information even when the source code contains a correct
                // @ExcludeProperty
                if (method.isSynthetic()) {
                    continue
                }

                if (filter.includeAsGetter(method)) {
                    val name: Unit = filter.nameForMethod(method)
                    val property: Property? = properties!!.get(name)
                    if (property == null) {
                        properties.put("get-$name", Property(name, MethodGetter(method), null))
                    } else {
                        property.getter(MethodGetter(method))
                    }
                }

                if (filter.includeAsSetter(method)) {
                    val name: Unit = filter.nameForMethod(method)
                    val property: Property? = properties!!.get(name)
                    if (property == null) {
                        properties.put("set-$name", Property(name, null, MethodSetter(method)))
                    } else {
                        property.setter(MethodSetter(method))
                    }
                }
            }

            propertiesForFilter[filter] = properties
        }
        return ObjectList.list(properties!!.values).sorted()
    }

    /**
     * Returns a map of the properties of the given object that match the given matcher
     *
     * @param object The object to search
     * @param matcher The matcher to match
     */
    fun properties(`object`: Any?, matcher: PropertyFilter): VariableMap<Any> {
        return properties(`object`, matcher, null)
    }

    /**
     * Returns a map of the properties of the given object that match the given matcher
     *
     * @param object The object to search
     * @param matcher The matcher to match
     * @param nullValue The null value to store when a property value is null
     */
    fun properties(`object`: Any?, matcher: PropertyFilter, nullValue: Any?): VariableMap<Any> {
        val variables: VariableMap = VariableMap()
        for (property in properties(matcher)) {
            if ("class" != property.name()) {
                val getter = property.getter()
                if (getter != null) {
                    val value: Unit = getter.get(`object`)
                    if (value == null) {
                        if (nullValue != null) {
                            variables.add(property.name(), nullValue)
                        }
                    } else {
                        variables.add(property.name(), value)
                    }
                }
            }
        }
        return variables
    }

    /**
     * Returns the first property with the given name
     *
     * @param name The property name
     * @return The property
     */
    fun property(name: String?): Property {
        return properties(NamedMethod(ANY_NAMING_CONVENTION, name)).first()
    }

    /**
     * Returns a list of fields reachable from the given root that match the given matcher
     *
     * @param root The root object to search
     * @param matcher The field matcher
     */
    fun reachableFields(root: Any?, matcher: Matcher<Field>): ObjectList<Field> {
        return reachableFields(root, matcher, HashSet())
    }

    /**
     * Returns a list of objects reachable from the given root
     */
    fun reachableObjects(root: Any?): ObjectList<Any> {
        return reachableObjects(
            root,
            Matcher<Field> { field -> !field.isStatic() && !field.isTransient() && !field.isPrimitive() })
    }

    /**
     * Returns a list of objects reachable from the root that match the given matcher
     *
     * @param root The root object
     * @param matcher The matcher to match
     */
    fun reachableObjects(root: Any?, matcher: Matcher<Field>): ObjectList<Any> {
        val values: ObjectList = ObjectList()

        for (field in reachableFields(root, matcher)) {
            val value = field.get()
            if (value != null) {
                values.add(value)
            }
        }

        return values
    }

    /**
     * Returns a list of objects reachable from the root that implement the given interface
     *
     * @param root The root object
     * @param _interface The interface that must be implemented
     */
    fun reachableObjectsImplementing(root: Any?, _interface: Class<*>): ObjectList<Any> {
        return reachableObjects(
            root,
            Matcher<Field> { field -> _interface.isAssignableFrom(field.type().asJavaType()) })
    }

    /**
     * Returns the simple name of this type
     */
    fun simpleName(): String {
        return Classes.simpleName(type)
    }

    /**
     * Returns the simple name of this type without any anonymous class number
     */
    fun simpleNameWithoutSyntheticSuffix(): String {
        return simpleName().replace("\\.[0-9]+".toRegex(), "")
    }

    /**
     * Returns the superclass of this type
     */
    fun superClass(): Type<*>? {
        if (type == null) {
            return null
        } else {
            val superclass = type.superclass
            return if (superclass == null) null else typeForClass(superclass)
        }
    }

    /**
     * Returns a list of the superclasses of this type
     */
    fun superClasses(): ObjectList<Type<*>> {
        val superclasses: ObjectList<Type<*>> = ObjectList<Type<*>>()

        val superClass = superClass()
        if (superClass != null) {
            superclasses.add(superClass)
            superclasses.addAll(superClass.superClasses())
        }

        return superclasses
    }

    /**
     * Returns a list of the super-interfaces of this type
     */
    fun superInterfaces(): ObjectSet<Type<*>> {
        val superinterfaces: ObjectSet<Type<*>> = ObjectSet<Type<*>>(LinkedHashSet<E>())

        // Go through each interface of this type,
        for (at in interfaces()) {
            // and recursively add any superinterfaces,
            superinterfaces.add(at)
            superinterfaces.addAll(at.superInterfaces())
        }

        // then get the superclass and add all supertypes of that class
        return superinterfaces
    }

    /**
     * Returns a list of all supertypes of this type
     */
    fun superTypes(): ObjectList<Type<*>> {
        val supertypes: ObjectList<Type<*>> = superClasses()

        for (at in supertypes.copy()) {
            supertypes.addAll(at.superInterfaces())
        }

        return supertypes
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        return simpleName()
    }

    /**
     * Returns the fields reachable from the given root which match the given matcher
     *
     * @param root The root object
     * @param matcher The matcher to match against
     * @param visited The set of fields that have been visited (to prevent cycles)
     */
    private fun reachableFields(
        root: Any?,
        matcher: Matcher<Field>,
        visited: MutableSet<Field>
    ): ObjectList<Field> {
        val fields: ObjectList<Field> = ObjectList<Field>()
        if (root != null) {
            try {
                val type: Type<*> = type<Any>(root)
                if (type != null) {
                    for (field in type.allFields()) {
                        if (!field.isPrimitive() && !field.isStatic()) {
                            if (field.parentType().asJavaType().getModule()
                                    .isOpen(field.parentType().asJavaType().getPackageName())
                            ) {
                                val value = field.get(root)
                                field.`object`(root)
                                if (matcher.matches(field)) {
                                    if (value != null && !visited.contains(field)) {
                                        visited.add(field)
                                        fields.add(field)
                                        fields.addAll(reachableFields(value, matcher, visited))
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                illegalArgument("Error while finding reachable fields")
            }
        }
        return fields
    }

    companion object {
        /** Map from Class to Type  */
        private val types: MutableMap<Class<*>?, Type<*>> = ConcurrentHashMap<Class<*>, Type<*>>()

        /**
         * Gets the type of the given object
         */
        fun <T> type(`object`: Any): Type<T> {
            ensureNotNull(`object`)

            return typeForClass(`object`.javaClass) as Type<T>
        }

        /**
         * Gets the [Type] wrapper for the given [Class]
         */
        fun <T> typeForClass(type: Class<T>?): Type<T> {
            return types.computeIfAbsent(type) { type: Class<*>? -> Type(type) } as Type<T>
        }

        /**
         * Gets the [Type] wrapper for the given class name. Throws an [IllegalArgumentException] if the class
         * cannot be found.
         */
        fun <T> typeForName(className: String): Type<T> {
            return try {
                typeForClass(
                    Class.forName(
                        className
                    )
                ) as Type<T>
            } catch (e: ClassNotFoundException) {
                illegalArgument("Cannot find class $className", e)
            }
        }
    }
}
