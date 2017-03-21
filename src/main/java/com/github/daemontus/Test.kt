package com.github.daemontus

import com.android.dex.Dex
import com.github.daemontus.classfile.readClassFile
import com.sun.tools.classfile.ClassFile
import java.io.DataInputStream
import java.util.zip.ZipFile

data class ProjectStatistics(
        var classCount: Int = 0,
        var methodCount: Int = 0,
        var fieldCount: Int = 0,
        var bytecodeSize: Long = 0,
        var nativeSize: Long = 0
)

fun main(args: Array<String>) {
    val zip = ZipFile("/Users/daemontus/Downloads/rt.jar")
    val stats = ProjectStatistics()

    zip.process(stats)

    println("Stats: $stats")
}

fun ZipFile.process(stats: ProjectStatistics) {
    for (entry in this.entries()) {
        when {
            entry.name.endsWith(".class") -> {
                println(DataInputStream(getInputStream(entry)).readClassFile())
                ClassFile.read(getInputStream(entry)).process(stats, entry.size)
            }
            entry.name.endsWith(".dex") -> {
                Dex(getInputStream(entry)).process(stats, entry.size)
            }
            entry.name.endsWith(".so") || entry.name.endsWith(".dll") || entry.name.endsWith(".dylib") -> {
                stats.nativeSize += entry.size
            }
        }
    }
}

fun ClassFile.process(stats: ProjectStatistics, fileSize: Long) {
    println(this.constant_pool.getClassInfo(this.this_class).name)
    this.methods[0].attributes[0]
    stats.classCount += 1
    stats.methodCount += this.methods.size
    stats.fieldCount += this.fields.size
    stats.bytecodeSize += fileSize
}

fun Dex.process(stats: ProjectStatistics, fileSize: Long) {
    val typeIndex = this.classDefs().first().typeIndex
    val typeId = this.typeIds()[typeIndex]
    val name = this.strings()[typeId]
    println(name)
    stats.classCount += this.classDefs().count()
    stats.methodCount += this.methodIds().count()
    stats.fieldCount += this.fieldIds().count()
    stats.bytecodeSize += fileSize
}