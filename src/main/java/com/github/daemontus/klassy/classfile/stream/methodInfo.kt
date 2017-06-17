package com.github.daemontus.klassy.classfile.stream

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.MethodInfo
import java.io.DataInputStream
import java.io.DataOutputStream

fun DataInputStream.readMethodInfo(): MethodInfo {
    val accessFlags = readUnsignedShort()
    val nameIndex = readUnsignedShort()
    val descriptorIndex = readUnsignedShort()
    val attributesCount = readUnsignedShort()
    val attributes = Array<AttributeInfo>(attributesCount) { readAttributeInfo() }
    return MethodInfo(
            accessFlags = accessFlags,
            nameIndex = nameIndex,
            descriptorIndex = descriptorIndex,
            attributesCount = attributesCount,
            attributes = attributes
    )
}

fun DataOutputStream.writeMethodInfo(method: MethodInfo) = method.run {
    writeShort(accessFlags)
    writeShort(nameIndex)
    writeShort(descriptorIndex)
    writeShort(attributesCount)
    attributes.forEach { writeAttributeInfo(it) }
}