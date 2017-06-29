package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.Code
import com.github.daemontus.klassy.classfile.io.AttributeIO
import com.github.daemontus.klassy.classfile.io.AttributeInfoIO
import com.github.daemontus.klassy.classfile.io.ERR_UnexpectedEndOfStream
import com.github.daemontus.klassy.classfile.io.parserError
import java.io.DataOutputStream

interface CodeIO : AttributeInfoIO, AttributeIO {

    fun AttributeInfo.toCode(): Code = usingStream {
        val maxStack = readUnsignedShort()
        val maxLocals = readUnsignedShort()
        val codeLength = readInt()
        val code = ByteArray(codeLength)
        if (read(code) != codeLength) parserError(ERR_UnexpectedEndOfStream)
        val exceptionTableLength = readUnsignedShort()
        val exceptionTable = Array(exceptionTableLength) {
            Code.ExceptionTableEntry(
                    startPC = readUnsignedShort(),
                    endPC = readUnsignedShort(),
                    handlerPC = readUnsignedShort(),
                    catchType = readUnsignedShort()
            )
        }
        val attributesCount = readUnsignedShort()
        val attributes = Array<Attribute>(attributesCount) { readAttributeInfo() }
        Code(attributeNameIndex, attributeLength,
                maxStack = maxStack,
                maxLocals = maxLocals,
                codeLength = codeLength,
                code = code,
                exceptionTableLength = exceptionTableLength,
                exceptionTable = exceptionTable,
                attributesCount = attributesCount,
                attributes = attributes
        )
    }

    fun DataOutputStream.writeCode(code: Code) = code.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(maxStack)
        writeShort(maxLocals)
        writeInt(codeLength)
        write(this.code)
        writeShort(exceptionTableLength)
        exceptionTable.forEach {
            writeShort(it.startPC)
            writeShort(it.endPC)
            writeShort(it.handlerPC)
            writeShort(it.catchType)
        }
        writeShort(attributesCount)
        attributes.forEach { writeAttributeInfo(it.toAttributeInfo()) }
    }
}