package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.SourceDebugExtensions
import com.github.daemontus.klassy.classfile.io.ERR_UnexpectedEndOfStream
import com.github.daemontus.klassy.classfile.io.parserError
import java.io.DataInputStream
import java.io.DataOutputStream

interface SourceDebugExtensionsIO {

    fun DataInputStream.readSourceDebugExtensions(info: AttributeInfo): SourceDebugExtensions {
        val data = ByteArray(info.attributeLength)
        if (read(data) != info.attributeLength) parserError(ERR_UnexpectedEndOfStream)
        return SourceDebugExtensions(info.attributeNameIndex, info.attributeLength, data)
    }

    fun DataOutputStream.writeSourceDebugExtensions(extensions: SourceDebugExtensions) = extensions.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        write(debugExtension)
    }

}