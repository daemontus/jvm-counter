package com.github.daemontus.classfile

data class ClassFile(
        val version: Version,
        val constantPool: ConstantPool
) {
    data class Version(val major: Int, val minor: Int)

}

/**
 * Constant pool of a specific class.
 *
 * The constructor parameter is a direct list of constants read form a class file
 * INCLUDING the invalid entries implied by Long/Double constant entries.
 */
class ConstantPool internal constructor(
        private val items: List<Entry>
) {

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
        object InvalidConstant : Entry()
    }

    operator fun get(index: Int): Entry {
        return items[index]
    }

}

inline operator fun <reified C: ConstantPool.Entry> ConstantPool.get(index: ConstantPool.Index<C>): C {
    check(index)
    return this[index.value] as C
}

inline fun <reified C: ConstantPool.Entry> ConstantPool.check(index: ConstantPool.Index<C>) {
    val value = this[index.value]
    if (value !is C) {
        illegalConstant(index.value, C::class.java, value.javaClass)
    }
}

fun <T: ConstantPool.Entry> Int.asConstantPoolIndex(): ConstantPool.Index<T> = ConstantPool.Index<T>(this)

fun illegalConstant(index: Int, expected: Class<*>, actual: Class<*>) {
    throw MalformedClassFileException("Invalid constant at index ${index}. Expected $expected but got $actual")
}

class BootstrapMethods {

    data class Index(val value: Int)

}