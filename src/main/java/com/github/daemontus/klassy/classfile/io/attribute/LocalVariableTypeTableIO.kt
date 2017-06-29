package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.LocalVariableTypeTable
import java.io.DataInputStream
import java.io.DataOutputStream

interface LocalVariableTypeTableIO {

    fun DataInputStream.readLocalVariableTypeTable(info: AttributeInfo): LocalVariableTypeTable {
        val localVariableTypeTableLength = readUnsignedShort()
        val localVariableTypeTable = Array(localVariableTypeTableLength) {
            LocalVariableTypeTable.Entry(
                    startPC = readUnsignedShort(),
                    length = readUnsignedShort(),
                    nameIndex = readUnsignedShort(),
                    signatureIndex = readUnsignedShort(),
                    index = readUnsignedShort()
            )
        }

        return LocalVariableTypeTable(info.attributeNameIndex, info.attributeLength,
                localVariableTypeTableLength = localVariableTypeTableLength,
                localVariableTypeTable = localVariableTypeTable
        )
    }

    fun DataOutputStream.writeLocalVariableTypeTable(table: LocalVariableTypeTable) = table.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(localVariableTypeTableLength)
        localVariableTypeTable.forEach {
            writeShort(it.startPC)
            writeShort(it.length)
            writeShort(it.nameIndex)
            writeShort(it.signatureIndex)
            writeShort(it.index)
        }
    }

}