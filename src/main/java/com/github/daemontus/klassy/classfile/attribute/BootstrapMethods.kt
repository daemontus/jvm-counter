package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.u2
import java.io.DataInputStream
import java.io.DataOutputStream

class BootstrapMethods(                             //<1.28.0>
        @u2 val attributeNameIndex: Int,            //<1.28.1>
        val attributeLength: Int,                   //<1.28.2>
        @u2 val numBootstrapMethods: Int,           //<1.28.3>
        val bootstrapMethods: Array<Entry>          //<1.28.4>
) : AttributeInfo {

    class Entry(                                    //<1.28.5>
            @u2 val bootstrapMethodRef: Int,        //<1.28.6>
            @u2 val numBootstrapArguments: Int,     //<1.28.7>
            @u2 val bootstrapArguments: IntArray    //<1.28.8>
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