package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeVisibleParameterAnnotations
import java.io.DataOutputStream

internal fun AttributeInfo.toRuntimeVisibleParameterAnnotations(): RuntimeVisibleParameterAnnotations = usingStream {
    val numParameterAnnotations = readUnsignedByte()
    val parameterAnnotations = Array(numParameterAnnotations) {
        val numAnnotations = readUnsignedShort()
        val annotations = Array(numAnnotations) { readAnnotation() }
        RuntimeVisibleParameterAnnotations.Entry(numAnnotations, annotations)
    }
    RuntimeVisibleParameterAnnotations(attributeNameIndex, attributeLength,
            numParameterAnnotations = numParameterAnnotations,
            parameterAnnotations = parameterAnnotations
    )
}

internal fun DataOutputStream.writeRuntimeVisibleParameterAnnotations(annotations: RuntimeVisibleParameterAnnotations) = annotations.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeByte(numParameterAnnotations)
    parameterAnnotations.forEach {
        writeShort(it.numAnnotations)
        it.annotations.forEach {
            writeAnnotation(it)
        }
    }
}