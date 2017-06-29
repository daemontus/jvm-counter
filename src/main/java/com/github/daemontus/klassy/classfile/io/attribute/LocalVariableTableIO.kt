package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.LocalVariableTable
import java.io.DataInputStream
import java.io.DataOutputStream

interface LocalVariableTableIO {

    fun DataInputStream.readLocalVariableTable(info: AttributeInfo): LocalVariableTable {
        val localVariableTableLength = readUnsignedShort()
        val localVariableTable = Array(localVariableTableLength) {
            LocalVariableTable.Entry(
                    startPC = readUnsignedShort(),
                    length = readUnsignedShort(),
                    nameIndex = readUnsignedShort(),
                    descriptorIndex = readUnsignedShort(),
                    index = readUnsignedShort()
            )
        }
        return LocalVariableTable(info.attributeNameIndex, info.attributeLength,
                localVariableTableLength = localVariableTableLength,
                localVariableTable = localVariableTable
        )
    }

    fun DataOutputStream.writeLocalVariableTable(table: LocalVariableTable) = table.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(localVariableTableLength)
        localVariableTable.forEach {
            writeShort(it.startPC)
            writeShort(it.length)
            writeShort(it.nameIndex)
            writeShort(it.descriptorIndex)
            writeShort(it.index)
        }
    }

}