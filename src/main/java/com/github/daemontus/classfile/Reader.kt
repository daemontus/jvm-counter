package com.github.daemontus.classfile

import com.github.daemontus.classfile.ConstantPool.Entry.*
import java.io.DataInputStream

@Throws(MalformedClassFileException::class)
internal fun DataInputStream.readClassFile() : ClassFile {

    // read magic
    readInt().takeIf { it != 0xCAFEBABE.toInt() }?.let {
        throw MalformedClassFileException("Expected magic 0xCAFEBABE, but got 0x${Integer.toHexString(it)}")
    }

    return ClassFile(
            version = readClassVersion(),
            constantPool = readConstantPool()
    )
}

internal fun DataInputStream.readClassVersion(): ClassFile.Version {
    val minor = readUnsignedShort()
    val major = readUnsignedShort()
    return ClassFile.Version(major = major, minor = minor)
}

internal fun DataInputStream.readConstantPool(): ConstantPool {

    val size = readUnsignedShort()

    val items = ArrayList<ConstantPool.Entry>(size)
    var toRead = size - 1
    while (toRead > 0) {
        val tag = readByte().toInt()
        when (tag) {
            1 -> items.add(readUtf8())
            3 -> items.add(readIntConst())
            4 -> items.add(readFloatConst())
            5 -> {
                items.add(readLongConst())
                items.add(ConstantPool.Entry.InvalidConstant)
            }
            6 -> {
                items.add(readDoubleConst())
                items.add(ConstantPool.Entry.InvalidConstant)
            }
            7 -> items.add(readClassRef())
            8 -> items.add(readStringConst())
            9 -> items.add(readFieldRef())
            10 -> items.add(readMethodRef())
            11 -> items.add(readInterfaceMethodRef())
            12 -> items.add(readNameAndType())
            15 -> items.add(readMethodHandle())
            16 -> items.add(readMethodType())
            18 -> items.add(readInvokeDynamic())
        }
        toRead -= 1
    }

}

internal fun DataInputStream.readUtf8(): Utf8 {

}

internal fun DataInputStream.readIntConst(): IntConst {
    val bytes = readInt()
    return IntConst(value = bytes)
}

internal fun DataInputStream.readFloatConst(): FloatConst {
    val bytes = readFloat()
    return FloatConst(value = bytes)
}

internal fun DataInputStream.readLongConst(): LongConst {
    val bytes = readLong()
    return LongConst(value = bytes)
}

internal fun DataInputStream.readDoubleConst(): DoubleConst {
    val bytes = readDouble()
    return DoubleConst(value = bytes)
}

internal fun DataInputStream.readClassRef(): ClassRef {
    val nameIndex = readUnsignedShort().asConstantPoolIndex<Utf8>()
    return ClassRef(name = nameIndex)
}

internal fun DataInputStream.readStringConst(): StringConst {
    val stringIndex = readUnsignedShort().asConstantPoolIndex<Utf8>()
    return StringConst(id = stringIndex)
}

internal fun DataInputStream.readFieldRef(): FieldRef {
    val classIndex = readUnsignedShort().asConstantPoolIndex<ClassRef>()
    val nameAndTypeIndex = readUnsignedShort().asConstantPoolIndex<NameAndType>()
    return FieldRef(id = nameAndTypeIndex, classId = classIndex)
}


internal fun DataInputStream.readMethodRef(): MethodRef {
    val classIndex = readUnsignedShort().asConstantPoolIndex<ClassRef>()
    val nameAndTypeIndex = readUnsignedShort().asConstantPoolIndex<NameAndType>()
    return MethodRef(id = nameAndTypeIndex, classId = classIndex)
}

internal fun DataInputStream.readInterfaceMethodRef(): InterfaceMethodRef {
    val classIndex = readUnsignedShort().asConstantPoolIndex<ClassRef>()
    val nameAndTypeIndex = readUnsignedShort().asConstantPoolIndex<NameAndType>()
    return InterfaceMethodRef(id = nameAndTypeIndex, classId = classIndex)
}

internal fun DataInputStream.readNameAndType(): NameAndType {
    val nameIndex = readUnsignedShort()
    val descriptorIndex 
}

internal fun DataInputStream.readMethodHandle(): MethodHandle {

}

internal fun DataInputStream.readMethodType(): MethodType {

}

internal fun DataInputStream.readInvokeDynamic(): InvokeDynamic {

}