package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class Exceptions(                           //<1.10.0>
        @u2 val attributeNameIndex: Int,    //<1.10.1>
        val attributeLength: Int,           //<1.10.2>
        @u2 val numberOfExceptions: Int,    //<1.10.3>
        @u2 val exceptionsIndexTable: IntArray  //<1.10.4>
) : Attribute {

    companion object {
        fun read(stream: DataInputStream, attribute: AttributeInfo): Exceptions = stream.run {
            val numberOfExceptions = readUnsignedShort()
            val exceptionsIndexTable = IntArray(numberOfExceptions) { readUnsignedShort() }
            Exceptions(attribute.attributeNameIndex, attribute.attributeLength,
                    numberOfExceptions = numberOfExceptions,
                    exceptionsIndexTable = exceptionsIndexTable
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {

        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numberOfExceptions)
        exceptionsIndexTable.forEach { writeShort(it) }
    }

}