package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.LineNumberTable
import java.io.DataOutputStream

internal fun AttributeInfo.toLineNumberTable(): LineNumberTable = usingStream {
    val lineNumberTableLength = readUnsignedShort()
    val lineNumberTable = Array(lineNumberTableLength) {
        LineNumberTable.Entry(startPC = readUnsignedShort(), lineNumber = readUnsignedShort())
    }
    LineNumberTable(attributeNameIndex, attributeLength,
            lineNumberTableLength = lineNumberTableLength,
            lineNumberTable = lineNumberTable
    )
}

internal fun DataOutputStream.writeLineNumberTable(table: LineNumberTable) = table.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeShort(lineNumberTableLength)
    lineNumberTable.forEach {
        writeShort(it.startPC)
        writeShort(it.lineNumber)
    }
}