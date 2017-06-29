package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.FieldInfo
import java.io.DataInputStream
import java.io.DataOutputStream

interface FieldInfoIO : AttributeInfoIO, AttributeIO {

    fun DataInputStream.readFieldInfo(): FieldInfo {
        val accessFlags = readUnsignedShort()
        val nameIndex = readUnsignedShort()
        val descriptorIndex = readUnsignedShort()
        val attributesCount = readUnsignedShort()
        val attributes = Array<Attribute>(attributesCount) { readAttributeInfo() }
        return FieldInfo(
                accessFlags = accessFlags,
                nameIndex = nameIndex,
                descriptorIndex = descriptorIndex,
                attributesCount = attributesCount,
                attributes = attributes
        )
    }

    fun DataOutputStream.writeFieldInfo(field: FieldInfo) = field.run {
        writeShort(accessFlags)
        writeShort(nameIndex)
        writeShort(descriptorIndex)
        writeShort(attributesCount)
        attributes.forEach { writeAttributeInfo(it.toAttributeInfo()) }
    }

}