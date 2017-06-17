package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class Signature(                        //<1.14.0>
        val attributeNameIndex: Int,    //<1.14.1>
        val attributeLength: Int,       //<1.14.2>
        val signatureIndex: Int         //<1.14.3>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): Signature
            = Signature(attribute.attributeNameIndex, attribute.attributeLength,
                signatureIndex = stream.readUnsignedShort()
        )
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(signatureIndex)
    }

}