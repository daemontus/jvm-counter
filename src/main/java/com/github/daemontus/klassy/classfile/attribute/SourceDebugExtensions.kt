package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataOutputStream

class SourceDebugExtensions(            //<1.16.0>
        val attributeNameIndex: Int,    //<1.16.1>
        val attributeLength: Int,       //<1.16.2>
        val debugExtension: ByteArray   //<1.16.3>
) : AttributeInfo {

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        write(debugExtension)
    }

}