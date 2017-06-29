package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class RuntimeVisibleTypeAnnotations(            //<1.25.51>
        @u2 val attributeNameIndex: Int,        //<1.25.0>
        val attributeLength: Int,               //<1.25.1>
        @u2 val numAnnotations: Int,            //<1.25.2>
        val annotations: Array<TypeAnnotation>  //<1.25.3>
) : Attribute {

    companion object {
        fun read(stream: DataInputStream, attribute: AttributeInfo): RuntimeVisibleTypeAnnotations = stream.run {
            val numAnnotations = readUnsignedShort()
            val annotations = Array(numAnnotations) {
                TypeAnnotation.read(stream)
            }
            RuntimeVisibleTypeAnnotations(attribute.attributeNameIndex, attribute.attributeLength,
                    numAnnotations = numAnnotations, annotations = annotations
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numAnnotations)
        annotations.forEach { it.write(stream) }
    }

}