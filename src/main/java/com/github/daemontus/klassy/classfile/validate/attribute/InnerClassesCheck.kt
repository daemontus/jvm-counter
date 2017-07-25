package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.INNER_CLASSES
import com.github.daemontus.klassy.classfile.Mask
import com.github.daemontus.klassy.classfile.attribute.InnerClasses
import com.github.daemontus.klassy.classfile.validate.*

fun InnerClasses.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.11.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is ClassFile) {
        problems.onError("[1.11.3] Appears in `attributes <1.1.16>`.")
    }
    if (!classFile.constantPool.checkString(attributeNameIndex) { it == INNER_CLASSES }) {
        problems.onError("[1.11.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.11.1>` must be `InnerClasses <!INNER_CLASSES>`.")
    }
    if (parent is ClassFile && parent.attributes.count { it is InnerClasses } > 1) {
        problems.onError("[1.11.5] At most one inner classes attribute can be present for each class.")
    }
    if (classes.size != numberOfClasses) {
        problems.onError("[1.11.6] Size of the `classes <1.11.4>` is `number_of_classes <1.11.3>`.")
    }
    for ((innerClassInfoIndex, outerClassInfoIndex, innerNameIndex, accessFlags) in classes) {
        if (!classFile.constantPool.isCpIndex(innerClassInfoIndex)) {
            problems.onError("[1.11.7] `inner_class_info_index <1.11.5>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
        }
        if (!classFile.constantPool.checkType<CpInfo.ClassInfo>(innerClassInfoIndex)) {
            problems.onError("[1.11.8] `constant_pool <1.1.5>` entry at index `inner_class_info_index <1.11.5>` must be of type `Class_info <1.3.0>`.")
        }
        if (outerClassInfoIndex != 0 && !classFile.constantPool.isCpIndex(outerClassInfoIndex)) {
            problems.onError("[1.11.9] `outer_class_info_index <1.11.6>` is either `0` or a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
        }
        if (outerClassInfoIndex != 0 && !classFile.constantPool.checkType<CpInfo.ClassInfo>(outerClassInfoIndex)) {
            problems.onError("[1.11.10] If not zero, index `outer_class_info_index <1.11.6>` points to a `constant_pool <1.1.5>` entry of type `Class_info <1.3.0>`.")
        }
        if (innerNameIndex != 0 && !classFile.constantPool.isCpIndex(innerNameIndex)) {
            problems.onError("[1.11.11] `inner_name_index <1.11.7>` is either `0` or a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
        }
        if (innerNameIndex != 0 && !classFile.constantPool.checkType<CpInfo.Utf8Info>(innerNameIndex)) {
            problems.onError("[1.11.12] If not zero, index `inner_name_index <1.11.7>` points to a `constant_pool <1.1.5>` entry of type `Utf8_info <1.3.34>`.")
        }
        val allowedMasks = listOf(
                Mask.PUBLIC, Mask.PRIVATE, Mask.PROTECTED, Mask.STATIC, Mask.FINAL,
                Mask.INTERFACE, Mask.ABSTRACT, Mask.SYNTHETIC, Mask.ANNOTATION, Mask.ENUM
        ).fold(0) { a, b -> a.or(b) }
        if (accessFlags.and(allowedMasks.inv()) != 0) {
            problems.onWarning("[1.11.13] `inner_class_access_flags <1.11.8>` is bit mask of: <!PUBLIC> <!PRIVATE> <!PROTECTED> <!STATIC> <!FINAL> <!INTERFACE> <!ABSTRACT> <!SYNTHETIC> <!ANNOTATION> <!ENUM>")
        }
        if (classFile.version >= "51.0" && innerNameIndex == 0 && outerClassInfoIndex != 0) {
            problems.onError("[1.11.14] If `major_version.minor_version <1.1.3>.<1.1.2> >= 51.0` and `inner_name_index <1.11.7>` is `0`, `outer_class_info_index <1.11.6>` must be `0` too.")
        }
    }
}