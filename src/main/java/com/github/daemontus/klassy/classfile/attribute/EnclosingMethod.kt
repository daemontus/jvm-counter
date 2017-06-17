package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class EnclosingMethod(              //<1.12.0>
    val attributeNameIndex: Int,    //<1.12.1>
    val attributeLength: Int,       //<1.12.2>
    val classIndex: Int,            //<1.12.3>
    val methodIndex: Int            //<1.12.4>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): EnclosingMethod = EnclosingMethod(
                attribute.attributeNameIndex, attribute.attributeLength,
                classIndex = stream.readUnsignedShort(), methodIndex = stream.readUnsignedShort()
        )
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(classIndex)
        writeShort(methodIndex)
    }

}