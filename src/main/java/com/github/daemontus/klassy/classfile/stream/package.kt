package com.github.daemontus.klassy.classfile.stream

class ParserError(message: String) : Exception(message)

fun parserError(message: String): Nothing = throw ParserError(message)

val ERR_UnexpectedEndOfStream = "The class file ended unexpectedly. It is either malformed or incomplete."
fun ERR_UnknownCpInfoTag(tag: Byte) = "Invalid constant pool item with tag: $tag."