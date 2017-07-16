package com.github.daemontus.klassy.classfile.validate

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.attribute.BootstrapMethods

internal fun CpInfo.validate(classFile: ClassFile, problems: MutableList<ValidationProblem>) {
    val cp = classFile.constantPool
    when (this) {
        is CpInfo.Utf8Info -> {
            if (tag != CONST_Utf8) problems.onError("[1.3.1] `tag <1.3.35>` has the value `1 <!CONST_Utf8>`.")
            if (bytes.size != length) {
                problems.onError("[1.3.31] `bytes <1.3.37>` is of size `length <1.3.36>`.")
            }
            // TODO: [1.3.32] `bytes <1.3.37` represent a *modified UTF-8 string* (definition needed).
        }
        is CpInfo.IntegerInfo -> {
            if (tag != CONST_Integer) problems.onError("[1.3.2] `tag <1.3.19>` has the value `3 <!CONST_Integer>`.")
        }
        is CpInfo.FloatInfo -> {
            if (tag != CONST_Float) problems.onError("[1.3.3] `tag <1.3.22>` has the value `4 <!CONST_Float>`.")
        }
        is CpInfo.LongInfo -> {
            if (tag != CONST_Long) problems.onError("[1.3.4] `tag <1.3.25>` has the value `5 <!CONST_Long>`.")
        }
        is CpInfo.DoubleInfo -> {
            if (tag != CONST_Double) problems.onError("[1.3.5] `tag <1.3.28>` has the value `6 <!CONST_Double>`.")
        }
        is CpInfo.ClassInfo -> {
            if (tag != CONST_Class) problems.onError("[1.3.6] `tag <1.3.1>` has the value `7 <!CONST_Class>`.")
            if (!cp.isCpIndex(nameIndex)) {
                problems.onError("[1.3.15] `name_index <1.3.2>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.Utf8Info>(nameIndex)) {
                problems.onError("[1.3.16] `constant_pool <1.1.5>` entry at index `name_index <1.3.2>` must be of type `Utf8_info <1.3.34>` .")
            }
            if (!cp.checkString(nameIndex) { it.isFullyQuantifiedName() || it.isArrayType() }) {
                problems.onError("[1.3.17] `constant_pool <1.1.5>` entry at index `name_index <1.3.2>` must represent a fully quantified binary name [1.0.7] or an `ArrayType <1.2.4>`.")
            }
        }
        is CpInfo.StringInfo -> {
            if (tag != CONST_String) problems.onError("[1.3.7] `tag <1.3.16>` has the value `8 <!CONST_String>`.")
            if (!cp.isCpIndex(stringIndex)) {
                problems.onError("[1.3.25] `string_index <1.3.17>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.Utf8Info>(stringIndex)) {
                problems.onError("[1.3.26] `constant_pool <1.1.5>` entry at `string_index <1.3.17>` must be of type `Utf8_info <1.3.34>`.")
            }
        }
        is CpInfo.FieldRefInfo -> {
            if (tag != CONST_FieldRef) problems.onError("[1.3.8] `tag <1.3.4>` has the value `9 <!CONST_Fieldref>`.")
            if (!cp.isCpIndex(classIndex)) {
                problems.onError("[1.3.19] `class_index <1.3.5>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.ClassInfo>(classIndex)) {
                problems.onError("[1.3.20] `constant_pool <1.1.5>` entry at index `class_index <1.3.5>` must be of type `Class_info <1.3.0>`.")
            }
            if (!cp.isCpIndex(nameAndTypeIndex)) {
                problems.onError("[1.3.21] `name_and_type_index <1.3.6>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.NameAndTypeInfo>(nameAndTypeIndex)) {
                problems.onError("[1.3.22] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.6>` must be of type `NameAndType_info <1.3.30>`.")
            }
            if (!cp.hasDescriptorType(nameAndTypeIndex) { it.isFieldDescriptor() }) {
                problems.onError("[1.3.23] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.6>` must have a `FieldDescriptor <1.2.0>` as type.")
            }
        }
        is CpInfo.MethodRefInfo -> {
            if (tag != CONST_MethodRef) problems.onError("[1.3.9] `tag <1.3.8>` has the value `10 <!CONST_Methodref>`.")
            if (!cp.isCpIndex(classIndex)) {
                problems.onError("[1.3.19] `class_index <1.3.9>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.ClassInfo>(classIndex)) {
                problems.onError("[1.3.20] `constant_pool <1.1.5>` entry at index `class_index <1.3.9>` must be of type `Class_info <1.3.0>`.")
            }
            if (!cp.isCpIndex(nameAndTypeIndex)) {
                problems.onError("[1.3.21] `name_and_type_index <1.3.10>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.NameAndTypeInfo>(nameAndTypeIndex)) {
                problems.onError("[1.3.22] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.10>` must be of type `NameAndType_info <1.3.30>`.")
            }
            if (!cp.hasDescriptorType(nameAndTypeIndex) { it.isMethodDescriotor() }) {
                problems.onError("[1.3.24] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.10>` must have a `MethodDescriptor <1.2.5>` as type.")
            }
        }
        is CpInfo.InterfaceMethodRefInfo -> {
            if (tag != CONST_InterfaceMethodRef) problems.onError("[1.3.10] `tag <1.3.12>` has the value `11 <!CONST_InterfaceMethodref>`.")
            if (!cp.isCpIndex(classIndex)) {
                problems.onError("[1.3.19] `class_index <1.3.13>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.ClassInfo>(classIndex)) {
                problems.onError("[1.3.20] `constant_pool <1.1.5>` entry at index `class_index <1.3.13>` must be of type `Class_info <1.3.0>`.")
            }
            if (!cp.isCpIndex(nameAndTypeIndex)) {
                problems.onError("[1.3.21] `name_and_type_index <1.3.14>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.NameAndTypeInfo>(nameAndTypeIndex)) {
                problems.onError("[1.3.22] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.14>` must be of type `NameAndType_info <1.3.30>`.")
            }
            if (!cp.hasDescriptorType(nameAndTypeIndex) { it.isMethodDescriotor() }) {
                problems.onError("[1.3.24] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.14>` must have a `MethodDescriptor <1.2.5>` as type.")
            }
        }
        is CpInfo.NameAndTypeInfo -> {
            if (tag != CONST_NameAndType) problems.onError("[1.3.11] `tag <1.3.31>` has the value `12 <!CONST_NameAndType>`.")
            if (!cp.isCpIndex(nameIndex)) {
                problems.onError("[1.3.15] `name_index <1.3.32>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.Utf8Info>(nameIndex)) {
                problems.onError("[1.3.16] `constant_pool <1.1.5>` entry at index `name_index <1.3.32>` must be of type `Utf8_info <1.3.34>` .")
            }
            if (!cp.checkString(nameIndex) { it.isUnquantifiedName() }) {
                problems.onError("[1.3.18] `constant_pool <1.1.5>` entry at index `name_index <1.3.32>` must represent a valid unqualified name [1.0.5].")
            }
            if (!cp.isCpIndex(descriptorIndex)) {
                problems.onError("[1.3.27] `descriptor_index <1.3.33>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.Utf8Info>(descriptorIndex)) {
                problems.onError("[1.3.28] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.33><1.3.44>` must be of type `Utf8_info <1.3.34>`.")
            }
            if (!cp.checkString(descriptorIndex) { it.isFieldDescriptor() || it.isMethodDescriotor() }) {
                problems.onError("[1.3.29] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.33>` must represent a valid `FieldDescriptor <1.2.0>` or `MethodDescriptor <1.2.5>`.")
            }
        }
        is CpInfo.MethodHandleInfo -> {
            if (tag != CONST_MethodHandle) problems.onError("[1.3.12] `tag <1.3.39>` has the value `15 <!CONST_MethodHandle>`.")
            if (referenceKind !in 1..9) {
                problems.onError("[1.3.33] `reference_kind <1.3.40>` must be in range `1..9`.")
            }
            if (!cp.isCpIndex(referenceIndex)) {
                problems.onError("[1.3.34] `reference_index <1.3.41>` must be a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (referenceKind in 1..4 && !cp.checkType<CpInfo.FieldRefInfo>(referenceIndex)) {
                problems.onError("[1.3.35] If `reference_kind <1.3.40>` is in range `1..4`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Fieldref_info <1.3.3>`.")
            }
            if (referenceKind in listOf(5,8) && !cp.checkType<CpInfo.MethodRefInfo>(referenceIndex)) {
                problems.onError("[1.3.36] If `reference_kind <1.3.40>` is `5` or `8`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Methodref_info <1.3.7>`.")
            }
            // (!52 && !M) || (52 && !M && !I)
            val is52plus = classFile.majorVersion >= 52
            val isMethod = cp.checkType<CpInfo.MethodRefInfo>(referenceIndex)
            val isInterface = cp.checkType<CpInfo.InterfaceMethodRefInfo>(referenceIndex)
            if (referenceKind in listOf(6,7) && ((!is52plus && !isMethod) || (is52plus && !isMethod && !isInterface))) {
                problems.onError("[1.3.37] If `reference_kind <1.3.40>` is `6` or `7` and `major_version <1.1.2>` is less than `52`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `Methodref_info <1.3.7>`. If `major_version <1.1.2> >= 52`, it can also be `InterfaceMethodref_info <1.3.11>`.")
            }
            if (referenceKind == 9 && !cp.checkType<CpInfo.InterfaceMethodRefInfo>(referenceIndex)) {
                problems.onError("[1.3.38] If `reference_kind <1.3.40>` is `9`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must be of type `InterfaceMethodref_info <1.3.11>`.")
            }
            if (referenceKind in listOf(5,6,7,9) && cp.checkReferenceName(referenceIndex) { name ->
                name == "<init>" || name == "<clinit>"
            }) {
                problems.onError("[1.3.39] If `reference_kind <1.3.40>` is in `[5,6,7,9]`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must not have a name `<init>` or `<clinit>`.")
            }
            if (referenceKind == 8 && cp.checkReferenceName(referenceIndex) { it != "<init>" }) {
                problems.onError("[1.3.40] If `reference_kind <1.3.40>` is `8`, `constant_pool <1.1.5>` entry at index `reference_index <1.3.41>` must have a name `<init>`.")
            }
        }
        is CpInfo.MethodTypeInfo -> {
            if (tag != CONST_MethodType) problems.onError("[1.3.13] `tag_<1.3.43>` has the value `16 <!CONST_MethodType>`.")
            if (!cp.isCpIndex(descriptorIndex)) {
                problems.onError("[1.3.27] `descriptor_index <1.3.44>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.Utf8Info>(descriptorIndex)) {
                problems.onError("[1.3.28] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.44>` must be of type `Utf8_info <1.3.34>`.")
            }
            if (!cp.checkString(descriptorIndex) { it.isMethodDescriotor() }) {
                problems.onError("[1.3.30] `constant_pool <1.1.5>` entry at index `descriptor_index <1.3.44>` must represent a valid `MethodDescriptor <1.2.5>`.")
            }
        }
        is CpInfo.InvokeDynamicInfo -> {
            if (tag != CONST_InvokeDynamic) problems.onError("[1.3.14] `tag_<1.3.44>` has the value `18 <!CONST_InvokeDynamic>`.")
            if (!cp.isCpIndex(nameAndTypeIndex)) {
                problems.onError("[1.3.21] `name_and_type_index <1.3.48>` is a valid index [1.0.4] into `constant_pool <1.1.5>`.")
            }
            if (!cp.checkType<CpInfo.NameAndTypeInfo>(nameAndTypeIndex)) {
                problems.onError("[1.3.22] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.48>` must be of type `NameAndType_info <1.3.30>`.")
            }
            if (!cp.hasDescriptorType(nameAndTypeIndex) { it.isMethodDescriotor() }) {
                problems.onError("[1.3.24] `constant_pool <1.1.5>` entry at index `name_and_type_index <1.3.48>` must have a `MethodDescriptor <1.2.5>` as type.")
            }
            try {
                val bootstrapAttr = classFile.attributes.find { it is BootstrapMethods } as BootstrapMethods
                if (bootstrapMethodAttrIndex !in bootstrapAttr.bootstrapMethods.indices) error("Validation failed")
            } catch (e: Exception) {
                problems.onError("[1.3.41] `bootstrap_method_attr_index <1.3.47>` is a valid index into the `bootstrap_methods <1.28.4>`.")
            }
        }
    }
}