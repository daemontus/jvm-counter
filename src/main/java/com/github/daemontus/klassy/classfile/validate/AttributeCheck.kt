package com.github.daemontus.klassy.classfile.validate

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.attribute.ConstantValue
import com.github.daemontus.klassy.classfile.validate.attribute.validate

fun Attribute.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (!classFile.constantPool.isCpIndex(attributeNameIndex)) {
        problems.onError("[1.6.1] `attribute_name_index <1.6.1>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(attributeNameIndex)) {
        problems.onError("[1.6.2] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.6.1>` must be of type `Utf8_info <1.3.34>`.")
    }
    when (this) {
        is AttributeInfo -> {
            if (this.info.size != attributeLength) {
                problems.onError("[1.6.3] `info <1.6.3>` has size `attribute_length <1.6.2>`.")
            }
        }
        is ConstantValue -> this.validate(classFile, parent, problems)
    }
}