package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class AnnotationDefault(                            //<1.27.0>
        @u2 val attributeNameIndex: Int,            //<1.27.1>
        val attributeLength: Int,                   //<1.27.2>
        val defaultValue: Annotation.ElementValue   //<1.27.3>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): AnnotationDefault {
            return AnnotationDefault(attribute.attributeNameIndex, attribute.attributeLength,
                    defaultValue = Annotation.ElementValue.read(stream)
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        defaultValue.write(stream)
    }

}