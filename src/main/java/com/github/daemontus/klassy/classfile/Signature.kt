package com.github.daemontus.klassy.classfile

import com.github.daemontus.*

/*

This is the structure of the type hierarchy. We don't actually implement it as
nested classes, since that would be incredibly verbose.

JavaTypeSignature:
    | BaseTypeSignature:
        | B | C | D | F | I | J | S | Z
    | ReferenceTypeSignature:
        | TypeVariableSignature
        | ArrayTypeSignature
        | ClassTypeSignature {
            SimpleClassTypeSignature {
                TypeArgument:
                    | Star
                    | Reference { Wildcard }
            }
        }

ClassSignature {
    TypeParameter
}
 */

typealias FieldSignature = ReferenceTypeSignature
typealias ReadResult<T> = Result<Pair<T, Int>, String>

sealed class JavaTypeSignature
sealed class ReferenceTypeSignature : JavaTypeSignature()
interface ThrowsSignature

sealed class BaseTypeSignature : JavaTypeSignature() {
    object B : BaseTypeSignature()
    object C : BaseTypeSignature()
    object D : BaseTypeSignature()
    object F : BaseTypeSignature()
    object I : BaseTypeSignature()
    object J : BaseTypeSignature()
    object S : BaseTypeSignature()
    object Z : BaseTypeSignature()
}


data class TypeVariableSignature(
        val name: String
) : ReferenceTypeSignature(), ThrowsSignature

data class ArrayTypeSignature(
        val inner: JavaTypeSignature
) : ReferenceTypeSignature()

data class ClassTypeSignature(
        val fullPackage: List<String>,
        val fullType: List<SimpleClassTypeSignature>
) : ReferenceTypeSignature(), ThrowsSignature

data class SimpleClassTypeSignature(
        val name: String,
        val typeArguments: List<TypeArgument>
)

sealed class TypeArgument {

    object Star : TypeArgument()

    data class Reference(
            val wildcard: Wildcard?,
            val signature: ReferenceTypeSignature
    ) : TypeArgument() {

        enum class Wildcard { PLUS, MINUS }

    }
}

data class ClassSignature(
        val typeParameters: List<TypeParameter>,
        val superClassSignature: ClassTypeSignature,
        val interfaceSignature: List<ClassTypeSignature>
)

data class MethodSignature(
        val typeParameters: List<TypeParameter>,
        val parameters: List<JavaTypeSignature>,
        val result: JavaTypeSignature?,
        val throws: List<ThrowsSignature>
)

data class TypeParameter(
        val name: String,
        val classBound: ReferenceTypeSignature?,
        val interfaceBound: List<ReferenceTypeSignature>
)

@Suppress("UNCHECKED_CAST") // kind of ugly, but will save you a copy
private fun <A, B, E> Result.Error<A, E>.morph(): Result.Error<B, E> = this as Result.Error<B, E>

fun readWildcard(from: String, at: Int): ReadResult<TypeArgument.Reference.Wildcard> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    val wildcard =
    when (from[at]) {
        '+' -> TypeArgument.Reference.Wildcard.PLUS
        '-' -> TypeArgument.Reference.Wildcard.MINUS
        else -> null
    }
    return if (wildcard == null) {
        "Expected '+' or '-' at position $at, but got '${from[at]}'.".asError()
    } else {
        (wildcard to (at + 1)).asOk()
    }
}

fun readTypeArgument(from: String, at: Int): ReadResult<TypeArgument> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] == '*') return (TypeArgument.Star to (at + 1)).asOk()
    else {
        val wildcard = readWildcard(from, at)
        val referenceAt = wildcard.unwrapOr(null to at).second
        return readReferenceTypeSignature(from, referenceAt).map { (ref, next) ->
            TypeArgument.Reference(wildcard.unwrapOr(null)?.first, ref) to next
        }
    }
}

fun readTypeArguments(from: String, at: Int): ReadResult<List<TypeArgument>> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != '<') return "Expected '<' at position $at, but got '${from[at]}'".asError()
    val arguments = ArrayList<TypeArgument>()
    var index = at + 1
    do {
        val argument = readTypeArgument(from, index)
        when (argument) {
            is Result.Error -> return argument.morph()
            is Result.Ok -> {
                index = argument.ok.second
                arguments.add(argument.ok.first)
            }
        }
        if (index !in from.indices) return "Unexpected end of string.".asError()
    } while (from[index] != '>')
    return (arguments to (index + 1)).asOk()
}

fun readSimpleClassTypeSignature(from: String, at: Int): ReadResult<SimpleClassTypeSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    return readUnquantifiedName(from, at).andThen { (name, next) ->
        val typeArguments = readTypeArguments(from, next)
        return when (typeArguments) {
            is Result.Error -> SimpleClassTypeSignature(name, emptyList()) to next
            is Result.Ok -> SimpleClassTypeSignature(name, typeArguments.ok.first) to typeArguments.ok.second
        }.asOk()
    }
}

