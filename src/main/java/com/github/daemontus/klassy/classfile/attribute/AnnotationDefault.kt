package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

data class AnnotationDefault(                       //<1.27.0>
        @u2 override val attributeNameIndex: Int,   //<1.27.1>
        override val attributeLength: Int,          //<1.27.2>
        val defaultValue: Annotation.ElementValue   //<1.27.3>
) : Attribute