package com.github.daemontus

/**
 * Design:
 *
 * Source code is translated into an object hierarchy.
 *
 * However, objects have an abstract placeholder (class: name + package; method: name + class + signature; ...)
 * and a specific implementation.
 * Hence a class can have a reference to another method which is resolved at run time.
 *
 * Note: Standard JVM way of implementing the abstract placeholders is using plain strings. We refrain from this
 * convention and use hashes instead to a) reduce JVM-specific code b) allow easier interop with implementation
 * objects, which share no similar quality.
 *
 * However, the abstract placeholders don't have a reference to implementation. This is resolved only after scanning
 * the whole project.
 *
 * Specifically, each implementation object has an associated abstract placeholder (or multiple placeholders).
 * Hence if two methods have different signatures, but equivalent implementation, they share the implementation object.
 * (although this is probably rare).
 *
 * Duplicate implementations should be handled directly by the preprocessor by flagging them as warnings and pointing
 * to their original location in the bundle.
 *
 *
 * Due to the specifics of this approach, we can observe that when forming the code tree, the abstract placeholders
 * always hold references upward (method -> class -> package), since they don't know their implementation/content yet,
 * while the implementation objects hold dependencies downwards (package -> class -> method -> bytecode). These two trees are
 * then connected using the Name-Code mapping.
 *
 */

// ----- Abstract Placeholders -----

typealias Hash<T> = String

interface Hashable<T> {
    val hash : Hash<T>
}

enum class Access{
    PUBLIC, PACKAGE, PROTECTED, PRIVATE
}

data class PackageId(
        override val hash: Hash<PackageId>,
        val name: String,
        val parentId: Hash<PackageId>?
) : Hashable<PackageId>

// Type is either a class, an interface or a native type
data class TypeId(
        override val hash: Hash<TypeId>,
        val name: String,
        val packageId: Hash<PackageId>?
) : Hashable<TypeId>

data class MethodId(
        override val hash: Hash<MethodId>,
        val name: String,
        val static: Boolean,
        val returnType: Hash<TypeId>,
        val argumentTypes: List<Hash<TypeId>>
) : Hashable<MethodId>

data class BytecodeData(
        override val hash: Hash<BytecodeData>
        //TODO
) : Hashable<BytecodeData>

data class MethodData(
        override val hash: Hash<MethodData>,
        val access: Access,
        val bytecode: Hash<BytecodeData>
) : Hashable<MethodData>

data class TypeData(
        override val hash: Hash<TypeData>,
        val access: Access,
        val parentTypeId: Hash<TypeData>,
        val interfaceTypeIds: List<Hash<TypeData>>,
        val methods: List<Hash<MethodData>>
) : Hashable<TypeData>

data class PackageData(
        override val hash: Hash<PackageData>,
        val classes: List<Hash<TypeData>>
) : Hashable<PackageData>