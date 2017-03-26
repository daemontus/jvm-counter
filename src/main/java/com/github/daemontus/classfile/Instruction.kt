package com.github.daemontus.classfile

interface Instruction

class UnknownInstructionBlock(
        val data: ByteArray
) : Instruction