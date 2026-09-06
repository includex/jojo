// Verification
package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** HelperLayerTraceHarness: 복원된 HelperLayer 격리 원본·게임 추적 픽스처의 Kotlin 실행부이다. */
object HelperLayerTraceHarness {
    /** Case: case 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(
        /** name: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val name: String,
        /** info: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val info: List<HelperLayer.Info>,
        /** replacement: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val replacement: List<Pair<String, String>>,
        /** events: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val events: List<String>
    )

    /** json: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun json(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    /** unjson: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun unjson(s: String) = s.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\")
    /** balanced: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun balanced(text: String, from: Int): String {
        val open = text[from]
        val close = if (open == '[') ']' else '}'
        /**
         * `level` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var level = 0
        /**
         * `quoted` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var quoted = false
        /**
         * `escaped` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var escaped = false; for (i in from until text.length) {
            /**
             * `c` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val c = text[i]; if (quoted) {
                if (escaped) escaped = false else if (c == '\\') escaped = true else if (c == '\"') quoted = false
            } else if (c == '\"') quoted =
                true else if (c == open) level++ else if (c == close && --level == 0) return text.substring(from, i + 1)
        }; error("unclosed JSON block")
    }

    /** fieldBlock: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun fieldBlock(obj: String, key: String): String {
        val at = obj.indexOf("\"$key\""); require(at >= 0)
        val start = obj.indexOfAny(charArrayOf('[', '{'), at); return balanced(obj, start)
    }

    /** splitObjects: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun splitObjects(array: String): List<String> {
        val out = mutableListOf<String>()
        var i = 0; while (i < array.length) {
            if (array[i] == '{') {
                val x = balanced(array, i); out += x; i += x.length
            } else i++
        }; return out
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val raw = Files.readString(Path.of(args[0]))
        val cases = splitObjects(fieldBlock(raw, "cases")).map { obj ->
            val name = Regex("\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").find(obj)!!.groupValues[1]
            val info =
                Regex("""\[\s*(\d+)\s*,\s*"([^"]*)"\s*,\s*"((?:\\.|[^"])*)"\s*]""").findAll(fieldBlock(obj, "info"))
                    .map { m ->
                        HelperLayer.Info(
                            m.groupValues[1].toInt(),
                            unjson(m.groupValues[2]),
                            unjson(m.groupValues[3])
                        )
                    }.toList()
            val replacement =
                Regex(""""((?:\\.|[^"])*)"\s*:\s*"((?:\\.|[^"])*)""").findAll(fieldBlock(obj, "replacement"))
                    .map { m -> unjson(m.groupValues[1]) to unjson(m.groupValues[2]) }.toList()
            val events =
                Regex("\\\"([^\\\"]+)\\\"").findAll(fieldBlock(obj, "events")).map { it.groupValues[1] }.toList()
            Case(name, info, replacement, events)
        }

        /** run: 하나의 Helper 픽스처 사례를 실행한다. */
        fun run(spec: Case): String {
            val calls = mutableListOf<Pair<String, Int>>()
            var removeCount = 0
            val layer = HelperLayer(object : HelperLayer.Model {
                /** getInfo: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun getInfo() = spec.info
                /** replaceSpeInfo: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun replaceSpeInfo(text: String, flags: Int): String {
                    calls += text to flags; return spec.replacement.fold(text) { acc, (a, b) -> acc.replace(a, b) }
                }
            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
            }) { removeCount++ }; layer.onCreate(); fun snap(step: String): String {
                /**
                 * `v` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val v = layer.view()
                /**
                 * `callsJson` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val callsJson = calls.joinToString(",", "[", "]") { "[\"${json(it.first)}\",${it.second}]" }
                /**
                 * `routes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val routes = (0 until removeCount).joinToString(
                    ",",
                    "[",
                    "]"
                ) { "\"removeFromParent\"" }; return "{\"step\":\"${json(step)}\",\"backgrounds\":[\"${v.prefab.background}\"],\"richText\":\"${
                    json(
                        v.richText
                    )
                }\",\"replaceCalls\":$callsJson,\"attached\":${v.attached},\"button\":{\"path\":\"${v.prefab.buttonPath}\",\"priority\":${v.prefab.listenerPriority}},\"tabs\":[],\"routes\":$routes}"
            }

            /**
             * `out` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val out = mutableListOf(snap("create")); spec.events.forEach { event ->
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = event.split(':'); if (p[0] == "button") layer.onButtonTouch(p[1].toInt()); out += snap(event)
            }; return out.joinToString(",", "[", "]")
        }

        /**
         * `output` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val output = cases.joinToString(",", "{", "}") { "\"${json(it.name)}\":${run(it)}" }; Files.createDirectories(
            Path.of(args[1]).parent
        ); Files.writeString(Path.of(args[1]), output); println(output)
    }
}
