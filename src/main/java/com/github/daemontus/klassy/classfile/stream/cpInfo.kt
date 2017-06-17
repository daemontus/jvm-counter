package com.github.daemontus.klassy.classfile.stream

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.CpInfo.*
import java.io.DataInputStream
import java.io.DataOutputStream

fun DataInputStream.readCpInfo(): CpInfo {
    val tag = readUnsignedByte()
    return when (tag) {
        CONST_Utf8 -> readUnsignedShort().let { length ->
            val bytes = ByteArray(length)
            val read = read(bytes)
            if (read != length) parserError(ERR_UnexpectedEndOfStream)
            Utf8Info(tag, length = length, bytes = bytes)
        }
        CONST_Integer -> IntegerInfo(tag, bytes = readInt())
        CONST_Float -> FloatInfo(tag, bytes = readFloat())
        CONST_Long -> LongInfo(tag, bytes = readLong())
        CONST_Double -> DoubleInfo(tag, bytes = readDouble())
        CONST_Class -> ClassInfo(tag, nameIndex = readUnsignedShort())
        CONST_String -> StringInfo(tag, stringIndex = readUnsignedShort())
        CONST_FieldRef -> FieldRefInfo(tag,
                classIndex = readUnsignedShort(),
                nameAndTypeIndex = readUnsignedShort()
        )
        CONST_MethodRef -> MethodRefInfo(tag,
                classIndex = readUnsignedShort(),
                nameAndTypeIndex = readUnsignedShort()
        )
        CONST_InterfaceMethodRef -> InterfaceMethodRefInfo(tag,
                classIndex = readUnsignedShort(),
                nameAndTypeIndex = readUnsignedShort()
        )
        CONST_NameAndType -> NameAndTypeInfo(tag,
                nameIndex = readUnsignedShort(),
                descriptorIndex = readUnsignedShort()
        )
        CONST_MethodHandle -> MethodHandleInfo(tag,
                referenceKind = readUnsignedShort(),
                referenceIndex = readUnsignedShort()
        )
        CONST_MethodType -> MethodTypeInfo(tag,
                descriptorIndex = readUnsignedShort()
        )
        CONST_InvokeDynamic -> InvokeDynamicInfo(tag,
                bootstrapMethodAttrIndex = readUnsignedShort(),
                nameAndTypeIndex = readUnsignedShort()
        )
        else -> parserError(ERR_UnknownCpInfoTag(tag))
    }
}

fun DataOutputStream.writeCpInfo(cp: CpInfo) {
    writeByte(cp.tag)
    when(cp) {
        is Utf8Info -> {
            writeShort(cp.length)
            write(cp.bytes)
        }
        is IntegerInfo -> writeInt(cp.bytes)
        is FloatInfo -> writeFloat(cp.bytes)
        is LongInfo -> writeLong(cp.bytes)
        is DoubleInfo -> writeDouble(cp.bytes)
        is ClassInfo -> writeShort(cp.nameIndex)
        is StringInfo -> writeShort(cp.stringIndex)
        is FieldRefInfo -> {
            writeShort(cp.classIndex)
            writeShort(cp.nameAndTypeIndex)
        }
        is MethodRefInfo -> {
            writeShort(cp.classIndex)
            writeShort(cp.nameAndTypeIndex)
        }
        is InterfaceMethodRefInfo -> {
            writeShort(cp.classIndex)
            writeShort(cp.nameAndTypeIndex)
        }
        is NameAndTypeInfo -> {
            writeShort(cp.nameIndex)
            writeShort(cp.descriptorIndex)
        }
        is MethodHandleInfo -> {
            writeShort(cp.referenceKind)
            writeShort(cp.referenceIndex)
        }
        is MethodTypeInfo -> writeShort(cp.descriptorIndex)
        is InvokeDynamicInfo -> {
            writeShort(cp.bootstrapMethodAttrIndex)
            writeShort(cp.nameAndTypeIndex)
        }
    }
}