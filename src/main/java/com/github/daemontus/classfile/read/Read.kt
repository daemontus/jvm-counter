package com.github.daemontus.classfile.read

import com.github.daemontus.classfile.ClassFile
import com.github.daemontus.classfile.ConstantPool.Entry.ClassRef
import com.github.daemontus.classfile.InvalidClassFileException
import com.github.daemontus.classfile.asConstantPoolIndex
import java.io.DataInputStream

fun logReader(string: String) {
    println(string)
}

@Throws(InvalidClassFileException::class)
internal fun DataInputStream.readClassFile() : ClassFile {

    logReader(" - start reading class file")

    // read magic
    readInt().takeIf { it != 0xCAFEBABE.toInt() }?.let {
        throw InvalidClassFileException("Expected magic 0xCAFEBABE, but got 0x${Integer.toHexString(it)}")
    }

    logReader(" - class file magic read successfully")

    val version = readClassVersion()

    val constantPool = readConstantPool()

    val access = ClassFile.Access(readUnsignedShort())

    logReader(" - class access: $access")

    val thisClass = readUnsignedShort().asConstantPoolIndex<ClassRef>()

    logReader(" - this class: $thisClass")

    val superClass = readUnsignedShort().asConstantPoolIndex<ClassRef>().takeIf { it.value != 0 }

    logReader(" - super class: $superClass")

    val interfaceCount = readUnsignedShort()
    val interfaces = (1..interfaceCount).map {
        readUnsignedShort().asConstantPoolIndex<ClassRef>()
    }

    logReader(" - interfaces: $interfaces")

    return ClassFile(
            version = version,
            access = access,
            thisClass = thisClass,
            superClass = superClass,
            interfaces = interfaces,
            constantPool = constantPool
    )
}

internal fun DataInputStream.readClassVersion(): ClassFile.Version {
    val minor = readUnsignedShort()
    val major = readUnsignedShort()
    logReader(" - class file version: $major.$minor")
    return ClassFile.Version(major = major, minor = minor)
}
