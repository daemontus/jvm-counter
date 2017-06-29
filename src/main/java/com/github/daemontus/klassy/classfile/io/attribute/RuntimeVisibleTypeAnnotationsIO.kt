package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeVisibleTypeAnnotations
import java.io.DataInputStream
import java.io.DataOutputStream

interface RuntimeVisibleTypeAnnotationsIO : TypeAnnotationIO {

    fun DataInputStream.readRuntimeVisibleTypeAnnotations(info: AttributeInfo): RuntimeVisibleTypeAnnotations {
        val numAnnotations = readUnsignedShort()
        val annotations = Array(numAnnotations) {
            readTypeAnnotation()
        }
        return RuntimeVisibleTypeAnnotations(info.attributeNameIndex, info.attributeLength,
                numAnnotations = numAnnotations, annotations = annotations
        )
    }

    fun DataOutputStream.writeRuntimeVisibleTypeAnnotations(annotations: RuntimeVisibleTypeAnnotations) = annotations.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numAnnotations)
        this.annotations.forEach { writeTypeAnnotation(it) }
    }
}