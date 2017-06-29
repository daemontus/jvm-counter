package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.io.DataOutputStream

class Synthetic(                        //<1.13.0>
        @u2 val attributeNameIndex: Int,    //<1.13.1>
        val attributeLength: Int        //<1.13.2>
) : Attribute {

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
    }

}