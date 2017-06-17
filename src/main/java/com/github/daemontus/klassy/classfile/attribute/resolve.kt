package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.stream.ERR_CpInvalidType
import com.github.daemontus.klassy.classfile.stream.parserError
import java.io.ByteArrayInputStream
import java.io.DataInputStream

fun Attribute.resolve(pool: Array<CpInfo>): AttributeInfo {

    val name = pool[attributeNameIndex - 1] //[1.0.4]
    if (name !is CpInfo.Utf8Info) {
        parserError(ERR_CpInvalidType(attributeNameIndex, CpInfo.Utf8Info::class.java, name.javaClass))
    } else {
        DataInputStream(ByteArrayInputStream(this.info)).run {
            return when (String(name.bytes)) {
                CONSTANT_VALUE -> ConstantValue.read(this, this@resolve)
                CODE -> Code.read(this, this@resolve, pool)
                STACK_MAP_TABLE -> StackMapTable.read(this, this@resolve)
                else -> this@resolve
            }
        }
    }
}