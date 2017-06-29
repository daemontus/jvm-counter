package com.github.daemontus.klassy.classfile.io.attribute

import com.github.daemontus.klassy.classfile.AttributeInfo
import java.io.ByteArrayInputStream
import java.io.DataInputStream

inline fun <R> AttributeInfo.usingStream(action: DataInputStream.() -> R) : R {
    return DataInputStream(ByteArrayInputStream(this.info)).use(action)
}