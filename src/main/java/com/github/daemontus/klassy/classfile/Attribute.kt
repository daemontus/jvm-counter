package com.github.daemontus.klassy.classfile

/**
 * Common supertype for all class file attributes.
 */
interface Attribute {

    /**
     * Index into the constant pool where the name of this attribute is stored as a [CpInfo.Utf8Info].
     */
    @u2 val attributeNameIndex: Int

    /**
     * The amount of bytes stored in this attribute (excluding name index and length).
     */
    val attributeLength: Int

}

