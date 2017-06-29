package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeInvisibleTypeAnnotations
import java.io.DataOutputStream

internal fun AttributeInfo.toRuntimeInvisibleTypeAnnotations(): RuntimeInvisibleTypeAnnotations = usingStream {
    val numAnnotations = readUnsignedShort()
    val annotations = Array(numAnnotations) {
        readTypeAnnotation()
    }
    RuntimeInvisibleTypeAnnotations(attributeNameIndex, attributeLength,
            numAnnotations = numAnnotations, annotations = annotations
    )
}

internal fun DataOutputStream.writeRuntimeInvisibleTypeAnnotations(annotations: RuntimeInvisibleTypeAnnotations) = annotations.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeShort(numAnnotations)
    this.annotations.forEach { writeTypeAnnotation(it) }
}