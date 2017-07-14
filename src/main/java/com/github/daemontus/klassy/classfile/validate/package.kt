package com.github.daemontus.klassy.classfile.validate

import com.github.daemontus.klassy.classfile.CpInfo

interface ValidationProblem

data class ValidationError(val message: String) : ValidationProblem
data class ValidationWarning(val message: String) : ValidationProblem

fun MutableList<ValidationProblem>.onError(message: String) {
    add(ValidationError(message))
}

fun MutableList<ValidationProblem>.onWarning(message: String) {
    add(ValidationWarning(message))
}

fun Int.hasMask(mask: Int): Boolean = this.and(mask) != 0

fun Array<CpInfo>.isCpIndex(index: Int): Boolean {
    val aI = index - 1      // array index
    return (aI in this.indices) && ((aI == 0) || ((this[aI-1] !is CpInfo.DoubleInfo) && (this[aI-1] !is CpInfo.LongInfo)))
}

inline fun <reified T> Array<CpInfo>.getCp(index: Int): T = this[index - 1] as T

inline fun <reified T> Array<CpInfo>.checkType(index: Int) = this[index - 1] is T

fun Array<CpInfo>.isObjectClass(index: Int): Boolean {
    try {
        val classInfo = getCp<CpInfo.ClassInfo>(index)
        val nameInfo = getCp<CpInfo.Utf8Info>(classInfo.nameIndex)
        return String(nameInfo.bytes) == "java/lang/Object"
    } catch (e: Exception) {
        /* Ignore, if this fails, there are more serious problems */
        return false;
    }
}

fun Array<CpInfo>.hasDescriptorType(index: Int, predicate: (String) -> Boolean): Boolean {
    try {
        val nameAndType = getCp<CpInfo.NameAndTypeInfo>(index)
        val descriptor = getCp<CpInfo.Utf8Info>(nameAndType.descriptorIndex)
        return predicate(String(descriptor.bytes))
    } catch (e: Exception) {
        /* Ignore, if this fails, there are more serious problems */
        return false;
    }
}

fun Array<CpInfo>.checkReferenceName(index: Int, predicate: (String) -> Boolean): Boolean {
    try {
        val item = getCp<CpInfo>(index)
        val nameAndTypeIndex = when (item) {
            is CpInfo.MethodRefInfo -> item.nameAndTypeIndex
            is CpInfo.InterfaceMethodRefInfo -> item.nameAndTypeIndex
            else -> error("Cannot validate!")
        }
        val nameIndex = getCp<CpInfo.NameAndTypeInfo>(nameAndTypeIndex).nameIndex
        val name = getCp<CpInfo.Utf8Info>(nameIndex)
        return predicate(String(name.bytes))
    } catch (e: Exception) {
        /* Ignore, if this fails, there are more serious problems */
        return false
    }
}

// [1.0.5]
fun String.isUnquantifiedName(): Boolean = this.isNotEmpty() && '.' !in this && ';' !in this && '[' !in this && '/' !in this

// [1.0.6]
fun String.isMethodName(): Boolean = this.isUnquantifiedName() && (('<' !in this && '>' !in this) || this == "<init>" || this == "<clinit>")

// [1.0.7]
fun String.isFullyQuantifiedName(): Boolean {
    val names = this.split("/")
    return names.isNotEmpty() && names.all { it.isUnquantifiedName() }
}