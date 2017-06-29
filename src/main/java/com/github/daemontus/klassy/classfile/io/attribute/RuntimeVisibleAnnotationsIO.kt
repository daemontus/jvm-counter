package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeVisibleAnnotations
import java.io.DataOutputStream

internal fun AttributeInfo.toRuntimeVisibleAnnotations(): RuntimeVisibleAnnotations = usingStream {
    val numAnnotations = readUnsignedShort()
    val annotations = Array(numAnnotations) { readAnnotation() }
    return RuntimeVisibleAnnotations(attributeNameIndex, attributeLength,
            numAnnotations = numAnnotations,
            annotations = annotations
    )
}

internal fun DataOutputStream.writeRuntimeVisibleAnnotations(annotations: RuntimeVisibleAnnotations) = annotations.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeShort(numAnnotations)
    this.annotations.forEach { writeAnnotation(it) }
}