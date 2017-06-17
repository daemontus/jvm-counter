package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.DataInputStream
import java.io.DataOutputStream

class InnerClasses(                     //<1.11.0>
        val attributeNameIndex: Int,    //<1.11.1>
        val attributeLength: Int,       //<1.11.2>
        val numberOfClasses: Int,       //<1.11.3>
        val classes: Array<Entry>       //<1.11.4>
) : AttributeInfo {

    companion object {
        fun read(stream: DataInputStream, attribute: Attribute): InnerClasses = stream.run {
            val numberOfClasses = readUnsignedShort()
            val classes = Array(numberOfClasses) {
                Entry(
                        innerClassInfoIndex = readUnsignedShort(),
                        outerClassInfoIndex = readUnsignedShort(),
                        innerNameIndex = readUnsignedShort(),
                        innerClassAccessFlags = readUnsignedShort()
                )
            }
            InnerClasses(attribute.attributeNameIndex, attribute.attributeLength,
                    numberOfClasses = numberOfClasses, classes = classes
            )
        }
    }

    override fun write(stream: DataOutputStream) = stream.run {
        writeShort(attributeNameIndex)
        writeInt(attributeLength)
        writeShort(numberOfClasses)
        classes.forEach {
            writeShort(it.innerClassInfoIndex)
            writeShort(it.outerClassInfoIndex)
            writeShort(it.innerNameIndex)
            writeShort(it.innerClassAccessFlags)
        }
    }

    class Entry(                            //<1.11.9>
            val innerClassInfoIndex: Int,   //<1.11.5>
            val outerClassInfoIndex: Int,   //<1.11.6>
            val innerNameIndex: Int,        //<1.11.7>
            val innerClassAccessFlags: Int  //<1.11.8>
    )

}