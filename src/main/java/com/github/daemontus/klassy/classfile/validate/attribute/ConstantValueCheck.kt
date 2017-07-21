package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.FieldType.BaseType
import com.github.daemontus.klassy.classfile.FieldType.BaseType.*
import com.github.daemontus.klassy.classfile.attribute.ConstantValue
import com.github.daemontus.klassy.classfile.validate.*

fun ConstantValue.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.7.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is FieldInfo) {
        problems.onError("[1.7.3] Appears in `attributes <1.4.5>`.")
    }
    if (classFile.constantPool.checkString(attributeNameIndex) { it != CONSTANT_VALUE }) {
        problems.onError("[1.7.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.7.1>` must be `ConstantValue <!CONSTANT_VALUE>`.")
    }
    if (parent is FieldInfo && parent.attributes.count { it is ConstantValue } > 1) {
        problems.onError("[1.7.6] There may be at most one attribute of this type.")
    }
    if (!classFile.constantPool.isCpIndex(constantValueIndex)) {
        problems.onError("[1.7.6] `constantvalue_index <1.7.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    try {
        val field = parent as FieldInfo
        val fieldDescriptor = String(classFile.constantPool.getCp<CpInfo.Utf8Info>(field.descriptorIndex).bytes)
        val descriptor = fieldDescriptor.readFieldDescriptor()
        val valid = when (descriptor) {
            is B, C, I, S, Z -> classFile.constantPool.checkType<CpInfo.IntegerInfo>(constantValueIndex)
            is J -> classFile.constantPool.checkType<CpInfo.LongInfo>(constantValueIndex)
            is F -> classFile.constantPool.checkType<CpInfo.FloatInfo>(constantValueIndex)
            is D -> classFile.constantPool.checkType<CpInfo.DoubleInfo>(constantValueIndex)
            else -> false
        }
        if (!valid) error("Validation error")
    } catch (e: Exception) {
        problems.onError("problems.onError(\"[1.7.7] `contant_pool <1.1.5>` entry at index `constantvalue_index <1.7.3>` has a type matching the `FieldDescriptor <1.2.0>` of this field (int, short, char, byte and boolean are all represented as integers).\")")
    }
}