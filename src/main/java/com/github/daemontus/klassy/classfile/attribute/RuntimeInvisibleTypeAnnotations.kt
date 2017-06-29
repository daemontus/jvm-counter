package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class RuntimeInvisibleTypeAnnotations(              //<1.26.0>
        @u2 override val attributeNameIndex: Int,   //<1.26.1>
        override val attributeLength: Int,          //<1.26.2>
        @u2 val numAnnotations: Int,                //<1.26.3>
        val annotations: Array<TypeAnnotation>      //<1.26.4>
) : Attribute