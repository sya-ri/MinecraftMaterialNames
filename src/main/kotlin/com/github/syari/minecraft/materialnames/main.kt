package com.github.syari.minecraft.materialnames

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val materials = getLineFromPath(args.getOrNull(0)).toMaterialList()
    materials.forEach { println(it) }
}

private fun getLineFromPath(path: String?): List<String> {
    if (path == null) {
        println("Usage: java -jar MinecraftMaterialNames lang.json")
        exitProcess(1)
    }
    val file = File(path)
    if (file.exists().not() || file.isFile.not()) {
        println("File not found (${file.absolutePath})")
        exitProcess(2)
    }
    return file.readLines()
}

private val materialRegex = """^ {4}"(block|item)\.minecraft\.\w*": ".*",${'$'}""".toRegex()

private fun List<String>.toMaterialList() = filter { it.matches(materialRegex) }.map { line ->
    line.split(": ").let { it[0].run { substring(5, lastIndex) } to it[1].run { substring(0, lastIndex - 1) }.run(::convertToOriginal) }
}

private fun convertToOriginal(unicode: String): String {
    return buildString {
        unicode.split("\\\\u".toRegex()).forEach {
            if (4 <= it.length) {
                try {
                    append(String(intArrayOf(it.substring(0, 4).toInt(16)), 0, 1))
                    append(it.substring(4, it.length))
                } catch (ex: NumberFormatException) {
                    append(it)
                }
            } else {
                append(it)
            }
        }
    }
}
