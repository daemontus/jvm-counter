package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.Synthetic
import java.io.DataInputStream
import java.io.DataOutputStream

interface SyntheticIO {

    fun DataInputStream.readSynthetic(info: AttributeInfo): Synthetic {
        return Synthetic(info.attributeNameIndex, info.attributeLength)
    }

    fun DataOutputStream.writeSynthetic(synthetic: Synthetic) {
        writeShort(synthetic.attributeNameIndex)
        writeInt(synthetic.attributeLength)
    }

}