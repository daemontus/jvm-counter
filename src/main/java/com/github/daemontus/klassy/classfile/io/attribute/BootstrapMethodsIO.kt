package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.BootstrapMethods
import java.io.DataInputStream
import java.io.DataOutputStream

interface BootstrapMethodsIO {

    fun DataInputStream.readBootstrapMethods(info: AttributeInfo): BootstrapMethods {
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
        return BootstrapMethods(info.attributeNameIndex, info.attributeLength,
                numBootstrapMethods = numBootstrapMethods,
                bootstrapMethods = bootstrapMethods
        )
    }

    fun DataOutputStream.writeBootstrapMethods(methods: BootstrapMethods) = methods.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numBootstrapMethods)
        bootstrapMethods.forEach {
            writeShort(it.bootstrapMethodRef)
            writeShort(it.numBootstrapArguments)
            it.bootstrapArguments.forEach { writeShort(it) }
        }
    }

}