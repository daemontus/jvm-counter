package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.io.DataOutputStream

class Deprecated(                       //<1.20.0>
        @u2 val attributeNameIndex: Int,//<1.20.1>
        val attributeLength: Int        //<1.20.2>
) : Attribute {

    override fun write(stream: DataOutputStream) {
        stream.writeShort(attributeNameIndex)
        stream.writeInt(attributeLength)
    }

}