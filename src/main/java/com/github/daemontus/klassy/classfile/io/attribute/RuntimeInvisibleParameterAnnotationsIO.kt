package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeInvisibleParameterAnnotations
import java.io.DataInputStream
import java.io.DataOutputStream

interface RuntimeInvisibleParameterAnnotationsIO : AnnotationIO {

    fun DataInputStream.readRuntimeInvisibleParameterAnnotations(info: AttributeInfo): RuntimeInvisibleParameterAnnotations {
        val numParameterAnnotations = readUnsignedByte()
        val parameterAnnotations = Array(numParameterAnnotations) {
            val numAnnotations = readUnsignedShort()
            val annotations = Array(numAnnotations) { readAnnotation() }
            RuntimeInvisibleParameterAnnotations.Entry(numAnnotations, annotations)
        }
        return RuntimeInvisibleParameterAnnotations(info.attributeNameIndex, info.attributeLength,
                numParameterAnnotations = numParameterAnnotations,
                parameterAnnotations = parameterAnnotations
        )
    }

    fun DataOutputStream.writeRuntimeInvisibleParameterAnnotations(annotations: RuntimeInvisibleParameterAnnotations) = annotations.run {
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

}