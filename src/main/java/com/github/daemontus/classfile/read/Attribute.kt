package com.github.daemontus.classfile.read

import com.github.daemontus.classfile.*
import com.github.daemontus.classfile.ConstantPool.Entry.Utf8
import com.github.daemontus.classfile.Deprecated
import com.github.daemontus.classfile.StackMapTable.StackMapFrame
import com.github.daemontus.classfile.StackMapTable.VerificationTypeInfo
import java.io.DataInputStream

fun DataInputStream.readAttribute(cp: ConstantPool): Attribute {
    val nameIndex = readUnsignedShort().asConstantPoolIndex<Utf8>()
    val length = readInt()

    if (length < 0) {
        throw InvalidClassFileException("Attribute with name index $nameIndex too long for this implementation.")
    }

    val name = cp[nameIndex].value

    logReader("name: $name")
    logReader("length: $length")

    val attribute = when (name) {
        AttributeNames.ConstantValue -> readConstantValue(nameIndex)
        AttributeNames.Code -> readCode(nameIndex, cp)
        AttributeNames.StackMapTable -> readStackMapTable(nameIndex)
        AttributeNames.Exceptions -> readExceptions(nameIndex)
        AttributeNames.InnerClasses -> readInnerClasses(nameIndex)
        AttributeNames.EnclosingMethod -> readEnclosingMethod(nameIndex)
        AttributeNames.Synthetic -> Synthetic(nameIndex)
        AttributeNames.Signature -> readSignature(nameIndex)
        AttributeNames.SourceFile -> readSourceFile(nameIndex)
        AttributeNames.LineNumberTable -> readLineNumberTable(nameIndex)
        AttributeNames.LocalVariableTable -> readLocalVariableTable(nameIndex)
        AttributeNames.LocalVariableTypeTable -> readLocalVariableTypeTable(nameIndex)
        AttributeNames.Deprecated -> Deprecated(nameIndex)
        else -> UnknownAttribute(nameIndex, ByteArray(length).apply { read(this) })
    }

    return attribute
}

fun DataInputStream.readConstantValue(nameIndex: ConstantPool.Index<Utf8>): ConstantValue {
    val constantIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry>()
    logReader("constant index: $constantIndex")
    return ConstantValue(name = nameIndex, constant = constantIndex)
}

fun DataInputStream.readCode(nameIndex: ConstantPool.Index<Utf8>, constantPool: ConstantPool): Code {
    val maxStack = readUnsignedShort()
    val maxLocals = readUnsignedShort()
    val codeLength = readInt()

    if (codeLength < 0) {
        throw InvalidClassFileException("Code length is higher than maximum supported value: $codeLength.")
    }

    val codeBytes = ByteArray(codeLength)
    read(codeBytes)

    val exceptionTableLength = readUnsignedShort()

    val exceptionTable = (1..exceptionTableLength).map {
        Code.ExceptionTableEntry(
                startPC = readUnsignedShort(),
                endPC = readUnsignedShort(),
                handlerPC = readUnsignedShort(),
                catchType = readUnsignedShort().asConstantPoolIndex()
        )
    }

    val attributeCount = readUnsignedShort()
    val attributes = (1..attributeCount).map {
        logReader("read attribute")
        logReaderNested {
            readAttribute(constantPool)
        }
    }

    logReader("max stack: $maxStack")
    logReader("max locals: $maxLocals")
    logReader("code length: $codeLength")
    logReader("exception table length: $exceptionTableLength")
    logReader("attribute count: $attributeCount")

    return Code(
            name = nameIndex,
            maxStack = maxStack, maxLocals = maxLocals,
            code = listOf(UnknownInstructionBlock(codeBytes)),
            exceptionTable = exceptionTable,
            attributes = attributes
    )
}

fun DataInputStream.readStackMapTable(nameIndex: ConstantPool.Index<Utf8>): StackMapTable {
    val entries = readUnsignedShort()

    fun readVerificationTypeInfo(): VerificationTypeInfo {
        val tag = readByte().toInt()
        return when (tag) {
            0 -> VerificationTypeInfo.Top
            1 -> VerificationTypeInfo.Integer
            2 -> VerificationTypeInfo.Float
            3 -> VerificationTypeInfo.Double
            4 -> VerificationTypeInfo.Long
            5 -> VerificationTypeInfo.Null
            6 -> VerificationTypeInfo.UninitializedThis
            7 -> VerificationTypeInfo.ObjectVariable(readUnsignedShort().asConstantPoolIndex())
            8 -> VerificationTypeInfo.Uninitialized(readUnsignedShort())
            else -> throw InvalidClassFileException("Unknown verification type info tag: $tag")
        }
    }

    fun readStackMapFrame(): StackMapFrame {
        val tag = readByte().toInt()
        return when (tag) {
            in 0..63 -> StackMapFrame.SameFrame(offsetDelta = tag)
            in 64..127 -> StackMapFrame.SameLocalsOneStack(
                    offsetDelta = tag - 64, stack = readVerificationTypeInfo()
            )
            //128-246 reserved for future use
            247 -> StackMapFrame.SameLocalsOneStack(
                    offsetDelta = readUnsignedShort(), stack = readVerificationTypeInfo()
            )
            in 248..250 -> StackMapFrame.ChopFrame(chop = 251 - tag, offsetDelta = readUnsignedByte())
            251 -> StackMapFrame.SameFrame(offsetDelta = readUnsignedByte())
            in 252..254 -> {
                val append = tag - 251
                StackMapFrame.AppendFrame(append = append, offsetDelta = readUnsignedByte(),
                        locals = (1..append).map { readVerificationTypeInfo() }
                )
            }
            255 -> StackMapFrame.FullFrame(
                    offsetDelta = readUnsignedByte(),
                    locals = (1..readUnsignedShort()).map { readVerificationTypeInfo() },
                    stack = (1..readUnsignedShort()).map { readVerificationTypeInfo() }
            )
            else -> throw InvalidClassFileException("Unknown stack map frame tag: $tag")
        }
    }

    logReader("stack map table size: $entries")

    return StackMapTable(
            name = nameIndex,
            table = (1..entries).map {
        readStackMapFrame()
    })
}

