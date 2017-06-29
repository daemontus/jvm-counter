package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.attribute.Annotation
import com.github.daemontus.klassy.classfile.attribute.TypeAnnotation
import com.github.daemontus.klassy.classfile.io.ERR_UnknownTypeAnnotationTargetType
import com.github.daemontus.klassy.classfile.io.parserError
import java.io.DataInputStream
import java.io.DataOutputStream

interface TypeAnnotationIO : AnnotationIO {

    fun DataInputStream.readTypeAnnotation(): TypeAnnotation {
        val targetInfo = readTypeAnnotationTarget()
        val pathLength = readUnsignedByte()
        val path = Array(pathLength) {
            TypeAnnotation.TypePath.Entry(
                    typePathKind = readUnsignedByte(),
                    typeArgumentIndex = readUnsignedByte()
            )
        }
        val typeIndex = readUnsignedShort()
        val numElementValuePairs = readUnsignedShort()
        val elementValuePairs = Array(numElementValuePairs) {
            Annotation.Pair(
                    elementNameIndex = readUnsignedShort(),
                    value = readAnnotationElementValue()
            )
        }
        return TypeAnnotation(targetInfo = targetInfo, targetPath = TypeAnnotation.TypePath(pathLength, path),
                typeIndex = typeIndex, numElementValuePairs = numElementValuePairs,
                elementValuePairs = elementValuePairs
        )
    }

    fun DataInputStream.readTypeAnnotationTarget(): TypeAnnotation.Target {
        val targetType = readUnsignedByte()
        return when (targetType) {
            0x00, 0x01 -> TypeAnnotation.Target.TypeParameterTarget(targetType, typeParameterIndex = readUnsignedByte())
            0x10 -> TypeAnnotation.Target.SupertypeTarget(targetType, supertypeIndex = readUnsignedShort())
            0x11, 0x12 -> TypeAnnotation.Target.TypeParameterBoundTarget(targetType,
                    typeParameterIndex = readUnsignedByte(),
                    boundIndex = readUnsignedByte()
            )
            0x13, 0x14, 0x15 -> TypeAnnotation.Target.EmptyTarget(targetType)
            0x16 -> TypeAnnotation.Target.FormalParameterTarget(targetType, formalParameterIndex = readUnsignedByte())
            0x17 -> TypeAnnotation.Target.ThrowsTarget(targetType, throwsTypeIndex = readUnsignedShort())
            0x40, 0x41 -> {
                val tableLength = readUnsignedShort()
                val table = Array(tableLength) {
                    TypeAnnotation.Target.LocalVarTarget.Entry(
                            startPC = readUnsignedShort(),
                            length = readUnsignedShort(),
                            index = readUnsignedShort()
                    )
                }
                TypeAnnotation.Target.LocalVarTarget(targetType, tableLength = tableLength, table = table)
            }
            0x42 -> TypeAnnotation.Target.CatchTarget(targetType, exceptionTableIndex = readUnsignedShort())
            0x43, 0x44, 0x45, 0x46 -> TypeAnnotation.Target.OffsetTarget(targetType, offset = readUnsignedShort())
            in 0x47..0x4B -> TypeAnnotation.Target.TypeArgumentTarget(targetType,
                    offset = readUnsignedShort(),
                    typeArgumentIndex = readUnsignedByte()
            )
            else -> parserError(ERR_UnknownTypeAnnotationTargetType(targetType))
        }
    }

    fun DataOutputStream.writeTypeAnnotation(annotation: TypeAnnotation) = annotation.run {
        writeTypeAnnotationTarget(targetInfo)
        writeByte(targetPath.pathLength)
        targetPath.path.forEach {
            writeByte(it.typePathKind)
            writeByte(it.typeArgumentIndex)
        }
        writeShort(typeIndex)
        writeShort(numElementValuePairs)
        elementValuePairs.forEach {
            writeShort(it.elementNameIndex)
            writeAnnotationElementValue(it.value)
        }
    }

    fun DataOutputStream.writeTypeAnnotationTarget(target: TypeAnnotation.Target) = target.run {
        writeByte(targetType)
        when (this) {
            is TypeAnnotation.Target.TypeParameterTarget -> writeByte(typeParameterIndex)
            is TypeAnnotation.Target.SupertypeTarget -> writeShort(supertypeIndex)
            is TypeAnnotation.Target.TypeParameterBoundTarget -> {
                writeByte(typeParameterIndex)
                writeByte(boundIndex)
            }
            is TypeAnnotation.Target.EmptyTarget -> Unit
            is TypeAnnotation.Target.FormalParameterTarget -> writeByte(formalParameterIndex)
            is TypeAnnotation.Target.ThrowsTarget -> writeShort(throwsTypeIndex)
            is TypeAnnotation.Target.LocalVarTarget -> {
                writeShort(tableLength)
                table.forEach {
                    writeShort(it.startPC)
                    writeShort(it.length)
                    writeShort(it.index)
                }
            }
            is TypeAnnotation.Target.CatchTarget -> writeShort(exceptionTableIndex)
            is TypeAnnotation.Target.OffsetTarget -> writeShort(offset)
            is TypeAnnotation.Target.TypeArgumentTarget -> {
                writeShort(offset)
                writeByte(typeArgumentIndex)
            }
        }
    }

}