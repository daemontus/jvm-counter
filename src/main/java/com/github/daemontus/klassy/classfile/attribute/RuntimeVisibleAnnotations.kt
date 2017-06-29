package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class RuntimeVisibleAnnotations(               //<1.21.0>
        @u2 override val attributeNameIndex: Int,   //<1.21.1>
        override val attributeLength: Int,          //<1.21.2>
        @u2 val numAnnotations: Int,                //<1.21.3>
        val annotations: Array<Annotation>          //<1.21.4>
) : Attribute {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as RuntimeVisibleAnnotations

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (numAnnotations != other.numAnnotations) return false
        if (!Arrays.equals(annotations, other.annotations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + numAnnotations
        result = 31 * result + Arrays.hashCode(annotations)
        return result
    }

}