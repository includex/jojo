package com.jojo.game

import com.badlogic.gdx.utils.JsonValue

internal data class ScenarioStageCallEnvironment(
    val moduleName: String,
    val stage: ScenarioStage,
    val campaign: CampaignState,
    val battleContext: ScenarioInterpreter.BattleScriptContext,
    val gvars: MutableMap<Int, Any?>,
    val pvars: MutableMap<Int, Any?>,
    val randomTrace: MutableList<ScenarioInterpreter.RandomTrace>,
    val stopAfterRandomTraceCount: Int?,
    val stagePresentationSkipped: Boolean,
    val externalBattlePresentation: Boolean,
    val pendingAskResult: Int?,
    val suspendFor: (Float) -> Unit,
    val suspendForBattleBackgroundLoad: (Int) -> Unit,
    val suspendForInfo: (String, ScenarioInterpreter.ModalKind, Float) -> Unit,
    val suspendForAmbition: (Int) -> Unit,
    val suspendForMapInfo: (String, Boolean, Boolean, Boolean) -> Unit,
    val suspendForSection: (Int, String) -> Unit,
    val suspendForWinCondition: (String) -> Unit,
    val nextModelRandom: () -> Int,
    val onEnd: () -> Unit,
    val onSetState: (PlaybackState) -> Unit,
    val suspendForExternalFightCommand: () -> Unit,
    val conditionEnvironment: () -> ScenarioConditionEnvironment,
)

internal object ScenarioStageCallDispatcher {
    class Result(val value: Any?)

    private const val LOAD_BG_JUMP_OFFSET_GLOBAL = 4051
    private const val INFO_CTRL_GLOBAL = 4071

    fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): Result? {
        val stage = env.stage
        val gvars = env.gvars
        val campaign = env.campaign
        val battleContext = env.battleContext
        val moduleName = env.moduleName

        val value: Any? = when (path) {
            "stage.loadBg" -> {
                if (moduleName.startsWith("S_")) {
                    var mapIndex = args.intAt(0)
                    val jumpOffset = gvars[LOAD_BG_JUMP_OFFSET_GLOBAL].asInt()
                    if (mapIndex < 0 || jumpOffset != 0) {
                        gvars[LOAD_BG_JUMP_OFFSET_GLOBAL] = 0
                        mapIndex += 100 * jumpOffset
                    }
                    if (env.externalBattlePresentation) env.suspendForBattleBackgroundLoad(mapIndex)
                    else stage.selectBattleMap(mapIndex)
                } else stage.apply(ScenarioCommand.LoadBackground(args.intAt(0), args.intAt(1)))
                0
            }
            "stage.setEventName" -> {
                val text = args.firstOrNull().asText()
                stage.apply(ScenarioCommand.SetEventName(text))
                if (!env.stagePresentationSkipped && stage.battleDrawRequested) env.suspendForInfo(text, ScenarioInterpreter.ModalKind.EVENT, 1f)
                0
            }
            "stage.setStageName" -> {
                val text = args.firstOrNull().asText()
                stage.setStageName(text)
                if (!env.stagePresentationSkipped && stage.battleDrawRequested) env.suspendForInfo(text, ScenarioInterpreter.ModalKind.EVENT, 1f)
                0
            }
            "stage.clsUnit" -> { stage.clearUnits(); 0 }
            "stage.setMenuVisible" -> { stage.setMenuVisible(args.firstOrNull().asBooleanValue()); 0 }
            "stage.menuVisible" -> stage.menuVisible
            "stage.sceneIndex" -> stage.sceneIndex
            "stage.incSceneIdx" -> { stage.incrementSceneIndex(); 0 }
            "stage.addAmbition" -> {
                if (moduleName.startsWith("R_")) env.suspendForAmbition(args.firstOrNull().asInt())
                else stage.addAmbition(args.firstOrNull().asInt())
                0
            }
            "model.setAmbition" -> { stage.addAmbition(args.firstOrNull().asInt() - stage.ambition); 0 }
            "model.ambition" -> stage.ambition
            "model.addMoney" -> { campaign.addMoney(args.firstOrNull().asInt()); 0 }
            "model.money" -> campaign.money
            "model.setFace" -> { stage.setFace(args.firstOrNull().asInt()); 0 }
            "model.random" -> env.nextModelRandom().also { rnd ->
                env.randomTrace += ScenarioInterpreter.RandomTrace(
                    module = moduleName,
                    function = frame.sourceFunction,
                    line = node.get("location")?.getInt("line", -1)?.takeIf { it > 0 }
                        ?: error("$moduleName ${frame.function.name} random source line is missing"),
                    value = rnd,
                )
                if (env.stopAfterRandomTraceCount?.let { env.randomTrace.size >= it } == true) env.onEnd()
            }
            "model.initLocalVar" -> { stage.resetLocalVariables(); 0 }
            "model.unitJoin" -> { stage.joinUnit(args.firstOrNull().asInt()); 0 }
            "stage.setWinCondition" -> { stage.setWinCondition(args.firstOrNull().asText()); 0 }
            "stage.showWinCondition" -> {
                env.suspendForWinCondition(args.firstOrNull().asText())
                0
            }
            "stage.bottomTxt" -> {
                val text = args.firstOrNull().asText()
                if (moduleName.startsWith("R_")) {
                    env.suspendForMapInfo(
                        text,
                        args.getOrNull(1).asBooleanValue(),
                        args.getOrNull(2).asBooleanValue(),
                        args.getOrNull(3).asBooleanValue(),
                    )
                } else stage.setBottomText(text)
                0
            }
            "stage.setGlobalData" -> {
                stage.setBattleGlobalData(
                    args.firstOrNull().asInt(),
                    args.getOrNull(1).asInt(),
                    args.getOrNull(2).asInt(),
                    args.getOrNull(3).asInt(),
                    args.getOrNull(4).asInt(),
                    args.getOrNull(5).asInt(),
                )
                0
            }
            "stage.initFight" -> { stage.initFight(); 0 }
            "stage.startOper" -> { stage.startOperation(); 0 }
            "stage.setMaxRound" -> { stage.setMaxRound(args.intAt(0), battleContext.enabledFeatures); 0 }
            "stage.startFight" -> ScenarioFightDispatcher.startFight(stage, args, env.suspendForExternalFightCommand)
            "stage.bgSound" -> { stage.setBackgroundSound(args.firstOrNull().asInt()); 0 }
            "stage.sectionName" -> {
                stage.setSection(args.intAt(0), args.getOrNull(1).asText())
                if (moduleName.startsWith("R_")) env.suspendForSection(args.intAt(0), args.getOrNull(1).asText())
                0
            }
            "stage.showHead" -> {
                stage.showHead(args.intAt(0), args.intAt(1), args.intAt(2)).let { duration ->
                    if (duration > 0f) env.suspendFor(duration)
                }
                0
            }
            "stage.effectSound" -> { stage.effectSound(args.intAt(0), args.getOrNull(1)?.asInt() ?: 1); 0 }
            "stage.delay" -> { env.suspendFor(args.firstOrNull().asInt() * 0.1f); 0 }
            "stage.draw" -> { stage.drawBattle(); 0 }
            "stage.info" -> {
                val text = args.firstOrNull().asText()
                val delay = (args.getOrNull(1)?.asInt() ?: 1).coerceAtLeast(0).toFloat()
                val infoControl = gvars.remove(INFO_CTRL_GLOBAL).asInt()
                if (env.stagePresentationSkipped) Unit
                else if (infoControl != 0) stage.controlledInfo(infoControl, text)
                else env.suspendForInfo(text, ScenarioInterpreter.ModalKind.INFO, delay)
                0
            }
            "stage.infoTransfer" -> { stage.infoTransfer(args.intAt(0), args.getOrNull(1).asText(), gvars[4054].asInt()); 0 }
            "stage.setJoinBattle" -> { stage.setJoinBattle(args.intAt(0), args.intAt(1), args.getOrNull(2).asList(), args.getOrNull(3).asList()); 0 }
            "stage.setBattlePos" -> { stage.setBattlePositions(args.firstOrNull().asList()); 0 }
            "stage.setJoinEquip" -> { stage.setJoinEquip(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3), args.intAt(4), args.intAt(5)); 0 }
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
            "stage.jumpScene" -> {
                stage.jumpScene(args.intAt(0))
                env.onEnd()
                0
            }
            "stage.itemVars" -> { stage.addItemVariables(args.getOrNull(0).asList(), args.getOrNull(1).asList()); 0 }
            "stage.getItem" -> {
                val itemId = args.intAt(0)
                val supplied = args.getOrNull(1).asInt()
                val addToInventory = args.getOrNull(2)?.asBooleanValue() ?: true
                val unitSelector = args.getOrNull(3)?.asInt() ?: 0
                val action = args.getOrNull(4)?.asInt() ?: 5
                stage.getItem(itemId, supplied, addToInventory).let { message ->
                    if (moduleName.startsWith("R_")) env.suspendForInfo(message, ScenarioInterpreter.ModalKind.INFO, 1f)
                }
                if (moduleName.startsWith("S_") && env.externalBattlePresentation && action > 0 && unitSelector >= 0) {
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
            "stage.varOper" -> { ScenarioConditionEvaluator.applyStageVarOperation(args, env.conditionEnvironment()); 0 }
            "stage.varTest" -> ScenarioConditionEvaluator.testStageVariables(args, env.conditionEnvironment())
            "stage.video" -> 0
            "stage.ask" -> env.pendingAskResult ?: 0
            "stage.round" -> battleContext.round
            "stage.curCamp" -> battleContext.camp
            "stage.maxRound" -> battleContext.maxRound
            "stage.unitClickTest" -> battleContext.clickedCharacterId == args.firstOrNull().asInt()
            "stage.battleTest" -> battleContext.clickedCharacterId != null
            "stage.winTest" -> battleContext.enemyDefeated
            "stage.loseTest", "stage.isLose" -> battleContext.playerDefeated
            "stage.isNear" -> ScenarioConditionEvaluator.isNear(args, env.conditionEnvironment())
            "stage.isInPos" -> ScenarioConditionEvaluator.isInPosition(args, env.conditionEnvironment())
            "stage.isInRect" -> ScenarioConditionEvaluator.isInRectangle(args, env.conditionEnvironment())
            "stage.totalRectUnit" -> ScenarioConditionEvaluator.totalRectangleUnits(args, env.conditionEnvironment())
            "stage.totalUnit" -> ScenarioConditionEvaluator.totalUnits(args.firstOrNull().asInt(), env.conditionEnvironment())
            "stage.unitStateTest" -> ScenarioConditionEvaluator.unitStateTest(args, env.conditionEnvironment())
            else -> return null
        }
        return Result(value)
    }
}
