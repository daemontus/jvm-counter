package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class RuntimeVisibleAnnotations(                    //<1.21.0>
        @u2 override val attributeNameIndex: Int,   //<1.21.1>
        override val attributeLength: Int,          //<1.21.2>
        @u2 val numAnnotations: Int,                //<1.21.3>
        val annotations: Array<Annotation>          //<1.21.4>
) : Attribute