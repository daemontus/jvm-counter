package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class BootstrapMethods(                        //<1.28.0>
        @u2 override val attributeNameIndex: Int,   //<1.28.1>
        override val attributeLength: Int,          //<1.28.2>
        @u2 val numBootstrapMethods: Int,           //<1.28.3>
        val bootstrapMethods: Array<Entry>          //<1.28.4>
) : Attribute {

    data class Entry(                               //<1.28.5>
            @u2 val bootstrapMethodRef: Int,        //<1.28.6>
            @u2 val numBootstrapArguments: Int,     //<1.28.7>
            @u2 val bootstrapArguments: IntArray    //<1.28.8>
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Entry

            if (bootstrapMethodRef != other.bootstrapMethodRef) return false
            if (numBootstrapArguments != other.numBootstrapArguments) return false
            if (!Arrays.equals(bootstrapArguments, other.bootstrapArguments)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = bootstrapMethodRef
            result = 31 * result + numBootstrapArguments
            result = 31 * result + Arrays.hashCode(bootstrapArguments)
            return result
        }

    }

     override fun equals(other: Any?): Boolean {
         if (this === other) return true
         if (other?.javaClass != javaClass) return false

         other as BootstrapMethods

         if (attributeNameIndex != other.attributeNameIndex) return false
         if (attributeLength != other.attributeLength) return false
         if (numBootstrapMethods != other.numBootstrapMethods) return false
         if (!Arrays.equals(bootstrapMethods, other.bootstrapMethods)) return false

         return true
     }

     override fun hashCode(): Int {
         var result = attributeNameIndex
         result = 31 * result + attributeLength
         result = 31 * result + numBootstrapMethods
         result = 31 * result + Arrays.hashCode(bootstrapMethods)
         return result
     }


 }