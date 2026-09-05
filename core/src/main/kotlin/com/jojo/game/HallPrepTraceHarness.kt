package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/**
 * object  `HallPrepTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object HallPrepTraceHarness {
    private data class C(val id: String, val kind: String, val flag: Int, val events: List<String>)

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

    private fun objs(s: String): List<String> {
        val r = mutableListOf<String>()
        var i = 0; while (i < s.length) {
            if (s[i] == '{') {
                val x = bal(s, i); r += x; i += x.length
            } else i++
        }; return r
    }

    private fun str(s: String, k: String) = Regex("\\\"$k\\\"\\s*:\\s*\\\"([^\"]*)").find(s)!!.groupValues[1]
    private fun num(s: String, k: String) =
        Regex("\\\"$k\\\"\\s*:\\s*(\\d+)").find(s)?.groupValues?.get(1)?.toInt() ?: 0

    private fun arr(s: String, k: String): String {
        val a = s.indexOf("\"$k\"")
        val p = s.indexOf('[', a); return bal(s, p)
    }

    private fun cases(s: String) = objs(arr(s, "cases")).map { o ->
        C(
            str(o, "id"),
            str(o, "kind"),
            num(o, "flag"),
            Regex("\\\"([^\"]*)\\\"").findAll(arr(o, "events")).map { it.groupValues[1] }.toList()
        )
    }

    private fun js(v: List<String>) = v.joinToString(",", "[", "]")
    private fun hall(c: C): String {
        val x = HallPreparationFlow(); x.onCreate(c.flag)
        /**
         * 공개 메서드 `snap`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snap(k: String) = "{\"step\":\"$k\",\"flag\":${x.flag},\"layers\":${js(x.layers.map { "\"$it\"" })}}"
        val r = mutableListOf(snap("create")); c.events.forEach { e ->
            x.command(
                e.substringAfter(':').toInt()
            ); r += snap(e)
        }; return js(r)
    }

    private fun init(c: C): String {
        val x = BattleInitPresentationState(); x.create(c.flag)
        /**
         * 공개 메서드 `snap`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

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

    private fun sort(c: C): String {
        val x = BattleSortModel()

        /**
         * 공개 메서드 `snap`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snap(k: String) =
            "{\"step\":\"$k\",\"pos\":{\"x\":10,\"y\":20},\"attached\":${x.attached},\"calls\":${js(x.calls.map { it.toString() })}}"

        val r = mutableListOf(snap("create")); c.events.forEach { e ->
            val p = e.split(':'); if (p[0] == "button") x.button(
            p[1].toInt(),
            p[2].toInt()
        ) else x.cancel(p[1].toInt()); r += snap(e)
        }; return js(r)
    }

    private fun start(c: C): String {
        val x = BattleRosterModel()

        /**
         * 공개 메서드 `snap`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snap(k: String) =
            "{\"step\":\"$k\",\"slots\":${js(x.slots.map { it.toString() })},\"fights\":${js(x.fights.map { it.toString() })},\"label\":\"${x.label}\",\"ok\":${x.ok},\"events\":${
                js(x.events.map { it.toString() })
            }}"

        val r = mutableListOf(snap("create")); c.events.forEach { e ->
            val p = e.split(':'); if (p[0] == "fight") x.fight(p[1].toInt()) else x.cancel(p[1].toInt()); r += snap(e)
        }; return js(r)
    }

    private fun startInit(): String {
        val x =
            BattleDeploymentRules(); return "[{\"step\":\"create\",\"max\":${x.max},\"min\":${x.min},\"must\":${js(x.must.map { it.toString() })},\"mustJoin\":${
            js(
                x.mustJoin.map { it.toString() })
        },\"units\":${js(x.units.map { it.toString() })},\"order\":${js(x.order.map { it.toString() })},\"button2\":${x.button2},\"sort\":${x.sort},\"descending\":${x.descending}}]"
    }

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
