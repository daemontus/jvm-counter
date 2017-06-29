package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.RuntimeInvisibleAnnotations
import java.io.DataOutputStream

interface RuntimeInvisibleAnnotationsIO : AnnotationIO {

    fun AttributeInfo.toRuntimeInvisibleAnnotations(): RuntimeInvisibleAnnotations = usingStream {
        val numAnnotations = readUnsignedShort()
        val annotations = Array(numAnnotations) { readAnnotation() }
        RuntimeInvisibleAnnotations(attributeNameIndex, attributeLength,
                numAnnotations = numAnnotations,
                annotations = annotations
        )
    }

    fun DataOutputStream.writeRuntimeInvisibleAnnotations(annotations: RuntimeInvisibleAnnotations)= annotations.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numAnnotations)
        this.annotations.forEach { writeAnnotation(it) }
    }

}