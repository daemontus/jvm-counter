package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.FieldInfo
import com.github.daemontus.klassy.classfile.MethodInfo
import com.github.daemontus.klassy.classfile.SYNTHETIC
import com.github.daemontus.klassy.classfile.attribute.Code
import com.github.daemontus.klassy.classfile.attribute.Synthetic
import com.github.daemontus.klassy.classfile.validate.ValidationProblem
import com.github.daemontus.klassy.classfile.validate.checkString
import com.github.daemontus.klassy.classfile.validate.onError
import com.github.daemontus.klassy.classfile.validate.onWarning

fun Synthetic.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.13.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is ClassFile && parent !is FieldInfo && parent !is MethodInfo && parent !is Code) {
        problems.onError("[1.13.3] Appears in `attributes <1.1.16><1.4.5><1.5.5><1.8.10>`.")
    }
    if (classFile.constantPool.checkString(attributeNameIndex) { it != SYNTHETIC }) {
        problems.onError("[1.13.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.13.1>` must be `Synthetic <!SYNTHETIC>`.")
    }
    if (attributeLength != 0) {
        problems.onError("[1.13.5] `attribute_length <1.13.2>` must be `0`.")
    }
}