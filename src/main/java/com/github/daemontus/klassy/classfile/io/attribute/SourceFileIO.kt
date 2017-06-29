package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.SourceFile
import java.io.DataInputStream
import java.io.DataOutputStream

interface SourceFileIO {

    fun DataInputStream.readSourceFile(info: AttributeInfo): SourceFile {
        return SourceFile(info.attributeNameIndex, info.attributeLength,
                sourceFileIndex = readUnsignedShort()
        )
    }

    fun DataOutputStream.writeSourceFile(file: SourceFile) = file.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(sourceFileIndex)
    }

}