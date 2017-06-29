package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class LocalVariableTable(                           //<1.18.0>
        @u2 override val attributeNameIndex: Int,   //<1.18.1>
        override val attributeLength: Int,          //<1.18.2>
        @u2 val localVariableTableLength: Int,      //<1.18.3>
        val localVariableTable: Array<Entry>        //<1.18.4>
) : Attribute {

    class Entry(                                    //<1.18.5>
            @u2 val startPC: Int,                   //<1.18.6>
            @u2 val length: Int,                    //<1.18.7>
            @u2 val nameIndex: Int,                 //<1.18.8>
            @u2 val descriptorIndex: Int,           //<1.18.9>
            @u2 val index: Int                      //<1.18.10>
    )

}