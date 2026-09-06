// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.TreasureLayer

import java.nio.file.Files
import java.nio.file.Path

/** TreasureLayerTraceHarness: 원본 팩토리의 TreasureLayer 계약 픽스처를 실행하는 Kotlin 부분이다. */
object TreasureLayerTraceHarness {
    /** Case: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(
        /** name: 검증 대상의 표시 이름을 담는다. */
        val name: String,
        /** treasures: 검증 대상의 현재 상태 값을 담는다. */
        val treasures: List<TreasureLayer.Item>,
        /** discovered: 검증 대상의 현재 상태 값을 담는다. */
        val discovered: Set<Int>,
        /** events: 검증 이벤트 목록을 담는다. */
        val events: List<String>
    )

    /** q: 문자열을 JSON 인용 형식으로 변환한다. */
    private fun q(s: String?) = s?.let { "\"${it.replace("\\", "\\\\").replace("\"", "\\\"")}\"" } ?: "null"
    /** cases: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun cases(text: String): List<Case> =
        Regex("""\{\"name\":\"([^\"]+)\",\"treasures\":\[(.*?)]?,\"discovered\":\[(.*?)]?,\"events\":\[(.*?)]?}""")
            .findAll(text.replace(Regex("\\s+"), "")).map { m ->
                val items =
                    Regex("""\{\"id\":(\d+),\"name\":\"([^\"]*)\",\"icon\":(\d+),\"property\":(true|false),\"description\":\"([^\"]*)\"}""")
                        .findAll(m.groupValues[2]).map { x ->
                            TreasureLayer.Item(
                                x.groupValues[1].toInt(),
                                x.groupValues[2],
                                x.groupValues[3].toInt(),
                                x.groupValues[4].toBoolean(),
                                x.groupValues[5]
                            )
                        }.toList()
                val found = Regex("\\d+").findAll(m.groupValues[3]).map { it.value.toInt() }.toSet()
                val events = Regex("\"([^\"]+)\"").findAll(m.groupValues[4]).map { it.groupValues[1] }.toList()
                Case(m.groupValues[1], items, found, events)
            }.toList()

    /** state: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun state(l: TreasureLayer, step: String, removed: Boolean, route: Int?): String {
        val rows = (l.rows.filter { it.discovered } + l.rows.filterNot { it.discovered }).joinToString(
            ",",
            "[",
            "]"
        ) { r ->
            "{\"id\":${r.item.id},\"found\":${r.discovered},\"number\":${r.number ?: "null"},\"label0\":${q(r.label0)},\"label1\":${
                q(
                    r.label1
                )
            }}"
        }
        return "{\"step\":\"$step\",\"title\":${q(l.title)},\"rows\":$rows,\"removed\":$removed,\"route\":${route ?: "null"}}"
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(args: Array<String>) {
        val out = cases(Files.readString(Path.of(args[0]))).joinToString(",", "{", "}") { c ->
            val l = TreasureLayer(c.treasures, c.discovered)
            var removed = false
            var route: Int? = null
            val trace = mutableListOf(state(l, "create", removed, route))
            c.events.forEach { e ->
                when {
                    e == "cancel" || e == "button7" -> removed = true
                    e.startsWith("tap:") -> route = l.select(e.substringAfter(':').toInt())?.id
                    e.startsWith("touchStart:") -> Unit
                }; trace += state(l, e, removed, route)
            }
            "\"${c.name}\":[${trace.joinToString(",")}]"
        }
        Files.createDirectories(Path.of(args[1]).parent); Files.writeString(Path.of(args[1]), out); println(out)
    }
}
