package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeInvisibleAnnotations
import java.io.DataInputStream
import java.io.DataOutputStream

interface RuntimeInvisibleAnnotationsIO : AnnotationIO {

    fun DataInputStream.readRuntimeInvisibleAnnotations(info: AttributeInfo): RuntimeInvisibleAnnotations {
        val numAnnotations = readUnsignedShort()
        val annotations = Array(numAnnotations) { readAnnotation() }
        return RuntimeInvisibleAnnotations(info.attributeNameIndex, info.attributeLength,
                numAnnotations = numAnnotations,
                annotations = annotations
        )
    }

    fun DataOutputStream.writeRuntimeInvisibleAnnotations(annotations: RuntimeInvisibleAnnotations)= annotations.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numAnnotations)
        this.annotations.forEach { writeAnnotation(it) }
    }

}