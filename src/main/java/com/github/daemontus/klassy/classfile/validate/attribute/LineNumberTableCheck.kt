package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.LINE_NUMBER_TABLE
import com.github.daemontus.klassy.classfile.SOURCE_DEBUG_EXTENSIONS
import com.github.daemontus.klassy.classfile.attribute.Code
import com.github.daemontus.klassy.classfile.attribute.LineNumberTable
import com.github.daemontus.klassy.classfile.validate.ValidationProblem
import com.github.daemontus.klassy.classfile.validate.checkString
import com.github.daemontus.klassy.classfile.validate.onError
import com.github.daemontus.klassy.classfile.validate.onWarning

fun LineNumberTable.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.17.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is Code) {
        problems.onError("[1.17.3] Appears in `attributes <1.8.10>`.")
    }
    if (!classFile.constantPool.checkString(attributeNameIndex) { it == LINE_NUMBER_TABLE }) {
        problems.onError("[1.17.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.17.1>` must be `LineNumberTable <!LINE_NUMBER_TABLE>`.")
    }
    if (lineNumberTable.size != lineNumberTableLength) {
        problems.onError("[1.17.5] Size of the `line_number_table <1.17.4>` is `line_number_table_length <1.17.3>`.")
    }
    if (parent is Code) {
        lineNumberTable.forEach {
            if (it.startPC !in parent.code.indices) {
                problems.onError("[1.17.6] `start_pc <1.17.6>` is a valid instruction index in the corresponding `code <1.8.6>`.")
            }
        }
    }
}