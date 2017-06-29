package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeVisibleParameterAnnotations
import java.io.DataInputStream
import java.io.DataOutputStream

interface RuntimeVisibleParameterAnnotationsIO : AnnotationIO {

    fun DataInputStream.readRuntimeVisibleParameterAnnotations(info: AttributeInfo): RuntimeVisibleParameterAnnotations {
        val numParameterAnnotations = readUnsignedByte()
        val parameterAnnotations = Array(numParameterAnnotations) {
            val numAnnotations = readUnsignedShort()
            val annotations = Array(numAnnotations) { readAnnotation() }
            RuntimeVisibleParameterAnnotations.Entry(numAnnotations, annotations)
        }
        return RuntimeVisibleParameterAnnotations(info.attributeNameIndex, info.attributeLength,
                numParameterAnnotations = numParameterAnnotations,
                parameterAnnotations = parameterAnnotations
        )
    }

    fun DataOutputStream.writeRuntimeVisibleParameterAnnotations(annotations: RuntimeVisibleParameterAnnotations) = annotations.run {
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