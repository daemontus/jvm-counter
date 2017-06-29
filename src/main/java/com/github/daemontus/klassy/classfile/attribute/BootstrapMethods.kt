package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class BootstrapMethods(                             //<1.28.0>
        @u2 override val attributeNameIndex: Int,   //<1.28.1>
        override val attributeLength: Int,          //<1.28.2>
        @u2 val numBootstrapMethods: Int,           //<1.28.3>
        val bootstrapMethods: Array<Entry>          //<1.28.4>
) : Attribute {

    class Entry(                                    //<1.28.5>
            @u2 val bootstrapMethodRef: Int,        //<1.28.6>
            @u2 val numBootstrapArguments: Int,     //<1.28.7>
            @u2 val bootstrapArguments: IntArray    //<1.28.8>
    )

}