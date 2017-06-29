package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2

class Code(                                             //<1.8.0>
        @u2 override val attributeNameIndex: Int,       //<1.8.1>
        override val attributeLength: Int,              //<1.8.2>
        @u2 val maxStack: Int,                          //<1.8.3>
        @u2 val maxLocals: Int,                         //<1.8.4>
        @u2 val codeLength: Int,                        //<1.8.5>
        @u1 val code: ByteArray,                        //<1.8.6>
        @u2 val exceptionTableLength: Int,              //<1.8.7>
        val exceptionTable: Array<ExceptionTableEntry>, //<1.8.8>
        @u2 val attributesCount: Int,                   //<1.8.9>
        val attributes: Array<Attribute>            //<1.8.10>
) : Attribute {

    class ExceptionTableEntry(                          //<1.8.11>
            @u2 val startPC: Int,                       //<1.8.12>
            @u2 val endPC: Int,                         //<1.8.13>
            @u2 val handlerPC: Int,                     //<1.8.14>
            @u2 val catchType: Int                      //<1.8.15>
    )

}