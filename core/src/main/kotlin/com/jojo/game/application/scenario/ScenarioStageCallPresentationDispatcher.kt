package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.ScenarioStageCallDispatcher
import com.jojo.game.ScenarioStageCallEnvironment
import com.jojo.game.asBooleanValue
import com.jojo.game.asInt
import com.jojo.game.asList
import com.jojo.game.asText
import com.jojo.game.intAt
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.domain.scenario.ScenarioScriptPresentationRequest

/** Routes stage presentation, progression, and reward commands. */
internal object ScenarioStageCallPresentationDispatcher : com.jojo.game.ScenarioStageCallFamily {
    private const val infoControlGlobal = 4071

    override fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): ScenarioStageCallDispatcher.Result? {
        val stage = env.stage
        val value: Any = when (path) {
            "stage.sectionName" -> {
                stage.setSection(args.intAt(0), args.getOrNull(1).asText())
                if (env.moduleName.startsWith("R_")) env.suspendForSection(args.intAt(0), args.getOrNull(1).asText())
                0
            }

            "stage.showHead" -> {
                stage.showHead(args.intAt(0), args.intAt(1), args.intAt(2)).let { duration ->
                    if (duration > 0f) env.suspendFor(duration)
                }
                0
            }

            "stage.delay" -> { env.suspendFor(args.firstOrNull().asInt() * 0.1f); 0 }
            "stage.draw" -> { stage.drawBattle(); 0 }
            "stage.info" -> {
                val text = args.firstOrNull().asText()
                val delay = (args.getOrNull(1)?.asInt() ?: 1).coerceAtLeast(0).toFloat()
                val infoControl = env.gvars.remove(infoControlGlobal).asInt()
                if (env.stagePresentationSkipped) Unit
                else if (infoControl != 0) stage.controlledInfo(infoControl, text)
                else env.suspendForInfo(text, ScenarioModalKind.INFO, delay)
                0
            }

            "stage.infoTransfer" -> { stage.infoTransfer(args.intAt(0), args.getOrNull(1).asText(), env.gvars[4054].asInt()); 0 }
            "stage.ending" -> { stage.ending(args.intAt(0)); 0 }
            "stage.reward" -> {
                stage.reward(
                    bonusMoney = args.getOrNull(0).asInt(),
                    items = args.getOrNull(1).asList(),
                    end = args.getOrNull(2).asBooleanValue(),
                )
                env.onSetState(PlaybackState.MODAL)
                0
            }

            "stage.lose" -> { stage.lose(); 0 }
            "stage.end" -> { stage.endBattle(); 0 }
            "stage.jumpScene" -> { stage.jumpScene(args.intAt(0)); env.onEnd(); 0 }
            "stage.itemVars" -> { stage.addItemVariables(args.getOrNull(0).asList(), args.getOrNull(1).asList()); 0 }
            "stage.getItem" -> {
                val itemId = args.intAt(0)
                val supplied = args.getOrNull(1).asInt()
                val addToInventory = args.getOrNull(2)?.asBooleanValue() ?: true
                val unitSelector = args.getOrNull(3)?.asInt() ?: 0
                val action = args.getOrNull(4)?.asInt() ?: 5
                stage.getItem(itemId, supplied, addToInventory).let { message ->
                    if (env.moduleName.startsWith("R_")) env.suspendForInfo(message, ScenarioModalKind.INFO, 1f)
                }
                if (env.moduleName.startsWith("S_") && env.externalBattlePresentation && action > 0 && unitSelector >= 0) {
                    stage.requestScriptPresentation(
                        ScenarioScriptPresentationRequest.GetItem(
                            itemId = itemId,
                            suppliedCountOrLevel = supplied,
                            addToInventory = addToInventory,
                            unitSelector = unitSelector,
                            action = action,
                            completionMessage = stage.battleItemCompletionMessage(itemId),
                        ),
                    )
                    env.suspendFor(Float.MAX_VALUE)
                }
                0
            }

            else -> return null
        }
        return ScenarioStageCallDispatcher.Result(value)
    }
}
