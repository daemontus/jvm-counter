package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2

class RuntimeVisibleParameterAnnotations(           //<1.23.0>
        @u2 override val attributeNameIndex: Int,   //<1.23.1>
        override val attributeLength: Int,          //<1.23.2>
        @u1 val numParameterAnnotations: Int,       //<1.23.3>
        val parameterAnnotations: Array<Entry>      //<1.23.4>
) : Attribute {

    class Entry(                                    //<1.23.5>
            @u2 val numAnnotations: Int,            //<1.23.6>
            val annotations: Array<Annotation>      //<1.23.7>
    )

}