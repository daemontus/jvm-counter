package com.github.daemontus.classfile

import com.github.daemontus.classfile.ConstantPool.Entry.ClassRef

data class ClassFile(
        val version: Version,
        val access: Access,
        val thisClass: ConstantPool.Index<ClassRef>,
        val superClass: ConstantPool.Index<ClassRef>?,
        val interfaces: List<ConstantPool.Index<ClassRef>>,
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

}

class BootstrapMethods {

    data class Index(val value: Int)

}

fun Int.asBootstrapMethodIndex(): BootstrapMethods.Index = BootstrapMethods.Index(this)