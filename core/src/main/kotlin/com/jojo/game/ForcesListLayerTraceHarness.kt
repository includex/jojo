package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** Kotlin half of the recovered-factory ForcesListLayer trace. */
object ForcesListLayerTraceHarness {
    private data class Case(
        val name: String,
        val flag: Int,
        val mine: List<ForcesListLayer.Unit>,
        val enemy: List<ForcesListLayer.Unit>,
        val events: List<String>
    )

    private fun cases(input: String): List<Case> {
        val compact = input.replace(Regex("\\s+"), "")
        return Regex("""\{"name":"([^"]+)","flag":(\d+),"mine":\[(.*?)]\,"enemy":\[(.*?)]\,"events":\[(.*?)]}""").findAll(
            compact
        ).map { c ->
            /**
             * 공개 메서드 `units`
             *
             * ### 파라미터
            - `raw` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `List<ForcesListLayer.Unit>`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun units(raw: String): List<ForcesListLayer.Unit> = Regex("""\{([^{}]*)}""").findAll(raw).map { m ->
                val p = m.groupValues[1]

                /**
                 * 공개 메서드 `n`
                 *
                 * ### 파라미터
                - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Unit`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun n(k: String) = Regex("\"$k\":(\\d+)").find(p)?.groupValues?.get(1)?.toInt() ?: 0

                /**
                 * 공개 메서드 `b`
                 *
                 * ### 파라미터
                - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Unit`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

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

    private fun e(x: String) = x.replace("\\", "\\\\").replace("\"", "\\\"")
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

            /**
             * 공개 메서드 `state`
             *
             * ### 파라미터
            - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `String`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

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
