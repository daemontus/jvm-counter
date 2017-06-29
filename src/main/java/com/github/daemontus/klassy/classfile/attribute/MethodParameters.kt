package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class MethodParameters(                        //<1.29.0>
        @u2 override val attributeNameIndex: Int,   //<1.29.1>
        override val attributeLength: Int,          //<1.29.2>
        @u1 val parametersCount: Int,               //<1.29.3>
        val parameters: Array<Param>                //<1.29.4>
) : Attribute {

    data class Param(                           //<1.29.5>
            @u2 val nameIndex: Int,             //<1.29.6>
            @u2 val accessFlags: Int            //<1.29.7>
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as MethodParameters

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (parametersCount != other.parametersCount) return false
        if (!Arrays.equals(parameters, other.parameters)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + parametersCount
        result = 31 * result + Arrays.hashCode(parameters)
        return result
    }


}