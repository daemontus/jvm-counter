package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2

class SourceDebugExtensions(                        //<1.16.0>
        @u2 override val attributeNameIndex: Int,   //<1.16.1>
        override val attributeLength: Int,          //<1.16.2>
        @u1 val debugExtension: ByteArray           //<1.16.3>
) : Attribute