package com.github.daemontus.klassy.classfile

// Note that we also keep the counts as separate fields.
// This is mainly to have an exact representation of the class
// in case of malformed classes, when the information can be
// interesting.

class ClassFile(                                //<1.1.0>
        val magic: Int,                         //<1.1.1>
        @u2 val minorVersion: Int,              //<1.1.2>
        @u2 val majorVersion: Int,              //<1.1.3>
        @u2 val constantPoolCount: Int,         //<1.1.4>
        val constantPool: Array<CpInfo>,        //<1.1.5>
        @u2 val accessFlags: Int,               //<1.1.6>
        @u2 val thisClass: Int,                 //<1.1.7>
        @u2 val superClass: Int,                //<1.1.8>
        @u2 val interfacesCount: Int,           //<1.1.9>
        @u2 val interfaces: IntArray,           //<1.1.10>
        @u2 val fieldsCount: Int,               //<1.1.11>
        val fields: Array<FieldInfo>,           //<1.1.12>
        @u2 val methodsCount: Int,              //<1.1.13>
        val methods: Array<MethodInfo>,         //<1.1.14>
        @u2 val attributesCount: Int,           //<1.1.15>
        val attributes: Array<AttributeInfo>    //<1.1.16>
)