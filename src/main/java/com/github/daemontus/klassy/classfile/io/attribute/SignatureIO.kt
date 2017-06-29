package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.Signature
import java.io.DataOutputStream

interface SignatureIO {

    fun AttributeInfo.toSignature(): Signature = usingStream {
        Signature(attributeNameIndex, attributeLength,
                signatureIndex = readUnsignedShort()
        )
    }

    fun DataOutputStream.writeSignature(signature: Signature) = signature.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(signatureIndex)
    }

}