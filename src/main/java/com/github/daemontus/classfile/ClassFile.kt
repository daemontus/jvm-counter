package com.github.daemontus.classfile

data class ClassFile(
        val version: Version
) {
    data class Version(val major: Int, val minor: Int)

}

class ConstantPool {

    private val items: List<Entry> = kotlin.run {
        error("todo")
    }

    data class Index<C: Entry>(val value: Int)

    sealed class Entry {
        data class ClassRef(val name: Index<Utf8>) : Entry()
        data class FieldRef(val id: Index<NameAndType>, val classId: Index<ClassRef>) : Entry()
        data class MethodRef(val id: Index<NameAndType>, val classId: Index<ClassRef>) : Entry()
        data class InterfaceMethodRef(val id: Index<NameAndType>, val classId: Index<ClassRef>) : Entry()
        data class StringConst(val id: Index<Utf8>) : Entry()
        data class IntConst(val value: Int) : Entry()
        data class FloatConst(val value: Float) : Entry()
        data class LongConst(val value: Long) : Entry()
        data class DoubleConst(val value: Double) : Entry()
        data class NameAndType(val name: Index<Utf8>, val descriptor: Index<Utf8>) : Entry()
        data class Utf8(val value: String) : Entry()
        sealed class MethodHandle : Entry() {
            data class GetFieldRef(val ref: Index<FieldRef>)
            data class GetStaticRef(val ref: Index<FieldRef>)
            data class PutFieldRef(val ref: Index<FieldRef>)
            data class PutStaticRef(val ref: Index<FieldRef>)
            data class InvokeVirtualRef(val ref: Index<MethodRef>)
            data class NewInvokeSpecialRef(val ref: Index<MethodRef>)
            data class InvokeStaticRef(val ref: Index<Entry>)
            data class InvokeSpecialRef(val ref: Index<Entry>)
            data class InvokeInterface(val ref: Index<InterfaceMethodRef>)
        }
        data class MethodType(val descriptor: Index<Utf8>) : Entry()
        data class InvokeDynamic(val id: Index<NameAndType>, val bootstrapMethod: BootstrapMethods.Index) : Entry()
    }
}

class BootstrapMethods {

    data class Index(val value: Int)

}