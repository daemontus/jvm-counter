package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeVisibleTypeAnnotations
import java.io.DataOutputStream

internal fun AttributeInfo.toRuntimeVisibleTypeAnnotations(): RuntimeVisibleTypeAnnotations = usingStream {
    val numAnnotations = readUnsignedShort()
    val annotations = Array(numAnnotations) {
        readTypeAnnotation()
    }
    RuntimeVisibleTypeAnnotations(attributeNameIndex, attributeLength,
            numAnnotations = numAnnotations, annotations = annotations
    )
}

internal fun DataOutputStream.writeRuntimeVisibleTypeAnnotations(annotations: RuntimeVisibleTypeAnnotations) = annotations.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeShort(numAnnotations)
    this.annotations.forEach { writeTypeAnnotation(it) }
}