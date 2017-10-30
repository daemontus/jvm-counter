package com.github.daemontus

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Pattern

fun main(args: Array<String>) {
    val total = scrape("http://central.maven.org/maven2/")
    println("Total size: $total")
}

fun scrape(url: String): Pair<Long, Long> {
    try {
        val (counts, sizes) = Jsoup.connect(url).get().extractObjects()
                .filter { (it is RemoteObject.File) implies (it.extension == "jar") }
                .map {
                    when (it) {
                        is RemoteObject.Dir -> if (it.name != "..") scrape("$url${it.name}/") else 0L to 0L
                        is RemoteObject.File -> 1L to it.size
                    }
                }
                .unzip()
        return (counts.sum() to sizes.sum()).also {
            println("Scraped: $url for ${it.first} jars, total ${(it.second / 1000000.0).toInt()}MB")
        }
    } catch (e: Exception) {
        println(e)
        e.printStackTrace()
        return 0L to 0L
    }
}

private val fileDataPattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})\\s+(\\d+)")
private val dirDataPattern = Pattern.compile("-\\s+-").toRegex()

fun Document.extractObjects(): List<RemoteObject> {
    return this.select("#contents a").mapNotNull {
        val name = it.attr("href")
        val extraData = it.nextSibling().toString().trim()
        when {
            name.endsWith("/") -> RemoteObject.Dir(name.removeSuffix("/"))    // we found a directory
            extraData.isEmpty() -> null // these are extra files, like ..
            else -> {
                val match = fileDataPattern.matcher(extraData)
                if (!match.matches()) {
                    println("Cannot parse extra $extraData")
                    null
                } else {
                    RemoteObject.File(name, match.group(1), match.group(2).toLong())
                }
            }
        }
    }//.also { println("Result $it") }
}

sealed class RemoteObject {

    abstract val name: String

    data class Dir(override val name: String) : RemoteObject()
    data class File(override val name: String, val created: String, val size: Long) : RemoteObject()

    val extension: String
            get() = java.io.File(name).extension
}

inline infix fun Boolean.implies(other: Boolean): Boolean = !this || other