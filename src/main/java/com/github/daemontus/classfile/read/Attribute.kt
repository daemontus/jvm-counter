package com.github.daemontus.classfile.read

import com.github.daemontus.classfile.Attribute
import com.github.daemontus.classfile.ConstantPool.Entry.Utf8
import com.github.daemontus.classfile.InvalidClassFileException
import com.github.daemontus.classfile.asConstantPoolIndex
import java.io.DataInputStream

fun DataInputStream.readAttribute(): Attribute {
    val nameIndex = readUnsignedShort().asConstantPoolIndex<Utf8>()
    val length = readInt()

    logReader(" | - read attribute with name index $nameIndex and length $length")

    if (length < 0) {
        throw InvalidClassFileException("Attribute with name index $nameIndex too long for this implementation.")
    }
    val content = ByteArray(length)
    read(content)

    return Attribute(name = nameIndex)
}