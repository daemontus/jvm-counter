package com.github.daemontus.klassy.classfile.validate

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.attribute.*
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
        is Code -> this.validate(classFile, parent, problems)
        is EnclosingMethod -> this.validate(classFile, parent, problems)
        is Exceptions -> this.validate(classFile, parent, problems)
        is InnerClasses -> this.validate(classFile, parent, problems)
        is LineNumberTable -> this.validate(classFile, parent, problems)
        is Signature -> this.validate(classFile, parent, problems)
        is SourceDebugExtensions -> this.validate(classFile, parent, problems)
        is SourceFile -> this.validate(classFile, parent, problems)
        is StackMapTable -> this.validate(classFile, parent, problems)
        is Synthetic -> this.validate(classFile, parent, problems)
        is LocalVariableTable -> this.validate(classFile, parent, problems)
        is LocalVariableTypeTable -> this.validate(classFile, parent, problems)
    }
}