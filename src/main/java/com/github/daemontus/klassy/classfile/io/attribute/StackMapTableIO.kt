package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.attribute.StackMapTable
import com.github.daemontus.klassy.classfile.io.ERR_UnknownFrameType
import com.github.daemontus.klassy.classfile.io.ERR_UnknownVerificationTypeTag
import com.github.daemontus.klassy.classfile.io.parserError
import java.io.DataInputStream
import java.io.DataOutputStream

interface StackMapTableIO {

    fun AttributeInfo.toStackMapTable(): StackMapTable = usingStream {
        val numberOfEntries = readUnsignedShort()
        val entries = Array(numberOfEntries) {
            val frameType = readUnsignedByte()
            when (frameType) {
                in 0..63 -> StackMapTable.Frame.SameFrame(frameType)
                in 64..127 -> StackMapTable.Frame.SameLocalsOneStackItemFrame(frameType,
                        stack = readStackMapVerificationType()
                )
                247 -> StackMapTable.Frame.SameLocalsOneStackItemFrameExtended(frameType,
                        offsetDelta = readUnsignedShort(),
                        stack = readStackMapVerificationType()
                )
                in 248..250 -> StackMapTable.Frame.ChopFrame(frameType, offsetDelta = readUnsignedShort())
                251 -> StackMapTable.Frame.SameFrameExtended(frameType, offsetDelta = readUnsignedShort())
                in 252..254 -> StackMapTable.Frame.AppendFrame(frameType,
                        offsetDelta = readUnsignedShort(),
                        locals = Array(frameType - 251) {
                            readStackMapVerificationType()
                        }
                )
                255 -> {
                    val offsetDelta = readUnsignedShort()
                    val numberOfLocals = readUnsignedShort()
                    val locals = Array(numberOfLocals) { readStackMapVerificationType() }
                    val numberOfStackItems = readUnsignedShort()
                    val stack = Array(numberOfStackItems) { readStackMapVerificationType() }
                    StackMapTable.Frame.FullFrame(frameType, offsetDelta = offsetDelta,
                            numberOfLocals = numberOfLocals, locals = locals,
                            numberOfStackItems = numberOfStackItems, stack = stack
                    )
                }
                else -> parserError(ERR_UnknownFrameType(frameType))
            }
        }
        StackMapTable(attributeNameIndex, attributeLength,
                numberOfEntries = numberOfEntries, entries = entries
        )
    }

    fun DataInputStream.readStackMapVerificationType(): StackMapTable.VerificationType {
        val tag = readUnsignedByte()
        return when (tag) {
            TOP -> StackMapTable.VerificationType.Top(tag)
            INTEGER -> StackMapTable.VerificationType.Integer(tag)
            FLOAT -> StackMapTable.VerificationType.Float(tag)
            NULL -> StackMapTable.VerificationType.Null(tag)
            U_THIS -> StackMapTable.VerificationType.UninitializedThis(tag)
            OBJECT -> StackMapTable.VerificationType.Object(tag, readUnsignedShort())
            U_VAR -> StackMapTable.VerificationType.UninitializedVariable(tag, readUnsignedShort())
            LONG -> StackMapTable.VerificationType.Long(tag)
            DOUBLE -> StackMapTable.VerificationType.Double(tag)
            else -> parserError(ERR_UnknownVerificationTypeTag(tag))
        }
    }

    fun DataOutputStream.writeStackMapTable(table: StackMapTable) = table.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numberOfEntries)
        entries.forEach {
            when (it) {
                is StackMapTable.Frame.SameFrame -> writeByte(it.frameType)
                is StackMapTable.Frame.SameLocalsOneStackItemFrame -> {
                    writeByte(it.frameType)
                    writeStackMapVerificationType(it.stack)
                }
                is StackMapTable.Frame.SameLocalsOneStackItemFrameExtended -> {
                    writeByte(it.frameType)
                    writeShort(it.offsetDelta)
                    writeStackMapVerificationType(it.stack)
                }
                is StackMapTable.Frame.ChopFrame -> {
                    writeByte(it.frameType)
                    writeShort(it.offsetDelta)
                }
                is StackMapTable.Frame.SameFrameExtended -> {
                    writeByte(it.frameType)
                    writeShort(it.offsetDelta)
                }
                is StackMapTable.Frame.AppendFrame -> it.run {
                    writeByte(frameType)
                    writeShort(offsetDelta)
                    locals.forEach { writeStackMapVerificationType(it) }
                }
                is StackMapTable.Frame.FullFrame -> it.run {
                    writeByte(frameType)
                    writeShort(offsetDelta)
                    writeShort(numberOfLocals)
                    locals.forEach { writeStackMapVerificationType(it) }
                    writeShort(numberOfStackItems)
                    stack.forEach { writeStackMapVerificationType(it) }
                }
            }
        }
    }

    fun DataOutputStream.writeStackMapVerificationType(type: StackMapTable.VerificationType) = type.run {
        writeByte(tag)
        when(this) {
            is StackMapTable.VerificationType.Object -> writeShort(constantPoolIndex)
            is StackMapTable.VerificationType.UninitializedVariable -> writeShort(offset)
        }
    }
}