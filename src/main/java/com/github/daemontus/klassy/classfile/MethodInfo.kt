package com.github.daemontus.klassy.classfile

class MethodInfo(                               //<1.5.0>
        @u2 val accessFlags: Int,               //<1.5.1>
        @u2 val nameIndex: Int,                 //<1.5.2>
        @u2 val descriptorIndex: Int,           //<1.5.3>
        @u2 val attributesCount: Int,           //<1.5.4>
        val attributes: Array<Attribute>    //<1.5.5>
)