package com.github.daemontus.classfile.validate

import com.github.daemontus.classfile.*
import com.github.daemontus.classfile.Annotation
import com.github.daemontus.classfile.Deprecated

val ClassFileAttributes = listOf(
        SourceFile::class.java, InnerClasses::class.java, EnclosingMethod::class.java,
        BootstrapMethods::class.java, Synthetic::class.java, Deprecated::class.java,
        Signature::class.java, RuntimeVisibleAnnotations::class.java, RuntimeInvisibleAnnotations::class.java,
        RuntimeVisibleTypeAnnotations::class.java, RuntimeInvisibleTypeAnnotations::class.java,
        UnknownAttribute::class.java
)

val FieldAttributes = listOf(
        ConstantValue::class.java, Synthetic::class.java, Deprecated::class.java, Signature::class.java,
        RuntimeVisibleAnnotations::class.java, RuntimeInvisibleAnnotations::class.java,
        RuntimeVisibleTypeAnnotations::class.java, RuntimeInvisibleTypeAnnotations::class.java
)

val MethodAttributes = listOf(
        Code::class.java, Exceptions::class.java, RuntimeVisibleParameterAnnotations::class.java,
        RuntimeInvisibleParameterAnnotations::class.java, AnnotationDefault::class.java,
        MethodParameters::class.java, Synthetic::class.java, Deprecated::class.java, Signature::class.java,
        RuntimeVisibleAnnotations::class.java, RuntimeInvisibleAnnotations::class.java,
        RuntimeVisibleTypeAnnotations::class.java, RuntimeInvisibleTypeAnnotations::class.java
)

val CodeAttributes = listOf(
        LineNumberTable::class.java, LocalVariableTable::class.java, LocalVariableTypeTable::class.java, StackMapTable::class.java,
        RuntimeVisibleTypeAnnotations::class.java, RuntimeInvisibleTypeAnnotations::class.java
)

/**
 * Validation of general attributes
 */
fun Attribute.validate(classFile: ClassFile) {
    val cp = classFile.constantPool
    when (this) {
        is ConstantValue -> this.validate(cp)
        is Code -> this.validate(classFile)
        is StackMapTable -> this.validate(cp)
        is Exceptions -> this.validate(cp)
        is InnerClasses -> this.validate(classFile)
        is EnclosingMethod -> this.validate(cp)
        is Synthetic -> this.validate(cp)
        is Signature -> this.validate(cp)
        is SourceFile -> this.validate(cp)
        is Deprecated -> this.validate(cp)
        is RuntimeVisibleAnnotations -> this.validate(cp)
        is RuntimeInvisibleAnnotations -> this.validate(cp)
        is RuntimeVisibleParameterAnnotations -> this.validate(cp)
        is RuntimeInvisibleParameterAnnotations -> this.validate(cp)
        is RuntimeVisibleTypeAnnotations -> this.validate(cp)
        is RuntimeInvisibleTypeAnnotations -> this.validate(cp)
        is AnnotationDefault -> this.validate(cp)
        is BootstrapMethods -> this.validate(cp)
        is MethodParameters -> this.validate(cp)
    }
}

/**
 * Validation of code attributes
 */
fun Attribute.validate(classFile: ClassFile, code: Code) {
    when (this) {
        is LineNumberTable -> this.validate(classFile, code)
        is LocalVariableTable -> this.validate(classFile, code)
        is LocalVariableTypeTable -> this.validate(classFile, code)
        is StackMapTable -> this.validate(classFile.constantPool)
        is RuntimeVisibleTypeAnnotations -> this.validate(classFile.constantPool)
        is RuntimeInvisibleTypeAnnotations -> this.validate(classFile.constantPool)
    }
}

fun ConstantValue.validate(cp: ConstantPool) {
    cp.check(this.constant)
    cp.check(this.name)
    //TODO: Should we verify the field/constant type?
    logValidate(" - constant value attribute validated")
}

