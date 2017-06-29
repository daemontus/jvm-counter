package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class TypeAnnotation(                              //<1.25.4>
        val targetInfo: Target,                         //<1.25.5>
        val targetPath: TypePath,                       //<1.25.6>
        @u2 val typeIndex: Int,                         //<1.25.7>
        @u2 val numElementValuePairs: Int,              //<1.25.8>
        val elementValuePairs: Array<Annotation.Pair>   //<1.25.51>
) {

    sealed class Target {                       //<1.25.52>

        @u1 abstract val targetType: Int

        data class TypeParameterTarget(         //<1.25.15>
                override val targetType: Int,   //<1.25.16>
                @u1 val typeParameterIndex: Int //<1.25.17>
        ) : Target()

        data class SupertypeTarget(             //<1.25.18>
                override val targetType: Int,   //<1.25.19>
                @u2 val supertypeIndex: Int     //<1.25.20>
        ) : Target()

        data class TypeParameterBoundTarget(    //<1.25.21>
                override val targetType: Int,   //<1.25.22>
                @u1 val typeParameterIndex: Int,//<1.25.23>
                @u1 val boundIndex: Int         //<1.25.24>
        ) : Target()

        data class EmptyTarget(                 //<1.25.25>
                override val targetType: Int    //<1.25.26>
        ) : Target()

        data class FormalParameterTarget(       //<1.25.27>
                override val targetType: Int,   //<1.25.28>
                @u1 val formalParameterIndex: Int   //<1.25.29>
        ) : Target()

        data class ThrowsTarget(                //<1.25.30>
                override val targetType: Int,   //<1.25.31>
                @u2 val throwsTypeIndex: Int    //<1.25.32>
        ) : Target()

        data class LocalVarTarget(              //<1.25.33>
                override val targetType: Int,   //<1.25.34>
                @u2 val tableLength: Int,       //<1.25.35>
                val table: Array<Entry>         //<1.25.36>
        ) : Target() {
            data class Entry(                   //<1.25.37>
                    @u2 val startPC: Int,       //<1.25.38>
                    @u2 val length: Int,        //<1.25.39>
                    @u2 val index: Int          //<1.25.40>
            )

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as LocalVarTarget

                if (targetType != other.targetType) return false
                if (tableLength != other.tableLength) return false
                if (!Arrays.equals(table, other.table)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = targetType
                result = 31 * result + tableLength
                result = 31 * result + Arrays.hashCode(table)
                return result
            }


        }

        data class CatchTarget(                 //<1.25.41>
                override val targetType: Int,   //<1.25.42>
                @u2 val exceptionTableIndex: Int//<1.25.43>
        ) : Target()

        data class OffsetTarget(                //<1.25.44>
                override val targetType: Int,   //<1.25.45>
                @u2 val offset: Int             //<1.25.46>
        ) : Target()

        data class TypeArgumentTarget(          //<1.25.47>
                override val targetType: Int,   //<1.25.48>
                @u2 val offset: Int,            //<1.25.49>
                @u1 val typeArgumentIndex: Int  //<1.25.50>
        ) : Target()

    }

    data class TypePath(                    //<1.25.9>
            @u1 val pathLength: Int,        //<1.25.10>
            val path: Array<Entry>          //<1.25.11>
    ) {

        data class Entry(                       //<1.25.12>
                @u1 val typePathKind: Int,      //<1.25.13>
                @u1 val typeArgumentIndex: Int  //<1.25.14>
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as TypePath

            if (pathLength != other.pathLength) return false
            if (!Arrays.equals(path, other.path)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = pathLength
            result = 31 * result + Arrays.hashCode(path)
            return result
        }


    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as TypeAnnotation

        if (targetInfo != other.targetInfo) return false
        if (targetPath != other.targetPath) return false
        if (typeIndex != other.typeIndex) return false
        if (numElementValuePairs != other.numElementValuePairs) return false
        if (!Arrays.equals(elementValuePairs, other.elementValuePairs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = targetInfo.hashCode()
        result = 31 * result + targetPath.hashCode()
        result = 31 * result + typeIndex
        result = 31 * result + numElementValuePairs
        result = 31 * result + Arrays.hashCode(elementValuePairs)
        return result
    }


}