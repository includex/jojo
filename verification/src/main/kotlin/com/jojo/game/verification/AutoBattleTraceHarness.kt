package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** Direct games of TuoGuanLayer.js and MsgBox4's TUOGUAN persistence contract. */
object AutoBattleTraceHarness {
    private fun events(raw: String) = Regex("\\\"events\\\":\\[(.*?)]").findAll(raw)
        .map { m -> Regex("\\\"([^\\\"]*)\\\"").findAll(m.groupValues[1]).map { it.groupValues[1] }.toList() }.toList()

    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
    @JvmStatic
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

            /**
             * 공개 메서드 `create`
             *
             * ### 파라미터
            - 입력 파라미터: 없음
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun create() {
                removed = 0; if (kind == "msg4") checked = persisted == 1
            }

            /**
             * 공개 메서드 `snap`
             *
             * ### 파라미터
            - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun snap(step: String) =
                "{\"step\":\"${esc(step)}\",\"attached\":${removed == 0},\"dispatches\":[${dispatches.joinToString(",") { "\"$it\"" }}],\"calls\":[${
                    calls.joinToString(",")
                }],\"checked\":${if (kind == "msg4") checked.toString() else "null"},\"stored\":$persisted,\"cancelPriority\":${if (kind == "tuoguan") 2 else "null"}}"
            create()
            val trace = mutableListOf(snap("create")); allEvents[idx].forEach { e ->
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