fun Code.validate(classFile: ClassFile) {
    val cp = classFile.constantPool
    cp.check(this.name)
    //TODO: add other basic checks when parsing code
    // The value of start_pc must be a valid index into the code array of the opcode of an instruction.
    // The value of end_pc either must be a valid index into the code array of the opcode of an instruction or
    // must be equal to code_length, the length of the code array.
    // The verifier must check that each catch type is a subclass of type Throwable
    if (this.code.isEmpty()) {
        throw InvalidClassFileException("Empty code attribute")
    }
    if (this.code.size > (2.shl(16))) { //65536
        throw InvalidClassFileException("Code attribute too long: ${code.size}")
    }
    for ((startPC, endPC, handlerPC, catchType) in this.exceptionTable) {
        if (startPC < endPC) {
            throw InvalidClassFileException("Invalid exception handler: [$startPC, $endPC)")
        }
        if (startPC !in this.code.indices) {
            throw InvalidClassFileException("$startPC is not a valid index into the code array")
        }
        if (endPC !in this.code.indices || endPC == this.code.size) {
            throw InvalidClassFileException("$endPC is not a valid index into the code array")
        }
        if (handlerPC !in this.code.indices) {
            throw InvalidClassFileException("$handlerPC is not a valid index into the code array")
        }
        catchType?.let { catchType ->
            cp.check(catchType)
        }
    }

    attributes.all { it.javaClass in CodeAttributes }
    attributes.forEach { it.validate(classFile, this) }

    logValidate(" - code attribute validated")
}

fun StackMapTable.validate(cp: ConstantPool) {
    cp.check(this.name)
    table.forEach { frame ->
        when (frame) {
            is StackMapTable.StackMapFrame.SameLocalsOneStack -> {
                frame.stack.validate(cp)
            }
            is StackMapTable.StackMapFrame.AppendFrame -> {
                frame.locals.forEach { it.validate(cp) }
            }
            is StackMapTable.StackMapFrame.FullFrame -> {
                (frame.locals + frame.stack).forEach { it.validate(cp) }
            }
        }
    }

    logValidate(" - stack map table validated")
}

fun StackMapTable.VerificationTypeInfo.validate(cp: ConstantPool) {
    if (this is StackMapTable.VerificationTypeInfo.ObjectVariable) {
        cp.check(this.classId)
    }
}

fun Exceptions.validate(cp: ConstantPool) {
    cp.check(this.name)
    this.table.forEach {
        cp.check(it)
    }

    logValidate(" - exceptions validated")
}

fun InnerClasses.validate(classFile: ClassFile) {
    val cp = classFile.constantPool
    cp.check(this.name)
    this.classes.forEach {
        cp.check(it.innerClassInfo)
        it.outerClassInfo?.let { cp.check(it) }
        it.innerName?.let { cp.check(it) }
        if (classFile.version.major >= 51) {
            if (it.innerName == null && it.outerClassInfo != null) {
                throw InvalidClassFileException("Outer class cannot be set if inner name is not present (since version 51.0)")
            }
        }
        //TODO: Should we verify inner class access?
    }
}

fun EnclosingMethod.validate(cp: ConstantPool) {
    cp.check(this.name)
    cp.check(this.classId)
    // Method ID can be null if the class is enclosed by an initializer.
    methodId?.let { cp.check(it) }
}

fun Synthetic.validate(cp: ConstantPool) {
    cp.check(this.name)
}

fun Signature.validate(cp: ConstantPool) {
    cp.check(this.name)
    cp.check(this.value)
    //TODO: Should we verify the class signature? (JVM doesn't)
}

fun SourceFile.validate(cp: ConstantPool) {
    cp.check(this.name)
    cp.check(this.value)
}

fun LineNumberTable.validate(classFile: ClassFile, code: Code) {
    classFile.constantPool.check(this.name)
    this.table.forEach {
        if (it.startPC !in code.code.indices) {
            throw InvalidClassFileException("${it.startPC} is not a valid index into the code array.")
        }
    }
}

