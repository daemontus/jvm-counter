package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class LocalVariableTable(                           //<1.18.0>
        @u2 val attributeNameIndex: Int,            //<1.18.1>
        val attributeLength: Int,                   //<1.18.2>
        @u2 val localVariableTableLength: Int,      //<1.18.3>
        val localVariableTable: Array<Entry>        //<1.18.4>
) : Attribute {

    class Entry(                                    //<1.18.5>
            @u2 val startPC: Int,                   //<1.18.6>
            @u2 val length: Int,                    //<1.18.7>
            @u2 val nameIndex: Int,                 //<1.18.8>
            @u2 val descriptorIndex: Int,           //<1.18.9>
            @u2 val index: Int                      //<1.18.10>
    )

    companion object {
        fun read(stream: DataInputStream, attribute: AttributeInfo): LocalVariableTable = stream.run {
            val localVariableTableLength = readUnsignedShort()
            val localVariableTable = Array(localVariableTableLength) {
                Entry(
                        startPC = readUnsignedShort(),
                        length = readUnsignedShort(),
                        nameIndex = readUnsignedShort(),
                        descriptorIndex = readUnsignedShort(),
                        index = readUnsignedShort()
                )
            }
            LocalVariableTable(attribute.attributeNameIndex, attribute.attributeLength,
                    localVariableTableLength = localVariableTableLength,
                    localVariableTable = localVariableTable
            )
        }
    }


    override fun write(stream: DataOutputStream) = stream.run {
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