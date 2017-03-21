package com.github.daemontus.classfile

data class ClassFile(
        val version: Version,
        val constantPool: ConstantPool
) {
    data class Version(val major: Int, val minor: Int)

}

class BootstrapMethods {

    data class Index(val value: Int)

}

fun Int.asBootstrapMethodIndex(): BootstrapMethods.Index = BootstrapMethods.Index(this)