package com.github.daemontus.classfile

/**
 * Constant pool of a specific class.
 *
 * The constructor parameter is a direct list of constants read form a class file
 * INCLUDING the invalid entries implied by Long/Double constant entries.
 */
class ConstantPool internal constructor(
        private val items: List<Entry>
): Iterable<ConstantPool.Entry> {

    /**
     * WARNING: Java class file indexes constant pool items from 1!
     */
    data class Index<C: Entry>(val value: Int) {
        override fun toString(): String = "I($value)"
    }

    sealed class Entry {
        data class ClassRef(val name: Index<Utf8>) : Entry()
        data class FieldRef(val id: Index<NameAndType>, val classId: Index<ClassRef>) : Entry()
        data class MethodRef(val id: Index<NameAndType>, val classId: Index<ClassRef>) : Entry()
        data class InterfaceMethodRef(val id: Index<NameAndType>, val classId: Index<ClassRef>) : Entry()
        data class StringConst(val value: Index<Utf8>) : Entry()
        data class IntConst(val value: Int) : Entry()
        data class FloatConst(val value: Float) : Entry()
        data class LongConst(val value: Long) : Entry()
        data class DoubleConst(val value: Double) : Entry()
        data class NameAndType(val name: Index<Utf8>, val descriptor: Index<Utf8>) : Entry()
        data class Utf8(val value: String) : Entry()
        sealed class MethodHandle : Entry() {
            data class GetFieldRef(val ref: Index<FieldRef>) : MethodHandle()
            data class GetStaticRef(val ref: Index<FieldRef>) : MethodHandle()
            data class PutFieldRef(val ref: Index<FieldRef>) : MethodHandle()
            data class PutStaticRef(val ref: Index<FieldRef>) : MethodHandle()
            data class InvokeVirtualRef(val ref: Index<MethodRef>) : MethodHandle()
            data class NewInvokeSpecialRef(val ref: Index<MethodRef>) : MethodHandle()
            data class InvokeStaticRef(val ref: Index<Entry>) : MethodHandle()
            data class InvokeSpecialRef(val ref: Index<Entry>) : MethodHandle()
            data class InvokeInterfaceRef(val ref: Index<InterfaceMethodRef>) : MethodHandle()
        }
        data class MethodType(val descriptor: Index<Utf8>) : Entry()
        data class InvokeDynamic(val id: Index<NameAndType>, val bootstrapMethod: BootstrapMethods.Index) : Entry()
        object InvalidConstant : Entry()
    }

    val size = items.size

    operator fun get(index: Int): Entry {
        return items[index]
    }

    override fun iterator(): Iterator<Entry> = items.iterator()
}

inline operator fun <reified C: ConstantPool.Entry> ConstantPool.get(index: ConstantPool.Index<C>): C {
    check(index)
    return this[index.value - 1] as C
}

inline fun <reified C: ConstantPool.Entry> ConstantPool.check(index: ConstantPool.Index<C>) {
    if (index.value - 1 >= size || index.value < 1) {
        throw InvalidClassFileException("Invalid constant index ${index.value}. Index range is [1, ${size+1})")
    }
    val value = this[index.value - 1]
    if (value !is C) {
        illegalConstant(index.value, C::class.java, value.javaClass)
    }
}

fun <T: ConstantPool.Entry> Int.asConstantPoolIndex(): ConstantPool.Index<T> = ConstantPool.Index<T>(this)

fun illegalConstant(index: Int, expected: Class<*>, actual: Class<*>) {
    throw InvalidClassFileException("Invalid constant at index $index. Expected $expected but got $actual")
}
