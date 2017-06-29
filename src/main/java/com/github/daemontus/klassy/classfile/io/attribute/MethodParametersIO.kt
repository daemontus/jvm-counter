package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.MethodParameters
import java.io.DataInputStream
import java.io.DataOutputStream

interface MethodParametersIO {

    fun DataInputStream.readMethodParameters(info: AttributeInfo): MethodParameters {
        val parametersCount = readUnsignedByte()
        val parameters = Array(parametersCount) {
            MethodParameters.Param(
                    nameIndex = readUnsignedShort(),
                    accessFlags = readUnsignedShort()
            )
        }
        return MethodParameters(info.attributeNameIndex, info.attributeLength,
                parametersCount = parametersCount,
                parameters = parameters
        )
    }

    fun DataOutputStream.writeMethodParameters(parameters: MethodParameters) = parameters.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeByte(parametersCount)
        this.parameters.forEach {
            writeShort(it.nameIndex)
            writeShort(it.accessFlags)
        }
    }

}