package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class Deprecated(                                   //<1.20.0>
        @u2 override val attributeNameIndex: Int,   //<1.20.1>
        override val attributeLength: Int           //<1.20.2>
) : Attribute