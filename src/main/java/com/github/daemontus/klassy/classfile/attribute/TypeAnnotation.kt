package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2

class TypeAnnotation(                                   //<1.25.4>
        val targetInfo: Target,                         //<1.25.5>
        val targetPath: TypePath,                       //<1.25.6>
        @u2 val typeIndex: Int,                         //<1.25.7>
        @u2 val numElementValuePairs: Int,              //<1.25.8>
        val elementValuePairs: Array<Annotation.Pair>   //<1.25.51>
) {

    sealed class Target(                        //<1.25.52>
            @u1 val targetType: Int
    ) {

        class TypeParameterTarget(              //<1.25.15>
                targetType: Int,                //<1.25.16>
                @u1 val typeParameterIndex: Int //<1.25.17>
        ) : Target(targetType)

        class SupertypeTarget(                  //<1.25.18>
                targetType: Int,                //<1.25.19>
                @u2 val supertypeIndex: Int     //<1.25.20>
        ) : Target(targetType)

        class TypeParameterBoundTarget(         //<1.25.21>
                targetType: Int,                //<1.25.22>
                @u1 val typeParameterIndex: Int,//<1.25.23>
                @u1 val boundIndex: Int         //<1.25.24>
        ) : Target(targetType)

        class EmptyTarget(                      //<1.25.25>
                targetType: Int                 //<1.25.26>
        ) : Target(targetType)

        class FormalParameterTarget(            //<1.25.27>
                targetType: Int,                //<1.25.28>
                @u1 val formalParameterIndex: Int   //<1.25.29>
        ) : Target(targetType)

        class ThrowsTarget(                     //<1.25.30>
                targetType: Int,                //<1.25.31>
                @u2 val throwsTypeIndex: Int    //<1.25.32>
        ) : Target(targetType)

        class LocalVarTarget(                   //<1.25.33>
                targetType: Int,                //<1.25.34>
                @u2 val tableLength: Int,       //<1.25.35>
                val table: Array<Entry>         //<1.25.36>
        ) : Target(targetType) {
            class Entry(                        //<1.25.37>
                    @u2 val startPC: Int,       //<1.25.38>
                    @u2 val length: Int,        //<1.25.39>
                    @u2 val index: Int          //<1.25.40>
            )
        }

        class CatchTarget(                      //<1.25.41>
                targetType: Int,                //<1.25.42>
                @u2 val exceptionTableIndex: Int//<1.25.43>
        ) : Target(targetType)

        class OffsetTarget(                     //<1.25.44>
                targetType: Int,                //<1.25.45>
                @u2 val offset: Int             //<1.25.46>
        ) : Target(targetType)

        class TypeArgumentTarget(               //<1.25.47>
                targetType: Int,                //<1.25.48>
                @u2 val offset: Int,            //<1.25.49>
                @u1 val typeArgumentIndex: Int  //<1.25.50>
        ) : Target(targetType)

    }

    class TypePath(                         //<1.25.9>
            @u1 val pathLength: Int,        //<1.25.10>
            val path: Array<Entry>          //<1.25.11>
    ) {

        class Entry(                            //<1.25.12>
                @u1 val typePathKind: Int,      //<1.25.13>
                @u1 val typeArgumentIndex: Int  //<1.25.14>
        )
    }

}