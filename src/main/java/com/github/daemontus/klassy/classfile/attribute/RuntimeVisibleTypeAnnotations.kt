package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class RuntimeVisibleTypeAnnotations(                //<1.25.51>
        @u2 override val attributeNameIndex: Int,   //<1.25.0>
        override val attributeLength: Int,          //<1.25.1>
        @u2 val numAnnotations: Int,                //<1.25.2>
        val annotations: Array<TypeAnnotation>      //<1.25.3>
) : Attribute