package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class RuntimeVisibleParameterAnnotations(           //<1.23.0>
        @u2 val attributeNameIndex: Int,                //<1.23.1>
        val attributeLength: Int,                   //<1.23.2>
        @u1 val numParameterAnnotations: Int,           //<1.23.3>
        val parameterAnnotations: Array<Entry>      //<1.23.4>
) : Attribute {

    class Entry(                                    //<1.23.5>
            @u2 val numAnnotations: Int,                //<1.23.6>
            val annotations: Array<Annotation>      //<1.23.7>
    )

    companion object {
        fun read(stream: DataInputStream, attribute: AttributeInfo): RuntimeVisibleParameterAnnotations = stream.run {
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