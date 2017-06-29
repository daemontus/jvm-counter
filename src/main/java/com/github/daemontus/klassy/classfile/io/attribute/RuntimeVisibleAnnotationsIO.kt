package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeVisibleAnnotations
import java.io.DataInputStream
import java.io.DataOutputStream

interface RuntimeVisibleAnnotationsIO : AnnotationIO {

    fun DataInputStream.readRuntimeVisibleAnnotations(info: AttributeInfo): RuntimeVisibleAnnotations {
        val numAnnotations = readUnsignedShort()
        val annotations = Array(numAnnotations) { readAnnotation() }
        return RuntimeVisibleAnnotations(info.attributeNameIndex, info.attributeLength,
                numAnnotations = numAnnotations,
                annotations = annotations
        )
    }

    fun DataOutputStream.writeRuntimeVisibleAnnotations(annotations: RuntimeVisibleAnnotations) = annotations.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numAnnotations)
        this.annotations.forEach { writeAnnotation(it) }
    }
}