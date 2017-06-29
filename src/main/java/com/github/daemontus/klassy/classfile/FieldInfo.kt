package com.github.daemontus.klassy.classfile

class FieldInfo(                                //<1.4.0>
        @u2 val accessFlags: Int,               //<1.4.1>
        @u2 val nameIndex: Int,                 //<1.4.2>
        @u2 val descriptorIndex: Int,           //<1.4.3>
        @u2 val attributesCount: Int,           //<1.4.4>
        val attributes: Array<Attribute>    //<1.4.5>
)