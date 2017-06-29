package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.Synthetic
import java.io.DataOutputStream

internal fun AttributeInfo.toSynthetic(): Synthetic = Synthetic(attributeNameIndex, attributeLength)

internal fun DataOutputStream.writeSynthetic(synthetic: Synthetic) {
    writeShort(synthetic.attributeNameIndex)
    writeInt(synthetic.attributeLength)
}