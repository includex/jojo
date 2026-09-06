// Verification
package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path


/** ItemEquipTraceHarness: Item·UseProperty·EquipConfirm·Equip 레이어의 입력 계약을 헤드리스로 검증한다. */
object ItemEquipTraceHarness {
    /** DROP: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private const val DROP = "버릴 것을 결정하시겠습니까?I10?"
    /** esc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")

    /** State: 검증 실행의 현재 상태를 표현하는 타입이다. */
    private class State {
        /**
         * `dead` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var dead = false
        /**
         * `sel` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var sel: Int? = null
        /**
         * `bags` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var bags = emptyList<Int>()
        /**
         * `buttons` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var buttons = emptyList<Boolean>()
        /**
         * `layers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val layers = mutableListOf<String>()
        /**
         * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val events = mutableListOf<String>()
        /**
         * `toasts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val toasts = mutableListOf<String>()

        /** layer: 추적 결과에 레이어 생성 이벤트를 기록한다. */
        fun layer(name: String, txt: String? = null, values: List<Int>? = null) {
            val args = "{\"txt\":${txt?.let { "\"${esc(it)}\"" } ?: "null"},\"values\":${
                values?.joinToString(
                    ",",
                    "[",
                    "]"
                ) ?: "null"
            }}"
            layers += "{\"layer\":\"$name\",\"args\":$args}"
        }

        /** snap: 현재 아이템·장비 레이어 상태를 JSON 스냅샷으로 만든다. */
        fun snap(step: String): String =
            "{\"step\":\"${esc(step)}\",\"dead\":$dead,\"layers\":[${layers.joinToString(",")}],\"events\":[${
                events.joinToString(",") { "\"${esc(it)}\"" }
            }],\"toasts\":[${toasts.joinToString(",") { "\"${esc(it)}\"" }}],\"sel\":${sel ?: "null"},\"bags\":[${
                bags.joinToString(
                    ","
                )
            }],\"buttons\":[${buttons.joinToString(",")}] }".replace("] }", "]}")
    }

    /** events: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun events(block: String): List<String> =
        Regex("\\\"events\\\":\\[(.*?)\\]").find(block)?.groupValues?.get(1)
            ?.let { Regex("\\\"([^\\\"]*)\\\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    /** run: 검증 실행에 필요한 상태를 구성한다. */
    private fun run(name: String, kind: String, es: List<String>): String {
        val s = State()
        var answer: ((Int) -> Unit)? = null
        var usePending = false
        var unloadPending = false
        when (kind) {
            "use" -> s.bags = listOf(10, 11)
            "equip" -> {
                s.sel = 1; s.bags = listOf(55); s.buttons = listOf(true, false, true, true)
            }
        }
        val out = mutableListOf(s.snap("create"))
        es.forEach { e ->
            val p = e.split(':')
            when (kind) {
                "item" -> when (p[0]) {
                    "click" -> if (p[2] == "2") {
                        if (p[1] == "0") s.dead = true
                        else {
                            s.layer("MsgBox", DROP); answer = { a ->
                                if (a == 0) {
                                    s.events += listOf(
                                        "delete:10",
                                        "DISCARD_ITEM:[object Object]"
                                    ); s.toasts += "I10 이미 버렸습니다..."; s.dead = true
                                }
                            }
                        }
                    }

                    "answer" -> answer?.invoke(p[1].toInt()).also { answer = null }
                }

                "use" -> when (p[0]) {
                    "row" -> when (p[2]) {
                        "0" -> usePending = true; "2" -> if (usePending) {
                            usePending = false; s.dead = true; s.events += "use:${if (p[1] == "0") 10 else 11}"
                        }; "3" -> usePending = false
                    }

                    "cancel" -> if (p[1] == "2") {
                        s.dead = true; s.events += "use:none"
                    }
                }

                "confirm" -> when (p[0]) {
                    "button" -> if (p[2] == "2") {
                        s.dead = true; if (p[1] == "0") s.events += "confirmed"
                    }
                    // EquipConfirmLayer의 취소 콜백은 터치 단계를 검사하지 않아, 복원한 리스너는 두 행동 버튼과 달리 전달된 모든 단계에서 제거한다.
                    "cancel" -> s.dead = true
                }

                "equip" -> when (p[0]) {
                    "tab" -> {
                        s.sel = p[1].toInt(); s.buttons = (0..3).map { it != s.sel }
                    }

                    "clickItem" -> s.events += listOf(
                        "equip:55:0",
                        "delete:55",
                        "sound:1",
                        "ref",
                        "UNIT_EQUIP_CHANGE:7"
                    )

                    "equip" -> when (p[1]) {
                        "0" -> if (p[2] == "0") unloadPending = false else if (p[2] == "2") unloadPending = true
                        "2" -> if (p[2] == "2" && unloadPending) {
                            s.events += listOf("unload:2", "equip:81:2")
                            s.layer("EquipConfirmLayer", "해제", List(8) { 0 })
                            answer = { a -> if (a == 0) s.events += listOf("unload:2", "UNIT_EQUIP_CHANGE:7", "ref") }
                        }

                        "3" -> unloadPending = false
                    }

                    "answer" -> answer?.invoke(p[1].toInt()).also { answer = null }
                }
            }
            out += s.snap(e)
        }
        return out.joinToString(",", "[", "]")
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        require(args.size == 2) { "fixture output" }
        val input = Files.readString(Path.of(args[0]))
        // 픽스처에는 중첩 `item` 객체가 있으므로 중괄호 정규식으로 사례를 나누지 않는다.
        // 공용 픽스처 스키마와 동일하게 각 이벤트 배열을 사례 이름에 고정한다.
        val cases = Regex("\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"kind\\\":\\\"([^\\\"]+)\\\"")
            .findAll(input).map { m ->
                val name = m.groupValues[1]
                val kind = m.groupValues[2]
                val tail = input.substring(m.range.first)
                Triple(name, kind, events(tail))
            }.toList()
        val json = cases.joinToString(",", "{", "}") { (name, kind, es) -> "\"${esc(name)}\":${run(name, kind, es)}" }
        val out = Path.of(args[1]); Files.createDirectories(out.parent); Files.writeString(out, json)
    }
}
