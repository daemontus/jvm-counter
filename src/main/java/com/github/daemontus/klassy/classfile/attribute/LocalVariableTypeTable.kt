package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class LocalVariableTypeTable(                           //<1.19.0>
        @u2 override val attributeNameIndex: Int,       //<1.19.1>
        override val attributeLength: Int,              //<1.19.2>
        @u2 val localVariableTypeTableLength: Int,      //<1.19.3>
        val localVariableTypeTable: Array<Entry>        //<1.19.4>
) : Attribute {

    class Entry(                                        //<1.19.5>
            @u2 val startPC: Int,                       //<1.19.6>
            @u2 val length: Int,                        //<1.19.7>
            @u2 val nameIndex: Int,                     //<1.19.8>
            @u2 val signatureIndex: Int,                //<1.19.9>
            @u2 val index: Int                          //<1.19.10>
    )

}