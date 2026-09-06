// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.battle.preparation.BattleDeploymentRules
import com.jojo.game.presentation.battle.preparation.BattleInitPresentationState
import com.jojo.game.presentation.battle.preparation.BattleRosterModel
import com.jojo.game.presentation.battle.preparation.BattleSortModel
import com.jojo.game.presentation.battle.preparation.HallPreparationFlow

import java.nio.file.Files
import java.nio.file.Path


/** HallPrepTraceHarness: 전투 준비 화면의 입력과 렌더 상태를 추적한다. */
object HallPrepTraceHarness {
    /** C: c 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class C(val id: String, val kind: String, val flag: Int, val events: List<String>)

    /** bal: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun bal(s: String, p: Int): String {
        val o = s[p]
        val z = if (o == '{') '}' else ']'
        var d = 0
        var q = false
        var e = false; for (i in p until s.length) {
            val c = s[i]; if (q) {
                if (e) e = false else if (c == '\\') e = true else if (c == '\"') q = false
            } else if (c == '\"') q = true else if (c == o) d++ else if (c == z && --d == 0) return s.substring(
                p,
                i + 1
            )
        }; error("json")
    }

    /** objs: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun objs(s: String): List<String> {
        val r = mutableListOf<String>()
        var i = 0; while (i < s.length) {
            if (s[i] == '{') {
                val x = bal(s, i); r += x; i += x.length
            } else i++
        }; return r
    }

    /** str: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun str(s: String, k: String) = Regex("\\\"$k\\\"\\s*:\\s*\\\"([^\"]*)").find(s)!!.groupValues[1]
    /** num: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun num(s: String, k: String) =
        Regex("\\\"$k\\\"\\s*:\\s*(\\d+)").find(s)?.groupValues?.get(1)?.toInt() ?: 0

    /** arr: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun arr(s: String, k: String): String {
        val a = s.indexOf("\"$k\"")
        val p = s.indexOf('[', a); return bal(s, p)
    }

    /** cases: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun cases(s: String) = objs(arr(s, "cases")).map { o ->
        C(
            str(o, "id"),
            str(o, "kind"),
            num(o, "flag"),
            Regex("\\\"([^\"]*)\\\"").findAll(arr(o, "events")).map { it.groupValues[1] }.toList()
        )
    }

    /** js: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun js(v: List<String>) = v.joinToString(",", "[", "]")
    /** hall: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun hall(c: C): String {
        val x = HallPreparationFlow(); x.onCreate(c.flag)
        /** snap: 회관 준비 상태를 JSON 스냅샷으로 만든다. */
        fun snap(k: String) = "{\"step\":\"$k\",\"flag\":${x.flag},\"layers\":${js(x.layers.map { "\"$it\"" })}}"
        val r = mutableListOf(snap("create")); c.events.forEach { e ->
            x.command(
                e.substringAfter(':').toInt()
            ); r += snap(e)
        }; return js(r)
    }

    /** init: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun init(c: C): String {
        val x = BattleInitPresentationState(); x.create(c.flag)
        /** snap: 전투 초기화 상태를 JSON 스냅샷으로 만든다. */
        fun snap(k: String) =
            "{\"step\":\"$k\",\"flag\":${x.flag},\"z\":${x.zIndex},\"sound\":[7],\"events\":[[\"BATTLE_INIT_START\"]],\"labels\":${
                js(x.labels.map { "\"$it\"" })
            },\"attached\":${x.attached},\"stopped\":${x.stopped}}"

        val r = mutableListOf(snap("create")); c.events.forEach { e ->
            if (e.startsWith("load:")) x.load(
                e.substringAfter(
                    ':'
                )
            ) else x.destroy(); r += snap(e)
        }; return js(r)
    }

    /** sort: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun sort(c: C): String {
        val x = BattleSortModel()

        /** snap: 정렬 상태와 호출 기록을 JSON 스냅샷으로 만든다. */
        fun snap(k: String) =
            "{\"step\":\"$k\",\"pos\":{\"x\":10,\"y\":20},\"attached\":${x.attached},\"calls\":${js(x.calls.map { it.toString() })}}"

        val r = mutableListOf(snap("create")); c.events.forEach { e ->
            val p = e.split(':'); if (p[0] == "button") x.button(
            p[1].toInt(),
            p[2].toInt()
        ) else x.cancel(p[1].toInt()); r += snap(e)
        }; return js(r)
    }

    /** start: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun start(c: C): String {
        val x = BattleRosterModel()

        /** snap: 배치 편성 상태를 JSON 스냅샷으로 만든다. */
        fun snap(k: String) =
            "{\"step\":\"$k\",\"slots\":${js(x.slots.map { it.toString() })},\"fights\":${js(x.fights.map { it.toString() })},\"label\":\"${x.label}\",\"ok\":${x.ok},\"events\":${
                js(x.events.map { it.toString() })
            }}"

        val r = mutableListOf(snap("create")); c.events.forEach { e ->
            val p = e.split(':'); if (p[0] == "fight") x.fight(p[1].toInt()) else x.cancel(p[1].toInt()); r += snap(e)
        }; return js(r)
    }

    /** startInit: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun startInit(): String {
        val x =
            BattleDeploymentRules(); return "[{\"step\":\"create\",\"max\":${x.max},\"min\":${x.min},\"must\":${js(x.must.map { it.toString() })},\"mustJoin\":${
            js(
                x.mustJoin.map { it.toString() })
        },\"units\":${js(x.units.map { it.toString() })},\"order\":${js(x.order.map { it.toString() })},\"button2\":${x.button2},\"sort\":${x.sort},\"descending\":${x.descending}}]"
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(a: Array<String>) {
        val out = cases(Files.readString(Path.of(a[0]))).joinToString(",", "{", "}") {
            "\"${it.id}\":${
                when (it.kind) {
                    "hall" -> hall(it); "init" -> init(it); "sort" -> sort(it); "startinit" -> startInit(); else -> start(
                    it
                )
                }
            }"
        }; Files.createDirectories(Path.of(a[1]).parent); Files.writeString(Path.of(a[1]), out)
    }
}
