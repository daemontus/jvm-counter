package com.github.daemontus.klassy.classfile.validate

import com.github.daemontus.klassy.classfile.*

fun MethodInfo.validate(classFile: ClassFile, problems: MutableList<ValidationProblem>) {
    val isPublic = accessFlags.hasMask(Mask.PUBLIC)
    val isProtected = accessFlags.hasMask(Mask.PROTECTED)
    val isPrivate = accessFlags.hasMask(Mask.PRIVATE)
    val isFinal = accessFlags.hasMask(Mask.FINAL)
    val isSynchronized = accessFlags.hasMask(Mask.SYNCHRONIZED)
    val isNative = accessFlags.hasMask(Mask.NATIVE)
    val isAbstract = accessFlags.hasMask(Mask.ABSTRACT)
    val isStatic = accessFlags.hasMask(Mask.STATIC)
    val isStrict = accessFlags.hasMask(Mask.STRICT)
    if ((isPublic && isProtected) || (isPublic && isPrivate) || (isProtected && isPrivate)) {
        problems.onError("[1.5.2] In `access_flags <1.5.1>`, at most one of `<!PUBLIC>`, `<!PRIVATE>`, `<!PROTECTED>` can be set.")
    }
    if (classFile.accessFlags.hasMask(Mask.INTERFACE) && (isProtected || isFinal || isSynchronized || isNative)) {
        problems.onError("[1.5.3] If `access_flags <1.1.6>` has `<!INTERFACE>` set, then in `access_flags <1.5.1>`, `<!PROTECTED>`, `<!FINAL>`, `<!SYNCHRONIZED>`, `<!NATIVE>` must *not* be set.")
    }
    if (classFile.accessFlags.hasMask(Mask.INTERFACE)) {
        val above52 = classFile.majorVersion >= 52 && !isPublic && !isPrivate
        val below52 = classFile.majorVersion < 52 && (!isPublic || !isAbstract)
        if (above52 || below52) {
            problems.onError("[1.5.5] If `access_flags <1.1.6>` has `<!INTERFACE>` set and `major_version <1.1.3> < 52`, then in `access_flags <1.5.1>`, `<!PUBLIC>` and `<!ABSTRACT>` must be set. If `major_version <1.1.3> >= 52`, `<!PUBLIC>` or `<!PRIVATE>` must be set.")
        }
    }
    if (isAbstract && (isPrivate || isStatic || isFinal || isSynchronized || isNative || isStrict)) {
        problems.onError("[1.5.6] If `access_flags <1.5.1>` has `<!ABSTRACT>` set, `<!PRIVATE>`, `<!STATIC>`, `<!FINAL>`, `<!SYNCHRONIZED>`, `<!NATIVE>` and `<!STRICT>` must *not* be set.")
    }
    if (!classFile.constantPool.isCpIndex(nameIndex)) {
        problems.onError("[1.5.8] `name_index <1.5.2>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(nameIndex)) {
        problems.onError("[1.5.9] `constant_pool <1.1.5>` entry at index `name_index <1.5.2>` must be of type `Utf8_info <1.3.34>`.")
    }
    val allowedFlags = listOf(Mask.PRIVATE, Mask.PUBLIC, Mask.PROTECTED, Mask.VARARGS, Mask.STRICT, Mask.SYNTHETIC).fold(0) { a, b -> a.or(b) }
    if (classFile.constantPool.checkString(nameIndex) { it == "<init>"} && accessFlags.and(allowedFlags.inv()) != 0) {
        problems.onError("[1.5.7] If `constant_pool <1.1.5>` entry at index `name_index <1.5.2>` represents the name `<init>`, only `<!PRIVATE>`, `<!PUBLIC>`, `<!PROTECTED>`, `<!VARARGS>`, `<!STRICT>` and `<!SYNTHETIC>` flags *can* be set.")
    }
    if (!classFile.constantPool.checkString(nameIndex) { it.isUnquantifiedName() }) {
        problems.onError("[1.5.10] `constant_pool <1.1.5>` entry at index `name_index <1.5.2>` must represent a valid unqualified name [1.0.5].")
    }
    if (!classFile.constantPool.isCpIndex(descriptorIndex)) {
        problems.onError("[1.5.11] `descriptor_index <1.5.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(descriptorIndex)) {
        problems.onError("[1.5.12] `constant_pool <1.1.5>` entry at index `descriptor_index <1.5.3>` must be of type `Utf8_info <1.3.34>`.")
    }
    if (!classFile.constantPool.checkString(descriptorIndex) { it.isMethodDescriotor() }) {
        problems.onError("[1.5.13] `constant_pool <1.1.5>` entry at index `descriptor_index <1.5.3>` must represent a valid `MethodDescriptor <1.2.5>`.")
    }
    if (attributes.size != attributesCount) {
        problems.onError("[1.5.14] `attributes <1.5.5>` has size `attributes_count <1.5.4>`.")
    }
    attributes.forEach { it.validate(classFile, problems) }
}