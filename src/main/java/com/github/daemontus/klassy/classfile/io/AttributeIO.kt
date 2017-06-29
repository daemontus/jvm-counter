package com.github.daemontus.klassy.classfile.io

import com.github.daemontus.klassy.classfile.*
import com.github.daemontus.klassy.classfile.attribute.*
import com.github.daemontus.klassy.classfile.attribute.Deprecated
import com.github.daemontus.klassy.classfile.io.attribute.*

internal fun AttributeInfo.toAttribute(pool: Array<CpInfo>): Attribute {
    val name = pool[attributeNameIndex - 1] //[1.0.4]
    if (name !is CpInfo.Utf8Info) {
        parserError(ERR_CpInvalidType(attributeNameIndex, CpInfo.Utf8Info::class.java, name.javaClass))
    } else {
        return when (String(name.bytes)) {
            CONSTANT_VALUE -> this.toConstantValue()
            CODE -> this.toCode()
            STACK_MAP_TABLE -> this.toStackMapTable()
            EXCEPTIONS -> this.toExceptions()
            INNER_CLASSES -> this.toInnerClasses()
            ENCLOSING_METHOD -> this.toEnclosingMethod()
            SYNTHETIC -> this.toSynthetic()
            SIGNATURE -> this.toSignature()
            SOURCE_FILE -> this.toSourceFile()
            SOURCE_DEBUG_EXTENSIONS -> this.toSourceDebugExtensions()
            LINE_NUMBER_TABLE -> this.toLineNumberTable()
            LOCAL_VARIABLE_TABLE -> this.toLocalVariableTable()
            LOCAL_VARIABLE_TYPE_TABLE -> this.toLocalVariableTypeTable()
            DEPRECATED -> this.toDeprecated()
            METHOD_PARAMETERS -> this.toMethodParameters()
            BOOTSTRAP_METHODS -> this.toBootstrapMethods()
            RUNTIME_VISIBLE_ANNOTATIONS -> this.toRuntimeVisibleAnnotations()
            RUNTIME_INVISIBLE_ANNOTATIONS -> this.toRuntimeInvisibleAnnotations()
            RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS -> this.toRuntimeVisibleParameterAnnotations()
            RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS -> this.toRuntimeInvisibleParameterAnnotations()
            RUNTIME_VISIBLE_TYPE_ANNOTATIONS -> this.toRuntimeVisibleTypeAnnotations()
            RUNTIME_INVISIBLE_TYPE_ANNOTATIONS -> this.toRuntimeInvisibleTypeAnnotations()
            ANNOTATION_DEFAULT -> this.toAnnotationDefault()
            else -> this
        }
    }
}

internal fun Attribute.toAttributeInfo(): AttributeInfo = when (this) {
    is ConstantValue -> this.toAttributeInfo()
    is Code -> this.toAttributeInfo()
    is StackMapTable -> this.toAttributeInfo()
    is Exceptions -> this.toAttributeInfo()
    is InnerClasses -> this.toAttributeInfo()
    is EnclosingMethod -> this.toAttributeInfo()
    is Synthetic -> this.toAttributeInfo()
    is Signature -> this.toAttributeInfo()
    is SourceFile -> this.toAttributeInfo()
    is SourceDebugExtensions -> this.toAttributeInfo()
    is LineNumberTable -> this.toAttributeInfo()
    is LocalVariableTable -> this.toAttributeInfo()
    is LocalVariableTypeTable -> this.toAttributeInfo()
    is Deprecated -> this.toAttributeInfo()
    is MethodParameters -> this.toAttributeInfo()
    is BootstrapMethods -> this.toAttributeInfo()
    is RuntimeVisibleAnnotations -> this.toAttributeInfo()
    is RuntimeInvisibleAnnotations -> this.toAttributeInfo()
    is RuntimeVisibleParameterAnnotations -> this.toAttributeInfo()
    is RuntimeInvisibleParameterAnnotations -> this.toAttributeInfo()
    is RuntimeVisibleTypeAnnotations -> this.toAttributeInfo()
    is RuntimeInvisibleTypeAnnotations -> this.toAttributeInfo()
    is AnnotationDefault -> this.toAttributeInfo()
    is AttributeInfo -> this
    else -> parserError("Unknown attribute $this")
}