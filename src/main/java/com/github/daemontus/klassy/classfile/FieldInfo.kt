package com.github.daemontus.klassy.classfile

import java.util.*

data class FieldInfo(                           //<1.4.0>
        @u2 val accessFlags: Int,               //<1.4.1>
        @u2 val nameIndex: Int,                 //<1.4.2>
        @u2 val descriptorIndex: Int,           //<1.4.3>
        @u2 val attributesCount: Int,           //<1.4.4>
        val attributes: Array<Attribute>        //<1.4.5>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as FieldInfo

        if (accessFlags != other.accessFlags) return false
        if (nameIndex != other.nameIndex) return false
        if (descriptorIndex != other.descriptorIndex) return false
        if (attributesCount != other.attributesCount) return false
        if (!Arrays.equals(attributes, other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accessFlags
        result = 31 * result + nameIndex
        result = 31 * result + descriptorIndex
        result = 31 * result + attributesCount
        result = 31 * result + Arrays.hashCode(attributes)
        return result
    }
}