package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class LocalVariableTypeTable(                       //<1.19.0>
        val attributeNameIndex: Int,                //<1.19.1>
        val attributeLength: Int,                   //<1.19.2>
        val localVariableTypeTableLength: Int,      //<1.19.3>
        val localVariableTypeTable: Array<Entry>    //<1.19.4>
) : AttributeInfo {

    class Entry(                                    //<1.19.5>
            val startPC: Int,                       //<1.19.6>
            val length: Int,                        //<1.19.7>
            val nameIndex: Int,                     //<1.19.8>
            val signatureIndex: Int,                //<1.19.9>
            val index: Int                          //<1.19.10>
    )

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): LocalVariableTypeTable = stream.run {
            val localVariableTypeTableLength = readUnsignedShort()
            val localVariableTypeTable = Array(localVariableTypeTableLength) {
                Entry(
                        startPC = readUnsignedShort(),
                        length = readUnsignedShort(),
                        nameIndex = readUnsignedShort(),
                        signatureIndex = readUnsignedShort(),
                        index = readUnsignedShort()
                )
            }

            LocalVariableTypeTable(attribute.attributeNameIndex, attribute.attributeLength,
                    localVariableTypeTableLength = localVariableTypeTableLength,
                    localVariableTypeTable = localVariableTypeTable
            )
        }
    }


    override fun write(stream: DataOutputStream) = stream.run {
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