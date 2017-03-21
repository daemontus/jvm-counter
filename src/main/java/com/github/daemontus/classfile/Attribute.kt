package com.github.daemontus.classfile

import com.github.daemontus.classfile.ConstantPool.Entry.Utf8

data class Attribute(
        val name: ConstantPool.Index<Utf8>
)