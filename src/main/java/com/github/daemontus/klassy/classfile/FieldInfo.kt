package com.github.daemontus.klassy.classfile

class FieldInfo(                                //<1.4.0>
        val accessFlags: Int,                   //<1.4.1>
        val nameIndex: Int,                     //<1.4.2>
        val descriptorIndex: Int,               //<1.4.3>
        val attributesCount: Int,               //<1.4.4>
        val attributes: Array<AttributeInfo>    //<1.4.5>
)