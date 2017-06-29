package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u2

class InnerClasses(                                 //<1.11.0>
        @u2 override val attributeNameIndex: Int,   //<1.11.1>
        override val attributeLength: Int,          //<1.11.2>
        @u2 val numberOfClasses: Int,               //<1.11.3>
        val classes: Array<Entry>                   //<1.11.4>
) : Attribute {

    class Entry(                                //<1.11.9>
            @u2 val innerClassInfoIndex: Int,   //<1.11.5>
            @u2 val outerClassInfoIndex: Int,   //<1.11.6>
            @u2 val innerNameIndex: Int,        //<1.11.7>
            @u2 val innerClassAccessFlags: Int  //<1.11.8>
    )

}