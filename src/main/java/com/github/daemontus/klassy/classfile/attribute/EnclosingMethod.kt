package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class EnclosingMethod(                  //<1.12.0>
        @u2 val attributeNameIndex: Int,//<1.12.1>
        val attributeLength: Int,       //<1.12.2>
        @u2 val classIndex: Int,            //<1.12.3>
        @u2 val methodIndex: Int            //<1.12.4>
) : Attribute {

    companion object {
        fun read(stream: DataInputStream, attribute: AttributeInfo): EnclosingMethod = EnclosingMethod(
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