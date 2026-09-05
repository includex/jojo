package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

private class ProgressLoadingState {
    var bg = ""
    var label = ""
    var progress = 0.0
    fun onCreate(arg: String?) { bg = "bg4"; if (arg != null) label = arg }
    fun set(value: Double) { progress = value }
}

object Progress2TraceHarness {
    private fun state(step: String, p: ProgressLoadingState) = "{\"step\":\"$step\",\"bg\":\"${p.bg}\",\"label\":\"${p.label}\",\"spin\":{\"t\":2,\"a\":-360},\"progress\":${p.progress}}"
    @JvmStatic fun main(args: Array<String>) {
        val input = Files.readString(Path.of(args[0])).trim()
        val cases = Regex("\\{\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"arg\\\":(null|\\\"[^\\\"]*\\\"),\\\"events\\\":\\[([^]]*)]}").findAll(input)
        val output = cases.joinToString(",", "{", "}") { m ->
            val p = ProgressLoadingState()
            val arg = m.groupValues[2].let { if (it == "null") null else it.removeSurrounding("\"") }
            p.onCreate(arg)
            val trace = mutableListOf(state("create", p))
            Regex("-?\\d+(?:\\.\\d+)?").findAll(m.groupValues[3]).forEach { e -> p.set(e.value.toDouble()); trace += state("set:${e.value}", p) }
            "\"${m.groupValues[1]}\":[${trace.joinToString(",")}]"
        }
        Files.createDirectories(Path.of(args[1]).parent)
        Files.writeString(Path.of(args[1]), output)
    }
}
