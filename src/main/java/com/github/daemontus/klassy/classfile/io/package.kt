package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.ClassFile
import java.io.DataInputStream
import java.io.File

class ParserError(message: String) : Exception(message)

internal fun parserError(message: String): Nothing = throw ParserError(message)

internal val ERR_UnexpectedEndOfStream = "The class file ended unexpectedly."
internal fun ERR_UnknownCpInfoTag(tag: Int) = "Invalid constant pool item with tag: $tag."
internal fun ERR_UnknownVerificationTypeTag(tag: Int) = "Invalid verification type tag: $tag."
internal fun ERR_UnknownElementValueTag(tag: Int) = "Invalid annotation element value tag: ${tag.toChar()}"
internal fun ERR_UnknownTypeAnnotationTargetType(targetType: Int) = "Invalid type annotation target type: $targetType"
internal fun ERR_UnknownFrameType(frameType: Int) = "Invalid stack map table frame type: $frameType."
internal fun ERR_CpInvalidType(index: Int, expected: Class<*>, actual: Class<*>)
        = "Constant pool item at index $index expected to be $expected, but was $actual"

fun File.readClassFile(): ClassFile {
    return DataInputStream(this.inputStream()).use {
        readClassFile()
    }
}