package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class BootstrapMethods(
        val attributeNameIndex: Int,
        val attributeLength: Int,
        val numBootstrapMethods: Int,
        val bootstrapMethods: Array<Entry>
) : AttributeInfo {

    class Entry(
            val bootstrapMethodRef: Int,
            val numBootstrapArguments: Int,
            val bootstrapArguments: IntArray
    )

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): BootstrapMethods = stream.run {
            val numBootstrapMethods = readUnsignedShort()
            val bootstrapMethods = Array(numBootstrapMethods) {
                val bootstrapMethodRef = readUnsignedShort()
                val numBootstrapArguments = readUnsignedShort()
                val bootstrapArguments = IntArray(numBootstrapArguments) { readUnsignedShort() }
                Entry(
                        bootstrapMethodRef = bootstrapMethodRef,
                        numBootstrapArguments = numBootstrapArguments,
                        bootstrapArguments = bootstrapArguments
                )
            }
            BootstrapMethods(attribute.attributeNameIndex, attribute.attributeLength,
                    numBootstrapMethods = numBootstrapMethods,
                    bootstrapMethods = bootstrapMethods
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
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