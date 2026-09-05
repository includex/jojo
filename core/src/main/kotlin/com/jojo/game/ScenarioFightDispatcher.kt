package com.jojo.game

/**
 * Dispatches authored FightLayer scripting commands to ScenarioStage and coordinates
 * presentation suspension for live Fight animations.
 */
internal object ScenarioFightDispatcher {

    fun startFight(
        stage: ScenarioStage,
        args: List<Any?>,
        suspendExternal: () -> Unit,
    ): ScenarioInterpreter.FightReference {
        val fightId = stage.startFight(args.intAt(0), args.intAt(1), args.intAt(2))
        suspendExternal()
        return ScenarioInterpreter.FightReference(fightId)
    }

    fun dispatchFightCall(
        path: String,
        args: List<Any?>,
        stage: ScenarioStage,
        moduleName: String,
        suspendExternal: () -> Unit,
    ): Boolean {
        if (!path.startsWith("fight.")) return false
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
