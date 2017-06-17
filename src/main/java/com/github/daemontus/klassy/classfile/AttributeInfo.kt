package com.github.daemontus.klassy.classfile

import java.io.DataOutputStream

interface AttributeInfo {
    fun write(stream: DataOutputStream)
}

class Attribute(                            //<1.6.0>
        val attributeNameIndex: Int,        //<1.6.1>
        val attributeLength: Int,           //<1.6.2>
        val info: ByteArray                 //<1.6.3>
) : AttributeInfo {

    override fun write(stream: DataOutputStream) {
        stream.run {
            writeShort(attributeNameIndex)
            writeInt(attributeLength)
            write(info)
        }
    }

}