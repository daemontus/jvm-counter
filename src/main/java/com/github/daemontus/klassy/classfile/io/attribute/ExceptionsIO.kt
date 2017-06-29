package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.Exceptions
import java.io.DataInputStream
import java.io.DataOutputStream

interface ExceptionsIO {

    fun DataInputStream.readExceptions(info: AttributeInfo): Exceptions {
        val numberOfExceptions = readUnsignedShort()
        val exceptionsIndexTable = IntArray(numberOfExceptions) { readUnsignedShort() }
        return Exceptions(info.attributeNameIndex, info.attributeLength,
                numberOfExceptions = numberOfExceptions,
                exceptionsIndexTable = exceptionsIndexTable
        )
    }

    fun DataOutputStream.writeExceptions(exceptions: Exceptions) = exceptions.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numberOfExceptions)
        exceptionsIndexTable.forEach { writeShort(it) }
    }

}