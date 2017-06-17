package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class MethodParameters(                     //<1.29.0>
        val attributeNameIndex: Int,        //<1.29.1>
        val attributeLength: Int,           //<1.29.2>
        val parametersCount: Int,           //<1.29.3>
        val parameters: Array<Param>        //<1.29.4>
) : AttributeInfo {

    class Param(                            //<1.29.5>
            val nameIndex: Int,             //<1.29.6>
            val accessFlags: Int            //<1.29.7>
    )

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): MethodParameters = stream.run {
            val parametersCount = readUnsignedShort()
            val parameters = Array(parametersCount) {
                Param(
                        nameIndex = readUnsignedShort(),
                        accessFlags = readUnsignedShort()
                )
            }
            MethodParameters(attribute.attributeNameIndex, attribute.attributeLength,
                    parametersCount = parametersCount,
                    parameters = parameters
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(parametersCount)
        parameters.forEach {
            writeShort(it.nameIndex)
            writeShort(it.accessFlags)
        }
    }

}