package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.Deprecated
import java.io.DataInputStream
import java.io.DataOutputStream

interface DeprecatedIO {

    fun DataInputStream.readDeprecated(info: AttributeInfo): Deprecated {
        return Deprecated(info.attributeNameIndex, info.attributeLength)
    }

    fun DataOutputStream.writeDeprecated(deprecated: Deprecated) {
        writeShort(deprecated.attributeNameIndex)
        writeInt(deprecated.attributeLength)
    }

}