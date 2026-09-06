// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.battle.overlay.RoundLayer

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** RoundLayerTraceHarness: 복원된 RoundLayer의 시간 제한과 수명 주기를 추적하는 실행부이다. */
object RoundLayerTraceHarness {
    /** Spec: spec 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Spec(
        /** name: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val name: String,
        /** roundPresent: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val roundPresent: Boolean,
        /** round: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val round: Int,
        /** maxPresent: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val maxPresent: Boolean,
        /** max: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val max: Int?,
        /** events: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val events: List<String>
    )

    /** block: 입력 데이터에서 지정한 블록을 추출한다. */
    private fun block(text: String, from: Int): String {
        val open = text[from]
        val close = if (open == '{') '}' else ']'
        var n = 0
        var quote = false; for (i in from until text.length) {
            val c = text[i]; if (c == '\"') quote =
                !quote; else if (!quote && c == open) n++; else if (!quote && c == close && --n == 0) return text.substring(
                from,
                i + 1
            )
        }; error("unclosed")
    }

    /** objects: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun objects(array: String): List<String> {
        val out = mutableListOf<String>()
        var p = 0; while (p < array.length) {
            if (array[p] == '{') {
                val x = block(array, p); out += x; p += x.length
            } else p++
        }; return out
    }

    /** field: 입력 데이터에서 지정한 블록을 추출한다. */
    private fun field(o: String, k: String) = o.indexOf("\"$k\"").let { if (it < 0) error(k); else it }
    /** bool: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun bool(o: String, k: String) =
        Regex("\\\"$k\\\"\\s*:\\s*(true|false)").find(o)!!.groupValues[1].toBoolean()

    /** intNull: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun intNull(o: String, k: String): Int? =
        Regex("\\\"$k\\\"\\s*:\\s*(null|-?\\d+)").find(o)?.groupValues?.get(1)?.takeUnless { it == "null" }?.toInt()

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(args: Array<String>) {
        val raw = Files.readString(Path.of(args[0]))
        val casesBlock = block(raw, raw.indexOf('[', field(raw, "cases")))
        val specs = objects(casesBlock).map { o ->
            val name = Regex("\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").find(o)!!.groupValues[1]
            val ab = block(o, o.indexOf('{', field(o, "args")))
            val eb = block(o, o.indexOf('[', field(o, "events"))); Spec(
            name,
            bool(ab, "roundPresent"),
            intNull(ab, "round") ?: 0,
            bool(ab, "maxPresent"),
            intNull(ab, "max"),
            Regex("\\\"([^\\\"]+)\\\"").findAll(eb).map { it.groupValues[1] }.toList()
        )
        /** run: 검증 실행에 필요한 상태를 구성한다. */
        }; fun run(s: Spec): String {
            var removed = 0
            val events = mutableListOf<String>()
            val layer = RoundLayer({ removed++ },
                { events += "complete" }); layer.onCreate(
                RoundLayer.CreateArgs(
                    s.roundPresent,
                    s.round,
                    s.max,
                    s.maxPresent
                )
            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
            ); fun snap(step: String): String {
                val v =
                    layer.view; return "{\"step\":\"$step\",\"roundLabelsVisible\":${v.roundLabelsVisible},\"campLabelsVisible\":${v.campLabelsVisible},\"roundText\":\"${v.roundText}\",\"scheduled\":[2],\"touchBindings\":0,\"removed\":$removed,\"events\":[${
                    events.joinToString(
                        ","
                    ) { "\"$it\"" }
                }]}"
            }

            val trace = mutableListOf(snap("create")); s.events.forEach { event ->
                val p = event.split(':'); if (p[0] == "elapsed") layer.elapsed(p[1].toFloat()); trace += snap(event)
            }; return trace.joinToString(",", "[", "]")
        }

        val output = specs.joinToString(",", "{", "}") { "\"${it.name}\":${run(it)}" }; Files.createDirectories(
            Path.of(
                args[1]
            ).parent
        ); Files.writeString(Path.of(args[1]), output); println(output)
    }
}
