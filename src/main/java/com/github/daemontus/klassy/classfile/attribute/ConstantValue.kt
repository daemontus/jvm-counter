package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class ConstantValue(                    //<1.7.0>
        val attributeNameIndex: Int,    //<1.7.1>
        val attributeLength: Int,       //<1.7.2>
        val constantValueIndex: Int     //<1.7.3>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): ConstantValue {
            return ConstantValue(attribute.attributeNameIndex, attribute.attributeLength,
                    constantValueIndex = stream.readUnsignedShort()
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(constantValueIndex)
    }

}