fun DataInputStream.readExceptions(nameIndex: ConstantPool.Index<Utf8>): Exceptions {
    val count = readUnsignedShort()
    logReader("exception count: $count")
    return Exceptions(
            name = nameIndex,
            table = (1..count).map {
                readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.ClassRef>()
            }
    )
}

fun DataInputStream.readInnerClasses(nameIndex: ConstantPool.Index<Utf8>): InnerClasses {
    val count = readUnsignedShort()
    logReader("inner classes count: $count")
    return InnerClasses(
            name = nameIndex,
            classes = (1..count).map {
                InnerClasses.Entry(
                        innerClassInfo = readUnsignedShort().asConstantPoolIndex(),
                        outerClassInfo = readUnsignedShort().takeIf { it > 0 }?.asConstantPoolIndex(),
                        innerName = readUnsignedShort().takeIf { it > 0 }?.asConstantPoolIndex(),
                        innerAccess = readUnsignedShort()
                )
            }
    )
}

fun DataInputStream.readEnclosingMethod(nameIndex: ConstantPool.Index<Utf8>): EnclosingMethod {
    val classIndex = readUnsignedShort().asConstantPoolIndex<ConstantPool.Entry.ClassRef>()
    val methodIndex = readUnsignedShort().takeIf { it > 0 }?.asConstantPoolIndex<ConstantPool.Entry.NameAndType>()
    logReader("class index: $classIndex")
    logReader("method index: $methodIndex")
    return EnclosingMethod(name = nameIndex, classId = classIndex, methodId = methodIndex)
}

fun DataInputStream.readSignature(nameIndex: ConstantPool.Index<Utf8>): Signature {
    val signatureIndex = readUnsignedShort().asConstantPoolIndex<Utf8>()
    logReader("signature: $signatureIndex")
    return Signature(name = nameIndex, value = signatureIndex)
}

fun DataInputStream.readSourceFile(nameIndex: ConstantPool.Index<Utf8>): SourceFile {
    val valueIndex = readUnsignedShort().asConstantPoolIndex<Utf8>()
    logReader("source file: $valueIndex")
    return SourceFile(name = nameIndex, value = valueIndex)
}

fun DataInputStream.readLineNumberTable(nameIndex: ConstantPool.Index<Utf8>): LineNumberTable {
    val entries = readUnsignedShort()
    logReader("table size: $entries")
    return LineNumberTable(name = nameIndex,
            table = (1..entries).map {
                LineNumberTable.Entry(
                        startPC = readUnsignedShort(), lineNumber = readUnsignedShort()
                )
    })
}

fun DataInputStream.readLocalVariableTable(nameIndex: ConstantPool.Index<Utf8>): LocalVariableTable {
    val entries = readUnsignedShort()
    logReader("table size: $entries")
    return LocalVariableTable(name = nameIndex,
            table = (1..entries).map {
                LocalVariableTable.Entry(
                        startPC = readUnsignedShort(),
                        length = readUnsignedShort(),
                        name = readUnsignedShort().asConstantPoolIndex(),
                        descriptor = readUnsignedShort().asConstantPoolIndex(),
                        index = readUnsignedShort()
                )
    })
}

fun DataInputStream.readLocalVariableTypeTable(nameIndex: ConstantPool.Index<Utf8>): LocalVariableTypeTable {
    val entries = readUnsignedShort()
    logReader("table size: $entries")
    return LocalVariableTypeTable(name = nameIndex,
            table = (1..entries).map {
                LocalVariableTypeTable.Entry(
                        startPC = readUnsignedShort(),
                        length = readUnsignedShort(),
                        name = readUnsignedShort().asConstantPoolIndex(),
                        signature = readUnsignedShort().asConstantPoolIndex(),
                        index = readUnsignedShort()
                )
            })
}