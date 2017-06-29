package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2

class Annotation(                           //<1.21.5>
        @u2 val typeIndex: Int,             //<1.21.6>
        @u2 val numElementValuePairs: Int,  //<1.21.7>
        val pairs: Array<Pair>              //<1.21.8>
) {
    class Pair(                             //<1.21.9>
            @u2 val elementNameIndex: Int,  //<1.21.10>
            val value: ElementValue         //<1.21.11>
    )

    sealed class ElementValue(              //<1.21.12>
            @u1 val tag: Int
    ) {

        class ConstValue(                   //<1.21.13>
                tag: Int,                   //<1.21.14>
                @u2 val constValueIndex: Int//<1.21.15>
        ) : ElementValue(tag)

        class EnumValue(                    //<1.21.16>
                tag: Int,                   //<1.21.17>
                @u2 val typeNameIndex: Int, //<1.21.18>
                @u2 val constNameIndex: Int //<1.21.19>
        ) : ElementValue(tag)

        class ClassValue(                   //<1.21.20>
                tag: Int,                   //<1.21.21>
                @u2 val classInfoIndex: Int //<1.21.22>
        ) : ElementValue(tag)

        class AnnotationValue(              //<1.21.23>
                tag: Int,                   //<1.21.24>
                val annotation: Annotation  //<1.21.25>
        ) : ElementValue(tag)

        class ArrayValue(                       //<1.21.26>
                tag: Int,                       //<1.21.27>
                @u2 val numValues: Int,         //<1.21.28>
                val values: Array<ElementValue> //<1.21.29>
        ) : ElementValue(tag)

    }

}