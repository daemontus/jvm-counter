package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class SourceFile(                       //<1.15.0>
        val attributeNameIndex: Int,    //<1.15.1>
        val attributeLength: Int,       //<1.15.2>
        val sourceFileIndex: Int        //<1.15.3>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): SourceFile
                = SourceFile(   attribute.attributeNameIndex, attribute.attributeLength,
                                sourceFileIndex = stream.readUnsignedShort()
        )
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(sourceFileIndex)
    }

}