package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** Direct event-contract games for Feats, JiQi, Attribute and Exclusive UI. */
object CharacterAbilityTraceHarness {
    private fun q(s: String) = "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    private fun events(s: String) = Regex("\\\"events\\\":\\[(.*?)]").find(s)?.groupValues?.get(1)
        ?.let { Regex("\\\"([^\\\"]*)\\\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    private fun labels(vararg xs: Pair<String, String>) =
        xs.sortedBy { it.first }.joinToString(",", "[", "]") { "[${q(it.first)},${q(it.second)}]" }

    private fun snap(step: String, dead: Boolean, ls: String, layers: List<String>, exclusive: String? = null) =
        "{\"step\":${q(step)},\"dead\":$dead,\"labels\":$ls,\"layers\":[${layers.joinToString(",") { q(it) }}]${exclusive ?: ""}}"

    private fun run(name: String, es: List<String>): String {
        var dead = false
        val layers = mutableListOf<String>()
        val out = mutableListOf<String>()

        /**
         * 공개 메서드 `add`
         *
         * ### 파라미터
        - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `ls` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `ex` (`String?=null`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun add(step: String, ls: String, ex: String? = null) {
            out += snap(step, dead, ls, layers, ex)
        }
        when (name) {
            "feats_rows_help_and_cancel" -> {
                val ls = labels("Feats/label" to "7/14", "label0" to "운기", "label1" to "15", "label2" to "5")
                add("create", ls); es.forEach { e ->
                    when (e) {
                        "button:1:2" -> layers += "MsgBox"; "button:0:2", "cancel:2" -> dead = true
                    }; add(e, ls)
                }
            }

            "jiqi_rates_cancel" -> {
                val ls = labels("bg/label0" to "100", "bg/label1" to "85", "bg/label2" to "70", "bg/label3" to "55")
                add("create", ls); es.forEach { e -> if (e == "cancel:2") dead = true; add(e, ls) }
            }

            "attribute_pool_rows" -> {
                add("create", labels("label0" to "민첩", "label1" to "-3"))
            }

            "exclusive_tabs_lazy_close" -> {
                var sel = 0
                var flag = 1
                var panels = listOf(true, false)
                var buttons = listOf(false, true, true)
                var rows = listOf(1, 0)
                var ls = labels("label0" to "Item1", "label1" to "Item2", "label2" to "----", "label3" to "T1")

                /**
                 * 공개 메서드 `ex`
                 *
                 * ### 파라미터
                - 입력 파라미터: 없음
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Unit`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun ex() = ",\"sel\":$sel,\"flag\":$flag,\"panels\":[${panels.joinToString(",")}],\"buttons\":[${
                    buttons.joinToString(",")
                }],\"rows\":[${rows.joinToString(",")}]"
                add("create", ls, ex()); es.forEach { e ->
                    when (e) {
                        "tab:1:2" -> {
                            sel = 1; flag = 3; panels = listOf(false, true); buttons = listOf(true, false, true); rows =
                                listOf(1, 1); ls =
                                labels("label0" to "U7", "label1" to "Item9", "label2" to "Z1", "label3" to "T1")
                        }; "tab:0:2" -> {
                        sel = 0; panels = listOf(true, false); buttons = listOf(false, true, true)
                    }; "tab:2:2", "cancel:2" -> dead = true
                    }; add(e, ls, ex())
                }
            }
        }
        return out.joinToString(",", "[", "]")
    }

    @JvmStatic
    fun main(a: Array<String>) {
        val raw = Files.readString(Path.of(a[0]))
        val cases = Regex("\\\"name\\\":\\\"([^\\\"]+)\\\"").findAll(raw)
            .map { m -> m.groupValues[1] to events(raw.substring(m.range.first)) }.toList()
        val json = cases.joinToString(",", "{", "}") { (n, e) -> q(n) + ":" + run(n, e) }
        val out = Path.of(a[1]); Files.createDirectories(out.parent); Files.writeString(out, json)
    }
}
