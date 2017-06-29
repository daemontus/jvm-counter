package com.github.daemontus.klassy.classfile

// CpInfo tag values
val CONST_Utf8 = 1
val CONST_Integer = 3
val CONST_Float = 4
val CONST_Long = 5
val CONST_Double = 6
val CONST_Class = 7
val CONST_String = 8
val CONST_FieldRef = 9
val CONST_MethodRef = 10
val CONST_InterfaceMethodRef = 11
val CONST_NameAndType = 12
val CONST_MethodHandle = 15
val CONST_MethodType = 16
val CONST_InvokeDynamic = 18

// Attribute names
val CONSTANT_VALUE = "ConstantValue"
val CODE = "Code"
val STACK_MAP_TABLE = "StackMapTable"
val EXCEPTIONS = "Exceptions"
val INNER_CLASSES = "InnerClasses"
val ENCLOSING_METHOD = "EnclosingMethod"
val SYNTHETIC = "Synthetic"
val SIGNATURE = "Signature"
val SOURCE_FILE = "SourceFile"
val SOURCE_DEBUG_EXTENSIONS = "SourceDebugExtensions"
val LINE_NUMBER_TABLE = "LineNumberTable"
val LOCAL_VARIABLE_TABLE = "LocalVariableTable"
val LOCAL_VARIABLE_TYPE_TABLE = "LocalVariableTypeTable"
val DEPRECATED = "Deprecated"
val RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations"
val RUNTIME_INVISIBLE_ANNOTATIONS = "RuntimeInvisibleAnnotations"
val RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS = "RuntimeVisibleParameterAnnotations"
val RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = "RuntimeInvisibleParameterAnnotations"
val RUNTIME_VISIBLE_TYPE_ANNOTATIONS = "RuntimeVisibleTypeAnnotations"
val RUNTIME_INVISIBLE_TYPE_ANNOTATIONS = "RuntimeInvisibleTypeAnnotations"
val ANNOTATION_DEFAULT = "AnnotationDefault"
val BOOTSTRAP_METHODS = "BootstrapMethods"
val METHOD_PARAMETERS = "MethodParameters"

// VerificationType tags
val TOP = 0x0
val INTEGER = 0x1
val FLOAT = 0x2
val DOUBLE = 0x3
val LONG = 0x4
val NULL = 0x5
val U_THIS = 0x6
val OBJECT = 0x7
val U_VAR = 0x8