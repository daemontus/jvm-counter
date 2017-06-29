package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.EnclosingMethod
import java.io.DataOutputStream

internal fun AttributeInfo.toEnclosingMethod(): EnclosingMethod = usingStream {
    EnclosingMethod(attributeNameIndex, attributeLength,
            classIndex = readUnsignedShort(), methodIndex = readUnsignedShort()
    )
}

internal fun DataOutputStream.writeEnclosingMethod(method: EnclosingMethod) = method.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeShort(classIndex)
    writeShort(methodIndex)
}