package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class RuntimeVisibleAnnotations(                //<1.21.0>
        @u2 val annotationNameIndex: Int,       //<1.21.1>
        val annotationLength: Int,              //<1.21.2>
        @u2 val numAnnotations: Int,            //<1.21.3>
        val annotations: Array<Annotation>      //<1.21.4>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): RuntimeVisibleAnnotations = stream.run {
            val numAnnotations = readUnsignedShort()
            val annotations = Array(numAnnotations) { Annotation.read(stream) }
            RuntimeVisibleAnnotations(attribute.attributeNameIndex, attribute.attributeLength,
                    numAnnotations = numAnnotations,
                    annotations = annotations
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(annotationNameIndex)
        writeInt(annotationLength)
        writeShort(numAnnotations)
        annotations.forEach { it.write(stream) }
    }

}