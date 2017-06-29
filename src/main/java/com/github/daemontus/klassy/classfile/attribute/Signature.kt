package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

data class Signature(                               //<1.14.0>
        @u2 override val attributeNameIndex: Int,   //<1.14.1>
        override val attributeLength: Int,          //<1.14.2>
        @u2 val signatureIndex: Int                 //<1.14.3>
) : Attribute