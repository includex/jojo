// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.*

/** ScenarioFightDispatcher: 시나리오 스크립트의 전투 시작·종료 호출을 전장 흐름과 캠페인 상태에 연결한다. */
internal object ScenarioFightDispatcher {

    /**
     * `startFight`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startFight(
        stage: ScenarioStage,
        args: List<Any?>,
        suspendExternal: () -> Unit,
    ): ScenarioFightReference {
        /**
         * `fightId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val fightId = stage.startFight(args.intAt(0), args.intAt(1), args.intAt(2))
        suspendExternal()
        return ScenarioFightReference(fightId)
    }

    /**
     * `dispatchFightCall`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispatchFightCall(
        path: String,
        args: List<Any?>,
        stage: ScenarioStage,
        moduleName: String,
        suspendExternal: () -> Unit,
    ): Boolean {
        if (!path.startsWith("fight.")) return false
        /**
         * `fightId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val fightId = requireNotNull(stage.activeFightId) {
            "$moduleName invoked a fight command without an active stage.startFight()"
        }
        when (path) {
            "fight.showUnit" -> {
                stage.enqueueFightCommand(
                    ScenarioFightCommand.ShowUnit(
                        fightId = fightId,
                        mine = args.firstOrNull().asBooleanValue(),
                        text = args.getOrNull(1).asText(),
                        entryAction = args.intAt(2),
                    ),
                )
                suspendExternal()
            }

            "fight.showStart" -> {
                stage.enqueueFightCommand(ScenarioFightCommand.ShowStart(fightId))
                suspendExternal()
            }

            "fight.setAction" -> {
                stage.enqueueFightCommand(
                    ScenarioFightCommand.SetAction(fightId, args.firstOrNull().asBooleanValue(), args.intAt(1)),
                )
                suspendExternal()
            }

            "fight.say" -> {
                stage.enqueueFightCommand(
                    ScenarioFightCommand.Say(
                        fightId = fightId,
                        mine = args.firstOrNull().asBooleanValue(),
                        text = args.getOrNull(1).asText(),
                        flag = args.getOrNull(2).asBooleanValue(),
                    ),
                )
                suspendExternal()
            }

            "fight.attack2" -> {
                stage.enqueueFightCommand(
                    ScenarioFightCommand.Attack2(
                        fightId = fightId,
                        mine = args.firstOrNull().asBooleanValue(),
                        style = args.intAt(1),
                        defended = args.getOrNull(2).asBooleanValue(),
                    ),
                )
                suspendExternal()
            }

            "fight.attack1" -> {
                stage.enqueueFightCommand(
                    ScenarioFightCommand.Attack1(
                        fightId = fightId,
                        mine = args.firstOrNull().asBooleanValue(),
                        style = args.intAt(1),
                        critical = args.getOrNull(2).asBooleanValue(),
                    ),
                )
                suspendExternal()
            }

            "fight.death" -> {
                stage.enqueueFightCommand(
                    ScenarioFightCommand.Death(fightId, enemy = args.firstOrNull().asBooleanValue()),
                )
                suspendExternal()
            }

            "fight.end" -> {
                stage.enqueueFightCommand(ScenarioFightCommand.End(fightId))
            }

            else -> return false
        }
        return true
    }
}
