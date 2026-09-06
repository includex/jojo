// Scenario
package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.domain.scenario.ScenarioCommand

/** ScenarioStageCallStateDispatcher: stage API의 시나리오 상태 변경 호출을 캠페인·무대 상태에 반영한다. */
internal object ScenarioStageCallStateDispatcher : ScenarioStageCallFamily {
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
            "stage.setEventName" -> {
                /**
                 * `text` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val text = args.firstOrNull().asText()
                stage.apply(ScenarioCommand.SetEventName(text))
                if (!env.stagePresentationSkipped && stage.battleDrawRequested) {
                    env.suspendForInfo(text, ScenarioModalKind.EVENT, 1f)
                }
                0
            }

            "stage.setStageName" -> {
                /**
                 * `text` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val text = args.firstOrNull().asText()
                stage.setStageName(text)
                if (!env.stagePresentationSkipped && stage.battleDrawRequested) {
                    env.suspendForInfo(text, ScenarioModalKind.EVENT, 1f)
                }
                0
            }

            "stage.clsUnit" -> { stage.clearUnits(); 0 }
            "stage.setMenuVisible" -> { stage.setMenuVisible(args.firstOrNull().asBooleanValue()); 0 }
            "stage.menuVisible" -> stage.menuVisible
            "stage.sceneIndex" -> stage.sceneIndex
            "stage.incSceneIdx" -> { stage.incrementSceneIndex(); 0 }
            "stage.addAmbition" -> {
                if (env.moduleName.startsWith("R_")) env.suspendForAmbition(args.firstOrNull().asInt())
                else stage.addAmbition(args.firstOrNull().asInt())
                0
            }

            "model.setAmbition" -> { stage.addAmbition(args.firstOrNull().asInt() - stage.ambition); 0 }
            "model.ambition" -> stage.ambition
            "model.addMoney" -> { env.campaign.addMoney(args.firstOrNull().asInt()); 0 }
            "model.money" -> env.campaign.money
            "model.setFace" -> { stage.setFace(args.firstOrNull().asInt()); 0 }
            "model.random" -> env.nextModelRandom().also { random ->
                env.randomTrace += ScenarioRandomTrace(
                    module = env.moduleName,
                    function = frame.sourceFunction,
                    line = node.get("location")?.getInt("line", -1)?.takeIf { it > 0 }
                        ?: error("${env.moduleName} ${frame.function.name} random source line is missing"),
                    value = random,
                )
                if (env.stopAfterRandomTraceCount?.let { env.randomTrace.size >= it } == true) env.onEnd()
            }

            "model.initLocalVar" -> { stage.resetLocalVariables(); 0 }
            "model.unitJoin" -> { stage.joinUnit(args.firstOrNull().asInt()); 0 }
            "stage.setWinCondition" -> { stage.setWinCondition(args.firstOrNull().asText()); 0 }
            "stage.showWinCondition" -> { env.suspendForWinCondition(args.firstOrNull().asText()); 0 }
            "stage.bottomTxt" -> {
                /**
                 * `text` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val text = args.firstOrNull().asText()
                if (env.moduleName.startsWith("R_")) {
                    env.suspendForMapInfo(
                        text,
                        args.getOrNull(1).asBooleanValue(),
                        args.getOrNull(2).asBooleanValue(),
                        args.getOrNull(3).asBooleanValue(),
                    )
                } else stage.setBottomText(text)
                0
            }

            else -> return null
        }
        return ScenarioStageCallDispatcher.Result(value)
    }
}
