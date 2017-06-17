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
        DataInputStream(ByteArrayInputStream(this.info)).use { stream ->
            return when (String(name.bytes)) {
                CONSTANT_VALUE -> ConstantValue.read(stream, this)
                CODE -> Code.read(stream, this, pool)
                STACK_MAP_TABLE -> StackMapTable.read(stream, this)
                EXCEPTIONS -> Exceptions.read(stream, this)
                INNER_CLASSES -> InnerClasses.read(stream, this)
                ENCLOSING_METHOD -> EnclosingMethod.read(stream, this)
                SYNTHETIC -> Synthetic(attributeNameIndex, attributeLength)
                SIGNATURE -> Signature.read(stream, this)
                SOURCE_FILE -> SourceFile.read(stream, this)
                SOURCE_DEBUG_EXTENSIONS -> SourceDebugExtensions(attributeNameIndex, attributeLength, info)
                LINE_NUMBER_TABLE -> LineNumberTable.read(stream, this)
                LOCAL_VARIABLE_TABLE -> LocalVariableTable.read(stream, this)
                DEPRECATED -> Deprecated(attributeNameIndex, attributeLength)
                METHOD_PARAMETERS -> MethodParameters.read(stream, this)
                BOOTSTRAP_METHODS -> BootstrapMethods.read(stream, this)
                else -> this
            }
        }
    }
}