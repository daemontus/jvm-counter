package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class Annotation(                      //<1.21.5>
        @u2 val typeIndex: Int,             //<1.21.6>
        @u2 val numElementValuePairs: Int,  //<1.21.7>
        val pairs: Array<Pair>              //<1.21.8>
) {
    data class Pair(                        //<1.21.9>
            @u2 val elementNameIndex: Int,  //<1.21.10>
            val value: ElementValue         //<1.21.11>
    )

    sealed class ElementValue {             //<1.21.12>

        @u1 abstract val tag: Int

        class ConstValue(                   //<1.21.13>
                override val tag: Int,      //<1.21.14>
                @u2 val constValueIndex: Int//<1.21.15>
        ) : ElementValue()

        class EnumValue(                    //<1.21.16>
                override val tag: Int,      //<1.21.17>
                @u2 val typeNameIndex: Int, //<1.21.18>
                @u2 val constNameIndex: Int //<1.21.19>
        ) : ElementValue()

        class ClassValue(                   //<1.21.20>
                override val tag: Int,      //<1.21.21>
                @u2 val classInfoIndex: Int //<1.21.22>
        ) : ElementValue()

        class AnnotationValue(              //<1.21.23>
                override val tag: Int,      //<1.21.24>
                val annotation: Annotation  //<1.21.25>
        ) : ElementValue()

        class ArrayValue(                       //<1.21.26>
                override val tag: Int,          //<1.21.27>
                @u2 val numValues: Int,         //<1.21.28>
                val values: Array<ElementValue> //<1.21.29>
        ) : ElementValue()

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Annotation

        if (typeIndex != other.typeIndex) return false
        if (numElementValuePairs != other.numElementValuePairs) return false
        if (!Arrays.equals(pairs, other.pairs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = typeIndex
        result = 31 * result + numElementValuePairs
        result = 31 * result + Arrays.hashCode(pairs)
        return result
    }


}