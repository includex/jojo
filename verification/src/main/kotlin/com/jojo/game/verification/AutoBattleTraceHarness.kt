// Verification
package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** AutoBattleTraceHarness: TuoGuanLayer와 MsgBox4의 자동 전투·지속 상태 계약을 검증한다. */
object AutoBattleTraceHarness {
    /** events: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun events(raw: String) = Regex("\\\"events\\\":\\[(.*?)]").findAll(raw)
        .map { m -> Regex("\\\"([^\\\"]*)\\\"").findAll(m.groupValues[1]).map { it.groupValues[1] }.toList() }.toList()

    /** esc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val raw = Files.readString(Path.of(args[0]))
        val descriptors =
            Regex("\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"kind\\\":\\\"([^\\\"]+)\\\"").findAll(raw).toList()
        val allEvents = events(raw)
        val result = descriptors.mapIndexed { idx, m ->
            val name = m.groupValues[1]
            val kind = m.groupValues[2]
            val portion = raw.substring(m.range.first)
            val stored = if (kind == "msg4") Regex("\\\"stored\\\":(\\d+)").find(portion)?.groupValues?.get(1)?.toInt()
                ?: 0 else 0
            Regex("\\\"flag\\\":(\\d+)").find(portion)?.groupValues?.get(1)?.toInt() ?: 1
            var persisted = stored
            var checked = false
            var removed = 0
            val dispatches = mutableListOf<String>()
            val calls = mutableListOf<Int>()

            /** create: 검증에 필요한 초기 상태를 생성한다. */
            fun create() {
                removed = 0; if (kind == "msg4") checked = persisted == 1
            }

            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
            fun snap(step: String) =
                "{\"step\":\"${esc(step)}\",\"attached\":${removed == 0},\"dispatches\":[${dispatches.joinToString(",") { "\"$it\"" }}],\"calls\":[${
                    calls.joinToString(",")
                }],\"checked\":${if (kind == "msg4") checked.toString() else "null"},\"stored\":$persisted,\"cancelPriority\":${if (kind == "tuoguan") 2 else "null"}}"
            create()
            /**
             * `trace` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val trace = mutableListOf(snap("create")); allEvents[idx].forEach { e ->
            /**
             * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val p = e.split(':'); if (kind == "tuoguan" && p[0] == "cancel" && p[1] == "2") {
            dispatches += "CANCEL_TUOGUAN"; removed++
        } else if (kind == "msg4") when (p[0]) {
            "toggle" -> checked = p[1] == "1"; "button" -> if (p[2] == "2") {
                persisted = if (checked) 1 else 0; calls += (p[1].toInt() or if (checked) 2 else 0); removed++
            }; "recreate" -> create()
        }; trace += snap(e)
        }; "\"${esc(name)}\":${trace.joinToString(",", "[", "]")}"
        }
            .joinToString(",", "{", "}")
        val out = Path.of(args[1]); Files.createDirectories(out.parent); Files.writeString(out, result)
    }
}
