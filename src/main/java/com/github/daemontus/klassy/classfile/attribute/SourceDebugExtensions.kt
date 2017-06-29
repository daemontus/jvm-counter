package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.io.DataOutputStream

class SourceDebugExtensions(                //<1.16.0>
        @u2 val attributeNameIndex: Int,    //<1.16.1>
        val attributeLength: Int,           //<1.16.2>
        @u1 val debugExtension: ByteArray   //<1.16.3>
) : Attribute {

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        write(debugExtension)
    }

}