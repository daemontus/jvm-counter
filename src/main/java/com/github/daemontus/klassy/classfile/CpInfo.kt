package com.github.daemontus.klassy.classfile

sealed class CpInfo(                                        //<1.3.49>
        @u1 val tag: Int
) {

    class ClassInfo(                                        //<1.3.0>
            tag: Int,                                       //<1.3.1>
            @u2 val nameIndex: Int                          //<1.3.2>
    ) : CpInfo(tag)

    class FieldRefInfo(                                     //<1.3.3>
            tag: Int,                                       //<1.3.4>
            @u2 val classIndex: Int,                        //<1.3.5>
            @u2 val nameAndTypeIndex: Int                   //<1.3.6>
    ) : CpInfo(tag)

    class MethodRefInfo(                                    //<1.3.7>
            tag: Int,                                       //<1.3.8>
            @u2 val classIndex: Int,                        //<1.3.9>
            @u2 val nameAndTypeIndex: Int                   //<1.3.10>
    ) : CpInfo(tag)

    class InterfaceMethodRefInfo(                           //<1.3.11>
            tag: Int,                                       //<1.3.12>
            @u2 val classIndex: Int,                        //<1.3.13>
            @u2 val nameAndTypeIndex: Int                   //<1.3.14>
    ) : CpInfo(tag)

    class StringInfo(                                       //<1.3.15>
            tag: Int,                                       //<1.3.16>
            @u2 val stringIndex: Int                        //<1.3.17>
    ) : CpInfo(tag)

    class IntegerInfo(                                      //<1.3.18>
            tag: Int,                                       //<1.3.19>
            val bytes: Int                                  //<1.3.20>
    ) : CpInfo(tag)

    class FloatInfo(                                        //<1.3.21>
            tag: Int,                                       //<1.3.22>
            val bytes: Float                                //<1.3.23>
    ) : CpInfo(tag)

    class LongInfo(                                         //<1.3.24>
            tag: Int,                                       //<1.3.25>
            val bytes: Long                                 //<1.3.26>
    ) : CpInfo(tag)

    class DoubleInfo(                                       //<1.3.27>
            tag: Int,                                       //<1.3.28>
            val bytes: Double                               //<1.3.29>
    ) : CpInfo(tag)

    class NameAndTypeInfo(                                  //<1.3.30>
            tag: Int,                                       //<1.3.31>
            @u2 val nameIndex: Int,                         //<1.3.32>
            @u2 val descriptorIndex: Int                    //<1.3.33>
    ) : CpInfo(tag)

    class Utf8Info(                                         //<1.3.34>
            tag: Int,                                       //<1.3.35>
            @u2 val length: Int,                            //<1.3.36>
            val bytes: ByteArray                            //<1.3.37>
    ) : CpInfo(tag)

    class MethodHandleInfo(                                 //<1.3.38>
            tag: Int,                                       //<1.3.39>
            @u2 val referenceKind: Int,                     //<1.3.40>
            @u2 val referenceIndex: Int                     //<1.3.41>
    ) : CpInfo(tag)

    class MethodTypeInfo(                                   //<1.3.42>
            tag: Int,                                       //<1.3.43>
            @u2 val descriptorIndex: Int                    //<1.3.44>
    ) : CpInfo(tag)

    class InvokeDynamicInfo(                                //<1.3.45>
            tag: Int,                                       //<1.3.46>
            @u2 val bootstrapMethodAttrIndex: Int,          //<1.3.47>
            @u2 val nameAndTypeIndex: Int                   //<1.3.48>
    ) : CpInfo(tag)

}