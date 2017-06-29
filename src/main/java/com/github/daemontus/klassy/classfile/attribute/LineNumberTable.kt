package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class LineNumberTable(                          //<1.17.0>
        @u2 val attributeNameIndex: Int,        //<1.17.1>
        val attributeLength: Int,               //<1.17.2>
        @u2 val lineNumberTableLength: Int,     //<1.17.3>
        val lineNumberTable: Array<Entry>       //<1.17.4>
) : Attribute {

    class Entry(                                //<1.17.5>
        @u2 val startPC: Int,                   //<1.17.6>
        @u2 val lineNumber: Int                 //<1.17.7>
    )

    companion object {
        fun read(stream: DataInputStream, attribute: AttributeInfo): LineNumberTable = stream.run {

            val lineNumberTableLength = readUnsignedShort()
            val lineNumberTable = Array(lineNumberTableLength) {
                Entry(startPC = readUnsignedShort(), lineNumber = readUnsignedShort())
            }

            LineNumberTable(attribute.attributeNameIndex, attribute.attributeLength,
                    lineNumberTableLength = lineNumberTableLength,
                    lineNumberTable = lineNumberTable
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(lineNumberTableLength)
        lineNumberTable.forEach {
            writeShort(it.startPC)
            writeShort(it.lineNumber)
        }
    }
}