package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2

class MethodParameters(                             //<1.29.0>
        @u2 override val attributeNameIndex: Int,   //<1.29.1>
        override val attributeLength: Int,          //<1.29.2>
        @u1 val parametersCount: Int,               //<1.29.3>
        val parameters: Array<Param>                //<1.29.4>
) : Attribute {

    class Param(                                //<1.29.5>
            @u2 val nameIndex: Int,             //<1.29.6>
            @u2 val accessFlags: Int            //<1.29.7>
    )

}