package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.EXCEPTIONS
import com.github.daemontus.klassy.classfile.MethodInfo
import com.github.daemontus.klassy.classfile.attribute.Exceptions
import com.github.daemontus.klassy.classfile.validate.*

fun Exceptions.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.10.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is MethodInfo) {
        problems.onError("[1.10.3] Appears in `attributes <1.5.5>`.")
    }
    if (classFile.constantPool.checkString(attributeNameIndex) { it != EXCEPTIONS }) {
        problems.onError("[1.10.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.10.1>` must be `Exceptions <!EXCEPTIONS>`.")
    }
    if (parent is MethodInfo && parent.attributes.count { it is Exceptions } > 1) {
        problems.onError("[1.10.5] At most one exceptions attribute can be present for each method.")
    }
    if (exceptionsIndexTable.size != numberOfExceptions) {
        problems.onError("[1.10.6] Size of the `exception_index_table <1.10.4>` is `number_of_exceptions <1.10.3>`.")
    }
    if (exceptionsIndexTable.any { !classFile.constantPool.isCpIndex(it) }) {
        problems.onError("[1.10.7] Every `exception_index_table <1.10.4>` entry is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (exceptionsIndexTable.any { !classFile.constantPool.checkType<CpInfo.ClassInfo>(it) }) {
        problems.onError("[1.10.8] Every `exception_index_table <1.10.4>` entry corresponds to a `constant_pool <1.1.5>` entry of type `Class_info <1.3.0>`.")
    }
}