package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class Code(                                        //<1.8.0>
        @u2 override val attributeNameIndex: Int,       //<1.8.1>
        override val attributeLength: Int,              //<1.8.2>
        @u2 val maxStack: Int,                          //<1.8.3>
        @u2 val maxLocals: Int,                         //<1.8.4>
        @u2 val codeLength: Int,                        //<1.8.5>
        @u1 val code: ByteArray,                        //<1.8.6>
        @u2 val exceptionTableLength: Int,              //<1.8.7>
        val exceptionTable: Array<ExceptionTableEntry>, //<1.8.8>
        @u2 val attributesCount: Int,                   //<1.8.9>
        val attributes: Array<Attribute>                //<1.8.10>
) : Attribute {

    data class ExceptionTableEntry(                     //<1.8.11>
            @u2 val startPC: Int,                       //<1.8.12>
            @u2 val endPC: Int,                         //<1.8.13>
            @u2 val handlerPC: Int,                     //<1.8.14>
            @u2 val catchType: Int                      //<1.8.15>
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Code

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (maxStack != other.maxStack) return false
        if (maxLocals != other.maxLocals) return false
        if (codeLength != other.codeLength) return false
        if (!Arrays.equals(code, other.code)) return false
        if (exceptionTableLength != other.exceptionTableLength) return false
        if (!Arrays.equals(exceptionTable, other.exceptionTable)) return false
        if (attributesCount != other.attributesCount) return false
        if (!Arrays.equals(attributes, other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + maxStack
        result = 31 * result + maxLocals
        result = 31 * result + codeLength
        result = 31 * result + Arrays.hashCode(code)
        result = 31 * result + exceptionTableLength
        result = 31 * result + Arrays.hashCode(exceptionTable)
        result = 31 * result + attributesCount
        result = 31 * result + Arrays.hashCode(attributes)
        return result
    }


}