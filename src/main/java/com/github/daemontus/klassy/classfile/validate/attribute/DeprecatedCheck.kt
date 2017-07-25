package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.CONSTANT_VALUE
import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.DEPRECATED
import com.github.daemontus.klassy.classfile.FieldInfo
import com.github.daemontus.klassy.classfile.attribute.Deprecated
import com.github.daemontus.klassy.classfile.validate.ValidationProblem
import com.github.daemontus.klassy.classfile.validate.checkString
import com.github.daemontus.klassy.classfile.validate.onError
import com.github.daemontus.klassy.classfile.validate.onWarning

fun Deprecated.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.20.1] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is FieldInfo) {
        problems.onError("[1.20.2] Appears in `attributes <1.1.16><1.4.5><1.5.5>`.")
    }
    if (!classFile.constantPool.checkString(attributeNameIndex) { it == DEPRECATED }) {
        problems.onError("[1.20.3] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.20.1>` must be `Deprecated <!DEPRECATED>`.")
    }
    if (attributeLength != 0) {
        problems.onError("[1.20.4] `attribute_length <1.20.2>` must be `0`.")
    }
}