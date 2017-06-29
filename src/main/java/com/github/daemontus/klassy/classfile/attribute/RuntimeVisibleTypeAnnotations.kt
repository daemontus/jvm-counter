package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class RuntimeVisibleTypeAnnotations(           //<1.25.51>
        @u2 override val attributeNameIndex: Int,   //<1.25.0>
        override val attributeLength: Int,          //<1.25.1>
        @u2 val numAnnotations: Int,                //<1.25.2>
        val annotations: Array<TypeAnnotation>      //<1.25.3>
) : Attribute {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as RuntimeVisibleTypeAnnotations

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