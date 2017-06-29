package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class EnclosingMethod(                              //<1.12.0>
        @u2 override val attributeNameIndex: Int,   //<1.12.1>
        override val attributeLength: Int,          //<1.12.2>
        @u2 val classIndex: Int,                    //<1.12.3>
        @u2 val methodIndex: Int                    //<1.12.4>
) : Attribute