package com.jojo.game.verification
import com.jojo.game.presentation.battle.edit.*

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** Direct Kotlin mutation implementation for recovered EditLayer owners.  This stays
 * deliberately data-only: Cocos nodes are exercised by the JS half, while
 * this half owns the same observable game-state transition contract. */
/**
 * object  `EditMutationTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object EditMutationTraceHarness {
    /**
     * data class  `Case`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Case(val id: String, val owner: String, val flag: Int, val events: List<String>)

    private fun bal(s: String, p: Int): String {
        val o = s[p]
        val z = if (o == '{') '}' else ']'
        var d = 0
        var q = false
        var e = false; for (i in p until s.length) {
            val c = s[i]; if (q) {
                if (e) e = false else if (c == '\\') e = true else if (c == '"') q = false
            } else if (c == '"') q = true else if (c == o) d++ else if (c == z && --d == 0) return s.substring(p, i + 1)
        }; error("json")
    }

    private fun objects(s: String): List<String> {
        val r = mutableListOf<String>()
        var i = 0; while (i < s.length) {
            if (s[i] == '{') {
                val z = bal(s, i); r += z; i += z.length
            } else i++
        }; return r
    }

    private fun str(s: String, k: String) = Regex("\\\"$k\\\"\\s*:\\s*\\\"([^\"]*)").find(s)!!.groupValues[1]
    private fun num(s: String, k: String) = Regex("\\\"$k\\\"\\s*:\\s*(\\d+)").find(s)!!.groupValues[1].toInt()
    private fun block(s: String, k: String): String {
        val p = s.indexOf('[', s.indexOf("\"$k\"")); return bal(s, p)
    }

    private fun cases(s: String) = objects(block(s, "cases")).map {
        Case(
            str(it, "id"),
            str(it, "owner"),
            num(it, "flag"),
            Regex("\\\"([^\"]*)\\\"").findAll(block(it, "events")).map { m -> m.groupValues[1] }.toList()
        )
    }

    private fun q(s: String) = "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    private fun arr(v: List<String>) = v.joinToString(",", "[", "]")
    private fun snap(
        step: String,
        attached: Boolean,
        layers: List<String>,
        toasts: List<String>,
        dispatch: List<Pair<String, Int>>,
        tail: String
    ) =
        "{\"step\":${q(step)},\"attached\":$attached,\"layers\":${arr(layers.map(::q))},\"toasts\":${arr(toasts.map(::q))},\"dispatch\":${
            dispatch.joinToString(
                ",",
                "[",
                "]"
            ) { "[${q(it.first)},${it.second}]" }
        },$tail}"

    private fun battle(c: Case): String {
        var attached = true
        var weather = 0
        var round = 10
        val toast = mutableListOf<String>()
        val dispatch = mutableListOf<Pair<String, Int>>()
        val edit = BattleEditLayer2(weather, round, (c.flag and 4) != 0)
        val out = mutableListOf<String>()

        /**
         * 공개 메서드 `s`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun s(k: String) = snap(k, attached, emptyList(), toast, dispatch, "\"weather\":$weather,\"round\":$round")

        /**
         * 공개 메서드 `applyEffects`
         *
         * ### 파라미터
        - `effects` (`List<BattleEditLayer2.Effect>`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun applyEffects(effects: List<BattleEditLayer2.Effect>) {
            effects.forEach {
                when (it) {
                    is BattleEditLayer2.Effect.SetWeather -> weather =
                        it.value; is BattleEditLayer2.Effect.SetRound -> round =
                    it.value; is BattleEditLayer2.Effect.Toast -> toast += it.text; is BattleEditLayer2.Effect.KillAll -> dispatch += "KILL_ALL" to it.flag; BattleEditLayer2.Effect.Remove -> attached =
                    false; else -> Unit
                }
            }
        }
        out += s("create"); for (e in c.events) {
            val p = e.split(':'); when (p[0]) {
                "weather" -> edit.selectWeather(p[1].toInt()); "round" -> {
                    edit.textChanged(p[1]); edit.editingDidEnd()
                }; "apply" -> applyEffects(
                    edit.touchButton(
                        0,
                        p[1].toInt()
                    )
                ); "kill" -> applyEffects(edit.touchButton(p[1].toInt(), p[2].toInt()))
            }; out += s(e)
        }; return arr(out)
    }

    private fun unit(c: Case): String {
        var attached = true
        var level = 1
        val toasts = mutableListOf<String>()
        val out = mutableListOf<String>()

        /**
         * 공개 메서드 `snapshot`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snapshot(k: String): String = snap(k, attached, emptyList(), toasts, emptyList(), "\"level\":$level")
        out += snapshot("create")
        for (e in c.events) {
            val p = e.split(':')
            if (p[0] == "apply" && p[1] == "2") {
                if ((c.flag and 2) == 0) toasts += "만렙 시작이 활성화되지 않아 유닛 레벨을 수정할 수 없습니다."
                else {
                    level = 50; attached = false
                }
            }
            out += snapshot(e)
        }
        return arr(out)
    }

    private fun global(c: Case): String {
        var attached = true
        var amb = 10
        var money = 100
        var stage = 1
        var clears = 0
        var pending = false
        val layers = mutableListOf<String>()
        val toasts = mutableListOf<String>()
        val flow = EditGlobalSourceOracle(amb, money, stage, List(10) { "S$it" })
        val out = mutableListOf<String>()

        /**
         * 공개 메서드 `snapshot`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snapshot(k: String) = snap(
            k,
            attached,
            layers,
            toasts,
            emptyList(),
            "\"ambition\":$amb,\"money\":$money,\"stage\":$stage,\"clears\":$clears"
        )

        /**
         * 공개 메서드 `applyEffects`
         *
         * ### 파라미터
        - `effects` (`List<EditGlobalSourceOracle.Effect>`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun applyEffects(effects: List<EditGlobalSourceOracle.Effect>) {
            effects.forEach {
                when (it) {
                    is EditGlobalSourceOracle.Effect.SetAmbition -> amb =
                        it.value; is EditGlobalSourceOracle.Effect.SetMoney -> money =
                    it.value; is EditGlobalSourceOracle.Effect.SetStage -> stage =
                    it.value; EditGlobalSourceOracle.Effect.Close -> attached =
                    false; EditGlobalSourceOracle.Effect.AskClearInventory -> {
                    layers += "MsgBox"; pending = true
                }; EditGlobalSourceOracle.Effect.ClearInventory -> clears++; is EditGlobalSourceOracle.Effect.Toast -> toasts += it.text; else -> Unit
                }
            }
        }
        out += snapshot("create")
        for (e in c.events) {
            val p = e.split(':'); when (p[0]) {
                "ambition" -> flow.endEdit(
                    EditGlobalSourceOracle.Field.AMBITION,
                    p[1].toInt()
                ); "money" -> flow.endEdit(
                    EditGlobalSourceOracle.Field.MONEY,
                    p[1].toInt()
                ); "stage" -> flow.selectScene(p[1].toInt())
                "apply" -> if (p[1] == "2") applyEffects(flow.button(0))
                "store" -> if (p[2] == "2") applyEffects(flow.button(2))
                "confirm" -> if (pending) applyEffects(flow.clearInventoryAnswer(p[1].toInt()))
            }; out += snapshot(e)
        }
        return arr(out)
    }

    private fun roster(c: Case): String {
        var attached = true
        val names = List(27) { "U$it" }
        val flow = EditRosterFlow(
            listOf(EditRosterFlow.UnitRow(0, "U0", false), EditRosterFlow.UnitRow(7, "U7", false)),
            names
        )
        val layers = mutableListOf<String>()
        val toasts = mutableListOf<String>()
        val out = mutableListOf<String>()

        /**
         * 공개 메서드 `applyEffects`
         *
         * ### 파라미터
        - `effects` (`List<EditRosterFlow.Effect>`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun applyEffects(effects: List<EditRosterFlow.Effect>) {
            effects.forEach {
                when (it) {
                    EditRosterFlow.Effect.OpenGlobalEditor -> layers += "EditLayer"; EditRosterFlow.Effect.OpenLearnUnitSkill -> layers += "LearnUnitSkillLayer"; EditRosterFlow.Effect.Close -> attached =
                    false; is EditRosterFlow.Effect.Toast -> toasts += it.text; else -> Unit
                }
            }
        }

        /**
         * 공개 메서드 `snapshot`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snapshot(k: String) = snap(
            k,
            attached,
            layers,
            toasts,
            emptyList(),
            "\"joined\":${arr(flow.rows().filter { !it.leave }.map { it.id }.sorted().map { it.toString() })}"
        )
        out += snapshot("create")
        for (e in c.events) {
            val p = e.split(':')
            if (p[2] == "2") applyEffects(flow.button(p[1].toInt()))
            out += snapshot(e)
        }
        return arr(out)
    }

    private fun avatar(c: Case): String {
        var attached = true
        var avatar: String? = null
        var loads = 0
        var page = -1
        val toasts = mutableListOf("선택하려면 최소한 하나의 모드를 설치해야 합니다!")
        val out = mutableListOf<String>()

        /**
         * 공개 메서드 `snapshot`
         *
         * ### 파라미터
        - `k` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun snapshot(k: String) = snap(
            k,
            attached,
            emptyList(),
            toasts,
            emptyList(),
            "\"avatar\":${avatar ?: "null"},\"loads\":$loads,\"page\":$page"
        )
        out += snapshot("create")
        for (e in c.events) {
            val p = e.split(':'); if (p.last() == "2" && p[0] == "apply") {
                avatar = "[1,786437]"; loads++; attached = false
            }; if (p.last() == "2" && p[0] == "next") page = 0; if (p.last() == "2" && p[0] == "reset") {
                avatar = "[1,null]"; loads++; attached = false
            }; out += snapshot(e)
        }
        return arr(out)
    }

    @JvmStatic
    fun main(a: Array<String>) {
        val out = cases(Files.readString(Path.of(a[0]))).joinToString(",", "{", "}") { c ->
            q(c.id) + ":" + when (c.owner) {
                "battle" -> battle(c); "unit" -> unit(c); "global" -> global(c); "roster" -> roster(c); else -> avatar(c)
            }
        }; Files.createDirectories(Path.of(a[1]).parent); Files.writeString(Path.of(a[1]), out)
    }
}

/** Isolated EditLayer3 source oracle; reachable only behind the source EDIT developer flag. */
private class EditGlobalSourceOracle(ambition: Int, money: Int, stage: Int, private val names: List<String>) {
    /**
     * enum class  `Field`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Field { SCENE, AMBITION, MONEY }
    sealed interface Effect {
        data class SetAmbition(val value: Int) : Effect
        data class SetMoney(val value: Int) : Effect
        data class SetStage(val value: Int) : Effect
        data object ReplaceHall : Effect
        data object Close : Effect
        data object AskClearInventory : Effect
        data object ClearInventory : Effect
        data class Toast(val text: String) : Effect
    }

    private val original = mutableMapOf(Field.AMBITION to ambition, Field.MONEY to money)
    private val pending = linkedMapOf<Field, Int>()
    var sceneLabel = names[stage.coerceIn(names.indices)] + if (stage % 2 == 0) "R" else "S"; private set

    /**
     * 공개 메서드 `endEdit`
     *
     * ### 파라미터
    - `field` (`Field`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun endEdit(field: Field, value: Int) {
        if (original[field] == value) pending.remove(field) else pending[field] = value
    }

    /**
     * 공개 메서드 `selectScene`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectScene(index: Int) {
        pending[Field.SCENE] = index; sceneLabel = names[index]
    }

    /**
     * 공개 메서드 `button`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Effect>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun button(tag: Int): List<Effect> = when (tag) {
        0 -> buildList {
            pending.forEach { (f, v) ->
                when (f) {
                    Field.SCENE -> addAll(
                        listOf(
                            Effect.SetStage(v * 2),
                            Effect.ReplaceHall
                        )
                    ); Field.AMBITION -> add(
                    Effect.SetAmbition(
                        v.coerceIn(
                            1,
                            100
                        )
                    )
                ); Field.MONEY -> add(Effect.SetMoney(v.coerceIn(0, 9_999_999)))
                }
            }; add(Effect.Close)
        }; 2 -> listOf(Effect.AskClearInventory); else -> emptyList()
    }

    /**
     * 공개 메서드 `clearInventoryAnswer`
     *
     * ### 파라미터
    - `answer` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun clearInventoryAnswer(answer: Int) =
        if (answer == 0) listOf(Effect.ClearInventory, Effect.Toast("모든 장비와 아이템을 버렸습니다!")) else emptyList()
}
