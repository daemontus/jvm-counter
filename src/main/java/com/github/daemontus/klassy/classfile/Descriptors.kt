package com.github.daemontus.klassy.classfile

import com.github.daemontus.klassy.classfile.validate.isFullyQuantifiedName

sealed class FieldType {                    // <1.2.1>

    sealed class BaseType : FieldType() {   // <1.2.2>
        object B : BaseType()
        object C : BaseType()
        object D : BaseType()
        object F : BaseType()
        object I : BaseType()
        object J : BaseType()
        object S : BaseType()
        object Z : BaseType()

    }

    data class ObjectType(val name: String) : FieldType() // <1.2.3>

    data class ArrayType(val type: FieldType) : FieldType()  // <1.2.4>

}

data class MethodDescriptor(
        val parameters: List<FieldType>,
        val returnType: FieldType?
)

fun String.readBaseType(): Pair<FieldType.BaseType, String>? {
    if (this.isEmpty()) return null
    val remaining = this.drop(1)
    return when (this[0]) {
        'B' -> FieldType.BaseType.B
        'C' -> FieldType.BaseType.C
        'D' -> FieldType.BaseType.D
        'F' -> FieldType.BaseType.F
        'I' -> FieldType.BaseType.I
        'J' -> FieldType.BaseType.J
        'S' -> FieldType.BaseType.S
        'Z' -> FieldType.BaseType.Z
        else -> null
    }?.to(remaining)
}

fun String.readObjectType(): Pair<FieldType.ObjectType, String>? {
    if (this.isEmpty() || this[0] != 'L') return null
    val end = this.indexOf(';')
    if (end < 0) return null
    val name = this.substring(1, end)
    if (!name.isFullyQuantifiedName()) return null
    return FieldType.ObjectType(name) to this.substring(end + 1)
}

fun String.readArrayType(): Pair<FieldType.ArrayType, String>? {
    if (this.isEmpty() || this[0] != '[') return null
    val type = readFieldType() ?: return null
    //TODO Check max dimensions
    return FieldType.ArrayType(type.first) to type.second
}

fun String.isArrayType(): Boolean = this.readArrayType()?.second?.isEmpty() ?: false

fun String.readFieldType(): Pair<FieldType, String>? {
    return readBaseType() ?: readObjectType() ?: readArrayType()
}

fun String.isFieldType(): Boolean = this.readFieldType()?.second?.isEmpty() ?: false

fun String.isFieldDescriptor(): Boolean = this.isFieldType()

fun String.readFieldDescriptor(): FieldType = this.readFieldType()!!.first

fun String.readMethodDescriptor(): Pair<MethodDescriptor, String>? {
    if (this.isEmpty() || this[0] != '(') return null
    val parameters = ArrayList<FieldType>()
    //TODO Check max parameters
    var remaining = this.drop(1)
    while (remaining[0] != ')') {
        val parameter = remaining.readFieldType() ?: return null
        parameters.add(parameter.first)
        remaining = parameter.second
    }
    if (remaining[1] == 'V') {
        return MethodDescriptor(parameters, null) to remaining.drop(2)
    } else {
        val returnType = remaining.drop(1).readFieldType() ?: return null
        return MethodDescriptor(parameters, returnType.first) to returnType.second
    }
}

fun String.isMethodDescriotor() = this.readMethodDescriptor()?.second?.isEmpty() ?: false