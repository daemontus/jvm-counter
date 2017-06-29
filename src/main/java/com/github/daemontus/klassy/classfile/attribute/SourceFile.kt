package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

data class SourceFile(                              //<1.15.0>
        @u2 override val attributeNameIndex: Int,   //<1.15.1>
        override val attributeLength: Int,          //<1.15.2>
        @u2 val sourceFileIndex: Int                //<1.15.3>
) : Attribute