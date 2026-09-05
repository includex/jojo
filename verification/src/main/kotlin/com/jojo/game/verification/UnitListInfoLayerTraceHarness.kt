package com.jojo.game.verification

import com.jojo.game.presentation.scenario.hall.*

import com.jojo.game.*
import com.jojo.game.domain.battle.*

import java.nio.file.Files
import java.nio.file.Path

/**
 * Behavioural games of recovered UnitListLayer, MineUnitInfoLayer,
 * OtherUnitInfoLayer and their InfoBase animation.  This deliberately has no
 * renderer dependency: the public state, event and completion contract is
 * exercised against the same fixture used by the recovered factories.
 */
/**
 * object  `UnitListInfoLayerTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object UnitListInfoLayerTraceHarness {
    private data class Unit(
        val id: Int,
        val name: String,
        val post: String,
        val lv: Int,
        val hp: Int,
        val hpMax: Int,
        val mp: Int,
        val mpMax: Int,
        val exp: Int = 0,
        val expMax: Int = 100,
        val wExp: Int = 0,
        val wMax: Int = 100,
        val aExp: Int = 0,
        val aMax: Int = 100
    )

    private fun q(s: String) = "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    private fun field(o: String, n: String): String? =
        Regex("\\\"$n\\\"\\s*:\\s*(\\{(?:[^{}]|\\{[^{}]*})*}|\\[[^]]*]|\\\"(?:\\\\.|[^\"])*\\\"|-?\\d+|true|false)").find(
            o
        )?.groupValues?.get(1)

    private fun int(o: String, n: String, d: Int = 0) = field(o, n)?.trim('"')?.toIntOrNull() ?: d
    private fun str(o: String, n: String) = field(o, n)?.trim()?.removeSurrounding("\"") ?: ""
    private fun balanced(s: String, start: Int): String {
        var d = 0
        var quote = false
        var esc = false; for (i in start until s.length) {
            val c = s[i]; if (quote) {
                if (esc) esc = false else if (c == '\\') esc = true else if (c == '\"') quote = false
            } else if (c == '\"') quote = true else if (c == '{' || c == '[') d++ else if (c == '}' || c == ']') {
                d--; if (d == 0) return s.substring(start, i + 1)
            }
        }; error("unbalanced")
    }

    private fun cases(raw: String): List<String> {
        val a = raw.indexOf("\"cases\"")
        val b = raw.indexOf('[', a)
        val out = mutableListOf<String>()
        var i = b + 1; while (i < raw.length) {
            if (raw[i] == '{') {
                val x = balanced(raw, i); out += x; i += x.length
            } else i++
        }; return out
    }

    private fun units(o: String): List<Unit> {
        val a = field(o, "units") ?: "[]"; return Regex("\\{([^{}]*)}").findAll(a).map { m ->
            val x = "{${m.groupValues[1]}}"; Unit(
            int(x, "id"),
            str(x, "name"),
            str(x, "post"),
            int(x, "lv"),
            int(x, "hp"),
            int(x, "hpMax"),
            int(x, "mp"),
            int(x, "mpMax"),
            int(x, "exp"),
            int(x, "expMax", 100),
            int(x, "weaponExp"),
            int(x, "weaponMax", 100),
            int(x, "armorExp"),
            int(x, "armorMax", 100)
        )
        }.toList()
    }

    private fun single(o: String): Unit {
        val x = field(o, "unit") ?: "{}"; return Unit(
            int(x, "id"),
            str(x, "name"),
            str(x, "post"),
            int(x, "lv"),
            int(x, "hp"),
            int(x, "hpMax"),
            int(x, "mp"),
            int(x, "mpMax"),
            int(x, "exp"),
            int(x, "expMax", 100),
            int(x, "weaponExp"),
            int(x, "weaponMax", 100),
            int(x, "armorExp"),
            int(x, "armorMax", 100)
        )
    }

    private fun events(o: String) = Regex("\"events\"\\s*:\\s*\\[(.*?)]").find(o)?.groupValues?.get(1)
        ?.let { Regex("\"([^\"]*)\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    private fun data(o: String): Map<String, Int> {
        val x = field(o, "data") ?: "{}"; return Regex("\"([^\"]+)\"\\s*:\\s*(-?\\d+)").findAll(x)
            .associate { it.groupValues[1] to it.groupValues[2].toInt() }
    }

    private fun num(x: Double) = if (x == x.toLong().toDouble()) x.toLong().toString() else x.toString()
    private fun listRun(c: String): String {
        val byId = units(c).associateBy { it.id }
        val layer = HallUnitListLayer(byId.keys)
        val pos =
            field(c, "pos")?.let { Regex("-?\\d+").findAll(it).map { m -> m.value.toInt() }.toList() } ?: listOf(0, 0)
        val dispatched = mutableListOf<Int>()

        /**
         * 공개 메서드 `snap`
         *
         * ### 파라미터
        - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snap(step: String): String {
            val rows = layer.rows.mapIndexed { i, id ->
                val u = requireNotNull(byId[id]); "{\"tag\":$i,\"labels\":[${q(u.name)},${q(u.post)}]}"
            }.joinToString(",")
            val ev = dispatched.joinToString(",", "[", "]") { "{\"name\":\"CHANGE_UNIT\",\"id\":$it}" }
            return "{\"step\":${q(step)},\"active\":${layer.attached},\"pos\":{\"x\":${pos[0]},\"y\":${pos[1]}},\"rows\":[$rows],\"dead\":false,\"events\":$ev,\"routes\":[]}"
        }

        val out = mutableListOf(snap("create"))
        events(c).forEach { e ->
            val p = e.split(':'); when (p[0]) {
            "row" -> layer.onRow(p[1].toInt(), p[2].toInt())
                ?.let(dispatched::add); "cancel" -> layer.onCancel(p[1].toInt())
        }; out += snap(e)
        }
        return out.joinToString(",", "[", "]")
    }

    private fun infoRun(c: String): String {
        val mine = str(c, "kind") == "mine"
        val u = single(c)
        val d = data(c)
        var hp = d["HP"] ?: u.hp
        var mp = d["MP"] ?: u.mp
        var exp = d["EXP"] ?: u.exp
        var we = d["WQ_EXP"] ?: u.wExp
        var ae = d["HJ_EXP"] ?: u.aExp
        val battleUnit = BattleUnit(
            u.id.toString(),
            u.name,
            Faction.PLAYER,
            0,
            0,
            hitPoints = hp,
            maxHitPoints = u.hpMax,
            magicPoints = mp,
            maxMagicPoints = u.mpMax,
            level = u.lv
        )
        val entries = mutableListOf<InfoBaseValueAnimation.Value>(); fun add(
            key: String,
            idx: Int,
            src: Int,
            max: Int
        ) {
            d[key]?.let { entries += InfoBaseValueAnimation.Value(idx, src, src + it, max) }
        }
        add("HP_ADD", 0, hp, u.hpMax); add("MP_ADD", 1, mp, u.mpMax); if (mine) {
            add("EXP_ADD", 2, exp, u.expMax); add("WQ_EXP_ADD", 3, we, u.wMax); add("HJ_EXP_ADD", 4, ae, u.aMax)
        }
        val animation = if (mine) {
            val layer = MineUnitInfoLayer(); layer.onCreate(battleUnit, u.post, u.name); layer.valueAnimation(entries)
        } else {
            val layer = OtherUnitInfoLayer(); layer.onCreate(battleUnit, u.post, u.name); layer.valueAnimation(entries)
        }
        val labs = if (mine) arrayOf(
            mutableListOf(u.name, hp.toString(), mp.toString(), exp.toString()),
            mutableListOf(u.lv.toString(), u.hpMax.toString(), u.mpMax.toString(), u.expMax.toString()),
            mutableListOf(u.post),
            mutableListOf(if (we == u.wMax) "MAX" else we.toString()),
            mutableListOf(if (ae == u.aMax) "MAX" else ae.toString())
        ) else arrayOf(
            mutableListOf(u.name, hp.toString(), mp.toString()),
            mutableListOf(u.lv.toString(), u.hpMax.toString(), u.mpMax.toString()),
            mutableListOf(u.post)
        )
        val bars = if (mine) listOf(
            hp.toDouble() / u.hpMax,
            mp.toDouble() / u.mpMax,
            exp.toDouble() / u.expMax
        ) else listOf(hp.toDouble() / u.hpMax, mp.toDouble() / u.mpMax)

        /**
         * 공개 메서드 `encode`
         *
         * ### 파라미터
        - `x` (`InfoBaseValueAnimation.Value`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun encode(x: InfoBaseValueAnimation.Value) =
            "{\"idx\":${x.index},\"src\":${x.source},\"dsc\":${x.destination},\"max\":${x.max}}"

        /**
         * 공개 메서드 `snap`
         *
         * ### 파라미터
        - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snap(step: String): String {
            val ls =
                labs.mapIndexed { i, a -> "[${q("label$i")},[${a.joinToString(",") { q(it) }}]]" }.joinToString(",")
            val bs = bars.joinToString(",", "[", "]") { num(it) }
            val extra = animation.current?.let { "\"value\":${encode(it)}" } ?: "\"value\":null"
            return "{\"step\":${q(step)},\"dead\":false,\"labels\":[$ls],\"bars\":$bs,\"kvs\":[${
                animation.pending().joinToString(",") { encode(it) }
            }],$extra,\"arys\":[${animation.values.joinToString(",")}] }".replace("] }", "]}")
        }

        val out = mutableListOf(snap("create")); events(c).forEach { e ->
            if (e == "callback") animation.callback()?.let { x ->
                when (x.index) {
                    0 -> labs[0][1] = x.text; 1 -> labs[0][2] = x.text; 2 -> labs[0][3] = x.text; 3 -> labs[3][0] =
                    x.text; 4 -> labs[4][0] = x.text
                }
            }
            out += snap(e)
        }; return out.joinToString(",", "[", "]")
    }

    @JvmStatic
    fun main(a: Array<String>) {
        val raw = Files.readString(Path.of(a[0]))
        val json = cases(raw).joinToString(",", "{", "}") { c ->
            q(str(c, "name")) + ":" + (if (str(
                    c,
                    "kind"
                ) == "list"
            ) listRun(c) else infoRun(c))
        }
        val p = Path.of(a[1]); Files.createDirectories(p.parent); Files.writeString(p, json)
    }
}
