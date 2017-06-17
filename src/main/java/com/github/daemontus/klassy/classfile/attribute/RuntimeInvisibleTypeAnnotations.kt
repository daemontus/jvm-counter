package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class RuntimeInvisibleTypeAnnotations(          //<1.26.0>
        @u2 val attributeNameIndex: Int,        //<1.26.1>
        val attributeLength: Int,               //<1.26.2>
        @u2 val numAnnotations: Int,            //<1.26.3>
        val annotations: Array<TypeAnnotation>  //<1.26.4>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): RuntimeInvisibleTypeAnnotations = stream.run {
            val numAnnotations = readUnsignedShort()
            val annotations = Array(numAnnotations) {
                TypeAnnotation.read(stream)
            }
            RuntimeInvisibleTypeAnnotations(attribute.attributeNameIndex, attribute.attributeLength,
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