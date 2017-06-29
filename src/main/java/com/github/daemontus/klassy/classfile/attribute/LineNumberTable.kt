package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class LineNumberTable(                              //<1.17.0>
        @u2 override val attributeNameIndex: Int,   //<1.17.1>
        override val attributeLength: Int,          //<1.17.2>
        @u2 val lineNumberTableLength: Int,         //<1.17.3>
        val lineNumberTable: Array<Entry>           //<1.17.4>
) : Attribute {

    class Entry(                                    //<1.17.5>
        @u2 val startPC: Int,                       //<1.17.6>
        @u2 val lineNumber: Int                     //<1.17.7>
    )

}