package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.SourceFile
import java.io.DataOutputStream

interface SourceFileIO {

    fun AttributeInfo.toSourceFile(): SourceFile = usingStream {
        SourceFile(attributeNameIndex, attributeLength,
                sourceFileIndex = readUnsignedShort()
        )
    }

    fun DataOutputStream.writeSourceFile(file: SourceFile) = file.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(sourceFileIndex)
    }

}