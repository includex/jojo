// Verification
package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** SettingLayerTraceHarness: SettingLayer 복원 JS 격리 추적 픽스처의 Kotlin 실행부이다. */
object SettingLayerTraceHarness {
    /** Case: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(val name: String, val values: LinkedHashMap<String, Int>, val events: List<String>)

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val raw = Files.readString(Path.of(args[0]))
        val cases =
            Regex("""(?s)\{\s*"name"\s*:\s*"([^"]+)"\s*,\s*"initial"\s*:\s*\{([^}]*)}\s*,\s*"events"\s*:\s*\[([^]]*)]""")
                .findAll(raw).map { m ->
                    /**
                     * `values` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val values = LinkedHashMap<String, Int>()
                    Regex(""""([^"]+)"\s*:\s*(-?\d+)""").findAll(m.groupValues[2])
                        .forEach { v -> values[v.groupValues[1]] = v.groupValues[2].toInt() }
                    Case(
                        m.groupValues[1],
                        values,
                        Regex("\\\"([^\\\"]+)\\\"").findAll(m.groupValues[3]).map { it.groupValues[1] }.toList()
                    )
                }.toList()


        /** json: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun json(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")


        /** run: 검증 시나리오 입력을 적용하고 추적 결과를 반환한다. */
        fun run(spec: Case): String {
            val writes = mutableListOf<Pair<String, Int>>()
            val events = mutableListOf<String>()
            val store = object : SettingLayer.Store {
                /** getInt: 지정한 키 또는 인덱스의 검증 값을 조회한다. */
                override fun getInt(key: String, default: Int) = spec.values[key] ?: default
                /** putInt: 지정한 키 또는 상태에 검증 값을 반영한다. */
                override fun putInt(key: String, value: Int) {
                    spec.values[key] = value; writes += key to value
                }
            }
            /**
             * `sound` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val sound = object : SettingLayer.Sound {
                /** music: 검증 입력을 처리하고 관련 상태를 갱신한다. */
                override fun music(on: Boolean) {
                    events += "music:$on"
                }

                /** effect: 검증 입력을 처리하고 관련 상태를 갱신한다. */
                override fun effect(on: Boolean) {
                    events += "effect:$on"
                }
            }
            /**
             * `layer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val layer = SettingLayer(store, sound) { events += "applySpeed" }
            layer.onCreate()

            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
            fun snap(step: String): String {
                val v = layer.view()
                val values = spec.values.entries.joinToString(",") { "\"${json(it.key)}\":${it.value}" }
                val writeText = writes.joinToString(",", "[", "]") { "[\"${json(it.first)}\",${it.second}]" }
                val eventText = events.joinToString(",", "[", "]") { "\"${json(it)}\"" }
                return "{\"step\":\"${json(step)}\",\"attached\":${v.attached},\"flags\":${v.flags},\"speed\":${v.speed},\"values\":{$values},\"writes\":$writeText,\"events\":$eventText}"
            }

            /**
             * `trace` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val trace = mutableListOf(snap("create"))
            spec.events.forEach { event ->
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = event.split(':')
                when (p[0]) {
                    "flag" -> layer.check(p[1].toInt(), p[2].toBoolean())
                    "radio" -> layer.check2(p[1].toInt(), p[2].toInt())
                    "background" -> layer.selectBackground(p[1].toInt())
                    "slider" -> layer.onSlider(p[1].toFloat())
                    "close" -> layer.dismiss(p[1].toInt())
                    "destroy" -> layer.onDestroy()
                }
                trace += snap(event)
            }
            return trace.joinToString(",", "[", "]")
        }

        /**
         * `output` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val output = cases.joinToString(",", "{", "}") { "\"${json(it.name)}\":${run(it)}" }
        Files.createDirectories(Path.of(args[1]).parent)
        Files.writeString(Path.of(args[1]), output)
        println(output)
    }
}
