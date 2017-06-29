package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class RuntimeVisibleParameterAnnotations(      //<1.23.0>
        @u2 override val attributeNameIndex: Int,   //<1.23.1>
        override val attributeLength: Int,          //<1.23.2>
        @u1 val numParameterAnnotations: Int,       //<1.23.3>
        val parameterAnnotations: Array<Entry>      //<1.23.4>
) : Attribute {

    data class Entry(                                    //<1.23.5>
            @u2 val numAnnotations: Int,            //<1.23.6>
            val annotations: Array<Annotation>      //<1.23.7>
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Entry

            if (numAnnotations != other.numAnnotations) return false
            if (!Arrays.equals(annotations, other.annotations)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = numAnnotations
            result = 31 * result + Arrays.hashCode(annotations)
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as RuntimeVisibleParameterAnnotations

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (numParameterAnnotations != other.numParameterAnnotations) return false
        if (!Arrays.equals(parameterAnnotations, other.parameterAnnotations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + numParameterAnnotations
        result = 31 * result + Arrays.hashCode(parameterAnnotations)
        return result
    }


}