package com.github.daemontus.klassy.classfile.stream

class ParserError(message: String) : Exception(message)

fun parserError(message: String): Nothing = throw ParserError(message)

val ERR_UnexpectedEndOfStream = "The class file ended unexpectedly."
fun ERR_UnknownCpInfoTag(tag: Int) = "Invalid constant pool item with tag: $tag."
fun ERR_UnknownVerificationTypeTag(tag: Int) = "Invalid verification type tag: $tag."
fun ERR_UnknownElementValueTag(tag: Int) = "Invalid annotation element value tag: ${tag.toChar()}"
fun ERR_UnknownTypeAnnotationTargetType(targetType: Int) = "Invalid type annotation target type: $targetType"
fun ERR_UnknownFrameType(frameType: Int) = "Invalid stack map table frame type: $frameType."
fun ERR_CpInvalidType(index: Int, expected: Class<*>, actual: Class<*>)
        = "Constant pool item at index $index expected to be $expected, but was $actual"