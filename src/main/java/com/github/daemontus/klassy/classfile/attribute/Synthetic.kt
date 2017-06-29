package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

data class Synthetic(                               //<1.13.0>
        @u2 override val attributeNameIndex: Int,   //<1.13.1>
        override val attributeLength: Int           //<1.13.2>
) : Attribute