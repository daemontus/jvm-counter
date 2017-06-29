package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class Exceptions(                                   //<1.10.0>
        @u2 override val attributeNameIndex: Int,   //<1.10.1>
        override val attributeLength: Int,          //<1.10.2>
        @u2 val numberOfExceptions: Int,            //<1.10.3>
        @u2 val exceptionsIndexTable: IntArray      //<1.10.4>
) : Attribute