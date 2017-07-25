package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.ENCLOSING_METHOD
import com.github.daemontus.klassy.classfile.INNER_CLASSES
import com.github.daemontus.klassy.classfile.attribute.EnclosingMethod
import com.github.daemontus.klassy.classfile.attribute.InnerClasses
import com.github.daemontus.klassy.classfile.validate.*

fun EnclosingMethod.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "49.0") {
        problems.onWarning("[1.12.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.")
    }
    if (parent !is ClassFile) {
        problems.onError("[1.12.3] Appears in `attributes <1.1.16>`.")
    }
    if (!classFile.constantPool.checkString(attributeNameIndex) { it == ENCLOSING_METHOD }) {
        problems.onError("[1.12.5] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.12.1>` must be `EnclosingMethod <!ENCLOSING_METHOD>`.")
    }
    if (parent is ClassFile && parent.attributes.count { it is EnclosingMethod } > 1) {
        problems.onError("[1.12.4] At most one enclosing method attribute can be present for each class.")
    }
    if (!classFile.constantPool.isCpIndex(classIndex)) {
        problems.onError("[1.12.6] `class_index <1.12.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.ClassInfo>(classIndex)) {
        problems.onError("[1.12.7] `constant_pool <1.1.5>` entry at index `class_index <1.12.3>` must be of type `Class_info <1.3.0>`.")
    }
    if (!classFile.constantPool.isCpIndex(methodIndex)) {
        problems.onError("[1.12.8] `method_index <1.12.4>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.NameAndTypeInfo>(methodIndex)) {
        problems.onError("[1.12.9] `constant_pool <1.1.5>` entry at index `method_index <1.12.4>` must be of type `NameAndType_info <1.3.30>`.")
    }

}