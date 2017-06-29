package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.AnnotationDefault
import java.io.DataOutputStream

internal fun AttributeInfo.toAnnotationDefault(): AnnotationDefault = usingStream {
    AnnotationDefault(attributeNameIndex, attributeLength,
            defaultValue = readAnnotationElementValue()
    )
}

internal fun DataOutputStream.writeAnnotationDefault(default: AnnotationDefault) = default.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeAnnotationElementValue(defaultValue)
}