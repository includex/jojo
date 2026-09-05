package com.jojo.port.desktop

import com.jojo.port.WinConditionsLayer
import java.nio.file.Files
import java.nio.file.Path

/** Kotlin half of the source/port isolated layer trace contract. */
object LayerTraceDump {
    @JvmStatic fun main(args: Array<String>) {
        require(args.size == 2) { "usage: LayerTraceDump fixture.json output.json" }
        val cases = Files.readAllLines(Path.of(args[0])).mapNotNull { line ->
            Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":\\\"((?:\\\\.|[^\\\"])*)\\\",\\\"round\\\":(\\d+),\\\"events\\\":\\[([^]]*)]}").find(line)?.let { m ->
                Case(m.groupValues[1], unescape(m.groupValues[2]), m.groupValues[3].toInt(), m.groupValues[4].split(',').filter(String::isNotBlank).map(String::trim).map(String::toInt))
            }
        }
        require(cases.isNotEmpty()) { "no trace cases parsed" }
        val trace = cases.joinToString(prefix = "[\n", postfix = "\n]\n", separator = ",\n") { item ->
            val layer = WinConditionsLayer(); var callbacks = 0
            layer.onCreate(item.text, item.round) { callbacks++ }
            fun step(event: Int): String { val v = layer.view(); return "{\"event\":$event,\"first\":${quote(v.first)},\"second\":${quote(v.second)},\"attached\":${v.attached},\"callbacks\":$callbacks}" }
            val steps = mutableListOf(step(0))
            item.events.forEach { event -> layer.cancel(event); steps += step(event) }
            "  {\"id\":${quote(item.id)},\"steps\":[${steps.joinToString(",")}]}"
        }
        Files.createDirectories(Path.of(args[1]).parent)
        Files.writeString(Path.of(args[1]), trace)
    }
    private data class Case(val id: String, val text: String, val round: Int, val events: List<Int>)
    private fun unescape(value: String) = value.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\")
    private fun quote(value: String) = "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\""
}
