package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.MethodInfo
import java.io.DataInputStream
import java.io.DataOutputStream

internal fun DataInputStream.readMethodInfo(): MethodInfo {
    val accessFlags = readUnsignedShort()
    val nameIndex = readUnsignedShort()
    val descriptorIndex = readUnsignedShort()
    val attributesCount = readUnsignedShort()
    val attributes = Array<Attribute>(attributesCount) { readAttributeInfo() }
    return MethodInfo(
            accessFlags = accessFlags,
            nameIndex = nameIndex,
            descriptorIndex = descriptorIndex,
            attributesCount = attributesCount,
            attributes = attributes
    )
}

internal fun DataOutputStream.writeMethodInfo(method: MethodInfo) = method.run {
    writeShort(accessFlags)
    writeShort(nameIndex)
    writeShort(descriptorIndex)
    writeShort(attributesCount)
    attributes.forEach { writeAttributeInfo(it.toAttributeInfo()) }
}