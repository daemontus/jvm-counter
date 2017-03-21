package com.github.daemontus.classfile.validate

import com.github.daemontus.classfile.ClassFile
import com.github.daemontus.classfile.ConstantPool
import com.github.daemontus.classfile.ConstantPool.Entry.*
import com.github.daemontus.classfile.ConstantPool.Entry.MethodHandle.*
import com.github.daemontus.classfile.InvalidClassFileException
import com.github.daemontus.classfile.check

fun ConstantPool.validate(classFile: ClassFile) {

    logValidate(" - start constant pool validation")

    val it = this.iterator()
    while (it.hasNext()) {
        val item = it.next()
        when (item) {
            is ClassRef -> {
                check(item.name)
                logValidate(" | - ClassRef valid")
            }
            is FieldRef -> {
                check(item.classId)
                check(item.id)
                logValidate(" | - FieldRef valid")
            }
            is MethodRef -> {
                check(item.classId)
                check(item.id)
                logValidate(" | - MethodRef valid")
            }
            is InterfaceMethodRef -> {
                check(item.classId)
                check(item.id)
                logValidate(" | - InterfaceMethodRef valid")
            }
            is StringConst -> {
                check(item.value)
                logValidate(" | - StringConst valid")
            }
            is IntConst -> logValidate(" | - IntConst valid")
            is FloatConst -> logValidate(" | - FloatConst valid")
            is LongConst -> {
                if (!it.hasNext() || it.next() !is InvalidConstant)
                    throw InvalidClassFileException("Missing empty entry after $item")
                logValidate(" | - LongConst valid")
            }
            is DoubleConst -> {
                if (!it.hasNext() || it.next() !is InvalidConstant)
                    throw InvalidClassFileException("Missing empty entry after $item")
                logValidate(" | - DoubleConst valid")
            }
            is NameAndType -> {
                check(item.name)
                check(item.descriptor)
                logValidate(" | - NameAndType valid")
            }
            is Utf8 -> logValidate(" | - Utf8 valid")
            is MethodType -> {
                check(item.descriptor)
                logValidate(" | - MethodType valid")
            }
            is InvokeDynamic -> {
                check(item.id)
                //TODO: Bootstrap
                logValidate(" | - InvokeDynamic valid")
            }
            is InvalidConstant -> throw InvalidClassFileException("Invalid constant entry not after Long or Double.")
            is MethodHandle -> {
                when (item) {
                    is GetFieldRef -> check(item.ref)
                    is GetStaticRef -> check(item.ref)
                    is PutFieldRef -> check(item.ref)
                    is PutStaticRef -> check(item.ref)
                    is InvokeVirtualRef -> check(item.ref)
                    is NewInvokeSpecialRef -> check(item.ref)
                    is InvokeStaticRef -> check(item.ref)
                    is InvokeSpecialRef -> check(item.ref)
                    is InvokeInterfaceRef -> check(item.ref)
                }
                logValidate(" | - MethodHandle valid")
            }
        }
    }

}

