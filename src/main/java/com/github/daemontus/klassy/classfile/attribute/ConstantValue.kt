package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class ConstantValue(                                //<1.7.0>
        @u2 override val attributeNameIndex: Int,   //<1.7.1>
        override val attributeLength: Int,          //<1.7.2>
        @u2 val constantValueIndex: Int             //<1.7.3>
) : Attribute