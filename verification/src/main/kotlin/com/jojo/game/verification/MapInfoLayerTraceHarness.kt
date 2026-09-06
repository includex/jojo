// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.MapInfoLayer

import java.nio.file.Files
import java.nio.file.Path


/** MapInfoLayerTraceHarness: 검증 실행을 시작하고 추적 결과를 수집하는 타입이다. */
object MapInfoLayerTraceHarness {
    /** Case: case 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(
        /** name: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val name: String,
        /** setting: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val setting: Int,
        /** text: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val text: String,
        /** changePage: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val changePage: Boolean,
        /** weapon: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val weapon: Boolean,
        /** wait: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val wait: Boolean,
        /** events: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val events: List<String>
    )

    /** escape: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun escape(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val source = Files.readString(Path.of(args[0])).replace(Regex("\\s+"), "")
        val cases =
            Regex("""\{"name":"([^"]+)","setting":(\d+),"data":\{"txt":"((?:\\.|[^"])*)","changePage":(true|false),"wepon":(true|false),"wait":(true|false)},"events":\[(.*?)]}""").findAll(
                source
            ).map { m ->
                Case(
                    m.groupValues[1],
                    m.groupValues[2].toInt(),
                    m.groupValues[3].replace("\\n", "\n"),
                    m.groupValues[4].toBoolean(),
                    m.groupValues[5].toBoolean(),
                    m.groupValues[6].toBoolean(),
                    Regex("\"([^\"]+)\"").findAll(m.groupValues[7]).map { it.groupValues[1] }.toList()
                )
            }.toList()


        /** trace: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun trace(c: Case): String {
            var complete = 0
            var removed = 0
            val layer = MapInfoLayer(c.setting, { it }, { complete++ }, { removed++ })
            layer.onCreate(MapInfoLayer.Data(c.text, c.changePage, c.weapon, c.wait))

            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
            fun snap(step: String): String {
                val v = layer.view()
                val events =
                    (List(complete) { "\"complete\"" } + List(removed) { "\"removeFromParent\"" }).joinToString(",")
                val delay = v.autoCloseDelay?.toString() ?: "null"
                return "{\"step\":\"${escape(step)}\",\"text\":\"${escape(v.text)}\",\"typing\":${v.typing},\"autoCloseDelay\":$delay,\"attached\":${v.attached},\"events\":[$events]}"
            }

            /**
             * `out` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val out = mutableListOf(snap("create"))
            c.events.forEach { event ->
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = event.split(':')
                when (p[0]) {
                    "tick" -> layer.tick(); "cancel" -> layer.cancel(p[1].toInt()); "skip" -> layer.skip(); "elapse" -> layer.elapse(
                    p[1].toInt()
                )
                }
                out += snap(event)
            }
            return out.joinToString(",", "[", "]")
        }

        /**
         * `output` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val output = cases.joinToString(",", "{", "}") { "\"${escape(it.name)}\":${trace(it)}" }
        Files.createDirectories(Path.of(args[1]).parent); Files.writeString(Path.of(args[1]), output); println(output)
    }
}