fun readPackageSpecifier(from: String, at: Int): ReadResult<List<String>> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    val result = ArrayList<String>()
    var continueAt = at
    do {
        val name = readUnquantifiedName(from, continueAt)
        if (name is Result.Ok) {
            result.add(name.ok.first)
            continueAt = name.ok.second
        }
    } while (name.isOk())
    return (result to continueAt).asOk()
}

fun readClassTypeSignatureSuffix(from: String, at: Int): ReadResult<SimpleClassTypeSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != '.') return "Expected '.' at position $at, but got '${from[at]}'".asError()
    return readSimpleClassTypeSignature(from, at + 1)
}

fun readClassTypeSignature(from: String, at: Int): ReadResult<ClassTypeSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != 'L') return "Expected 'L' at position $at, but got '${from[at]}'".asError()
    return readPackageSpecifier(from, at + 1).andThen { (fullPackage, next) ->
        return readSimpleClassTypeSignature(from, next).andThen { (simpleClass, next) ->
            val fullType = ArrayList<SimpleClassTypeSignature>()
            fullType.add(simpleClass)
            var continueAt = next
            do {
                val nextName = readClassTypeSignatureSuffix(from, continueAt)
                if (nextName is Result.Ok) {
                    fullType.add(nextName.ok.first)
                    continueAt = nextName.ok.second
                }
            } while (nextName.isOk())
            return if (from[continueAt] != ';') {
                "Expected ';' at position $at, but got '${from[at]}'".asError()
            } else {
                (ClassTypeSignature(fullPackage, fullType) to (continueAt + 1)).asOk()
            }
        }
    }
}

fun readTypeVariableSignature(from: String, at: Int): ReadResult<TypeVariableSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != 'T') return "Expected 'L' at position $at, but got '${from[at]}'".asError()
    return readUnquantifiedName(from, at + 1).andThen { (name, next) ->
        return if (from[next] != ';') {
            "Expected ';' at position $at, but got '${from[at]}'".asError()
        } else {
            (TypeVariableSignature(name) to next + 1).asOk()
        }
    }
}

fun readArrayTypeSignature(from: String, at: Int): ReadResult<ArrayTypeSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != '[') return "Expected '[' at position $at, but got '${from[at]}'".asError()
    val inner = readJavaTypeSignature(from, at + 1)
    return inner.map { (sig, next) ->
        ArrayTypeSignature(sig) to next
    }
}

fun readReferenceTypeSignature(from: String, at: Int): ReadResult<ReferenceTypeSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    return when (from[at]) {
        'L' -> readClassTypeSignature(from, at)
        'T' -> readTypeVariableSignature(from, at)
        '[' -> readArrayTypeSignature(from, at)
        else -> "Expected 'L', 'T' or '[' at position $at, but got '${from[at]}'".asError()
    }
}

fun readFieldSignature(from: String, at: Int): ReadResult<FieldSignature> = readReferenceTypeSignature(from, at)

fun readBaseTypeSignature(from: String, at: Int): ReadResult<BaseTypeSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    return when (from[at]) {
        'B' -> (BaseTypeSignature.B to (at + 1)).asOk()
        'C' -> (BaseTypeSignature.C to (at + 1)).asOk()
        'D' -> (BaseTypeSignature.D to (at + 1)).asOk()
        'F' -> (BaseTypeSignature.F to (at + 1)).asOk()
        'I' -> (BaseTypeSignature.I to (at + 1)).asOk()
        'J' -> (BaseTypeSignature.J to (at + 1)).asOk()
        'S' -> (BaseTypeSignature.S to (at + 1)).asOk()
        'Z' -> (BaseTypeSignature.Z to (at + 1)).asOk()
        else -> "Expected base type signature at $at, but got '${from[at]}'".asError()
    }
}

fun readJavaTypeSignature(from: String, at: Int): ReadResult<JavaTypeSignature> {
    return readBaseTypeSignature(from, at).map { (it.first as JavaTypeSignature) to it.second }
            .orElse { readReferenceTypeSignature(from, at) }
}

fun readUnquantifiedName(from: String, at: Int): ReadResult<String> {
    val continueAt = listOf('.', ';', '[', '/').map { from.indexOf(it, startIndex = at) }.filter { it >= 0 }.min()
            ?: return "Name empty".asError()
    return (from.substring(at, continueAt) to continueAt).asOk()
}

fun readClassBound(from: String, at: Int): ReadResult<ReferenceTypeSignature?> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != ':') return "Expected ':' at position $at, but got '${from[at]}'".asError()
    return readReferenceTypeSignature(from, at + 1).or((null to at + 1).asOk())
}

