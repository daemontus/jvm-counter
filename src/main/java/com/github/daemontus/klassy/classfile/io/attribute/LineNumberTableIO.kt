package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.LineNumberTable
import java.io.DataInputStream
import java.io.DataOutputStream

interface LineNumberTableIO {

    fun DataInputStream.readLineNumberTable(info: AttributeInfo): LineNumberTable {
        val lineNumberTableLength = readUnsignedShort()
        val lineNumberTable = Array(lineNumberTableLength) {
            LineNumberTable.Entry(startPC = readUnsignedShort(), lineNumber = readUnsignedShort())
        }

        return LineNumberTable(info.attributeNameIndex, info.attributeLength,
                lineNumberTableLength = lineNumberTableLength,
                lineNumberTable = lineNumberTable
        )
    }

    fun DataOutputStream.writeLineNumberTable(table: LineNumberTable) = table.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(lineNumberTableLength)
        lineNumberTable.forEach {
            writeShort(it.startPC)
            writeShort(it.lineNumber)
        }
    }

}