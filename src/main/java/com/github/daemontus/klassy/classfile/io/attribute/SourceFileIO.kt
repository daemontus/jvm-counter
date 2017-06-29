package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.SourceFile
import java.io.DataOutputStream

internal fun AttributeInfo.toSourceFile(): SourceFile = usingStream {
    SourceFile(attributeNameIndex, attributeLength,
            sourceFileIndex = readUnsignedShort()
    )
}

internal fun DataOutputStream.writeSourceFile(file: SourceFile) = file.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeShort(sourceFileIndex)
}