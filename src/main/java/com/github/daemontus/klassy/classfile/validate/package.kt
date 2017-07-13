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