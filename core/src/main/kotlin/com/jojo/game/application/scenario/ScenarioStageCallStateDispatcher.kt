package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.ScenarioStageCallDispatcher
import com.jojo.game.ScenarioStageCallEnvironment
import com.jojo.game.asBooleanValue
import com.jojo.game.asInt
import com.jojo.game.asText
import com.jojo.game.domain.scenario.ScenarioCommand

/** Routes non-battle stage and model state commands. */
internal object ScenarioStageCallStateDispatcher : com.jojo.game.ScenarioStageCallFamily {
    override fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): ScenarioStageCallDispatcher.Result? {
        val stage = env.stage
        val value: Any = when (path) {
            "stage.setEventName" -> {
                val text = args.firstOrNull().asText()
                stage.apply(ScenarioCommand.SetEventName(text))
                if (!env.stagePresentationSkipped && stage.battleDrawRequested) {
                    env.suspendForInfo(text, ScenarioModalKind.EVENT, 1f)
                }
                0
            }

            "stage.setStageName" -> {
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
