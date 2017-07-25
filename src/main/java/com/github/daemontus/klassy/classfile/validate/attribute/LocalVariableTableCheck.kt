package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.attribute.Code
import com.github.daemontus.klassy.classfile.attribute.LocalVariableTable
import com.github.daemontus.klassy.classfile.validate.*

fun LocalVariableTable.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.18.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is Code) {
        problems.onError("[1.18.3] Appears in `attributes <1.8.10>`.")
    }
    if (classFile.constantPool.checkString(attributeNameIndex) { it != LOCAL_VARIABLE_TABLE }) {
        problems.onError("[1.18.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.18.1>` must be `LocalVariableTable <!LOCAL_VARIABLE_TABLE>`.")
    }
    //TODO:
    //[1.18.5] There can be at most one local variable table *per local variable* (whatever that means).
    if (localVariableTable.size != localVariableTableLength) {
        problems.onError("[1.18.6] Size of the `local_variable_table <1.18.4>` is `local_variable_table_length <1.18.3>`.")
    }
    localVariableTable.forEach {
        if (parent is Code) {
            if (it.startPC !in parent.code.indices) {
                problems.onError("[1.18.7] `start_pc <1.18.6>` is a valid instruction index in the corresponding `code <1.8.6>`.")
            }
            if (it.startPC + it.length !in parent.code.indices && it.startPC + it.length != parent.codeLength) {
                problems.onError("[1.18.8] `start_pc <1.18.6> + length <1.18.7>` is either a valid instruction index in the corresponding `code <1.8.6>`, or equal to `code_length <1.8.5>`.")
            }
            if (it.index >= parent.maxLocals) {
                //TODO: Long/double
                problems.onError("[1.18.15] `index <1.18.10>` is a valid index into the local variable array, i.e. `index <1.18.10> + 1 < max_locals <1.8.4>` if the descriptor is of type long/double and without the `+1` otherwise.")
            }
        }
        if (!classFile.constantPool.isCpIndex(it.nameIndex)) {
            problems.onError("[1.18.9] `name_index <1.18.8>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
        }
        if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(it.nameIndex)) {
            problems.onError("[1.18.10] `constant_pool <1.1.5>` entry at index `name_index <1.18.8>` is of type `Utf8_info <1.3.34>`.")
        }
        if (!classFile.constantPool.checkString(it.nameIndex) { it.isUnquantifiedName() }) {
            problems.onError("[1.18.11] `constant_pool <1.1.5>` entry at index `name_index <1.18.8>` is a valid unqualified name [1.0.5].")
        }
        if (!classFile.constantPool.isCpIndex(it.descriptorIndex)) {
            problems.onError("[1.18.12] `descriptor_index <1.18.9>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
        }
        if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(it.descriptorIndex)) {
            problems.onError("[1.18.13] `constant_pool <1.1.5>` entry at index `descriptor_index <1.18.9>` is of type `Utf8_info <1.3.34>`.")
        }
        if (!classFile.constantPool.checkString(it.descriptorIndex) { it.isFieldDescriptor() }) {
            problems.onError("[1.18.14] `constant_pool <1.1.5>` entry at index `descriptor_index <1.18.9>` is a valid `FieldDescriptor<1.2.0>`.")
        }
    }
}