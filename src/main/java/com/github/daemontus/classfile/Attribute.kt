package com.github.daemontus.classfile

import com.github.daemontus.classfile.ConstantPool.Entry.*
import java.util.*

object AttributeNames {
    val ConstantValue = "ConstantValue"
    val Code = "Code"
    val StackMapTable = "StackMapTable"
    val Exceptions = "Exceptions"
    val InnerClasses = "InnerClasses"
    val EnclosingMethod = "EnclosingMethod"
    val Synthetic = "Synthetic"
    val Signature = "Signature"
    val SourceFile = "SourceFile"
    val LineNumberTable = "LineNumberTable"
    val LocalVariableTable = "LocalVariableTable"
    val LocalVariableTypeTable = "LocalVariableTypeTable"
    val Deprecated = "Deprecated"
}

interface Attribute {
    val name: ConstantPool.Index<Utf8>
}

data class ConstantValue(
        override val name: ConstantPool.Index<Utf8>,
        val constant: ConstantPool.Index<*>
) : Attribute

data class Code(
        override val name: ConstantPool.Index<Utf8>,
        val maxStack: Int,
        val maxLocals: Int,
        val code: List<Instruction>,
        val exceptionTable: List<ExceptionTableEntry>,
        val attributes: List<Attribute>
) : Attribute {

    data class ExceptionTableEntry(
            val startPC: Int,
            val endPC: Int,
            val handlerPC: Int,
            val catchType: ConstantPool.Index<ClassRef>?
    )

}

data class StackMapTable(
        override val name: ConstantPool.Index<Utf8>,
        val table: List<StackMapFrame>
) : Attribute {

    sealed class VerificationTypeInfo {
        object Top : VerificationTypeInfo()
        object Integer : VerificationTypeInfo()
        object Float : VerificationTypeInfo()
        object Long : VerificationTypeInfo()
        object Double : VerificationTypeInfo()
        object Null : VerificationTypeInfo()
        object UninitializedThis : VerificationTypeInfo()
        data class ObjectVariable(
                val classId: ConstantPool.Index<ClassRef>
        ) : VerificationTypeInfo()
        data class Uninitialized(
                val offset: Int
        ) : VerificationTypeInfo()
    }

    sealed class StackMapFrame {
        // same locals, empty stack
        data class SameFrame(val offsetDelta: Int) : StackMapFrame()
        // same locals, one stack item
        data class SameLocalsOneStack(
                val offsetDelta: Int,
                val stack: VerificationTypeInfo
        ) : StackMapFrame()
        // chop locals removes, empty stack
        data class ChopFrame(
                val offsetDelta: Int,
                val chop: Int
        ) : StackMapFrame()
        // append locals added, empty stack
        data class AppendFrame(
                val offsetDelta: Int,
                val append: Int,
                val locals: List<VerificationTypeInfo>
        ) : StackMapFrame()
        // full locals and stack
        data class FullFrame(
                val offsetDelta: Int,
                val locals: List<VerificationTypeInfo>,
                val stack: List<VerificationTypeInfo>
        ) : StackMapFrame()
    }
}

data class Exceptions(
        override val name: ConstantPool.Index<Utf8>,
        val table: List<ConstantPool.Index<ClassRef>>
) : Attribute

data class InnerClasses(
        override val name: ConstantPool.Index<Utf8>,
        val classes: List<Entry>
) : Attribute {

    data class Entry(
            val innerClassInfo: ConstantPool.Index<ClassRef>,
            val outerClassInfo: ConstantPool.Index<ClassRef>?,
            val innerName: ConstantPool.Index<ClassRef>?,
            val innerAccess: Int    //TODO
    )

}

data class EnclosingMethod(
        override val name: ConstantPool.Index<Utf8>,
        val classId: ConstantPool.Index<ClassRef>,
        val methodId: ConstantPool.Index<NameAndType>?
) : Attribute

data class Synthetic(
        override val name: ConstantPool.Index<Utf8>
) : Attribute

data class Signature(
        override val name: ConstantPool.Index<Utf8>,
        val value: ConstantPool.Index<Utf8>
) : Attribute

data class SourceFile(
        override val name: ConstantPool.Index<Utf8>,
        val value: ConstantPool.Index<Utf8>
) : Attribute

/*
We don't read the source debug extensions and classify them as unknown instead...
data class SourceDebugExtension(
        override val name: ConstantPool.Index<Utf8>,
        val extension: ByteArray
) : Attribute
*/

data class LineNumberTable(
        override val name: ConstantPool.Index<Utf8>,
        val table: List<Entry>
) : Attribute {

    data class Entry(
            val startPC: Int,
            val lineNumber: Int
    )

}

data class LocalVariableTable(
        override val name: ConstantPool.Index<Utf8>,
        val table: List<Entry>
) : Attribute {

    data class Entry(
            val startPC: Int,
            val length: Int,
            val index: Int,
            val name: ConstantPool.Index<Utf8>,
            val descriptor: ConstantPool.Index<Utf8>
    )

}

data class LocalVariableTypeTable(
        override val name: ConstantPool.Index<Utf8>,
        val table: List<Entry>
) : Attribute {

    data class Entry(
            val startPC: Int,
            val length: Int,
            val index: Int,
            val name: ConstantPool.Index<Utf8>,
            val signature: ConstantPool.Index<Utf8>
    )

}

