package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** TuoGuanLayer와 MsgBox4의 자동 전투·지속 상태 계약을 검증한다. */
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

            /** 검증 모델을 초기 상태로 생성한다. */
            fun create() {
                removed = 0; if (kind == "msg4") checked = persisted == 1
            }

            /** 현재 자동 전투 상태를 JSON 조각으로 기록한다. */
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
