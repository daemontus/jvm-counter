package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.MethodParameters
import java.io.DataOutputStream

interface MethodParametersIO {

    fun AttributeInfo.toMethodParameters(): MethodParameters = usingStream {
        val parametersCount = readUnsignedByte()
        val parameters = Array(parametersCount) {
            MethodParameters.Param(
                    nameIndex = readUnsignedShort(),
                    accessFlags = readUnsignedShort()
            )
        }
        return MethodParameters(attributeNameIndex, attributeLength,
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