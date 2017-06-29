package com.github.daemontus.klassy.classfile

import java.util.*

// Note that we also keep the counts as separate fields.
// This is mainly to have an exact representation of the class
// in case of malformed classes, when the information can be
// interesting.

data class ClassFile(                           //<1.1.0>
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
        val attributes: Array<Attribute>        //<1.1.16>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ClassFile

        if (magic != other.magic) return false
        if (minorVersion != other.minorVersion) return false
        if (majorVersion != other.majorVersion) return false
        if (constantPoolCount != other.constantPoolCount) return false
        if (!Arrays.equals(constantPool, other.constantPool)) return false
        if (accessFlags != other.accessFlags) return false
        if (thisClass != other.thisClass) return false
        if (superClass != other.superClass) return false
        if (interfacesCount != other.interfacesCount) return false
        if (!Arrays.equals(interfaces, other.interfaces)) return false
        if (fieldsCount != other.fieldsCount) return false
        if (!Arrays.equals(fields, other.fields)) return false
        if (methodsCount != other.methodsCount) return false
        if (!Arrays.equals(methods, other.methods)) return false
        if (attributesCount != other.attributesCount) return false
        if (!Arrays.equals(attributes, other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = magic
        result = 31 * result + minorVersion
        result = 31 * result + majorVersion
        result = 31 * result + constantPoolCount
        result = 31 * result + Arrays.hashCode(constantPool)
        result = 31 * result + accessFlags
        result = 31 * result + thisClass
        result = 31 * result + superClass
        result = 31 * result + interfacesCount
        result = 31 * result + Arrays.hashCode(interfaces)
        result = 31 * result + fieldsCount
        result = 31 * result + Arrays.hashCode(fields)
        result = 31 * result + methodsCount
        result = 31 * result + Arrays.hashCode(methods)
        result = 31 * result + attributesCount
        result = 31 * result + Arrays.hashCode(attributes)
        return result
    }
}