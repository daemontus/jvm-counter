package com.github.daemontus.klassy.classfile.stream

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

fun DataInputStream.readAttributeInfo(): Attribute {
    val attributeNameIndex = readUnsignedShort()
    val attributeLength = readInt()
    val info = ByteArray(attributeLength)
    if (read(info) != attributeLength) parserError(ERR_UnexpectedEndOfStream)
    return Attribute(
            attributeNameIndex = attributeNameIndex,
            attributeLength = attributeLength,
            info = info
    )
}

fun DataOutputStream.writeAttributeInfo(attribute: AttributeInfo) = attribute.write(this)