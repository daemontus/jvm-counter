package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.attribute.InnerClasses
import java.io.DataInputStream
import java.io.DataOutputStream

interface InnerClassesIO {

    fun DataInputStream.readInnerClasses(info: AttributeInfo): InnerClasses {
        val numberOfClasses = readUnsignedShort()
        val classes = Array(numberOfClasses) {
            InnerClasses.Entry(
                    innerClassInfoIndex = readUnsignedShort(),
                    outerClassInfoIndex = readUnsignedShort(),
                    innerNameIndex = readUnsignedShort(),
                    innerClassAccessFlags = readUnsignedShort()
            )
        }
        return InnerClasses(info.attributeNameIndex, info.attributeLength,
                numberOfClasses = numberOfClasses, classes = classes
        )
    }

    fun DataOutputStream.writeInnerClasses(classes: InnerClasses) = classes.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numberOfClasses)
        this.classes.forEach {
            writeShort(it.innerClassInfoIndex)
            writeShort(it.outerClassInfoIndex)
            writeShort(it.innerNameIndex)
            writeShort(it.innerClassAccessFlags)
        }
    }
}