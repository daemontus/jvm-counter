package com.github.daemontus.klassy.classfile.validate

import com.github.daemontus.klassy.classfile.*

fun FieldInfo.validate(classFile: ClassFile, problems: MutableList<ValidationProblem>) {
    val isVolatile = accessFlags.hasMask(Mask.VOLATILE)
    val isFinal = accessFlags.hasMask(Mask.FINAL)
    val isPublic = accessFlags.hasMask(Mask.PUBLIC)
    val isPrivate = accessFlags.hasMask(Mask.PRIVATE)
    val isProtected = accessFlags.hasMask(Mask.PROTECTED)
    val isStatic = accessFlags.hasMask(Mask.STATIC)
    if (isVolatile && isFinal) {
        problems.onError("[1.4.2] In `access_flags <1.4.1>`, `<!VOLATILE>` and `<!FINAL>` *cannot* be set at the same time.")
    }
    if ((isPublic && isPrivate) || (isPrivate && isProtected) || (isPublic && isProtected)) {
        problems.onError("[1.4.3] In `access_flags <1.4.1>`, at most one of `<!PUBLIC>`, `<!PRIVATE>`, `<!PROTECTED>` can be set.")
    }
    if (classFile.accessFlags.hasMask(Mask.INTERFACE) && (!isPublic || !isStatic || !isFinal)) {
        problems.onError("[1.4.4] If `access_flags <1.1.6>` (class) has `<!INTERFACE>` set, then in `access_flags <1.4.1>`, `<!PUBLIC>`, `<!STATIC>` and `<!FINAL>` *must* be set.")
    }
    val allowedFlags = listOf(Mask.PUBLIC, Mask.STATIC, Mask.FINAL, Mask.SYNTHETIC).fold(0) { a, b -> a.or(b) }
    if (classFile.accessFlags.hasMask(Mask.INTERFACE) && accessFlags.and(allowedFlags.inv()) != 0) {
        problems.onError("[1.4.5] If `access_flags <1.1.6>` (class) has `<!INTERFACE>` set, then in `access_flags <1.4.1>`, only `<!PUBLIC>`, `<!STATIC>`, `<!FINAL>` and `<!SYNTHETIC>` *can* be set.")
    }
    if (!classFile.constantPool.isCpIndex(nameIndex)) {
        problems.onError("[1.4.6] `name_index <1.4.2>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(nameIndex)) {
        problems.onError("[1.4.7] `constant_pool <1.1.5>` entry at index `name_index <1.4.2>` must be of type `Utf8_info <1.3.34>`.")
    }
    if (!classFile.constantPool.checkString(nameIndex) { it.isUnquantifiedName() }) {
        problems.onError("[1.4.8] `constant_pool <1.1.5>` entry at index `name_index <1.4.2>` must represent a valid unqualified name [1.0.5].")
    }
    if (!classFile.constantPool.isCpIndex(descriptorIndex)) {
        problems.onError("[1.4.9] `descriptor_index <1.4.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(descriptorIndex)) {
        problems.onError("[1.4.7] `constant_pool <1.1.5>` entry at index `descriptor_index <1.4.3>` must be of type `Utf8_info <1.3.34>`.")
    }
    if (!classFile.constantPool.checkString(descriptorIndex) { it.isUnquantifiedName() }) {
        problems.onError("[1.4.8] `constant_pool <1.1.5>` entry at index `descriptor_index <1.4.3>` must represent a valid `FieldDescriptor <1.2.0>`.")
    }
    if (attributes.size != attributesCount) {
        problems.onError("[1.4.9] `attributes <1.4.5>` has size `attributes_count <1.4.4>`.")
    }
    this.attributes.forEach { it.validate(classFile, problems) }
}