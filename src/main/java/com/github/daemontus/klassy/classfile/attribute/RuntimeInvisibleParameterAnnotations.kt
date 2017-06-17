package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class RuntimeInvisibleParameterAnnotations(         //<1.24.0>
        val attributeNameIndex: Int,                //<1.24.1>
        val attributeLength: Int,                   //<1.24.2>
        val numParameterAnnotations: Int,           //<1.24.3>
        val parameterAnnotations: Array<Entry>      //<1.24.4>
) : AttributeInfo {

    class Entry(                                    //<1.24.5>
            val numAnnotations: Int,                //<1.24.6>
            val annotations: Array<Annotation>      //<1.24.7>
    )

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): RuntimeInvisibleParameterAnnotations = stream.run {
            val numParameterAnnotations = readUnsignedByte()
            val parameterAnnotations = Array(numParameterAnnotations) {
                val numAnnotations = readUnsignedShort()
                val annotations = Array(numAnnotations) { Annotation.read(stream) }
                Entry(numAnnotations, annotations)
            }
            RuntimeInvisibleParameterAnnotations(attribute.attributeNameIndex, attribute.attributeLength,
                    numParameterAnnotations = numParameterAnnotations,
                    parameterAnnotations = parameterAnnotations
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeByte(numParameterAnnotations)
        parameterAnnotations.forEach {
            writeShort(it.numAnnotations)
            it.annotations.forEach {
                it.write(stream)
            }
        }
    }
}