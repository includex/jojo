// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.battle.overlay.ForcesListLayer

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.UnitInfoLayer

import java.nio.file.Files
import java.nio.file.Path

/** ForcesListLayerTraceHarness: 복원된 ForcesListLayer 팩토리 추적의 Kotlin 실행부이다. */
object ForcesListLayerTraceHarness {
    /** Case: case 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(
        /** name: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val name: String,
        /** flag: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val flag: Int,
        /** mine: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val mine: List<ForcesListLayer.Unit>,
        /** enemy: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val enemy: List<ForcesListLayer.Unit>,
        /** events: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val events: List<String>
    )

    /** cases: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun cases(input: String): List<Case> {
        val compact = input.replace(Regex("\\s+"), "")
        return Regex("""\{"name":"([^"]+)","flag":(\d+),"mine":\[(.*?)]\,"enemy":\[(.*?)]\,"events":\[(.*?)]}""").findAll(
            compact
        ).map { c ->
            /** units: 원본 문자열에서 편성 유닛 목록을 읽는다. */
            fun units(raw: String): List<ForcesListLayer.Unit> = Regex("""\{([^{}]*)}""").findAll(raw).map { m ->
                val p = m.groupValues[1]

                /** n: 이름 필드를 결과에 기록한다. */
                fun n(k: String) = Regex("\"$k\":(\\d+)").find(p)?.groupValues?.get(1)?.toInt() ?: 0

                /** b: 버튼 필드를 결과에 기록한다. */
                fun b(k: String) = p.contains("\"$k\":true")
                val status = Regex("\"status\":(\\d+)").find(p)?.groupValues?.get(1)?.toInt()
                ForcesListLayer.Unit(
                    n("id"),
                    n("id").toString(),
                    "",
                    1,
                    10,
                    10,
                    5,
                    5,
                    0,
                    0,
                    0,
                    0,
                    0,
                    b("famous"),
                    status?.let { (0..4).associateWith { _ -> it } } ?: emptyMap(),
                    n("index"),
                    b("poison"),
                    b("fengZhou"))
            }.toList()
            Case(
                c.groupValues[1],
                c.groupValues[2].toInt(),
                units(c.groupValues[3]),
                units(c.groupValues[4]),
                Regex("\"([^\"]+)\"").findAll(c.groupValues[5]).map { it.groupValues[1] }.toList()
            )
        }.toList()
    }

    /** e: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun e(x: String) = x.replace("\\", "\\\\").replace("\"", "\\\"")
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(args: Array<String>) {
        val all = cases(Files.readString(Path.of(args[0]))).joinToString(",", "{", "}") { c ->
            val layer = ForcesListLayer()
            var destroyed = false
            var n1 = 1
            var n2 = 1
            var rendered = 0
            val routes = mutableListOf<String>()
            val trace = mutableListOf<String>()


            /** state: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            fun state(step: String): String {
                val view = layer.view()
                val rows = view.rows.mapIndexed { index, row ->
                    val colors = row.colors.mapIndexed { i, _ ->
                        when {
                            c.flag and 1 == 0 -> "black"; i == 4 && row.unit.poisoned -> "red"
                            i > 4 && row.unit.status[i - 5] == 0 -> "red"; i > 4 && row.unit.status[i - 5] == 1 -> "blue"; else -> "black"
                        }
                    }.joinToString(",", "[", "]") { "\"$it\"" }
                    "{\"tag\":$index,\"labels\":[${row.labels.joinToString(",") { "\"${e(it)}\"" }}],\"colors\":$colors}"
                }.joinToString(",", "[", "]")
                return "{\"step\":\"${e(step)}\",\"sel\":${view.selectedTab},\"rows\":$rows,\"tabsVisible\":${view.tabsVisible},\"dead\":${!view.attached},\"baseDestroyed\":$destroyed,\"routes\":[${
                    routes.joinToString(
                        ","
                    )
                }],\"n1\":${if (destroyed) 0 else n1},\"n2\":${if (destroyed) 0 else n2}}"
            }
            layer.onCreate(c.mine, c.enemy, c.flag); rendered = layer.view().rows.size; trace += state("create")
            c.events.forEach { event ->
                val p = event.split(':')
                when (p[0]) {
                    "tab" -> if (c.flag and 1 != 0 && p[2].toInt() == 2 && p[1].toInt() != layer.view().selectedTab) {
                        repeat(rendered) { if (it % 2 == 0) n1++ else n2++ }
                        layer.changeSel(p[1].toInt()); rendered = layer.view().rows.size
                        repeat(rendered) {
                            if (it % 2 == 0) {
                                n1--; if (n1 < 1) n1++
                            } else {
                                n2--; if (n2 < 1) n2++
                            }
                        }
                    }

                    "row" -> layer.onRowTouch(p[1].toInt(), p[2].toInt())?.let {
                        routes += "{\"layer\":\"UnitInfoLayer\",\"index\":${p[1]},\"unitIds\":[${
                            layer.view().rows.map { r -> r.unit.battleIndex }.joinToString(",")
                        }],\"flag\":${c.flag}}"
                    }

                    "close" -> layer.onClose(p[1].toInt())
                    "destroy" -> destroyed = true
                }
                trace += state(event)
            }
            "\"${e(c.name)}\":[${trace.joinToString(",")}]"
        }
        Files.createDirectories(Path.of(args[1]).parent); Files.writeString(Path.of(args[1]), all); println(all)
    }
}
