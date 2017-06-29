package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class LineNumberTable(                         //<1.17.0>
        @u2 override val attributeNameIndex: Int,   //<1.17.1>
        override val attributeLength: Int,          //<1.17.2>
        @u2 val lineNumberTableLength: Int,         //<1.17.3>
        val lineNumberTable: Array<Entry>           //<1.17.4>
) : Attribute {

    data class Entry(                               //<1.17.5>
        @u2 val startPC: Int,                       //<1.17.6>
        @u2 val lineNumber: Int                     //<1.17.7>
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as LineNumberTable

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (lineNumberTableLength != other.lineNumberTableLength) return false
        if (!Arrays.equals(lineNumberTable, other.lineNumberTable)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + lineNumberTableLength
        result = 31 * result + Arrays.hashCode(lineNumberTable)
        return result
    }


}