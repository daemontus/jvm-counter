package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.CODE
import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.MethodInfo
import com.github.daemontus.klassy.classfile.attribute.Code
import com.github.daemontus.klassy.classfile.attribute.StackMapTable
import com.github.daemontus.klassy.classfile.validate.*

fun Code.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.8.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is MethodInfo) {
        problems.onError("[1.8.2] Appears in `attributes <1.5.5>`.")
    }
    if (!classFile.constantPool.checkString(attributeNameIndex) { it == CODE }) {
        problems.onError("[1.8.3] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.8.1>` must be `Code <!CODE>`.")
    }
    //[1.8.4] is verified directly in MethodInfo
    if (codeLength !in 1..65535) {
        problems.onError("[1.8.5] `code_length <1.8.5>` is in `1..65535`.")
    }
    if (code.size != codeLength) {
        problems.onError("[1.8.6] `code <1.8.6>` has size `code_length <1.8.5>`.")
    }
    if (exceptionTable.size != exceptionTableLength) {
        problems.onError("[1.8.7] `exception_table <1.8.8>` has size `exception_table_length <1.8.7>`.")
    }
    if (attributes.size != attributesCount) {
        problems.onError("[1.8.8] `attributes <1.8.10>` have size `attributes_count <1.8.9>`.")
    }

    exceptionTable.forEach { it.validate(classFile, this, problems) }
    attributes.forEach { it.validate(classFile, this, problems) }
}

fun Code.ExceptionTableEntry.validate(classFile: ClassFile, code: Code, problems: MutableList<ValidationProblem>) {
    if (startPC >= endPC || startPC < 0 || startPC >= code.codeLength || endPC <= 0 || endPC > code.codeLength) {
        problems.onError("[1.8.9] `start_pc <1.8.12>` and `end_pc <1.8.13>` form a right-exclusive range in the `code <1.8.6>`, i.e. `start_pc <1.8.12> < end_pc <1.8.13>` and `start_pc <1.8.12> >= 0 && start_pc <1.8.12> < code_length <1.8.5>` and `end_pc <1.8.13> > 0 end_pc <1.8.13> <= code_length <1.8.5>`.")
    }
    if (handlerPC !in code.code.indices) {
        //TODO: This should probably be a valid instruction, right?!
        problems.onError("[1.8.10] `handler_pc <1.8.14>` must be a valid index into the `code <1.8.6>`.")
    }
    if (catchType != 0 && !classFile.constantPool.isCpIndex(catchType)) {
        problems.onError("[1.8.11] `catch_type <1.8.15>` is either `0` or a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (catchType != 0 && !classFile.constantPool.checkType<CpInfo.ClassInfo>(catchType)) {
        problems.onError("[1.8.12] If not `0`, `constant_pool <1.1.5>` entry at index `catch_type <1.8.15>` must be of type `Class_info <1.3.0>`.")
    }
}