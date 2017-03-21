package com.github.daemontus.classfile.read

import com.github.daemontus.classfile.ClassFile
import com.github.daemontus.classfile.MalformedClassFileException
import java.io.DataInputStream

@Throws(MalformedClassFileException::class)
internal fun DataInputStream.readClassFile() : ClassFile {

    logReader(" - start reading class file")

    // read magic
    readInt().takeIf { it != 0xCAFEBABE.toInt() }?.let {
        throw MalformedClassFileException("Expected magic 0xCAFEBABE, but got 0x${Integer.toHexString(it)}")
    }

    logReader(" - class file magic read successfully")

    return ClassFile(
            version = readClassVersion(),
            constantPool = readConstantPool()
    )
}

internal fun DataInputStream.readClassVersion(): ClassFile.Version {
    val minor = readUnsignedShort()
    val major = readUnsignedShort()
    logReader(" - class file format: major: $major; minor: $minor")
    return ClassFile.Version(major = major, minor = minor)
}
