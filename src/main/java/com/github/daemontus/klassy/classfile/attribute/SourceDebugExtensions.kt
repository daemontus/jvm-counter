package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class SourceDebugExtensions(                   //<1.16.0>
        @u2 override val attributeNameIndex: Int,   //<1.16.1>
        override val attributeLength: Int,          //<1.16.2>
        @u1 val debugExtension: ByteArray           //<1.16.3>
) : Attribute {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as SourceDebugExtensions

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (!Arrays.equals(debugExtension, other.debugExtension)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + Arrays.hashCode(debugExtension)
        return result
    }
}