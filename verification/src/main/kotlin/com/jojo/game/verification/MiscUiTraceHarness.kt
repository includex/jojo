package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** Direct games of NoticeInfoLayer, HelpLayer, InputBox, SelectListLayer and ListLayer. */
object MiscUiTraceHarness {
    private data class C(val id: String, val kind: String, val raw: String)

    private fun bal(s: String, i0: Int): String {
        val o = s[i0]
        val z = if (o == '{') '}' else ']'
        var d = 0
        var q = false
        var e = false; for (i in i0 until s.length) {
            val c = s[i]; if (q) {
                if (e) e = false else if (c == '\\') e = true else if (c == '"') q = false
            } else if (c == '"') q = true else if (c == o) d++ else if (c == z && --d == 0) return s.substring(
                i0,
                i + 1
            )
        }; error("unclosed")
    }

    private fun cases(s: String): List<C> {
        val a = bal(s, s.indexOf('[', s.indexOf("\"cases\"")))
        val r = mutableListOf<C>()
        var p = 0; while (p < a.length) {
            if (a[p] == '{') {
                val o = bal(a, p)
                val id = Regex("\"id\"\\s*:\\s*\"([^\"]*)\"").find(o)!!.groupValues[1]
                val k = Regex("\"kind\"\\s*:\\s*\"([^\"]*)\"").find(o)!!.groupValues[1]; r += C(id, k, o); p += o.length
            } else p++
        }; return r
    }

    private fun str(s: String, k: String, d: String = "") =
        Regex("\"$k\"\\s*:\\s*\"([^\"]*)\"").find(s)?.groupValues?.get(1) ?: d

    private fun num(s: String, k: String, d: Int = 0) =
        Regex("\"$k\"\\s*:\\s*(\\d+)").find(s)?.groupValues?.get(1)?.toInt() ?: d

