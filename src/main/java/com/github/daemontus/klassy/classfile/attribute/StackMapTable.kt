package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.stream.ERR_UnknownFrameType
import com.github.daemontus.klassy.classfile.stream.ERR_UnknownVerificationTypeTag
import com.github.daemontus.klassy.classfile.stream.parserError
import java.io.DataInputStream
import java.io.DataOutputStream

class StackMapTable(                    //<1.9.0>
        @u2 val attributeNameIndex: Int,    //<1.9.1>
        val attributeLength: Int,       //<1.9.2>
        @u2 val numberOfEntries: Int,       //<1.9.3>
        val entries: Array<Frame>       //<1.9.4>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): StackMapTable = stream.run {
            val numberOfEntries = readUnsignedShort()
            val entries = Array(numberOfEntries) {
                val frameType = readUnsignedByte()
                when (frameType) {
                    in 0..63 -> Frame.SameFrame(frameType)
                    in 64..127 -> Frame.SameLocalsOneStackItemFrame(frameType,
                            stack = VerificationType.read(stream)
                    )
                    247 -> Frame.SameLocalsOneStackItemFrameExtended(frameType,
                            offsetDelta = readUnsignedShort(),
                            stack = VerificationType.read(stream)
                    )
                    in 248..250 -> Frame.ChopFrame(frameType, offsetDelta = readUnsignedShort())
                    251 -> Frame.SameFrameExtended(frameType, offsetDelta = readUnsignedShort())
                    in 252..254 -> Frame.AppendFrame(frameType,
                            offsetDelta = readUnsignedShort(),
                            locals = Array(frameType - 251) {
                                VerificationType.read(stream)
                            }
                    )
                    255 -> {
                        val offsetDelta = readUnsignedShort()
                        val numberOfLocals = readUnsignedShort()
                        val locals = Array(numberOfLocals) { VerificationType.read(stream) }
                        val numberOfStackItems = readUnsignedShort()
                        val stack = Array(numberOfStackItems) { VerificationType.read(stream) }
                        Frame.FullFrame(frameType, offsetDelta = offsetDelta,
                                numberOfLocals = numberOfLocals, locals = locals,
                                numberOfStackItems = numberOfStackItems, stack = stack
                        )
                    }
                    else -> parserError(ERR_UnknownFrameType(frameType))
                }
            }
            StackMapTable(attribute.attributeNameIndex, attribute.attributeLength,
                numberOfEntries = numberOfEntries, entries = entries
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numberOfEntries)
        entries.forEach { it.write(this) }
    }

    sealed class Frame(@u1 val frameType: Int) {       //<1.9.52>

        class SameFrame(                            //<1.9.5>
                frameType: Int                      //<1.9.6>
        ) : Frame(frameType) {

            override fun write(stream: DataOutputStream) = stream.writeByte(frameType)

        }

        class SameLocalsOneStackItemFrame(          //<1.9.7>
                frameType: Int,                     //<1.9.8>
                val stack: VerificationType         //<1.9.9>
        ) : Frame(frameType) {

            override fun write(stream: DataOutputStream) = stream.run {
                writeByte(frameType)
                stack.write(this)
            }

        }

        class SameLocalsOneStackItemFrameExtended(  //<1.9.10>
                frameType: Int,                     //<1.9.11>
                @u2 val offsetDelta: Int,           //<1.9.12>
                val stack: VerificationType         //<1.9.13>
        ) : Frame(frameType) {

            override fun write(stream: DataOutputStream) = stream.run {
                writeByte(frameType)
                writeShort(offsetDelta)
                stack.write(this)
            }

        }

        class ChopFrame(                            //<1.9.14>
                frameType: Int,                     //<1.9.15>
                @u2 val offsetDelta: Int            //<1.9.16>
        ) : Frame(frameType) {

            override fun write(stream: DataOutputStream) = stream.run {
                writeByte(frameType)
                writeShort(offsetDelta)
            }

        }

        class SameFrameExtended(                    //<1.9.17>
                frameType: Int,                     //<1.9.18>
                @u2 val offsetDelta: Int            //<1.9.19>
        ) : Frame(frameType) {

            override fun write(stream: DataOutputStream) = stream.run {
                writeByte(frameType)
                writeShort(offsetDelta)
            }

        }

        class AppendFrame(                          //<1.9.20>
                frameType: Int,                     //<1.9.21>
                @u2 val offsetDelta: Int,           //<1.9.22>
                val locals: Array<VerificationType> //<1.9.23>
        ) : Frame(frameType) {

            override fun write(stream: DataOutputStream) = stream.run {
                writeByte(frameType)
                writeShort(offsetDelta)
                locals.forEach { it.write(this) }
            }

        }

        class FullFrame(                            //<1.9.24>
                frameType: Int,                     //<1.9.25>
                @u2 val offsetDelta: Int,           //<1.9.26>
                @u2 val numberOfLocals: Int,        //<1.9.27>
                val locals: Array<VerificationType>,//<1.9.28>
                @u2 val numberOfStackItems: Int,    //<1.9.29>
                val stack: Array<VerificationType>  //<1.9.30>
        ) : Frame(frameType) {

            override fun write(stream: DataOutputStream) = stream.run {
                writeByte(frameType)
                writeShort(offsetDelta)
                writeShort(numberOfLocals)
                locals.forEach { it.write(this) }
                writeShort(numberOfStackItems)
                stack.forEach { it.write(this) }
            }
        }

        abstract fun write(stream: DataOutputStream)

    }

    sealed class VerificationType(@u1 val tag: Int) {  //<1.9.51>

        companion object {
            fun read(stream: DataInputStream): VerificationType = stream.run {
                val tag = readUnsignedByte()
                when (tag) {
                    TOP -> Top(tag)
                    INTEGER -> Integer(tag)
                    FLOAT -> Float(tag)
                    NULL -> Null(tag)
                    U_THIS -> UninitializedThis(tag)
                    OBJECT -> Object(tag, readUnsignedShort())
                    U_VAR -> UninitializedVariable(tag, readUnsignedShort())
                    LONG -> Long(tag)
                    DOUBLE -> Double(tag)
                    else -> parserError(ERR_UnknownVerificationTypeTag(tag))
                }
            }
        }

        class Top(                  //<1.9.31>
                tag: Int            //<1.9.32>
        ) : VerificationType(tag)

        class Integer(              //<1.9.33>
                tag: Int            //<1.9.34>
        ) : VerificationType(tag)

        class Float(                //<1.9.35>
                tag: Int            //<1.9.36>
        ) : VerificationType(tag)

        class Null(                 //<1.9.37>
                tag: Int            //<1.9.38>
        ) : VerificationType(tag)

        class UninitializedThis(    //<1.9.39>
                tag: Int            //<1.9.40>
        ) : VerificationType(tag)

        class Object(               //<1.9.41>
                tag: Int,           //<1.9.42>
                @u2 val constantPoolIndex: Int  //<1.9.43>
        ) : VerificationType(tag)

        class UninitializedVariable(        //<1.9.44>
                tag: Int,                   //<1.9.45>
                @u2 val offset: Int         //<1.9.46>
        ) : VerificationType(tag)

        class Long(                 //<1.9.47>
                tag: Int            //<1.9.48>
        ) : VerificationType(tag)

        class Double(               //<1.9.49>
                tag: Int            //<1.9.50>
        ) : VerificationType(tag)

        fun write(stream: DataOutputStream) = stream.run {
            writeByte(tag.toInt())
            when(this@VerificationType) {
                is Object -> writeShort(constantPoolIndex)
                is UninitializedVariable -> writeShort(offset)
            }
        }
    }

}