package com.github.daemontus.klassy.classfile

// Note that we also keep the counts as separate fields.
// This is mainly to have an exact representation of the class
// in case of malformed classes, when the information can be
// interesting.

class ClassFile(                                //<1.1.0>
        val magic: Int,                         //<1.1.1>
        val minorVersion: Int,                  //<1.1.2>
        val majorVersion: Int,                  //<1.1.3>
        val constantPoolCount: Int,             //<1.1.4>
        val constantPool: Array<CpInfo>,        //<1.1.5>
        val accessFlags: Int,                   //<1.1.6>
        val thisClass: Int,                     //<1.1.7>
        val superClass: Int,                    //<1.1.8>
        val interfacesCount: Int,               //<1.1.9>
        val interfaces: IntArray,               //<1.1.10>
        val fieldsCount: Int,                   //<1.1.11>
        val fields: Array<FieldInfo>,           //<1.1.12>
        val methodsCount: Int,                  //<1.1.13>
        val methods: Array<MethodInfo>,         //<1.1.14>
        val attributesCount: Int,               //<1.1.15>
        val attributes: Array<AttributeInfo>    //<1.1.16>
)