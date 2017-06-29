package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.attribute.Annotation
import com.github.daemontus.klassy.classfile.io.ERR_UnknownElementValueTag
import com.github.daemontus.klassy.classfile.io.parserError
import java.io.DataInputStream
import java.io.DataOutputStream

interface AnnotationIO {

    fun DataInputStream.readAnnotation(): Annotation {
        val typeIndex = readUnsignedShort()
        val numElementValuePairs = readUnsignedShort()
        val pairs = Array(numElementValuePairs) {
            Annotation.Pair(
                    elementNameIndex = readUnsignedShort(),
                    value = readAnnotationElementValue()
            )
        }
        return Annotation(
                typeIndex = typeIndex,
                numElementValuePairs = numElementValuePairs,
                pairs = pairs
        )
    }

    fun DataInputStream.readAnnotationElementValue(): Annotation.ElementValue {
        val tag = readUnsignedByte()
        return when (tag.toChar()) {
            in listOf('B', 'C', 'D', 'F', 'I', 'J', 'S', 'Z', 's') ->
                Annotation.ElementValue.ConstValue(tag, constValueIndex = readUnsignedShort())
            'e' -> Annotation.ElementValue.EnumValue(tag,
                    typeNameIndex = readUnsignedShort(),
                    constNameIndex = readUnsignedShort()
            )
            'c' -> Annotation.ElementValue.ClassValue(tag, classInfoIndex = readUnsignedShort())
            '@' -> Annotation.ElementValue.AnnotationValue(tag, annotation = readAnnotation())
            '[' -> {
                val numValues = readUnsignedShort()
                val values = Array(numValues) { readAnnotationElementValue() }
                Annotation.ElementValue.ArrayValue(tag, numValues = numValues, values = values)
            }
            else -> parserError(ERR_UnknownElementValueTag(tag))
        }
    }

    fun DataOutputStream.writeAnnotation(annotation: Annotation) = annotation.run {
        writeShort(typeIndex)
        writeShort(numElementValuePairs)
        pairs.forEach {
            writeShort(it.elementNameIndex)
            writeAnnotationElementValue(it.value)
        }
    }

    fun DataOutputStream.writeAnnotationElementValue(value: Annotation.ElementValue): Unit = value.run {
        writeByte(tag)
        when (this) {
            is Annotation.ElementValue.ConstValue -> writeShort(constValueIndex)
            is Annotation.ElementValue.EnumValue -> {
                writeShort(typeNameIndex)
                writeShort(constNameIndex)
            }
            is Annotation.ElementValue.ClassValue -> writeShort(classInfoIndex)
            is Annotation.ElementValue.AnnotationValue -> writeAnnotation(annotation)
            is Annotation.ElementValue.ArrayValue -> {
                writeShort(numValues)
                values.forEach { writeAnnotationElementValue(it) }
            }
        }
    }

}