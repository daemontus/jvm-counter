package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.ClassFile
import com.github.daemontus.klassy.classfile.CpInfo
import com.github.daemontus.klassy.classfile.attribute.Code
import java.io.DataInputStream
import java.io.DataOutputStream
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
    val c = DataInputStream(this.inputStream()).use { stream ->
        stream.readClassFile()
    }
    return c.copy(
            fields = c.fields.map { it.copy(attributes = it.attributes.resolve(c.constantPool)) }.toTypedArray(),
            methods = c.methods.map { it.copy(attributes = it.attributes.resolve(c.constantPool)) }.toTypedArray(),
            attributes = c.attributes.resolve(c.constantPool)
    )
}

fun ClassFile.writeTo(file: File) {
    DataOutputStream(file.outputStream()).use { stream ->
        stream.writeClassFile(this)
    }
}

private fun Array<Attribute>.resolve(pool: Array<CpInfo>): Array<Attribute> = this.map {
    val r = if (it is AttributeInfo) it.toAttribute(pool) else it
    if (r !is Code) r else r.copy(attributes = r.attributes.resolve(pool))
}.toTypedArray()