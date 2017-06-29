package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.Exceptions
import java.io.DataOutputStream

internal fun AttributeInfo.toExceptions(): Exceptions = usingStream {
    val numberOfExceptions = readUnsignedShort()
    val exceptionsIndexTable = IntArray(numberOfExceptions) { readUnsignedShort() }
    return Exceptions(attributeNameIndex, attributeLength,
            numberOfExceptions = numberOfExceptions,
            exceptionsIndexTable = exceptionsIndexTable
    )
}

internal fun DataOutputStream.writeExceptions(exceptions: Exceptions) = exceptions.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeShort(numberOfExceptions)
    exceptionsIndexTable.forEach { writeShort(it) }
}