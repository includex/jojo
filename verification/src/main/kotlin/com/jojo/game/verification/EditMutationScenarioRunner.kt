// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.battle.edit.*

/** EditMutationScenarioRunner: 각 픽스처를 담당 상태 머신으로 전달해 실행한다. */
object EditMutationScenarioRunner {
    /** run: 검증 실행에 필요한 상태를 구성한다. */
    fun run(scenario: EditMutationCase): String = when (scenario.owner) {
        "battle" -> EditBattleMutationScenario.run(scenario)
        "unit" -> EditUnitMutationScenario.run(scenario)
        "global" -> EditGlobalMutationScenario.run(scenario)
        "roster" -> EditRosterMutationScenario.run(scenario)
        else -> EditAvatarMutationScenario.run(scenario)
    }
}

/** EditBattleMutationScenario: edit battle mutation scenario 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
object EditBattleMutationScenario {
    /** run: 검증 실행에 필요한 상태를 구성한다. */
    fun run(scenario: EditMutationCase): String {
        var attached = true
        var weather = 0
        var round = 10
        val toasts = mutableListOf<String>()
        val dispatch = mutableListOf<Pair<String, Int>>()
        val edit = BattleEditLayer2(weather, round, (scenario.flag and 4) != 0)
        val output = mutableListOf<String>()

        /** snapshot: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snapshot(step: String) = EditMutationTraceJson.snapshot(
            step, attached, emptyList(), toasts, dispatch, "\"weather\":$weather,\"round\":$round"
        )

        /** apply: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun apply(effects: List<BattleEditLayer2.Effect>) {
            effects.forEach { effect ->
                when (effect) {
                    is BattleEditLayer2.Effect.SetWeather -> weather = effect.value
                    is BattleEditLayer2.Effect.SetRound -> round = effect.value
                    is BattleEditLayer2.Effect.Toast -> toasts += effect.text
                    is BattleEditLayer2.Effect.KillAll -> dispatch += "KILL_ALL" to effect.flag
                    BattleEditLayer2.Effect.Remove -> attached = false
                    else -> Unit
                }
            }
        }

        output += snapshot("create")
        scenario.events.forEach { event ->
            /**
             * `parts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val parts = event.split(':')
            when (parts[0]) {
                "weather" -> edit.selectWeather(parts[1].toInt())
                "round" -> { edit.textChanged(parts[1]); edit.editingDidEnd() }
                "apply" -> apply(edit.touchButton(0, parts[1].toInt()))
                "kill" -> apply(edit.touchButton(parts[1].toInt(), parts[2].toInt()))
            }
            output += snapshot(event)
        }
        return EditMutationTraceJson.array(output)
    }
}

/** EditUnitMutationScenario: edit unit mutation scenario 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
object EditUnitMutationScenario {
    /** run: 검증 실행에 필요한 상태를 구성한다. */
    fun run(scenario: EditMutationCase): String {
        var attached = true
        var level = 1
        val toasts = mutableListOf<String>()
        val output = mutableListOf<String>()

        /** snapshot: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snapshot(step: String) = EditMutationTraceJson.snapshot(
            step, attached, emptyList(), toasts, emptyList(), "\"level\":$level"
        )

        output += snapshot("create")
        scenario.events.forEach { event ->
            val parts = event.split(':')
            if (parts[0] == "apply" && parts[1] == "2") {
                if ((scenario.flag and 2) == 0) {
                    toasts += "만렙 시작이 활성화되지 않아 유닛 레벨을 수정할 수 없습니다."
                } else {
                    level = 50
                    attached = false
                }
            }
            output += snapshot(event)
        }
        return EditMutationTraceJson.array(output)
    }
}

/** EditGlobalMutationScenario: edit global mutation scenario 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
object EditGlobalMutationScenario {
    /** run: 검증 실행에 필요한 상태를 구성한다. */
    fun run(scenario: EditMutationCase): String {
        var attached = true
        var ambition = 10
        var money = 100
        var stage = 1
        var clears = 0
        var pending = false
        val layers = mutableListOf<String>()
        val toasts = mutableListOf<String>()
        val flow = EditGlobalSourceOracle(ambition, money, stage, List(10) { "S$it" })
        val output = mutableListOf<String>()

        /** snapshot: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snapshot(step: String) = EditMutationTraceJson.snapshot(
            step,
            attached,
            layers,
            toasts,
            emptyList(),
            "\"ambition\":$ambition,\"money\":$money,\"stage\":$stage,\"clears\":$clears"
        )

        /** apply: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun apply(effects: List<EditGlobalSourceOracle.Effect>) {
            effects.forEach { effect ->
                when (effect) {
                    is EditGlobalSourceOracle.Effect.SetAmbition -> ambition = effect.value
                    is EditGlobalSourceOracle.Effect.SetMoney -> money = effect.value
                    is EditGlobalSourceOracle.Effect.SetStage -> stage = effect.value
                    EditGlobalSourceOracle.Effect.Close -> attached = false
                    EditGlobalSourceOracle.Effect.AskClearInventory -> {
                        layers += "MsgBox"
                        pending = true
                    }
                    EditGlobalSourceOracle.Effect.ClearInventory -> clears++
                    is EditGlobalSourceOracle.Effect.Toast -> toasts += effect.text
                    else -> Unit
                }
            }
        }

        output += snapshot("create")
        scenario.events.forEach { event ->
            /**
             * `parts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val parts = event.split(':')
            when (parts[0]) {
                "ambition" -> flow.endEdit(EditGlobalSourceOracle.Field.AMBITION, parts[1].toInt())
                "money" -> flow.endEdit(EditGlobalSourceOracle.Field.MONEY, parts[1].toInt())
                "stage" -> flow.selectScene(parts[1].toInt())
                "apply" -> if (parts[1] == "2") apply(flow.button(0))
                "store" -> if (parts[2] == "2") apply(flow.button(2))
                "confirm" -> if (pending) apply(flow.clearInventoryAnswer(parts[1].toInt()))
            }
            output += snapshot(event)
        }
        return EditMutationTraceJson.array(output)
    }
}

/** EditRosterMutationScenario: edit roster mutation scenario 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
object EditRosterMutationScenario {
    /** run: 검증 실행에 필요한 상태를 구성한다. */
    fun run(scenario: EditMutationCase): String {
        var attached = true
        val flow = EditRosterFlow(
            listOf(EditRosterFlow.UnitRow(0, "U0", false), EditRosterFlow.UnitRow(7, "U7", false)),
            List(27) { "U$it" }
        )
        val layers = mutableListOf<String>()
        val toasts = mutableListOf<String>()
        val output = mutableListOf<String>()

        /** apply: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun apply(effects: List<EditRosterFlow.Effect>) {
            effects.forEach { effect ->
                when (effect) {
                    EditRosterFlow.Effect.OpenGlobalEditor -> layers += "EditLayer"
                    EditRosterFlow.Effect.OpenLearnUnitSkill -> layers += "LearnUnitSkillLayer"
                    EditRosterFlow.Effect.Close -> attached = false
                    is EditRosterFlow.Effect.Toast -> toasts += effect.text
                    else -> Unit
                }
            }
        }

        /** snapshot: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snapshot(step: String) = EditMutationTraceJson.snapshot(
            step,
            attached,
            layers,
            toasts,
            emptyList(),
            "\"joined\":${EditMutationTraceJson.array(flow.rows().filter { !it.leave }.map { it.id }.sorted().map(Int::toString))}"
        )

        output += snapshot("create")
        scenario.events.forEach { event ->
            /**
             * `parts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val parts = event.split(':')
            if (parts[2] == "2") apply(flow.button(parts[1].toInt()))
            output += snapshot(event)
        }
        return EditMutationTraceJson.array(output)
    }
}

/** EditAvatarMutationScenario: edit avatar mutation scenario 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
object EditAvatarMutationScenario {
    /** run: 검증 실행에 필요한 상태를 구성한다. */
    fun run(scenario: EditMutationCase): String {
        var attached = true
        var avatar: String? = null
        var loads = 0
        var page = -1
        val toasts = mutableListOf("선택하려면 최소한 하나의 모드를 설치해야 합니다!")
        val output = mutableListOf<String>()

        /** snapshot: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snapshot(step: String) = EditMutationTraceJson.snapshot(
            step,
            attached,
            emptyList(),
            toasts,
            emptyList(),
            "\"avatar\":${avatar ?: "null"},\"loads\":$loads,\"page\":$page"
        )

        output += snapshot("create")
        scenario.events.forEach { event ->
            val parts = event.split(':')
            if (parts.last() == "2" && parts[0] == "apply") {
                avatar = "[1,786437]"
                loads++
                attached = false
            }
            if (parts.last() == "2" && parts[0] == "next") page = 0
            if (parts.last() == "2" && parts[0] == "reset") {
                avatar = "[1,null]"
                loads++
                attached = false
            }
            output += snapshot(event)
        }
        return EditMutationTraceJson.array(output)
    }
}
