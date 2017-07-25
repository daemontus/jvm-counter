package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.attribute.SourceFile
import com.github.daemontus.klassy.classfile.validate.*

fun SourceFile.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "45.3") {
        problems.onWarning("[1.15.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 45.3`.")
    }
    if (parent !is ClassFile) {
        problems.onError("[1.15.3] Appears in `attributes <1.1.16>`.")
    }
    if (classFile.constantPool.checkString(attributeNameIndex) { it != SOURCE_FILE }) {
        problems.onError("[1.15.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.15.1>` must be `SourceFile <!SOURCE_FILE>`.")
    }
    if (!classFile.constantPool.isCpIndex(sourceFileIndex)) {
        problems.onError("[1.15.5] `sourcefile_index <1.15.3>` is a valid index [1.0.4] into the `constant_pool <1.1.5>`.")
    }
    if (!classFile.constantPool.checkType<CpInfo.Utf8Info>(sourceFileIndex)) {
        problems.onError("[1.15.6] `constant_pool <1.1.5>` entry at index `sourcefile_index <1.15.3>` must be of type `Utf8_info <1.3.34>`.")
    }
    if (parent is ClassFile && parent.attributes.count { it is SourceFile } > 1) {
        problems.onError("[1.15.7] At most one source file attribute can be present for each class.")
    }
}