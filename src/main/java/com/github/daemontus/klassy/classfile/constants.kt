package com.github.daemontus.klassy.classfile

// CpInfo tag values
const val CONST_Utf8 = 1
const val CONST_Integer = 3
const val CONST_Float = 4
const val CONST_Long = 5
const val CONST_Double = 6
const val CONST_Class = 7
const val CONST_String = 8
const val CONST_FieldRef = 9
const val CONST_MethodRef = 10
const val CONST_InterfaceMethodRef = 11
const val CONST_NameAndType = 12
const val CONST_MethodHandle = 15
const val CONST_MethodType = 16
const val CONST_InvokeDynamic = 18

// Attribute names
const val CONSTANT_VALUE = "ConstantValue"
const val CODE = "Code"
const val STACK_MAP_TABLE = "StackMapTable"
const val EXCEPTIONS = "Exceptions"
const val INNER_CLASSES = "InnerClasses"
const val ENCLOSING_METHOD = "EnclosingMethod"
const val SYNTHETIC = "Synthetic"
const val SIGNATURE = "Signature"
const val SOURCE_FILE = "SourceFile"
const val SOURCE_DEBUG_EXTENSIONS = "SourceDebugExtensions"
const val LINE_NUMBER_TABLE = "LineNumberTable"
const val LOCAL_VARIABLE_TABLE = "LocalVariableTable"
const val LOCAL_VARIABLE_TYPE_TABLE = "LocalVariableTypeTable"
const val DEPRECATED = "Deprecated"
const val RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations"
const val RUNTIME_INVISIBLE_ANNOTATIONS = "RuntimeInvisibleAnnotations"
const val RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS = "RuntimeVisibleParameterAnnotations"
const val RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = "RuntimeInvisibleParameterAnnotations"
const val RUNTIME_VISIBLE_TYPE_ANNOTATIONS = "RuntimeVisibleTypeAnnotations"
const val RUNTIME_INVISIBLE_TYPE_ANNOTATIONS = "RuntimeInvisibleTypeAnnotations"
const val ANNOTATION_DEFAULT = "AnnotationDefault"
const val BOOTSTRAP_METHODS = "BootstrapMethods"
const val METHOD_PARAMETERS = "MethodParameters"

// VerificationType tags
const val TOP = 0x0
const val INTEGER = 0x1
const val FLOAT = 0x2
const val DOUBLE = 0x3
const val LONG = 0x4
const val NULL = 0x5
const val U_THIS = 0x6
const val OBJECT = 0x7
const val U_VAR = 0x8

const val MAGIC = 0xCAFEBABE.toInt()

object Mask {
    const val PUBLIC = 0x0001
    const val FINAL = 0x0010
    const val SUPER = 0x0020
    const val INTERFACE = 0x0200
    const val ABSTRACT = 0x0400
    const val SYNTHETIC = 0x1000
    const val ANNOTATION = 0x2000
    const val ENUM = 0x4000
}
