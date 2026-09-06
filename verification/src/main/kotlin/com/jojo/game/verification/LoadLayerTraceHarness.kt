package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*


object LoadLayerTraceHarness {
    @JvmStatic
    fun main(a: Array<String>) {
        val s = java.nio.file.Files.readString(java.nio.file.Path.of(a[0]))
        Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":(null|\\\"([^\\\"]*)\\\")} ").findAll(s).toList()
        val rows = Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":(null|\\\"([^\\\"]*)\\\")}").findAll(s)
            .map { m ->
                val v =
                    LoadLayer().onCreate(if (m.groupValues[2] == "null") null else m.groupValues[3]); "{\"id\":\"${m.groupValues[1]}\",\"view\":{\"labelActive\":${v.labelActive},\"label\":\"${v.label}\",\"anime\":\"${v.anime}\"}}"
            }.joinToString(
            ",",
            "[",
            "]"
        ); java.nio.file.Files.createDirectories(java.nio.file.Path.of(a[1]).parent); java.nio.file.Files.writeString(
            java.nio.file.Path.of(a[1]),
            rows
        )
    }
}
