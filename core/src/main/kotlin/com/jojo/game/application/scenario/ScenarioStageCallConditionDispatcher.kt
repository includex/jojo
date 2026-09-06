// Scenario
package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue

/** ScenarioStageCallConditionDispatcher: stage API의 조건·변수 질의를 스크립트 평가 결과로 변환한다. */
internal object ScenarioStageCallConditionDispatcher : ScenarioStageCallFamily {
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
         * `context` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val context = env.battleContext
        /**
         * `value` (Any): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Any = when (path) {
            "stage.varOper" -> { ScenarioConditionEvaluator.applyStageVarOperation(args, env.conditionEnvironment()); 0 }
            "stage.varTest" -> ScenarioConditionEvaluator.testStageVariables(args, env.conditionEnvironment())
            "stage.video" -> 0
            "stage.ask" -> env.pendingAskResult ?: 0
            "stage.round" -> context.round
            "stage.curCamp" -> context.camp
            "stage.maxRound" -> context.maxRound
            "stage.unitClickTest" -> context.clickedCharacterId == args.firstOrNull().asInt()
            "stage.battleTest" -> context.clickedCharacterId != null
            "stage.winTest" -> context.enemyDefeated
            "stage.loseTest", "stage.isLose" -> context.playerDefeated
            "stage.isNear" -> ScenarioConditionEvaluator.isNear(args, env.conditionEnvironment())
            "stage.isInPos" -> ScenarioConditionEvaluator.isInPosition(args, env.conditionEnvironment())
            "stage.isInRect" -> ScenarioConditionEvaluator.isInRectangle(args, env.conditionEnvironment())
            "stage.totalRectUnit" -> ScenarioConditionEvaluator.totalRectangleUnits(args, env.conditionEnvironment())
            "stage.totalUnit" -> ScenarioConditionEvaluator.totalUnits(args.firstOrNull().asInt(), env.conditionEnvironment())
            "stage.unitStateTest" -> ScenarioConditionEvaluator.unitStateTest(args, env.conditionEnvironment())
            else -> return null
        }
        return ScenarioStageCallDispatcher.Result(value)
    }
}
