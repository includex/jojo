// Scenario
package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue

/** ScenarioStageCallConditionDispatcher: stage API의 조건·변수 질의를 스크립트 평가 결과로 변환한다. */
internal object ScenarioStageCallConditionDispatcher : ScenarioStageCallFamily {
    override fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): ScenarioStageCallDispatcher.Result? {
        val context = env.battleContext
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