data class Deprecated(
        override val name: ConstantPool.Index<Utf8>
) : Attribute

data class Annotation(
        val type: ConstantPool.Index<Utf8>,
        val data: List<KeyValuePair>
) {
    data class KeyValuePair(
            val key: ConstantPool.Index<Utf8>,
            val value: Value
    )

    sealed class Value {
        data class Byte(val value: ConstantPool.Index<IntConst>) : Value()
        data class Char(val value: ConstantPool.Index<IntConst>) : Value()
        data class Double(val value: ConstantPool.Index<DoubleConst>) : Value()
        data class Float(val value: ConstantPool.Index<FloatConst>) : Value()
        data class Int(val value: ConstantPool.Index<IntConst>) : Value()
        data class Long(val value: ConstantPool.Index<LongConst>) : Value()
        data class Short(val value: ConstantPool.Index<IntConst>) : Value()
        data class Boolean(val value: ConstantPool.Index<IntConst>) : Value()
        data class String(val value: ConstantPool.Index<Utf8>) : Value()
        data class Enum(
                val typeName: ConstantPool.Index<Utf8>,
                val constName: ConstantPool.Index<Utf8>
        ) : Value()
        data class ClassInfo(val value: ConstantPool.Index<Utf8>) : Value()
        data class NestedAnnotation(val value: Annotation) : Value()
        data class Array(val values: List<Value>) : Value()
    }
}

data class RuntimeVisibleAnnotations(
        override val name: ConstantPool.Index<Utf8>,
        val annotations: List<Annotation>
) : Attribute

data class RuntimeInvisibleAnnotations(
        override val name: ConstantPool.Index<Utf8>,
        val annotations: List<Annotation>
) : Attribute

data class RuntimeVisibleParameterAnnotations(
        override val name: ConstantPool.Index<Utf8>,
        // first dimension are method parameters, second are annotations for each parameter
        val annotations: List<List<Annotation>>
) : Attribute

data class RuntimeInvisibleParameterAnnotations(
        override val name: ConstantPool.Index<Utf8>,
        // see runtime visible annotations
        val annotations: List<List<Annotation>>
) : Attribute

data class TypeAnnotation(
    val target: Target,
    val type: ConstantPool.Index<Utf8>,
    val data: List<Annotation.KeyValuePair>
) {

    sealed class Target {

        abstract val type: Int
        abstract val path: List<PathItem>

        data class TypeParameter(override val type: Int,
                                 override val path: List<PathItem>,
                                 val typeParameterIndex: Int
        ) : Target()

        data class Supertype(override val type: Int,
                             override val path: List<PathItem>,
                             val supertypeIndex: Int
        ) : Target()

        data class TypeParameterBound(override val type: Int,
                                      override val path: List<PathItem>,
                                      val typeParameterIndex: Int,
                                      val boundIndex: Int
        ) : Target()

        data class Empty(override val type: Int,
                         override val path: List<PathItem>
        ) : Target()

        data class FormalParameter(override val type: Int,
                                   override val path: List<PathItem>,
                                   val formalParameterIndex: Int
        ) : Target()

        data class Throws(override val type: Int,
                          override val path: List<PathItem>,
                          val throwsTypeIndex: Int
        ) : Target()

        data class LocalVar(override val type: Int,
                            override val path: List<PathItem>,
                            val table: List<Entry>
        ) : Target() {
            data class Entry(val startPC: Int, val length: Int, val index: Int)
        }

        data class Catch(override val type: Int,
                         override val path: List<PathItem>,
                         val exceptionTableIndex: Int
        ) : Target()

        data class Offset(override val type: Int,
                          override val path: List<PathItem>,
                          val offset: Int
        ) : Target()

        data class TypeArgument(override val type: Int,
                                override val path: List<PathItem>,
                                val offset: Int,
                                val typeArgumentIndex: Int
        ) : Target()

        data class PathItem(val pathKind: Int, val argumentIndex: Int)
    }

}

data class RuntimeVisibleTypeAnnotations(
        override val name: ConstantPool.Index<Utf8>,
        val annotations: List<TypeAnnotation>
) : Attribute

data class RuntimeInvisibleTypeAnnotations(
        override val name: ConstantPool.Index<Utf8>,
        val annotations: List<TypeAnnotation>
) : Attribute

data class AnnotationDefault(
        override val name: ConstantPool.Index<Utf8>,
        val defaultValue: List<Annotation.Value>
) : Attribute

data class BootstrapMethods(
        override val name: ConstantPool.Index<Utf8>,
        val bootstrapMethods: List<Entry>
) : Attribute {

    data class Index(val value: Int)

    data class Entry(
            val methodRef: ConstantPool.Index<MethodHandle>,
            val arguments: List<ConstantPool.Index<*>>
    )

    operator fun get(index: Index): Entry = bootstrapMethods[index.value]

}


class UnknownAttribute(
        override val name: ConstantPool.Index<Utf8>,
        val content: ByteArray
) : Attribute {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as UnknownAttribute

        if (name != other.name) return false
        if (!Arrays.equals(content, other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + Arrays.hashCode(content)
        return result
    }

    override fun toString(): String {
        return "UnknownAttribute(name=$name, content=${Arrays.toString(content)})"
    }

}