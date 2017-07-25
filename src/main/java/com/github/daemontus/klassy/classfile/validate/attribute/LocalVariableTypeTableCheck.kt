package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.attribute.Code
import com.github.daemontus.klassy.classfile.attribute.LocalVariableTable
import com.github.daemontus.klassy.classfile.attribute.LocalVariableTypeTable
import com.github.daemontus.klassy.classfile.validate.*

fun LocalVariableTypeTable.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "49.0") {
        problems.onWarning("[1.19.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.")
    }
    if (parent !is Code) {
        problems.onError("[1.19.3] Appears in `attributes <1.8.10>`.")
    }
    if (classFile.constantPool.checkString(attributeNameIndex) { it != LOCAL_VARIABLE_TYPE_TABLE }) {
        problems.onError("[1.19.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.19.1>` must be `LocalVariableTypeTable <!LOCAL_VARIABLE_TYPE_TABLE>`.")
    }
    //TODO:
    //[1.19.5] There can be at most one local variable table *per local variable* (whatever that means).
    if (localVariableTypeTable.size != localVariableTypeTableLength) {
        problems.onError("[1.19.6] Size of the `local_variable_type_table <1.19.4>` is `local_variable_type_table_length <1.19.3>`.")
    }
    localVariableTypeTable.forEach {
        if (parent is Code) {
            if (it.startPC !in parent.code.indices) {
                problems.onError("[1.19.7] `start_pc <1.19.6>` is a valid instruction index in the corresponding `code <1.8.6>`.")
            }
            if (it.startPC + it.length !in parent.code.indices && it.startPC + it.length != parent.codeLength) {
                problems.onError("[1.19.8] `start_pc <1.19.6> + length <1.19.7>` is either a valid instruction index in the corresponding `code <1.8.6>`, or equal to `code_length <1.8.5>`.")
            }
            if (it.index >= parent.maxLocals) {
                //TODO: Long/double
                problems.onError("[1.19.15] `index <1.19.10>` is a valid index into the local variable array, i.e. `index <1.19.10> + 1 < max_locals <1.8.4>` if the descriptor is of type long/double and without the `+1` otherwise.")
            }
        }
        if (!classFile.constantPool.isCpIndex(it.nameIndex)) {
            problems.onError("[1.19.9] `name_index <1.19.8>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
        }
        if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(it.nameIndex)) {
            problems.onError("[1.19.10] `constant_pool <1.1.5>` entry at index `name_index <1.19.8>` is of type `Utf8_info <1.3.34>`.")
        }
        if (!classFile.constantPool.checkString(it.nameIndex) { it.isUnquantifiedName() }) {
            problems.onError("[1.19.11] `constant_pool <1.1.5>` entry at index `name_index <1.19.8>` is a valid unqualified name [1.0.5].")
        }
        if (!classFile.constantPool.isCpIndex(it.signatureIndex)) {
            problems.onError("[1.19.12] `siganture_index <1.19.9>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
        }
        if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(it.signatureIndex)) {
            problems.onError("[1.19.13] `constant_pool <1.1.5>` entry at index `signature_index <1.19.9>` is of type `Utf8_info <1.3.34>`.")
        }
        if (!classFile.constantPool.checkString(it.signatureIndex) { it.isFieldSignature() }) {
            problems.onError("[1.19.14] `constant_pool <1.1.5>` entry at index `signature_index <1.19.9>` is a valid `FieldSignature<1.2.24>`.")
        }
    }
}