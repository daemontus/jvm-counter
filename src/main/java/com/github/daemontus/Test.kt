package com.github.daemontus

import com.android.dex.Dex
import com.android.dx.dex.DexOptions
import com.android.dx.dex.file.DexFile
import com.sun.tools.classfile.ClassFile
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel
import java.io.File
import java.io.FileInputStream
import java.util.jar.JarInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

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