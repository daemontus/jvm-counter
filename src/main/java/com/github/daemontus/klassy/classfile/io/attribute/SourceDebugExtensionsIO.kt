package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.SourceDebugExtensions
import com.github.daemontus.klassy.classfile.io.ERR_UnexpectedEndOfStream
import com.github.daemontus.klassy.classfile.io.parserError
import java.io.DataOutputStream

interface SourceDebugExtensionsIO {

    fun AttributeInfo.toSourceDebugExtensions(): SourceDebugExtensions = usingStream {
        val data = ByteArray(attributeLength)
        if (read(data) != attributeLength) parserError(ERR_UnexpectedEndOfStream)
        SourceDebugExtensions(attributeNameIndex, attributeLength, data)
    }

    fun DataOutputStream.writeSourceDebugExtensions(extensions: SourceDebugExtensions) = extensions.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        write(debugExtension)
    }

}