fun LocalVariableTable.validate(classFile: ClassFile, code: Code) {
    val cp = classFile.constantPool
    cp.check(name)
    this.table.forEach {
        if (it.startPC !in code.code.indices) {
            throw InvalidClassFileException("${it.startPC} is not a valid index into the code array.")
        }
        if (it.startPC + it.length > code.code.size) {
            throw InvalidClassFileException("${it.startPC + it.length} exceeds the length of the code array.")
        }
        cp.check(it.name)
        cp.check(it.descriptor)
        if (it.index >= code.maxLocals) {
            throw InvalidClassFileException("${it.index} cannot be a local variable")
        }
    }
}

fun LocalVariableTypeTable.validate(classFile: ClassFile, code: Code) {
    val cp = classFile.constantPool
    cp.check(name)
    this.table.forEach {
        if (it.startPC !in code.code.indices) {
            throw InvalidClassFileException("${it.startPC} is not a valid index into the code array.")
        }
        if (it.startPC + it.length > code.code.size) {
            throw InvalidClassFileException("${it.startPC + it.length} exceeds the length of the code array.")
        }
        cp.check(it.name)
        cp.check(it.signature)
        if (it.index >= code.maxLocals) {
            throw InvalidClassFileException("${it.index} cannot be a local variable")
        }
    }
}

fun Deprecated.validate(cp: ConstantPool) {
    cp.check(this.name)
}

fun Annotation.validate(cp: ConstantPool) {
    cp.check(this.type)
    this.data.forEach {
        cp.check(it.key)
        it.value.validate(cp)
    }
}

fun Annotation.Value.validate(cp: ConstantPool) {
    when (this) {
        is Annotation.Value.Byte -> cp.check(value)
        is Annotation.Value.Char -> cp.check(value)
        is Annotation.Value.Double -> cp.check(value)
        is Annotation.Value.Float -> cp.check(value)
        is Annotation.Value.Int -> cp.check(value)
        is Annotation.Value.Long -> cp.check(value)
        is Annotation.Value.Short -> cp.check(value)
        is Annotation.Value.Boolean -> cp.check(value)
        is Annotation.Value.String -> cp.check(value)
        is Annotation.Value.Enum -> {
            cp.check(typeName)
            cp.check(constName)
        }
        is Annotation.Value.ClassInfo -> cp.check(value)
        is Annotation.Value.NestedAnnotation -> value.validate(cp)
        is Annotation.Value.Array -> values.forEach {
            it.validate(cp)
        }
    }
}

fun RuntimeVisibleAnnotations.validate(cp: ConstantPool) {
    cp.check(name)
    this.annotations.forEach {
        it.validate(cp)
    }
}

fun RuntimeInvisibleAnnotations.validate(cp: ConstantPool) {
    cp.check(name)
    this.annotations.forEach {
        it.validate(cp)
    }
}

fun RuntimeVisibleParameterAnnotations.validate(cp: ConstantPool) {
    cp.check(name)
    this.annotations.flatMap { it }.forEach { it.validate(cp) }
}

fun RuntimeInvisibleParameterAnnotations.validate(cp: ConstantPool) {
    cp.check(name)
    this.annotations.flatMap { it }.forEach { it.validate(cp) }
}

fun TypeAnnotation.validate(cp: ConstantPool) {
    cp.check(this.type)
    this.data.forEach {
        cp.check(it.key)
        it.value.validate(cp)
    }
    //TODO Type target validation?
}

fun RuntimeVisibleTypeAnnotations.validate(cp: ConstantPool) {
    cp.check(name)
    this.annotations.forEach {
        it.validate(cp)
    }
}

fun RuntimeInvisibleTypeAnnotations.validate(cp: ConstantPool) {
    cp.check(name)
    this.annotations.forEach {
        it.validate(cp)
    }
}

fun AnnotationDefault.validate(cp: ConstantPool) {
    cp.check(name)
    this.defaultValue.validate(cp)
}

fun BootstrapMethods.validate(cp: ConstantPool) {
    cp.check(name)
    this.bootstrapMethods.forEach {
        cp.check(it.methodRef)
    }
}

fun MethodParameters.validate(cp: ConstantPool) {
    cp.check(name)
    this.parameters.forEach {
        it.name?.let { cp.check(it) }
    }
}