    private fun events(s: String) = Regex("\"events\"\\s*:\\s*\\[([^]]*)]").find(s)?.groupValues?.get(1)
        ?.let { Regex("\"([^\"]*)\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    private fun list(s: String) = Regex("\"list\"\\s*:\\s*\\[([^]]*)]").find(s)?.groupValues?.get(1)
        ?.let { Regex("\"([^\"]*)\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    private fun rows(s: String): List<Pair<Int, List<String>>> {
        val r = Regex("\\[\\s*(\\d+)\\s*,\\s*([^]]+)]").findAll(s.substringAfter("\"list\"")).map { m ->
            m.groupValues[1].toInt() to Regex("\"([^\"]*)\"").findAll(m.groupValues[2]).map { x -> x.groupValues[1] }
                .toList()
        }.toList(); return r
    }

    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    private fun arr(a: List<Any?>) = a.joinToString(",", "[", "]") {
        when (it) {
            null -> "null"; is String -> "\"${esc(it)}\""; else -> it.toString()
        }
    }

    private fun snap(kind: String, attached: Boolean, fields: String) =
        "{\"kind\":\"$kind\",\"attached\":$attached${if (fields.isEmpty()) "" else ",$fields"}}"

    private val help =
        "【평균 레벨】\n※ 아군 중 상위 3명의 레벨 합계를 평균으로 계산합니다。\n\n【훈련 관련】\n※ 훈련 시, 인물 레벨 >= 평균 레벨일 경우, 인물 경험치가 증가하지 않습니다。\n\n※ 훈련을 통해 대기 무장이나 낮은 등급의 장비를 업그레이드할 수 있습니다。\n\n※ 출전하는 무장은 공격 측이 되고, 나머지 무장은 적대 측이 되어 어느 한쪽 모두 퇴각하면 훈련이 종료됩니다。\n\n【전투 관련】\n※ [장비 레벨 > (보유 장비 무장의 레벨 / 5) + 1]일 경우, 경험치가 증가하지 않습니다。".replace(
            '。',
            '.'
        )

    private fun run(c: C): String {
        val ev = events(c.raw)
        val out = mutableListOf<String>()
        when (c.kind) {
            "notice" -> {
                var show = false
                var count = 0


                fun x() = snap(c.kind, true, "\"show\":$show,\"count\":$count")
                out += x(); ev.forEach { z ->
                    val p = z.split(':'); if (p[0] == "toggle" && p[1] == "2") {
                    show = !show; if (!show) count = 0
                } else if (p[0] == "notice" && show) count++; out += x()
                }
            }

            "help" -> {
                var attached = true; out += snap(
                    c.kind,
                    attached,
                    "\"label\":\"${esc(help)}\""
                ); ev.forEach { z -> if (z.endsWith(":2")) attached = false; out += snap(c.kind, attached, "") }
            }

            "input" -> {
                var saved = str(c.raw, "saved")
                val value = str(c.raw, "text")
                var attached = true
                val calls = mutableListOf<String?>()


                fun x() = snap(
                    c.kind,
                    attached,
                    "\"value\":\"${esc(value)}\",\"saved\":\"${esc(saved)}\",\"calls\":${arr(calls)}"
                )
                out += x(); ev.forEach { z ->
                    val p = z.split(':'); if (p[0] == "button" && p[2] == "2") {
                    attached = false; if (p[1] == "0") {
                        saved = value; calls += value
                    } else calls += null
                }; out += x()
                }
            }

            "select" -> {
                val xs = list(c.raw)
                val pc = num(c.raw, "pageCount", 50)
                var sel = num(c.raw, "sel").coerceIn(0, xs.lastIndex)
                var page = sel / pc
                var attached = true
                val calls = mutableListOf<Int>(); fun x(): String {
                    val start = page * pc
                    val rs = (start until minOf(start + pc, xs.size)).joinToString(",", "[", "]") { i ->
                        "[$i,\"${i}. ${
                            esc(xs[i])
                        }\",${i == sel}]"
                    }; return snap(
                        c.kind,
                        attached,
                        "\"page\":$page,\"label\":\"${page + 1}/${(xs.size + pc - 1) / pc}\",\"sel\":$sel,\"rows\":$rs,\"calls\":${
                            arr(calls)
                        }"
                    )
                }; out += x(); ev.forEach { z ->
                    val p = z.split(':'); when (p[0]) {
                    "prev" -> if (p[1] == "2") page =
                        if (page == 0) (xs.size + pc - 1) / pc - 1 else page - 1; "next" -> if (p[1] == "2") page =
                        (page + 1) % ((xs.size + pc - 1) / pc); "row" -> if (p[2] == "2") sel =
                        p[1].toInt(); "ok" -> if (p[1] == "2") {
                        calls += sel; attached = false
                    }; "cancel" -> if (p[1] == "2") {
                        calls += -1; attached = false
                    }
                }; out += x()
                }
            }

            else -> {
                val multi = num(c.raw, "flag") and 1 != 0
                val rs = rows(c.raw)
                val checked = rs.associate { it.first to false }.toMutableMap()
                var attached = true
                val calls = mutableListOf<Any?>()


                fun x() = snap(
                    c.kind,
                    attached,
                    "\"rows\":${
                        rs.joinToString(
                            ",",
                            "[",
                            "]"
                        ) { "[${it.first},${checked[it.first]}]" }
                    },\"calls\":${arr(calls)}"
                )
                out += x(); ev.forEach { z ->
                    val p = z.split(':'); when (p[0]) {
                    "row" -> if (p[2] == "2") {
                        if (!multi) checked.keys.forEach { checked[it] = false }; checked[p[1].toInt()] =
                            !(checked[p[1].toInt()] ?: false)
                    }; "ok" -> if (p[1] == "2") {
                        val v = checked.filterValues { it }.keys.toList(); calls += if (multi) v.joinToString(
                            ",",
                            "[",
                            "]"
                        ) else v.firstOrNull(); attached = false
                    }; "cancel" -> if (p[1] == "2") {
                        calls += if (multi) null else -1; attached = false
                    }
                }; out += x()
                }
            }
        }; return out.joinToString(",", "[", "]")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val out = cases(Files.readString(Path.of(args[0]))).joinToString(
            ",",
            "{",
            "}"
        ) { "\"${it.id}\":${run(it)}" }; Files.createDirectories(Path.of(args[1]).parent); Files.writeString(
            Path.of(
                args[1]
            ), out
        ); println(out)
    }
}
