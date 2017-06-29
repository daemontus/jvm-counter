package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.EnclosingMethod
import java.io.DataInputStream
import java.io.DataOutputStream

interface EnclosingMethodIO {

    fun DataInputStream.readEnclosingMethod(info: AttributeInfo): EnclosingMethod {
        return EnclosingMethod(info.attributeNameIndex, info.attributeLength,
                classIndex = readUnsignedShort(), methodIndex = readUnsignedShort()
        )
    }

    fun DataOutputStream.writeEnclosingMethod(method: EnclosingMethod) = method.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(classIndex)
        writeShort(methodIndex)
    }

}