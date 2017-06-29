package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.AttributeInfo
import com.github.daemontus.klassy.classfile.CpInfo

interface AttributeIO {

    fun AttributeInfo.toAttribute(constantPool: Array<CpInfo>)

    fun Attribute.toAttributeInfo(): AttributeInfo

}