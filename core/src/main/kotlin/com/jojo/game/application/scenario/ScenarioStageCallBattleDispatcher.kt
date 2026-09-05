package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.ScenarioStageCallDispatcher
import com.jojo.game.ScenarioStageCallEnvironment
import com.jojo.game.asInt
import com.jojo.game.asList
import com.jojo.game.intAt
import com.jojo.game.domain.scenario.ScenarioCommand

/** Routes battle setup, operation, and sound commands. */
internal object ScenarioStageCallBattleDispatcher : com.jojo.game.ScenarioStageCallFamily {
    private const val loadBackgroundJumpOffsetGlobal = 4051

    override fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): ScenarioStageCallDispatcher.Result? {
        val stage = env.stage
        val value: Any = when (path) {
            "stage.loadBg" -> {
                if (env.moduleName.startsWith("S_")) {
                    var mapIndex = args.intAt(0)
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
