package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2

class RuntimeInvisibleParameterAnnotations(             //<1.24.0>
        @u2 override val attributeNameIndex: Int,       //<1.24.1>
        override val attributeLength: Int,              //<1.24.2>
        @u1 val numParameterAnnotations: Int,           //<1.24.3>
        val parameterAnnotations: Array<Entry>          //<1.24.4>
) : Attribute {

    class Entry(                                        //<1.24.5>
            @u2 val numAnnotations: Int,                //<1.24.6>
            val annotations: Array<Annotation>          //<1.24.7>
    )

}