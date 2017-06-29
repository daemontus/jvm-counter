package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeInvisibleTypeAnnotations
import java.io.DataInputStream
import java.io.DataOutputStream

interface RuntimeInvisibleTypeAnnotationsIO : TypeAnnotationIO {

    fun DataInputStream.readRuntimeInvisibleTypeAnnotations(info: AttributeInfo): RuntimeInvisibleTypeAnnotations {
        val numAnnotations = readUnsignedShort()
        val annotations = Array(numAnnotations) {
            readTypeAnnotation()
        }
        return RuntimeInvisibleTypeAnnotations(info.attributeNameIndex, info.attributeLength,
                numAnnotations = numAnnotations, annotations = annotations
        )
    }

    fun DataOutputStream.writeRuntimeInvisibleTypeAnnotations(annotations: RuntimeInvisibleTypeAnnotations) = annotations.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numAnnotations)
        this.annotations.forEach { writeTypeAnnotation(it) }
    }

}