fun readInterfaceBound(from: String, at: Int): ReadResult<ReferenceTypeSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != ':') return "Expected ':' at position $at, but got '${from[at]}'".asError()
    return readReferenceTypeSignature(from, at + 1)
}

fun readTypeParameter(from: String, at: Int): ReadResult<TypeParameter> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    return readUnquantifiedName(from, at).andThen { (name, next) ->
        return readClassBound(from, next).map { (classBound, next) ->
            val interfaces = ArrayList<ReferenceTypeSignature>()
            var continueAt = next
            do {
                val inf = readInterfaceBound(from, continueAt)
                if (inf is Result.Ok) {
                    interfaces.add(inf.ok.first)
                    continueAt = inf.ok.second
                }
            } while (inf.isOk())
            (TypeParameter(name, classBound, interfaces) to continueAt)
        }
    }
}

fun readTypeParameters(from: String, at: Int): ReadResult<List<TypeParameter>> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != '<') return "Expected '<' at position $at, but got '${from[at]}'".asError()
    val arguments = ArrayList<TypeParameter>()
    var index = at + 1
    do {
        val argument = readTypeParameter(from, index)
        when (argument) {
            is Result.Error -> return argument.morph()
            is Result.Ok -> {
                index = argument.ok.second
                arguments.add(argument.ok.first)
            }
        }
        if (index !in from.indices) return "Unexpected end of string.".asError()
    } while (from[index] != '>')
    return (arguments to (index + 1)).asOk()
}

fun readClassSignature(from: String, at: Int): ReadResult<ClassSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    val typeParameters: ReadResult<List<TypeParameter>> = if (from[at] != '<') (emptyList<TypeParameter>() to at).asOk() else {
        readTypeParameters(from, at)
    }
    return typeParameters.andThen { (parameters, next) ->
        return readClassTypeSignature(from, next).map { (superclass, next) ->
            val interfaces = ArrayList<ClassTypeSignature>()
            var continueAt = next
            do {
                val inf = readClassTypeSignature(from, continueAt)
                if (inf is Result.Ok) {
                    interfaces.add(inf.ok.first)
                    continueAt = inf.ok.second
                }
            } while (inf.isOk())
            (ClassSignature(parameters, superclass, interfaces) to continueAt)
        }
    }
}

fun readThrowsSignature(from: String, at: Int): ReadResult<ThrowsSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    if (from[at] != '^') return "Expected '^' at position $at, but got '${from[at]}'".asError()
    return when (from[at+1]) {
        'L' -> readClassTypeSignature(from, at + 1)
        'T' -> readTypeVariableSignature(from, at + 1)
        else -> "Expected 'L' or 'T' at position $at, but got '${from[at]}'".asError()
    }
}

fun readMethodSignature(from: String, at: Int): ReadResult<MethodSignature> {
    if (at !in from.indices) return "Unexpected end of string.".asError()
    val typeParameters: ReadResult<List<TypeParameter>> = if (from[at] != '<') (emptyList<TypeParameter>() to at).asOk() else {
        readTypeParameters(from, at)
    }
    return typeParameters.andThen { (parameters, next) ->
        return if (from[next] != '(') {
            "Expected '(' at position $at, but got '${from[at]}'".asError()
        } else {
            val arguments = ArrayList<JavaTypeSignature>()
            var continueAt = next + 1
            do {
                val arg = readJavaTypeSignature(from, continueAt)
                if (arg is Result.Ok) {
                    arguments.add(arg.ok.first)
                    continueAt = arg.ok.second
                }
            } while (arg.isOk())
            return if (from[continueAt] != ')') {
                "Expected ')' at position $at, but got '${from[at]}'".asError()
            } else {
                val ret: ReadResult<JavaTypeSignature?> = if (from[continueAt + 1] == 'V') {
                    (null to continueAt + 1).asOk()
                } else {
                    readJavaTypeSignature(from, continueAt + 1)
                }
                return ret.map { (result, next) ->
                    val throws = ArrayList<ThrowsSignature>()
                    continueAt = next
                    do {
                        val throwClause = readThrowsSignature(from, continueAt)
                        if (throwClause is Result.Ok) {
                            throws.add(throwClause.ok.first)
                            continueAt = throwClause.ok.second
                        }
                    } while (throwClause.isOk())
                    MethodSignature(parameters, arguments, result, throws) to continueAt
                }
            }
        }
    }
}

fun ReadResult<*>.isSuccess(from: String): Boolean = this is Result.Ok && this.ok.second == from.length

fun String.isMethodSignature(): Boolean = readMethodSignature(this, 0).isSuccess(this)

fun String.isFieldSignature(): Boolean = readFieldSignature(this, 0).isSuccess(this)

fun String.isClassSignature(): Boolean = readClassSignature(this, 0).isSuccess(this)