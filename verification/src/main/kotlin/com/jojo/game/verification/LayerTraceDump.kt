package com.jojo.game.verification

import com.jojo.game.WinConditionsLayer
import java.nio.file.Files
import java.nio.file.Path

/** Kotlin half of the source/game isolated layer trace contract. */
object LayerTraceDump {
    @JvmStatic fun main(args: Array<String>) {
        require(args.size == 2) { "usage: LayerTraceDump fixture.json output.json" }
        val cases = Files.readAllLines(Path.of(args[0])).mapNotNull { line ->
            Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":\\\"((?:\\\\.|[^\\\"])*)\\\",\\\"round\\\":(\\d+),\\\"events\\\":\\[([^]]*)]}").find(line)?.let { match ->
                Case(match.groupValues[1], unescape(match.groupValues[2]), match.groupValues[3].toInt(), match.groupValues[4].split(',').filter(String::isNotBlank).map(String::trim).map(String::toInt))
            }
        }
        require(cases.isNotEmpty()) { "no trace cases parsed" }
        val trace = cases.joinToString(prefix = "[\n", postfix = "\n]\n", separator = ",\n") { item ->
            val layer = WinConditionsLayer(); var callbacks = 0
            layer.onCreate(item.text, item.round) { callbacks++ }
            fun step(event: Int): String { val view = layer.view(); return "{\"event\":$event,\"first\":${quote(view.first)},\"second\":${quote(view.second)},\"attached\":${view.attached},\"callbacks\":$callbacks}" }
            val steps = mutableListOf(step(0)); item.events.forEach { event -> layer.cancel(event); steps += step(event) }
            "  {\"id\":${quote(item.id)},\"steps\":[${steps.joinToString(",")}] }"
        }
        Files.createDirectories(Path.of(args[1]).parent)
        Files.writeString(Path.of(args[1]), trace)
    }
    private data class Case(val id: String, val text: String, val round: Int, val events: List<Int>)
    private fun unescape(value: String) = value.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\")
    private fun quote(value: String) = "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\""
}
