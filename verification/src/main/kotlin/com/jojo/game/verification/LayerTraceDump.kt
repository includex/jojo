// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.battle.overlay.WinConditionsLayer
import java.nio.file.Files
import java.nio.file.Path

/** LayerTraceDump: 원본과 게임의 격리 레이어 추적 계약을 실행한다. */
object LayerTraceDump {
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
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
            /** step: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            fun step(event: Int): String { val view = layer.view(); return "{\"event\":$event,\"first\":${quote(view.first)},\"second\":${quote(view.second)},\"attached\":${view.attached},\"callbacks\":$callbacks}" }
            val steps = mutableListOf(step(0)); item.events.forEach { event -> layer.cancel(event); steps += step(event) }
            "  {\"id\":${quote(item.id)},\"steps\":[${steps.joinToString(",")}] }"
        }
        Files.createDirectories(Path.of(args[1]).parent)
        Files.writeString(Path.of(args[1]), trace)
    }
    /** Case: case 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(val id: String, val text: String, val round: Int, val events: List<Int>)
    /** unescape: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun unescape(value: String) = value.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\")
    /** quote: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun quote(value: String) = "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\""
}
