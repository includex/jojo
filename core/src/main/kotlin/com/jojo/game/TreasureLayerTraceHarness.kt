package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** Kotlin half of the source-factory TreasureLayer contract fixture. */
object TreasureLayerTraceHarness {
    private data class Case(
        val name: String,
        val treasures: List<TreasureLayer.Item>,
        val discovered: Set<Int>,
        val events: List<String>
    )

    private fun q(s: String?) = s?.let { "\"${it.replace("\\", "\\\\").replace("\"", "\\\"")}\"" } ?: "null"
    private fun cases(text: String): List<Case> =
        Regex("""\{\"name\":\"([^\"]+)\",\"treasures\":\[(.*?)]?,\"discovered\":\[(.*?)]?,\"events\":\[(.*?)]?}""")
            .findAll(text.replace(Regex("\\s+"), "")).map { m ->
                val items =
                    Regex("""\{\"id\":(\d+),\"name\":\"([^\"]*)\",\"icon\":(\d+),\"property\":(true|false),\"description\":\"([^\"]*)\"}""")
                        .findAll(m.groupValues[2]).map { x ->
                            TreasureLayer.Item(
                                x.groupValues[1].toInt(),
                                x.groupValues[2],
                                x.groupValues[3].toInt(),
                                x.groupValues[4].toBoolean(),
                                x.groupValues[5]
                            )
                        }.toList()
                val found = Regex("\\d+").findAll(m.groupValues[3]).map { it.value.toInt() }.toSet()
                val events = Regex("\"([^\"]+)\"").findAll(m.groupValues[4]).map { it.groupValues[1] }.toList()
                Case(m.groupValues[1], items, found, events)
            }.toList()

    private fun state(l: TreasureLayer, step: String, removed: Boolean, route: Int?): String {
        val rows = (l.rows.filter { it.discovered } + l.rows.filterNot { it.discovered }).joinToString(
            ",",
            "[",
            "]"
        ) { r ->
            "{\"id\":${r.item.id},\"found\":${r.discovered},\"number\":${r.number ?: "null"},\"label0\":${q(r.label0)},\"label1\":${
                q(
                    r.label1
                )
            }}"
        }
        return "{\"step\":\"$step\",\"title\":${q(l.title)},\"rows\":$rows,\"removed\":$removed,\"route\":${route ?: "null"}}"
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val out = cases(Files.readString(Path.of(args[0]))).joinToString(",", "{", "}") { c ->
            val l = TreasureLayer(c.treasures, c.discovered)
            var removed = false
            var route: Int? = null
            val trace = mutableListOf(state(l, "create", removed, route))
            c.events.forEach { e ->
                when {
                    e == "cancel" || e == "button7" -> removed = true
                    e.startsWith("tap:") -> route = l.select(e.substringAfter(':').toInt())?.id
                    e.startsWith("touchStart:") -> Unit
                }; trace += state(l, e, removed, route)
            }
            "\"${c.name}\":[${trace.joinToString(",")}]"
        }
        Files.createDirectories(Path.of(args[1]).parent); Files.writeString(Path.of(args[1]), out); println(out)
    }
}
