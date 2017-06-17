package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.stream.ERR_UnexpectedEndOfStream
import com.github.daemontus.klassy.classfile.stream.parserError
import com.github.daemontus.klassy.classfile.stream.readAttributeInfo
import com.github.daemontus.klassy.classfile.stream.writeAttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class Code(                                             //<1.8.0>
        @u2 val attributeNameIndex: Int,                //<1.8.1>
        val attributeLength: Int,                       //<1.8.2>
        @u2 val maxStack: Int,                          //<1.8.3>
        @u2 val maxLocals: Int,                         //<1.8.4>
        @u2 val codeLength: Int,                        //<1.8.5>
        @u1 val code: ByteArray,                        //<1.8.6>
        @u2 val exceptionTableLength: Int,              //<1.8.7>
        val exceptionTable: Array<ExceptionTableEntry>, //<1.8.8>
        @u2 val attributesCount: Int,                   //<1.8.9>
        val attributes: Array<AttributeInfo>            //<1.8.10>
) : AttributeInfo {

    class ExceptionTableEntry(                          //<1.8.11>
            @u2 val startPC: Int,                       //<1.8.12>
            @u2 val endPC: Int,                         //<1.8.13>
            @u2 val handlerPC: Int,                     //<1.8.14>
            @u2 val catchType: Int                      //<1.8.15>
    )

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute, pool: Array<CpInfo>): Code = stream.run {
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
            val attributes = Array(attributesCount) { readAttributeInfo().resolve(pool) }
            Code(attribute.attributeNameIndex, attribute.attributeLength,
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
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(maxStack)
        writeShort(maxLocals)
        writeInt(codeLength)
        write(code)
        writeShort(exceptionTableLength)
        exceptionTable.forEach {
            writeShort(it.startPC)
            writeShort(it.endPC)
            writeShort(it.handlerPC)
            writeShort(it.catchType)
        }
        writeShort(attributesCount)
        attributes.forEach { writeAttributeInfo(it) }
    }

}