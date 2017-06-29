package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.ConstantValue
import java.io.DataInputStream
import java.io.DataOutputStream

interface ConstantValueIO {

    fun DataInputStream.readConstantValue(info: AttributeInfo): ConstantValue {
        return ConstantValue(info.attributeNameIndex, info.attributeLength,
                constantValueIndex = readUnsignedShort()
        )
    }

    fun DataOutputStream.writeConstantValue(value: ConstantValue) = value.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(constantValueIndex)
    }

}