package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.stream.ERR_UnexpectedEndOfStream
import com.github.daemontus.klassy.classfile.stream.parserError
import java.io.DataInputStream
import java.io.DataOutputStream

interface AttributeInfoIO {

    fun DataInputStream.readAttribute(): AttributeInfo {
        val nameIndex = readUnsignedShort()
        val length = readInt()
        val info = ByteArray(length)
        if (read(info) != length) parserError(ERR_UnexpectedEndOfStream)
        return AttributeInfo(
                attributeNameIndex = nameIndex,
                attributeLength = length,
                info = info
        )
    }

    fun DataOutputStream.writeAttribute(attribute: AttributeInfo) = attribute.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        write(info)
    }

}