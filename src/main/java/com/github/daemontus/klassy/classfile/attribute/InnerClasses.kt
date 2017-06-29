package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class InnerClasses(                            //<1.11.0>
        @u2 override val attributeNameIndex: Int,   //<1.11.1>
        override val attributeLength: Int,          //<1.11.2>
        @u2 val numberOfClasses: Int,               //<1.11.3>
        val classes: Array<Entry>                   //<1.11.4>
) : Attribute {

    data class Entry(                           //<1.11.9>
            @u2 val innerClassInfoIndex: Int,   //<1.11.5>
            @u2 val outerClassInfoIndex: Int,   //<1.11.6>
            @u2 val innerNameIndex: Int,        //<1.11.7>
            @u2 val innerClassAccessFlags: Int  //<1.11.8>
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as InnerClasses

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (numberOfClasses != other.numberOfClasses) return false
        if (!Arrays.equals(classes, other.classes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + numberOfClasses
        result = 31 * result + Arrays.hashCode(classes)
        return result
    }


}