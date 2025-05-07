package jonathanlocke.katalyst.resources.storage

import jonathanlocke.katalyst.resources.metadata.Extension
import jonathanlocke.katalyst.resources.streams.WriteMode

interface ResourceFolder : ResourceStoreNode {

    fun absolute(): ResourceFolder

    fun delete(): Boolean

    fun exists(): Boolean

    /**
     * Returns the child resource container at the given relative path
     */
    fun folder(path: String?): T?

    fun folders(): ObjectList<T?>?

    /**
     * Returns true if the path for this resource folder has a trailing slash
     */
    fun hasTrailingSlash(): Boolean {
        return path().hasTrailingSlash()
    }

    /**
     * Returns true if there's nothing in this folder
     */
    fun isEmpty(): Boolean {
        return folders().isEmpty() && resources().isEmpty()
    }

    /**
     * Returns true if this folder is materialized
     */
    fun isMaterialized(): Boolean

    /**
     * Returns true if this folder can be written to
     */
    fun isWritable(): Boolean {
        return unsupported()
    }

    /**
     * Returns a matcher that matches all resource paths in this folder
     */
    fun matchAllPathsIn(): Matcher<ResourcePathed?>? {
        return Matcher { resource -> path().equals(resource.path().parent()) }
    }

    /**
     * Returns a matcher that matches all resource paths under this folder
     */
    fun matchAllPathsUnder(): Matcher<ResourcePathed?>? {
        return Matcher { that: ResourcePathed -> this.contains(that) }
    }

    public override fun matcher(): Matcher<ResourcePathed?>? {
        return matchAllPathsUnder()
    }

    /**
     * Creates a local copy of this folder for efficiency and random access
     */
    fun materialize(): Folder {
        return materializeTo(temporaryFolderForProcess(NORMAL))
    }

    /**
     * Materializes this folder to the given folder
     */
    fun materializeTo(folder: Folder): Folder {
        if (!isMaterialized()) {
            folder.mkdirs().clearAll()
            for (resource in resources()) {
                val destination: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
                    folder.file(resource.fileName())
                resource.safeCopyTo(destination, OVERWRITE)
            }
        }
        return folder
    }

    /**
     * Creates all folders above and including this folder
     */
    fun mkdirs(): ResourceFolder<*> {
        unsupported()
        return this
    }

    /**
     * Returns the list of folders that match the given matcher
     *
     * @param matcher The matcher
     * @return The matching folders
     */
    fun nestedFolders(matcher: Matcher<T?>): ObjectList<T?> {
        val folders: ObjectList<T?> = ObjectList<T?>()
        for (at in folders()) {
            folders.add(at)
            folders.addAll(at.nestedFolders(matcher))
        }
        return folders
    }

    /**
     * Returns all nested resources
     */
    fun nestedResources(): ResourceStoreNodeList {
        return nestedResources({ value -> true })
    }

    /**
     * Returns all nested resources matching the given glob pattern
     *
     * @param glob The pattern to match
     * @return The list of matching resources
     */
    fun nestedResources(glob: String?): ResourceStoreNodeList? {
        return nestedResources(glob(glob))
    }

    /**
     * Returns any matching files that are recursively contained in this folder
     */
    fun nestedResources(matcher: Matcher<ResourcePathed?>): ResourceStoreNodeList {
        val list: ResourceStoreNodeList = ResourceStoreNodeList(filter { it is Resource })
        list.addAll(resources())
        for (at in folders()) {
            if (!equals(at)) {
                list.addAll(at.nestedResources(matcher))
            }
        }
        return list
    }

    fun newFolder(relativePath: ResourcePath): ResourceFolder<*>?

    /**
     * Returns the parent folder of this folder
     */
    fun parent(): ResourceFolder<*>?

    fun relativeTo(folder: ResourceFolder<*>): ResourceFolder<*>? {
        val relativePath: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
            absolute().path().relativeTo(folder.absolute().path())
        return newFolder(relativePath)
    }

    /**
     * Renames this folder to the given folder
     */
    fun renameTo(folder: ResourceFolder<*>): Boolean

    /**
     * Returns the resource of the given in this container
     */
    fun resource(name: FileName): Resource {
        return resource(name.asPath())
    }

    /**
     * Returns the resource of the given in this container
     */
    fun resource(name: String): Resource? {
        return resource(parseResourcePath(this, name))
    }

    /**
     * Returns the resource of the given in this container
     */
    fun resource(name: ResourcePathed): Resource?

    /**
     * Returns the storage-independent identifier for this folder
     */
    fun resourceFolderIdentifier(): ResourceFolderIdentifier?

    /**
     * Returns the resources in this folder matching the given matcher
     */
    fun resources(matcher: Matcher<ResourcePathed?>): ResourceStoreNodeList?

    /**
     * Returns the resources in this folder
     */
    fun resources(): ResourceStoreNodeList? {
        return resources(matchAll())
    }

    /**
     * Copies the resources in this package to the given folder
     */
    fun safeCopyTo(
        folder: ResourceFolder<*>,
        mode: WriteMode,
        reporter: ProgressReporter
    ) {
        safeCopyTo(folder, mode, matchAll(), reporter)
    }

    /**
     * Copies the resources in this package to the given folder
     */
    fun safeCopyTo(
        folder: ResourceFolder<*>,
        mode: WriteMode,
        matcher: Matcher<ResourcePathed?>,
        reporter: ProgressReporter
    ) {
        for (at in resources(matcher)) {
            val target: Resource = folder.mkdirs().resource(at.fileName())
            mode.ensureAllowed(at, target.asWritable())
            at.safeCopyTo(target.asWritable(), mode, reporter)
        }
    }

    /**
     * Returns a temporary file with the given base filename
     *
     * @param baseName The base filename
     * @return The writable file
     */
    fun temporaryFile(baseName: FileName): WritableResource? {
        return temporaryFile(baseName, TEMPORARY)
    }

    /**
     * Returns a temporary file with the given base filename and extension
     *
     * @param baseName The base filename
     * @param extension The extension
     * @return The writable file
     */
    fun temporaryFile(
        baseName: FileName,
        extension: Extension
    ): WritableResource?

    /**
     * Returns a temporary folder with the given base name
     *
     * @param baseName The base name
     * @return The temporary folder
     */
    fun temporaryFolder(baseName: FileName): ResourceFolder<*> {
        return unsupported()
    }

    /**
     * Returns this folder with a trailing slash
     */
    fun withTrailingSlash(): ResourceFolder<*>? {
        if (hasTrailingSlash()) {
            return this
        }
        return newFolder(path().withChild(""))
    }
}