package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.*
import java.io.DataInputStream
import java.io.DataOutputStream

internal fun DataInputStream.readCpInfo(): CpInfo {
    val tag = readUnsignedByte()
    return when (tag) {
        CONST_Utf8 -> readUnsignedShort().let { length ->
            val bytes = ByteArray(length)
            val read = read(bytes)
            if (read != length) parserError(ERR_UnexpectedEndOfStream)
            CpInfo.Utf8Info(tag, length = length, bytes = bytes)
        }
        CONST_Integer -> CpInfo.IntegerInfo(tag, bytes = readInt())
        CONST_Float -> CpInfo.FloatInfo(tag, bytes = readFloat())
        CONST_Long -> CpInfo.LongInfo(tag, bytes = readLong())
        CONST_Double -> CpInfo.DoubleInfo(tag, bytes = readDouble())
        CONST_Class -> CpInfo.ClassInfo(tag, nameIndex = readUnsignedShort())
        CONST_String -> CpInfo.StringInfo(tag, stringIndex = readUnsignedShort())
        CONST_FieldRef -> CpInfo.FieldRefInfo(tag,
                classIndex = readUnsignedShort(),
                nameAndTypeIndex = readUnsignedShort()
        )
        CONST_MethodRef -> CpInfo.MethodRefInfo(tag,
                classIndex = readUnsignedShort(),
                nameAndTypeIndex = readUnsignedShort()
        )
        CONST_InterfaceMethodRef -> CpInfo.InterfaceMethodRefInfo(tag,
                classIndex = readUnsignedShort(),
                nameAndTypeIndex = readUnsignedShort()
        )
        CONST_NameAndType -> CpInfo.NameAndTypeInfo(tag,
                nameIndex = readUnsignedShort(),
                descriptorIndex = readUnsignedShort()
        )
        CONST_MethodHandle -> CpInfo.MethodHandleInfo(tag,
                referenceKind = readUnsignedShort(),
                referenceIndex = readUnsignedShort()
        )
        CONST_MethodType -> CpInfo.MethodTypeInfo(tag,
                descriptorIndex = readUnsignedShort()
        )
        CONST_InvokeDynamic -> CpInfo.InvokeDynamicInfo(tag,
                bootstrapMethodAttrIndex = readUnsignedShort(),
                nameAndTypeIndex = readUnsignedShort()
        )
        else -> parserError(ERR_UnknownCpInfoTag(tag))
    }
}

internal fun DataOutputStream.writeCpInfo(constant: CpInfo) {
    writeByte(constant.tag)
    when(constant) {
        is CpInfo.Utf8Info -> {
            writeShort(constant.length)
            write(constant.bytes)
        }
        is CpInfo.IntegerInfo -> writeInt(constant.bytes)
        is CpInfo.FloatInfo -> writeFloat(constant.bytes)
        is CpInfo.LongInfo -> writeLong(constant.bytes)
        is CpInfo.DoubleInfo -> writeDouble(constant.bytes)
        is CpInfo.ClassInfo -> writeShort(constant.nameIndex)
        is CpInfo.StringInfo -> writeShort(constant.stringIndex)
        is CpInfo.FieldRefInfo -> {
            writeShort(constant.classIndex)
            writeShort(constant.nameAndTypeIndex)
        }
        is CpInfo.MethodRefInfo -> {
            writeShort(constant.classIndex)
            writeShort(constant.nameAndTypeIndex)
        }
        is CpInfo.InterfaceMethodRefInfo -> {
            writeShort(constant.classIndex)
            writeShort(constant.nameAndTypeIndex)
        }
        is CpInfo.NameAndTypeInfo -> {
            writeShort(constant.nameIndex)
            writeShort(constant.descriptorIndex)
        }
        is CpInfo.MethodHandleInfo -> {
            writeShort(constant.referenceKind)
            writeShort(constant.referenceIndex)
        }
        is CpInfo.MethodTypeInfo -> writeShort(constant.descriptorIndex)
        is CpInfo.InvokeDynamicInfo -> {
            writeShort(constant.bootstrapMethodAttrIndex)
            writeShort(constant.nameAndTypeIndex)
        }
    }
}