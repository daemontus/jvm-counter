package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.attribute.Signature
import com.github.daemontus.klassy.classfile.validate.*

fun Signature.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "49.0") {
        problems.onWarning("[1.14.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.")
    }
    if (parent !is ClassFile && parent !is FieldInfo && parent !is MethodInfo) {
        problems.onError("[1.14.3] Appears in `attributes <1.1.16><1.4.5><1.5.5>`.")
    }
    if (classFile.constantPool.checkString(attributeNameIndex) { it != SIGNATURE }) {
        problems.onError("[1.14.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.14.1>` must be `Signature <!Signature>`.")
    }
    if (!classFile.constantPool.isCpIndex(signatureIndex)) {
        problems.onError("[1.14.5] `signature_index <1.14.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(signatureIndex)) {
        problems.onError("[1.14.6] `constant_pool <1.1.5>` entry at index `signature_index <1.14.3>` must be of type `Utf8_info <1.3.34>`.")
    }
    if (    (parent is ClassFile && classFile.constantPool.checkString(signatureIndex) { !it.isClassSignature() })
            || (parent is FieldInfo && classFile.constantPool.checkString(signatureIndex) { !it.isFieldSignature() })
            || (parent is MethodInfo && classFile.constantPool.checkString(signatureIndex) { !it.isMethodSignature() })
    ) {
        problems.onError("[1.14.7] `constant_pool <1.1.5>` entry at index `signature_index <1.14.3>` must represent a `ClassSignature <1.1.17>`, `MethodSignature <1.1.21>` or `FieldSignature <1.1.24>` in accordance with the location of this attribute.")
    }
}