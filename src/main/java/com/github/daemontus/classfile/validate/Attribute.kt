package com.github.daemontus.classfile.validate

import com.github.daemontus.classfile.*
import com.github.daemontus.classfile.Deprecated
import com.sun.org.apache.bcel.internal.classfile.InnerClasses
import com.sun.org.apache.bcel.internal.classfile.SourceFile

val classFileAttributes = listOf(
        SourceFile::class, InnerClasses::class, EnclosingMethod::class,
        BootstrapMethods::class, Synthetic::class, Deprecated::class,
        Signature::class, RuntimeVisibleAnnotations::class, RuntimeInvisibleAnnotations::class,
        RuntimeVisibleTypeAnnotations::class, RuntimeInvisibleTypeAnnotations::class,
        UnknownAttribute::class
)

val FieldAttributes = listOf(
        ConstantValue::class, Synthetic::class, Deprecated::class, Signature::class,
        RuntimeVisibleAnnotations::class, RuntimeInvisibleAnnotations::class,
        RuntimeVisibleTypeAnnotations::class, RuntimeInvisibleTypeAnnotations::class
)

val MethodAttributes = listOf(
        Code::class, Exceptions::class, RuntimeVisibleParameterAnnotations::class,
        RuntimeInvisibleParameterAnnotations::class, AnnotationDefault::class,
        MethodParameters::class, Synthetic::class, Deprecated::class, Signature::class,
        RuntimeVisibleAnnotations::class, RuntimeInvisibleAnnotations::class,
        RuntimeVisibleTypeAnnotations::class, RuntimeInvisibleTypeAnnotations::class
)

val CodeAttributes = listOf(
        LineNumberTable::class, LocalVariableTable::class, LocalVariableTypeTable::class, StackMapTable::class,
        RuntimeVisibleTypeAnnotations::class, RuntimeInvisibleTypeAnnotations::class
)