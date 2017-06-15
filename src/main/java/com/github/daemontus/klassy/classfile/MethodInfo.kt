package com.github.daemontus.klassy.classfile

class MethodInfo(                               //<1.5.0>
        val accessFlags: Int,                   //<1.5.1>
        val nameIndex: Int,                     //<1.5.2>
        val descriptorIndex: Int,               //<1.5.3>
        val attributesCount: Int,               //<1.5.4>
        val attributes: Array<AttributeInfo>    //<1.5.5>
)