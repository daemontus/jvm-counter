package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class Exceptions(                              //<1.10.0>
        @u2 override val attributeNameIndex: Int,   //<1.10.1>
        override val attributeLength: Int,          //<1.10.2>
        @u2 val numberOfExceptions: Int,            //<1.10.3>
        @u2 val exceptionsIndexTable: IntArray      //<1.10.4>
) : Attribute {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Exceptions

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (numberOfExceptions != other.numberOfExceptions) return false
        if (!Arrays.equals(exceptionsIndexTable, other.exceptionsIndexTable)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + numberOfExceptions
        result = 31 * result + Arrays.hashCode(exceptionsIndexTable)
        return result
    }
}