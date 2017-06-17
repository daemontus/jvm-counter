package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataOutputStream

class Deprecated(                       //<1.20.0>
        val attributeNameIndex: Int,    //<1.20.1>
        val attributeLength: Int        //<1.20.2>
) : AttributeInfo {

    override fun write(stream: DataOutputStream) {
        stream.writeShort(attributeNameIndex)
        stream.writeInt(attributeLength)
    }

}