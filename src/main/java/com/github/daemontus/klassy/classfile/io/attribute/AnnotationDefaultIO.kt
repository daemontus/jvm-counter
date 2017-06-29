package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.AnnotationDefault
import java.io.DataInputStream
import java.io.DataOutputStream

class AnnotationDefaultIO : AnnotationIO {

    fun DataInputStream.readAnnotationDefault(info: AttributeInfo): AnnotationDefault {
        return AnnotationDefault(info.attributeNameIndex, info.attributeLength,
                defaultValue = readElementValue()
        )
    }

    fun DataOutputStream.writeAnnotationDefault(default: AnnotationDefault) = default.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeElementValue(defaultValue)
    }

}