package com.github.daemontus.classfile

import com.github.daemontus.classfile.ConstantPool.Entry.ClassRef
import com.github.daemontus.classfile.ConstantPool.Entry.Utf8

data class ClassFile(
        val version: Version,
        val access: Access,
        val thisClass: ConstantPool.Index<ClassRef>,
        val superClass: ConstantPool.Index<ClassRef>?,
        val interfaces: List<ConstantPool.Index<ClassRef>>,
        val fields: List<FieldInfo>,
        val methods: List<MethodInfo>,
        val attributes: List<Attribute>,
        val constantPool: ConstantPool
) {
    data class Version(val major: Int, val minor: Int) {
        override fun toString(): String = "$major.$minor"
    }

    data class Access(
            private val value: Int
    ) {
        val isPublic: Boolean = value.and(0x0001) != 0
        val isFinal: Boolean = value.and(0x0010) != 0
        val isInterface: Boolean = value.and(0x0200) != 0
        val isAbstract: Boolean = value.and(0x0400) != 0
        val isSynthetic: Boolean = value.and(0x1000) != 0
        val isAnnotation: Boolean = value.and(0x2000) != 0
        val isEnum: Boolean = value.and(0x4000) != 0

        //See class file format section 4.1 - super flag is ignored since Java SE 8
        val hasSuperFlag: Boolean = value.and(0x0020) != 0

        override fun toString(): String {
            val modifiers = ArrayList<String>()
            if (isPublic) modifiers.add("public")
            if (isFinal) modifiers.add("final")
            if (isInterface) modifiers.add("interface")
            if (isAbstract) modifiers.add("abstract")
            if (isSynthetic) modifiers.add("synthetic")
            if (isAnnotation) modifiers.add("annotation")
            if (isEnum) modifiers.add("enum")
            if (hasSuperFlag) modifiers.add("super")

            return modifiers.joinToString(prefix = "[", postfix = "]")
        }
    }

    data class FieldInfo(
        val access: FieldInfo.Access,
        val name: ConstantPool.Index<Utf8>,
        val descriptor: ConstantPool.Index<Utf8>,
        val attributes: List<Attribute>
    ) {
        data class Access(private val value: Int) {

            val isPublic: Boolean = value.and(0x0001) != 0
            val isPrivate: Boolean = value.and(0x0002) != 0
            val isProtected: Boolean = value.and(0x0004) != 0
            val isStatic: Boolean = value.and(0x0008) != 0
            val isFinal: Boolean = value.and(0x0010) != 0
            val isVolatile: Boolean = value.and(0x0040) != 0
            val isTransient: Boolean = value.and(0x0080) != 0
            val isSynthetic: Boolean = value.and(0x1000) != 0
            val isEnum: Boolean = value.and(0x4000) != 0

            override fun toString(): String {
                val modifiers = ArrayList<String>()
                if (isPublic) modifiers.add("public")
                if (isPrivate) modifiers.add("private")
                if (isProtected) modifiers.add("protected")
                if (isStatic) modifiers.add("static")
                if (isFinal) modifiers.add("final")
                if (isVolatile) modifiers.add("volatile")
                if (isTransient) modifiers.add("transient")
                if (isSynthetic) modifiers.add("synthetic")
                if (isEnum) modifiers.add("enum")

                return modifiers.joinToString(prefix = "[", postfix = "]")
            }
        }
    }

    data class MethodInfo(
        val access: MethodInfo.Access,
        val name: ConstantPool.Index<Utf8>,
        val descriptor: ConstantPool.Index<Utf8>,
        val attributes: List<Attribute>
    ) {

        data class Access(private val value: Int) {

            val isPublic: Boolean = value.and(0x0001) != 0
            val isPrivate: Boolean = value.and(0x0002) != 0
            val isProtected: Boolean = value.and(0x0004) != 0
            val isStatic: Boolean = value.and(0x0008) != 0
            val isFinal: Boolean = value.and(0x0010) != 0
            val isSynchronized: Boolean = value.and(0x0020) != 0
            val isBridge: Boolean = value.and(0x0040) != 0
            val isVarargs: Boolean = value.and(0x0080) != 0
            val isNative: Boolean = value.and(0x0100) != 0
            val isAbstract: Boolean = value.and(0x0400) != 0
            val isStrict: Boolean = value.and(0x0800) != 0
            val isSynthetic: Boolean = value.and(0x1000) != 0

            override fun toString(): String {
                val modifiers = ArrayList<String>()

                if (isPublic) modifiers.add("public")
                if (isPrivate) modifiers.add("private")
                if (isProtected) modifiers.add("protected")
                if (isStatic) modifiers.add("static")
                if (isFinal) modifiers.add("final")
                if (isSynchronized) modifiers.add("synchronized")
                if (isBridge) modifiers.add("bridge")
                if (isVarargs) modifiers.add("varargs")
                if (isNative) modifiers.add("native")
                if (isAbstract) modifiers.add("abstract")
                if (isStrict) modifiers.add("strict")
                if (isSynthetic) modifiers.add("synthetic")

                return modifiers.joinToString(prefix = "[", postfix = "]")
            }
        }

    }

}

fun Int.asBootstrapMethodIndex(): BootstrapMethods.Index = BootstrapMethods.Index(this)