package com.github.daemontus.klassy.classfile

import java.util.*

/**
 * Data class used to store an unknown attribute.
 *
 * The attribute can be unknown either because it has not been resolved yet,
 * or because there is really no known attribute type with this name.
 *
 */
data class AttributeInfo(                               //<1.6.0>
        @u2 override val attributeNameIndex: Int,   //<1.6.1>
        override val attributeLength: Int,          //<1.6.2>
        @u1 val info: ByteArray                     //<1.6.3>
) : Attribute {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as AttributeInfo

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (!Arrays.equals(info, other.info)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + Arrays.hashCode(info)
        return result
    }

    override fun toString(): String
            = "AttributeInfo[nameIndex: $attributeNameIndex, length: $attributeLength, data: ${Arrays.toString(info)}]"

}