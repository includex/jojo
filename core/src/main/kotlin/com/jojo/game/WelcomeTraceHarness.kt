package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** Direct lifecycle implementation of recovered ui/Welcome.js. */
private class WelcomeFlow {
    val routes = mutableListOf<List<Any>>()
    fun onCreate() = replaceScene("LOGIN", 1)
    fun onEvent(event: Int) { if (event == 3 || event == 5) replaceScene("LOGIN", 1) }
    private fun replaceScene(name: String, flag: Int) { routes += listOf(name, flag) }
}

object WelcomeTraceHarness {
    private fun routes(v: List<List<Any>>) = v.joinToString(",", "[", "]") { "[\"${it[0]}\",${it[1]}]" }
    @JvmStatic fun main(args: Array<String>) {
        val text=Files.readString(Path.of(args[0]))
        val cases=Regex("\\{\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"events\\\":\\[([^]]*)]}").findAll(text.replace(Regex("\\s+"),""))
        val json=cases.joinToString(",","{","}") { m ->
            val p=WelcomeFlow(); p.onCreate(); val trace=mutableListOf("{\"step\":\"create\",\"routes\":${routes(p.routes)}}")
            Regex("\\d+").findAll(m.groupValues[2]).forEach { e -> p.onEvent(e.value.toInt());trace += "{\"step\":\"event:${e.value}\",\"routes\":${routes(p.routes)}}" }
            "\"${m.groupValues[1]}\":[${trace.joinToString(",")}]"
        }
        Files.createDirectories(Path.of(args[1]).parent); Files.writeString(Path.of(args[1]),json)
    }
}
