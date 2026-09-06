// Scenario
package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.domain.scenario.ScenarioCommand

/** ScenarioStageCallBattleDispatcher: stage API의 전투 제어 호출을 전장 준비·결과 처리 명령으로 해석한다. */
internal object ScenarioStageCallBattleDispatcher : ScenarioStageCallFamily {
    /**
     * `loadBackgroundJumpOffsetGlobal` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private const val loadBackgroundJumpOffsetGlobal = 4051

    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): ScenarioStageCallDispatcher.Result? {
        /**
         * `stage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val stage = env.stage
        /**
         * `value` (Any): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Any = when (path) {
            "stage.loadBg" -> {
                if (env.moduleName.startsWith("S_")) {
                    /**
                     * `mapIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    var mapIndex = args.intAt(0)
                    /**
                     * `jumpOffset` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val jumpOffset = env.gvars[loadBackgroundJumpOffsetGlobal].asInt()
                    if (mapIndex < 0 || jumpOffset != 0) {
                        env.gvars[loadBackgroundJumpOffsetGlobal] = 0
                        mapIndex += 100 * jumpOffset
                    }
                    if (env.externalBattlePresentation) env.suspendForBattleBackgroundLoad(mapIndex)
                    else stage.selectBattleMap(mapIndex)
                } else stage.apply(ScenarioCommand.LoadBackground(args.intAt(0), args.intAt(1)))
                0
            }

            "stage.setGlobalData" -> {
                stage.setBattleGlobalData(
                    args.firstOrNull().asInt(), args.getOrNull(1).asInt(), args.getOrNull(2).asInt(),
                    args.getOrNull(3).asInt(), args.getOrNull(4).asInt(), args.getOrNull(5).asInt(),
                )
                0
            }

            "stage.initFight" -> { stage.initFight(); 0 }
            "stage.startOper" -> { stage.startOperation(); 0 }
            "stage.setMaxRound" -> { stage.setMaxRound(args.intAt(0), env.battleContext.enabledFeatures); 0 }
            "stage.startFight" -> ScenarioFightDispatcher.startFight(stage, args, env.suspendForExternalFightCommand)
            "stage.bgSound" -> { stage.setBackgroundSound(args.firstOrNull().asInt()); 0 }
            "stage.effectSound" -> { stage.effectSound(args.intAt(0), args.getOrNull(1)?.asInt() ?: 1); 0 }
            "stage.setJoinBattle" -> {
                stage.setJoinBattle(args.intAt(0), args.intAt(1), args.getOrNull(2).asList(), args.getOrNull(3).asList()); 0
            }

            "stage.setBattlePos" -> { stage.setBattlePositions(args.firstOrNull().asList()); 0 }
            "stage.setJoinEquip" -> {
                stage.setJoinEquip(
                    args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3), args.intAt(4), args.intAt(5),
                )
                0
            }

            else -> return null
        }
        return ScenarioStageCallDispatcher.Result(value)
    }
}
