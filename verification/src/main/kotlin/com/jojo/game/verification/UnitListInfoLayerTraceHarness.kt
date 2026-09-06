// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.battle.overlay.MineUnitInfoLayer
import com.jojo.game.presentation.battle.overlay.OtherUnitInfoLayer

import com.jojo.game.presentation.scenario.hall.*
import com.jojo.game.presentation.shared.InfoBaseValueAnimation

import com.jojo.game.*
import com.jojo.game.domain.battle.*

import java.nio.file.Files
import java.nio.file.Path


/** UnitListInfoLayerTraceHarness: 복원된 UnitListLayer·MineUnitInfoLayer·OtherUnitInfoLayer와 InfoBase 애니메이션의 동작을 실행한다. 렌더러에 의존하지 않고 복원 팩토리와 같은 픽스처로 상태·이벤트·완료 계약을 검증한다. */
object UnitListInfoLayerTraceHarness {
    /** Unit: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Unit(
        /** id: 상점 항목 식별자를 담는다. */
        val id: Int,
        /** name: 검증 대상의 표시 이름을 담는다. */
        val name: String,
        /** post: 검증 대상의 현재 상태 값을 담는다. */
        val post: String,
        /** lv: 검증 대상의 현재 상태 값을 담는다. */
        val lv: Int,
        /** hp: 검증 대상의 현재 상태 값을 담는다. */
        val hp: Int,
        /** hpMax: 검증 대상의 현재 상태 값을 담는다. */
        val hpMax: Int,
        /** mp: 검증 대상의 현재 상태 값을 담는다. */
        val mp: Int,
        /** mpMax: 검증 대상의 현재 상태 값을 담는다. */
        val mpMax: Int,
        /** exp: 검증 대상의 현재 상태 값을 담는다. */
        val exp: Int = 0,
        /** expMax: 검증 대상의 현재 상태 값을 담는다. */
        val expMax: Int = 100,
        /** wExp: 검증 대상의 현재 상태 값을 담는다. */
        val wExp: Int = 0,
        /** wMax: 검증 대상의 현재 상태 값을 담는다. */
        val wMax: Int = 100,
        /** aExp: 검증 대상의 현재 상태 값을 담는다. */
        val aExp: Int = 0,
        /** aMax: 검증 대상의 현재 상태 값을 담는다. */
        val aMax: Int = 100
    )

    /** q: 문자열을 JSON 인용 형식으로 변환한다. */
    private fun q(s: String) = "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    /** field: 입력 데이터에서 지정한 블록을 추출한다. */
    private fun field(o: String, n: String): String? =
        Regex("\\\"$n\\\"\\s*:\\s*(\\{(?:[^{}]|\\{[^{}]*})*}|\\[[^]]*]|\\\"(?:\\\\.|[^\"])*\\\"|-?\\d+|true|false)").find(
            o
        )?.groupValues?.get(1)

    /** int: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
    private fun int(o: String, n: String, d: Int = 0) = field(o, n)?.trim('"')?.toIntOrNull() ?: d
    /** str: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun str(o: String, n: String) = field(o, n)?.trim()?.removeSurrounding("\"") ?: ""
    /** balanced: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
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

    /** cases: 검증 입력을 처리하고 관련 상태를 갱신한다. */
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

    /** units: 검증 입력을 처리하고 관련 상태를 갱신한다. */
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

    /** single: 검증 입력을 처리하고 관련 상태를 갱신한다. */
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

    /** events: 입력 데이터에서 검증 이벤트 목록을 추출한다. */
    private fun events(o: String) = Regex("\"events\"\\s*:\\s*\\[(.*?)]").find(o)?.groupValues?.get(1)
        ?.let { Regex("\"([^\"]*)\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    /** data: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun data(o: String): Map<String, Int> {
        val x = field(o, "data") ?: "{}"; return Regex("\"([^\"]+)\"\\s*:\\s*(-?\\d+)").findAll(x)
            .associate { it.groupValues[1] to it.groupValues[2].toInt() }
    }

    /** num: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
    private fun num(x: Double) = if (x == x.toLong().toDouble()) x.toLong().toString() else x.toString()
    /** listRun: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun listRun(c: String): String {
        val byId = units(c).associateBy { it.id }
        val layer = HallUnitListLayer(byId.keys)
        val pos =
            field(c, "pos")?.let { Regex("-?\\d+").findAll(it).map { m -> m.value.toInt() }.toList() } ?: listOf(0, 0)
        val dispatched = mutableListOf<Int>()


        /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
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

    /** infoRun: 검증 입력을 처리하고 관련 상태를 갱신한다. */
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
        /** add: 검증 이벤트 또는 항목을 현재 기록에 추가한다. */
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


        /** encode: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun encode(x: InfoBaseValueAnimation.Value) =
            "{\"idx\":${x.index},\"src\":${x.source},\"dsc\":${x.destination},\"max\":${x.max}}"


        /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
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

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
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
