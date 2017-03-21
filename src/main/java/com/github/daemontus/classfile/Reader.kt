package com.github.daemontus.classfile

import java.io.DataInputStream

@Throws(MalformedClassFileException::class)
internal fun DataInputStream.readClassFile() : ClassFile {

    // read magic
    readInt().takeIf { it != 0xCAFEBABE.toInt() }?.let {
        throw MalformedClassFileException("Expected magic 0xCAFEBABE, but got 0x${Integer.toHexString(it)}")
    }

    return ClassFile(
            version = readClassVersion()
    )
}

internal fun DataInputStream.readClassVersion(): ClassFile.Version {
    val minor = readUnsignedShort()
    val major = readUnsignedShort()
    return ClassFile.Version(major = major, minor = minor)
}