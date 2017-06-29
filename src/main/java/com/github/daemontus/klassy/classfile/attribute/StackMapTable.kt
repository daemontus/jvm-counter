package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2

class StackMapTable(                                //<1.9.0>
        @u2 override val attributeNameIndex: Int,   //<1.9.1>
        override val attributeLength: Int,          //<1.9.2>
        @u2 val numberOfEntries: Int,               //<1.9.3>
        val entries: Array<Frame>                   //<1.9.4>
) : Attribute {

    sealed class Frame(@u1 val frameType: Int) {    //<1.9.52>

        class SameFrame(                            //<1.9.5>
                frameType: Int                      //<1.9.6>
        ) : Frame(frameType)

        class SameLocalsOneStackItemFrame(          //<1.9.7>
                frameType: Int,                     //<1.9.8>
                val stack: VerificationType         //<1.9.9>
        ) : Frame(frameType)

        class SameLocalsOneStackItemFrameExtended(  //<1.9.10>
                frameType: Int,                     //<1.9.11>
                @u2 val offsetDelta: Int,           //<1.9.12>
                val stack: VerificationType         //<1.9.13>
        ) : Frame(frameType)

        class ChopFrame(                            //<1.9.14>
                frameType: Int,                     //<1.9.15>
                @u2 val offsetDelta: Int            //<1.9.16>
        ) : Frame(frameType)

        class SameFrameExtended(                    //<1.9.17>
                frameType: Int,                     //<1.9.18>
                @u2 val offsetDelta: Int            //<1.9.19>
        ) : Frame(frameType)

        class AppendFrame(                          //<1.9.20>
                frameType: Int,                     //<1.9.21>
                @u2 val offsetDelta: Int,           //<1.9.22>
                val locals: Array<VerificationType> //<1.9.23>
        ) : Frame(frameType)

        class FullFrame(                            //<1.9.24>
                frameType: Int,                     //<1.9.25>
                @u2 val offsetDelta: Int,           //<1.9.26>
                @u2 val numberOfLocals: Int,        //<1.9.27>
                val locals: Array<VerificationType>,//<1.9.28>
                @u2 val numberOfStackItems: Int,    //<1.9.29>
                val stack: Array<VerificationType>  //<1.9.30>
        ) : Frame(frameType)

    }

    sealed class VerificationType(@u1 val tag: Int) {  //<1.9.51>

        class Top(                  //<1.9.31>
                tag: Int            //<1.9.32>
        ) : VerificationType(tag)

        class Integer(              //<1.9.33>
                tag: Int            //<1.9.34>
        ) : VerificationType(tag)

        class Float(                //<1.9.35>
                tag: Int            //<1.9.36>
        ) : VerificationType(tag)

        class Null(                 //<1.9.37>
                tag: Int            //<1.9.38>
        ) : VerificationType(tag)

        class UninitializedThis(    //<1.9.39>
                tag: Int            //<1.9.40>
        ) : VerificationType(tag)

        class Object(               //<1.9.41>
                tag: Int,           //<1.9.42>
                @u2 val constantPoolIndex: Int  //<1.9.43>
        ) : VerificationType(tag)

        class UninitializedVariable(        //<1.9.44>
                tag: Int,                   //<1.9.45>
                @u2 val offset: Int         //<1.9.46>
        ) : VerificationType(tag)

        class Long(                 //<1.9.47>
                tag: Int            //<1.9.48>
        ) : VerificationType(tag)

        class Double(               //<1.9.49>
                tag: Int            //<1.9.50>
        ) : VerificationType(tag)

    }

}