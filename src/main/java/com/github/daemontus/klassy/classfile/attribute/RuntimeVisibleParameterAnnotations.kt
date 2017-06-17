package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class RuntimeVisibleParameterAnnotations(           //<1.23.0>
        val attributeNameIndex: Int,                //<1.23.1>
        val attributeLength: Int,                   //<1.23.2>
        val numParameterAnnotations: Int,           //<1.23.3>
        val parameterAnnotations: Array<Entry>      //<1.23.4>
) : AttributeInfo {

    class Entry(                                    //<1.23.5>
            val numAnnotations: Int,                //<1.23.6>
            val annotations: Array<Annotation>      //<1.23.7>
    )

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): RuntimeVisibleParameterAnnotations = stream.run {
            val numParameterAnnotations = readUnsignedByte()
            val parameterAnnotations = Array(numParameterAnnotations) {
                val numAnnotations = readUnsignedShort()
                val annotations = Array(numAnnotations) { Annotation.read(stream) }
                Entry(numAnnotations, annotations)
            }
            RuntimeVisibleParameterAnnotations(attribute.attributeNameIndex, attribute.attributeLength,
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