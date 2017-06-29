package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class RuntimeInvisibleAnnotations(                  //<1.22.0>
        @u2 override val attributeNameIndex: Int,   //<1.22.1>
        override val attributeLength: Int,          //<1.22.2>
        @u2 val numAnnotations: Int,                //<1.22.3>
        val annotations: Array<Annotation>          //<1.22.4>
) : Attribute