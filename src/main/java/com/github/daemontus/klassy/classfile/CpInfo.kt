package com.github.daemontus.klassy.classfile

import java.util.*

sealed class CpInfo {                                       //<1.3.49>

    @u1 abstract val tag: Int

    data class ClassInfo(                                   //<1.3.0>
            override val tag: Int,                          //<1.3.1>
            @u2 val nameIndex: Int                          //<1.3.2>
    ) : CpInfo()

    data class FieldRefInfo(                                //<1.3.3>
            override val tag: Int,                          //<1.3.4>
            @u2 val classIndex: Int,                        //<1.3.5>
            @u2 val nameAndTypeIndex: Int                   //<1.3.6>
    ) : CpInfo()

    data class MethodRefInfo(                               //<1.3.7>
            override val tag: Int,                          //<1.3.8>
            @u2 val classIndex: Int,                        //<1.3.9>
            @u2 val nameAndTypeIndex: Int                   //<1.3.10>
    ) : CpInfo()

    data class InterfaceMethodRefInfo(                      //<1.3.11>
            override val tag: Int,                          //<1.3.12>
            @u2 val classIndex: Int,                        //<1.3.13>
            @u2 val nameAndTypeIndex: Int                   //<1.3.14>
    ) : CpInfo()

    data class StringInfo(                                  //<1.3.15>
            override val tag: Int,                          //<1.3.16>
            @u2 val stringIndex: Int                        //<1.3.17>
    ) : CpInfo()

    data class IntegerInfo(                                 //<1.3.18>
            override val tag: Int,                          //<1.3.19>
            val bytes: Int                                  //<1.3.20>
    ) : CpInfo()

    data class FloatInfo(                                   //<1.3.21>
            override val tag: Int,                          //<1.3.22>
            val bytes: Float                                //<1.3.23>
    ) : CpInfo()

    data class LongInfo(                                    //<1.3.24>
            override val tag: Int,                          //<1.3.25>
            val bytes: Long                                 //<1.3.26>
    ) : CpInfo()

    data class DoubleInfo(                                  //<1.3.27>
            override val tag: Int,                          //<1.3.28>
            val bytes: Double                               //<1.3.29>
    ) : CpInfo()

    data class NameAndTypeInfo(                             //<1.3.30>
            override val tag: Int,                          //<1.3.31>
            @u2 val nameIndex: Int,                         //<1.3.32>
            @u2 val descriptorIndex: Int                    //<1.3.33>
    ) : CpInfo()

    data class Utf8Info(                                    //<1.3.34>
            override val tag: Int,                          //<1.3.35>
            @u2 val length: Int,                            //<1.3.36>
            val bytes: ByteArray                            //<1.3.37>
    ) : CpInfo() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Utf8Info

            if (tag != other.tag) return false
            if (length != other.length) return false
            if (!Arrays.equals(bytes, other.bytes)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = tag
            result = 31 * result + length
            result = 31 * result + Arrays.hashCode(bytes)
            return result
        }
    }

    data class MethodHandleInfo(                            //<1.3.38>
            override val tag: Int,                          //<1.3.39>
            @u2 val referenceKind: Int,                     //<1.3.40>
            @u2 val referenceIndex: Int                     //<1.3.41>
    ) : CpInfo()

    data class MethodTypeInfo(                              //<1.3.42>
            override val tag: Int,                          //<1.3.43>
            @u2 val descriptorIndex: Int                    //<1.3.44>
    ) : CpInfo()

    data class InvokeDynamicInfo(                           //<1.3.45>
            override val tag: Int,                          //<1.3.46>
            @u2 val bootstrapMethodAttrIndex: Int,          //<1.3.47>
            @u2 val nameAndTypeIndex: Int                   //<1.3.48>
    ) : CpInfo()

}