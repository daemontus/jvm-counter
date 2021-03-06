package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.BootstrapMethods
import java.io.DataOutputStream

internal fun AttributeInfo.toBootstrapMethods(): BootstrapMethods = usingStream {
    val numBootstrapMethods = readUnsignedShort()
    val bootstrapMethods = Array(numBootstrapMethods) {
        val bootstrapMethodRef = readUnsignedShort()
        val numBootstrapArguments = readUnsignedShort()
        val bootstrapArguments = IntArray(numBootstrapArguments) { readUnsignedShort() }
        BootstrapMethods.Entry(
                bootstrapMethodRef = bootstrapMethodRef,
                numBootstrapArguments = numBootstrapArguments,
                bootstrapArguments = bootstrapArguments
        )
    }
    BootstrapMethods(attributeNameIndex, attributeLength,
            numBootstrapMethods = numBootstrapMethods,
            bootstrapMethods = bootstrapMethods
    )
}

internal fun DataOutputStream.writeBootstrapMethods(methods: BootstrapMethods) = methods.run {
    writeShort(attributeNameIndex)
    writeInt(attributeLength)
    writeShort(numBootstrapMethods)
    bootstrapMethods.forEach {
        writeShort(it.bootstrapMethodRef)
        writeShort(it.numBootstrapArguments)
        it.bootstrapArguments.forEach { writeShort(it) }
    }
}