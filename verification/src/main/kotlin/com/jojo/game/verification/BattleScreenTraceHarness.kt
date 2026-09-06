// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.battle.verification.BattleScreenIsolatedContract
import com.jojo.game.presentation.battle.verification.BattleScreenIsolatedUnit

/** BattleScreenTraceHarness: battle_layer_source_trace_harness.js와 짝을 이루는 Kotlin 실행부이다. */
object BattleScreenTraceHarness {
    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(x: String) = "\"$x\""
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(args: Array<String>) {
        // 픽스처 텍스트는 원본 계약의 일부이므로 공백을 유지한다.
        val flat = java.nio.file.Files.readString(java.nio.file.Path.of(args[0]))
        val cases =
            Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":\\\"([^\\\"]+)\\\",\\\"round\\\":(\\d+),(?:\\\"collocation\\\":(true|false),)?\\\"units\\\":\\[(.*?)],\\\"events\\\":\\[(.*?)]}").findAll(
                flat
            )
        val output = cases.joinToString(prefix = "[", postfix = "]") { m ->
            val units = Regex("\\{\\\"control\\\":(true|false),\\\"exist\\\":(true|false),\\\"acted\\\":(true|false)}")
                .findAll(m.groupValues[5]).map { u ->
                    BattleScreenIsolatedUnit(
                        u.groupValues[1] == "true",
                        u.groupValues[2] == "true",
                        u.groupValues[3] == "true"
                    )
                }.toList()
            val layer = BattleScreenIsolatedContract(units, m.groupValues[4] == "true", m.groupValues[3].toInt())
            val trace = mutableListOf<String>()

            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
            fun snap(step: String) {
                val v =
                    layer.view(); trace += "{\"step\":${q(step)},\"paused\":${v.paused},\"modal\":${v.modal},\"action\":${v.action},\"events\":[${
                    v.events.joinToString {
                        q(
                            it
                        )
                    }
                }]}"
            }
            snap("create")
            Regex("\\\"([^\\\"]+)\\\"").findAll(m.groupValues[6]).forEach { item ->
                val (kind, value) = item.groupValues[1].split(':')
                when (kind) {
                    "show" -> layer.showWinCondition(m.groupValues[2]); "cancel" -> layer.cancel(value.toInt()); "action" -> layer.nextNotOperUnit(
                    value.toInt()
                )
                }
                snap("$kind-$value")
            }
            "{\"case\":${q(m.groupValues[1])},\"trace\":[${trace.joinToString()}]}"
        }
        java.nio.file.Files.writeString(java.nio.file.Path.of(args[1]), output); println(output)
    }
}
