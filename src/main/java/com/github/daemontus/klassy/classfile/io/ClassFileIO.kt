package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.ClassFile
import java.io.DataInputStream
import java.io.DataOutputStream

internal fun DataInputStream.readClassFile(): ClassFile {
    val magic = readInt()
    val minorVersion = readUnsignedShort()
    val majorVersion = readUnsignedShort()
    val constantPoolCount = readUnsignedShort()
    val constantPool = Array(constantPoolCount - 1) { readCpInfo() }
    val accessFlags = readUnsignedShort()
    val thisClass = readUnsignedShort()
    val superClass = readUnsignedShort()
    val interfacesCount = readUnsignedShort()
    val interfaces = IntArray(interfacesCount) { readUnsignedShort() }
    val fieldsCount = readUnsignedShort()
    val fields = Array(fieldsCount) { readFieldInfo() }
    val methodsCount = readUnsignedShort()
    val methods = Array(methodsCount) { readMethodInfo() }
    val attributesCount = readUnsignedShort()
    val attributes = Array<Attribute>(attributesCount) { readAttributeInfo() }
    return ClassFile(
            magic = magic,
            minorVersion = minorVersion,
            majorVersion = majorVersion,
            constantPoolCount = constantPoolCount,
            constantPool = constantPool,
            accessFlags = accessFlags,
            thisClass = thisClass,
            superClass = superClass,
            interfacesCount = interfacesCount,
            interfaces = interfaces,
            fieldsCount = fieldsCount,
            fields = fields,
            methodsCount = methodsCount,
            methods = methods,
            attributesCount = attributesCount,
            attributes = attributes
    )
}

internal fun DataOutputStream.writeClassFile(file: ClassFile) = file.run {
    writeInt(magic)
    writeShort(minorVersion)
    writeShort(majorVersion)
    writeShort(constantPoolCount)
    constantPool.forEach { writeCpInfo(it) }
    writeShort(accessFlags)
    writeShort(thisClass)
    writeShort(superClass)
    writeShort(interfacesCount)
    interfaces.forEach { writeShort(it) }
    writeShort(fieldsCount)
    fields.forEach { writeFieldInfo(it) }
    writeShort(methodsCount)
    methods.forEach { writeMethodInfo(it) }
    writeShort(attributesCount)
    attributes.forEach { writeAttributeInfo(it.toAttributeInfo()) }
}