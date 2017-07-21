package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.STACK_MAP_TABLE
import com.github.daemontus.klassy.classfile.attribute.Code
import com.github.daemontus.klassy.classfile.attribute.StackMapTable
import com.github.daemontus.klassy.classfile.attribute.StackMapTable.*
import com.github.daemontus.klassy.classfile.validate.*

fun StackMapTable.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "50.0") {
        problems.onWarning("[1.9.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 50.0`.")
    }
    if (parent !is Code) {
        problems.onError("[1.9.3] Appears in `attributes <1.8.10>`.")
    }
    if (classFile.constantPool.checkString(attributeNameIndex) { it != STACK_MAP_TABLE }) {
        problems.onError("[1.9.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.9.1>` must be `StackMapTable <!STACK_MAP_TABLE>`.")
    }
    if (parent is Code && parent.attributes.count { it is StackMapTable } > 1) {
        problems.onError("[1.9.5] At most one stack map table can be present for each method.")
    }
    if (entries.size != numberOfEntries) {
        problems.onError("[1.9.6] `entries <1.9.4>` has size `number_of_entries <1.9.3>`.")
    }

    entries.forEach { it.validate(classFile, parent, problems) }
}

fun StackMapTable.Frame.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    when (this) {
        is Frame.SameFrame -> {
            if (frameType !in 0..63) {
                problems.onError("[1.9.19] `frame_type <1.9.6>` is in range `0..63`.")
            }
        }
        is Frame.SameLocalsOneStackItemFrame -> {
            if (frameType !in 64..127) {
                problems.onError("[1.9.20] `frame_type <1.9.8>` is in range `64..127`.")
            }
            this.stack.validate(classFile, parent, problems)
        }
        is Frame.SameLocalsOneStackItemFrameExtended -> {
            if (frameType != 247) {
                problems.onError("[1.9.21] `frame_type <1.9.11>` is `247`.")
            }
            this.stack.validate(classFile, parent, problems)
        }
        is Frame.ChopFrame -> {
            if (frameType !in 248..250) {
                problems.onError("[1.9.22] `frame_type <1.9.15>` is in range `248..250`.")
            }
        }
        is Frame.SameFrameExtended -> {
            if (frameType != 251) {
                problems.onError("[1.9.23] `frame_type <1.9.18>` is `251`.")
            }
        }
        is Frame.AppendFrame -> {
            if (frameType !in 252..254) {
                problems.onError("[1.9.24] `frame_type <1.9.21>` is in range `252..254`.")
            }
            if (locals.size != frameType - 251) {
                problems.onError("[1.9.26] The size of `locals <1.9.23>` is `frame_type <1.9.21> - 251`.")
            }
            locals.forEach { it.validate(classFile, parent, problems) }
        }
        is Frame.FullFrame -> {
            if (frameType != 255) {
                problems.onError("[1.9.25] `frame_type <1.9.25>` is `255`.")
            }
            if (locals.size != numberOfLocals) {
                problems.onError("[1.9.27] The size of `locals <1.9.28>` is `number_of_locals <1.9.27>`.")
            }
            if (stack.size != numberOfStackItems) {
                problems.onError("[1.9.28] The size of `stack <1.9.30>` is `numer_of_stack_items <1.9.29>`.")
            }
            locals.forEach { it.validate(classFile, parent, problems) }
            stack.forEach { it.validate(classFile, parent, problems) }
        }
    }
}

fun StackMapTable.VerificationType.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    val expectedTag = when (this) {
        is VerificationType.Top -> 0x0
        is VerificationType.Integer -> 0x1
        is VerificationType.Float -> 0x2
        is VerificationType.Double -> 0x3
        is VerificationType.Long -> 0x4
        is VerificationType.Null -> 0x5
        is VerificationType.UninitializedThis -> 0x6
        is VerificationType.Object -> 0x7
        is VerificationType.UninitializedVariable -> 0x8
    }
    if (tag != expectedTag) {
        problems.onError("[1.9.7-15] `tag` $tag does not match expected value $expectedTag.")
    }
    if (this is VerificationType.Object) {
        if (!classFile.constantPool.isCpIndex(constantPoolIndex)) {
            problems.onError("[1.9.16] `cpool_index <1.9.43>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
        }
        if (!classFile.constantPool.checkType<CpInfo.ClassInfo>(constantPoolIndex)) {
            problems.onError("[1.9.17] `constant_pool <1.1.5>` entry at index `cpool_index <1.9.43>` must be of type `Class_info <1.3.0>`.")
        }
    }
    if (parent is Code && this is VerificationType.UninitializedVariable && this.offset !in parent.code.indices) {
        problems.onError("[1.9.18] `offset <1.9.46>` must be a valid index into the `code <1.8.6>` of parent code attribute.")
    }
}