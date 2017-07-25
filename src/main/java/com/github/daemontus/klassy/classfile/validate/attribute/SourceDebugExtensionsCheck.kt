package com.github.daemontus.klassy.classfile.validate.attribute

import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.SOURCE_DEBUG_EXTENSIONS
import com.github.daemontus.klassy.classfile.SOURCE_FILE
import com.github.daemontus.klassy.classfile.attribute.SourceDebugExtensions
import com.github.daemontus.klassy.classfile.validate.ValidationProblem
import com.github.daemontus.klassy.classfile.validate.checkString
import com.github.daemontus.klassy.classfile.validate.onError
import com.github.daemontus.klassy.classfile.validate.onWarning

fun SourceDebugExtensions.validate(classFile: ClassFile, parent: Any, problems: MutableList<ValidationProblem>) {
    if (classFile.version < "49.0") {
        problems.onWarning("[1.16.2] Appears since `major_version.minor_version <1.1.3>.<1.1.2> >= 49.0`.")
    }
    if (parent !is ClassFile) {
        problems.onError("[1.16.3] Appears in `attributes <1.1.16>`.")
    }
    if (!classFile.constantPool.checkString(attributeNameIndex) { it == SOURCE_DEBUG_EXTENSIONS }) {
        problems.onError("[1.16.4] `constant_pool <1.1.5>` entry at index `attribute_name_index <1.16.1>` must be `SourceDebugExtensions <!SOURCE_DEBUG_EXTENSIONS>`.")
    }
    if (parent is ClassFile && parent.attributes.count { it is SourceDebugExtensions } > 1) {
        problems.onError("[1.16.6] At most one source debug extension attribute can be present for each class. ")
    }
    if (attributeLength != debugExtension.size) {
        problems.onError("[1.16.5] Size of the `debug_extension <1.16.3>` is `attribute_length <1.16.2>`.")
    }
}