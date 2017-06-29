package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.Deprecated
import java.io.DataOutputStream

internal fun AttributeInfo.toDeprecated(): Deprecated = Deprecated(attributeNameIndex, attributeLength)

internal fun DataOutputStream.writeDeprecated(deprecated: Deprecated) {
    writeShort(deprecated.attributeNameIndex)
    writeInt(deprecated.attributeLength)
}
