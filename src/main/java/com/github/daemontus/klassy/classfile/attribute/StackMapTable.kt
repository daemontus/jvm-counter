package com.github.daemontus.klassy.classfile.attribute

import com.github.daemontus.klassy.classfile.Attribute
import com.github.daemontus.klassy.classfile.u1
import com.github.daemontus.klassy.classfile.u2
import java.util.*

data class StackMapTable(                           //<1.9.0>
        @u2 override val attributeNameIndex: Int,   //<1.9.1>
        override val attributeLength: Int,          //<1.9.2>
        @u2 val numberOfEntries: Int,               //<1.9.3>
        val entries: Array<Frame>                   //<1.9.4>
) : Attribute {

    sealed class Frame {    //<1.9.52>

        @u1 abstract val frameType: Int

        data class SameFrame(                       //<1.9.5>
                override val frameType: Int         //<1.9.6>
        ) : Frame()

        data class SameLocalsOneStackItemFrame(     //<1.9.7>
                override val frameType: Int,        //<1.9.8>
                val stack: VerificationType         //<1.9.9>
        ) : Frame()

        data class SameLocalsOneStackItemFrameExtended( //<1.9.10>
                override val frameType: Int,        //<1.9.11>
                @u2 val offsetDelta: Int,           //<1.9.12>
                val stack: VerificationType         //<1.9.13>
        ) : Frame()

        data class ChopFrame(                       //<1.9.14>
                override val frameType: Int,        //<1.9.15>
                @u2 val offsetDelta: Int            //<1.9.16>
        ) : Frame()

        data class SameFrameExtended(               //<1.9.17>
                override val frameType: Int,        //<1.9.18>
                @u2 val offsetDelta: Int            //<1.9.19>
        ) : Frame()

        data class AppendFrame(                     //<1.9.20>
                override val frameType: Int,        //<1.9.21>
                @u2 val offsetDelta: Int,           //<1.9.22>
                val locals: Array<VerificationType> //<1.9.23>
        ) : Frame() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as AppendFrame

                if (frameType != other.frameType) return false
                if (offsetDelta != other.offsetDelta) return false
                if (!Arrays.equals(locals, other.locals)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = frameType
                result = 31 * result + offsetDelta
                result = 31 * result + Arrays.hashCode(locals)
                return result
            }
        }

        data class FullFrame(                       //<1.9.24>
                override val frameType: Int,        //<1.9.25>
                @u2 val offsetDelta: Int,           //<1.9.26>
                @u2 val numberOfLocals: Int,        //<1.9.27>
                val locals: Array<VerificationType>,//<1.9.28>
                @u2 val numberOfStackItems: Int,    //<1.9.29>
                val stack: Array<VerificationType>  //<1.9.30>
        ) : Frame() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as FullFrame

                if (frameType != other.frameType) return false
                if (offsetDelta != other.offsetDelta) return false
                if (numberOfLocals != other.numberOfLocals) return false
                if (!Arrays.equals(locals, other.locals)) return false
                if (numberOfStackItems != other.numberOfStackItems) return false
                if (!Arrays.equals(stack, other.stack)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = frameType
                result = 31 * result + offsetDelta
                result = 31 * result + numberOfLocals
                result = 31 * result + Arrays.hashCode(locals)
                result = 31 * result + numberOfStackItems
                result = 31 * result + Arrays.hashCode(stack)
                return result
            }
        }

    }

    sealed class VerificationType {     //<1.9.51>

        @u1 abstract val tag: Int

        data class Top(                 //<1.9.31>
                override val tag: Int   //<1.9.32>
        ) : VerificationType()

        data class Integer(             //<1.9.33>
                override val tag: Int   //<1.9.34>
        ) : VerificationType()

        data class Float(               //<1.9.35>
                override val tag: Int   //<1.9.36>
        ) : VerificationType()

        data class Null(                //<1.9.37>
                override val tag: Int   //<1.9.38>
        ) : VerificationType()

        data class UninitializedThis(   //<1.9.39>
                override val tag: Int   //<1.9.40>
        ) : VerificationType()

        data class Object(              //<1.9.41>
                override val tag: Int,  //<1.9.42>
                @u2 val constantPoolIndex: Int  //<1.9.43>
        ) : VerificationType()

        data class UninitializedVariable(   //<1.9.44>
                override val tag: Int,  //<1.9.45>
                @u2 val offset: Int     //<1.9.46>
        ) : VerificationType()

        data class Long(                //<1.9.47>
                override val tag: Int   //<1.9.48>
        ) : VerificationType()

        data class Double(              //<1.9.49>
                override val tag: Int   //<1.9.50>
        ) : VerificationType()

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as StackMapTable

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (numberOfEntries != other.numberOfEntries) return false
        if (!Arrays.equals(entries, other.entries)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex
        result = 31 * result + attributeLength
        result = 31 * result + numberOfEntries
        result = 31 * result + Arrays.hashCode(entries)
        return result
    }


}