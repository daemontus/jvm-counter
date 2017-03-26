package com.github.daemontus.classfile.read

import com.github.daemontus.classfile.ConstantPool
import com.github.daemontus.classfile.InvalidClassFileException
import com.github.daemontus.classfile.asBootstrapMethodIndex
import com.github.daemontus.classfile.asConstantPoolIndex
import java.io.DataInputStream


internal fun DataInputStream.readConstantPool(): ConstantPool {

    logReader("start reading class constant pool")

    val size = readUnsignedShort()

    logReader("constant pool count: $size")

    logReaderPush()

    val items = ArrayList<ConstantPool.Entry>(size)
    var toRead = size - 1
    while (toRead > 0) {

        logReader("${items.size} constant pool position")

        val tag = readByte().toInt()

        logReaderNested {
            logReader("tag: $tag")

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
                else -> throw InvalidClassFileException("Unexpected constant pool entry tag: $tag")
            }
            toRead -= 1
        }
    }

    logReaderPop()

    return ConstantPool(items)
}

internal fun DataInputStream.readUtf8(): ConstantPool.Entry.Utf8 {
    val length = readUnsignedShort()
    val string = String(ByteArray(length).apply {
        read(this)
    })
    logReader("type: Utf8")
    logReader("length: $length")
    logReader("value: $string")
    return ConstantPool.Entry.Utf8(value = string)
}

internal fun DataInputStream.readIntConst(): ConstantPool.Entry.IntConst {
    val bytes = readInt()
    logReader("type: Int")
    logReader("value: $bytes")
    return ConstantPool.Entry.IntConst(value = bytes)
}

internal fun DataInputStream.readFloatConst(): ConstantPool.Entry.FloatConst {
    val bytes = readFloat()
    logReader("type: Float")
    logReader("value: $bytes")
    return ConstantPool.Entry.FloatConst(value = bytes)
}

internal fun DataInputStream.readLongConst(): ConstantPool.Entry.LongConst {
    val bytes = readLong()
    logReader("type: Long")
    logReader("value: $bytes")
    return ConstantPool.Entry.LongConst(value = bytes)
}

internal fun DataInputStream.readDoubleConst(): ConstantPool.Entry.DoubleConst {
    val bytes = readDouble()
    logReader("type: Double")
    logReader("length: $bytes")
    return ConstantPool.Entry.DoubleConst(value = bytes)
}

internal fun DataInputStream.readClassRef(): ConstantPool.Entry.ClassRef {
    val nameIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.Utf8>()
    logReader("type: ClassRef")
    logReader("name index: $nameIndex")
    return ConstantPool.Entry.ClassRef(name = nameIndex)
}

internal fun DataInputStream.readStringConst(): ConstantPool.Entry.StringConst {
    val stringIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.Utf8>()
    logReader("type: String")
    logReader("utf8 index: $stringIndex")
    return ConstantPool.Entry.StringConst(value = stringIndex)
}

internal fun DataInputStream.readFieldRef(): ConstantPool.Entry.FieldRef {
    val classIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.ClassRef>()
    val nameAndTypeIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.NameAndType>()
    logReader("type: FieldRef")
    logReader("class index: $classIndex")
    logReader("name and type index: $nameAndTypeIndex")
    return ConstantPool.Entry.FieldRef(id = nameAndTypeIndex, classId = classIndex)
}


internal fun DataInputStream.readMethodRef(): ConstantPool.Entry.MethodRef {
    val classIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.ClassRef>()
    val nameAndTypeIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.NameAndType>()
    logReader("type: MethodRef")
    logReader("class index: $classIndex")
    logReader("name and type index: $nameAndTypeIndex")
    return ConstantPool.Entry.MethodRef(id = nameAndTypeIndex, classId = classIndex)
}

internal fun DataInputStream.readInterfaceMethodRef(): ConstantPool.Entry.InterfaceMethodRef {
    val classIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.ClassRef>()
    val nameAndTypeIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.NameAndType>()
    logReader("type: InterfaceMethodRef")
    logReader("class index: $classIndex")
    logReader("name and type index: $nameAndTypeIndex")
    return ConstantPool.Entry.InterfaceMethodRef(id = nameAndTypeIndex, classId = classIndex)
}

internal fun DataInputStream.readNameAndType(): ConstantPool.Entry.NameAndType {
    val nameIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.Utf8>()
    val descriptorIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.Utf8>()
    logReader("type: NameAndType")
    logReader("name index: $nameIndex")
    logReader("descriptor index: $descriptorIndex")
    return ConstantPool.Entry.NameAndType(name = nameIndex, descriptor = descriptorIndex)
}

internal fun DataInputStream.readMethodHandle(): ConstantPool.Entry.MethodHandle {
    val refKind = readByte().toInt()
    val refIndex = readUnsignedShort()
    logReader("type: MethodHandle")
    logReader("kind: $refKind")
    logReader("index: $refIndex")
    return when (refKind) {
        1 -> ConstantPool.Entry.MethodHandle.GetFieldRef(refIndex.asConstantPoolIndex())
        2 -> ConstantPool.Entry.MethodHandle.GetStaticRef(refIndex.asConstantPoolIndex())
        3 -> ConstantPool.Entry.MethodHandle.PutFieldRef(refIndex.asConstantPoolIndex())
        4 -> ConstantPool.Entry.MethodHandle.PutStaticRef(refIndex.asConstantPoolIndex())
        5 -> ConstantPool.Entry.MethodHandle.InvokeVirtualRef(refIndex.asConstantPoolIndex())
        6 -> ConstantPool.Entry.MethodHandle.InvokeStaticRef(refIndex.asConstantPoolIndex())
        7 -> ConstantPool.Entry.MethodHandle.InvokeSpecialRef(refIndex.asConstantPoolIndex())
        8 -> ConstantPool.Entry.MethodHandle.NewInvokeSpecialRef(refIndex.asConstantPoolIndex())
        9 -> ConstantPool.Entry.MethodHandle.InvokeInterfaceRef(refIndex.asConstantPoolIndex())
        else -> throw InvalidClassFileException("Unknown Method Handle kind: $refKind")
    }
}

internal fun DataInputStream.readMethodType(): ConstantPool.Entry.MethodType {
    val descriptorIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.Utf8>()
    logReader("type: MethodType")
    logReader("descriptor index: $descriptorIndex")
    return ConstantPool.Entry.MethodType(descriptor = descriptorIndex)
}

internal fun DataInputStream.readInvokeDynamic(): ConstantPool.Entry.InvokeDynamic {
    val bootstrapMethodIndex = readUnsignedShort().asBootstrapMethodIndex()
    val nameAndTypeIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.NameAndType>()
    logReader("type: InvokeDynamic")
    logReader("bootstrap method index: $bootstrapMethodIndex")
    logReader("name and type index: $nameAndTypeIndex")
    return ConstantPool.Entry.InvokeDynamic(id = nameAndTypeIndex, bootstrapMethod = bootstrapMethodIndex)
}