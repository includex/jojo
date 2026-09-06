// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

internal class ScenarioCallCoordinator(
    private val moduleName: String,
    private val functions: Map<String, RuntimeFunction>,
    private val campaign: CampaignState,
    private val stage: ScenarioStage,
    private val modalController: ScenarioModalController,
    private val dialogueCoordinator: ScenarioDialogueCoordinator,
    private val choiceCoordinator: ScenarioChoiceCoordinator,
    private val delayCoordinator: ScenarioDelayCoordinator,
    private val callStack: ScenarioCallStack,
    private val randomGenerator: ScenarioRandomGenerator,
    private val vars: MutableMap<Int, Any?>,
    private val gvars: MutableMap<Int, Any?>,
    private val pvars: MutableMap<Int, Any?>,
    private val globalVariables: MutableMap<String, Any?>,
    private val randomTrace: MutableList<ScenarioRandomTrace>,
    private val unhandledCalls: MutableMap<String, Int>,
    private val getBattleContext: () -> ScenarioBattleScriptContext,
    private val isExternalBattlePresentation: () -> Boolean,
    private val isStagePresentationSkipped: () -> Boolean,
    private val onEnd: () -> Unit,
    private val onSetState: (PlaybackState) -> Unit,
    private val resolveStageUnitReference: (Int, Int) -> ScenarioUnitReference?,
) {
    val expressionEnvironment: ScenarioExpressionEnvironment = ScenarioExpressionEnvironment(
        vars = vars,
        gvars = gvars,
        pvars = pvars,
        globalVariables = globalVariables,
        invokeCall = ::invokeCall,
    )


    fun eval(node: JsonValue, frame: Frame): Any? =
        ScenarioExpressionEvaluator.eval(node, frame, expressionEnvironment)


    fun evalBoolean(node: JsonValue, frame: Frame): Boolean =
        ScenarioExpressionEvaluator.evalBoolean(node, frame, expressionEnvironment)


    fun assign(target: JsonValue, value: Any?, frame: Frame) =
        ScenarioExpressionEvaluator.assign(target, value, frame, expressionEnvironment)


    fun evalArguments(args: JsonValue, frame: Frame): List<Any?> =
        ScenarioExpressionEvaluator.evalArguments(args, frame, expressionEnvironment)


    fun pushFunction(name: String, label: String? = null) =
        callStack.pushFunction(name, label, functions, moduleName)


    fun jumpToLabel(label: String) = callStack.jumpToLabel(label, functions)

    private fun unitReference(node: JsonValue, frame: Frame): ScenarioUnitReference? {
        val function = node.field("func")
        if (function.typeName() != "Attribute") return null
        return eval(function.field("value"), frame) as? ScenarioUnitReference
    }

    private fun headReference(node: JsonValue, frame: Frame): HeadReference? {
        val function = node.field("func")
        if (function.typeName() != "Attribute") return null
        return eval(function.field("value"), frame) as? HeadReference
    }

    private fun conditionEnvironment(): ScenarioConditionEnvironment = ScenarioConditionEnvironment(
        gvars = gvars,
        pvars = pvars,
        battleContext = getBattleContext(),
        stageUnitAttribute = stage::unitAttribute,
    )

    private fun tacticalEnvironment(): ScenarioTacticalEnvironment = ScenarioTacticalEnvironment(
        stage = stage,
        battleContext = getBattleContext(),
        externalBattlePresentation = isExternalBattlePresentation(),
        suspendFor = delayCoordinator::suspendFor,
        resolveStageUnitReference = resolveStageUnitReference,
        unitReference = ::unitReference,
        headReference = ::headReference,
    )

    private fun stageCallEnvironment(): ScenarioStageCallEnvironment = ScenarioStageCallEnvironment(
        moduleName = moduleName,
        stage = stage,
        campaign = campaign,
        battleContext = getBattleContext(),
        gvars = gvars,
        pvars = pvars,
        randomTrace = randomTrace,
        stopAfterRandomTraceCount = randomGenerator.stopAfterRandomTraceCount,
        stagePresentationSkipped = isStagePresentationSkipped(),
        externalBattlePresentation = isExternalBattlePresentation(),
        pendingAskResult = choiceCoordinator.pendingAskResult,
        suspendFor = delayCoordinator::suspendFor,
        suspendForBattleBackgroundLoad = delayCoordinator::suspendForBattleBackgroundLoad,
        suspendForInfo = modalController::suspendForInfo,
        suspendForAmbition = modalController::suspendForAmbition,
        suspendForMapInfo = modalController::suspendForMapInfo,
        suspendForSection = modalController::suspendForSection,
        suspendForWinCondition = modalController::suspendForWinCondition,
        nextModelRandom = randomGenerator::nextModelRandom,
        onEnd = onEnd,
        onSetState = onSetState,
        suspendForExternalFightCommand = delayCoordinator::suspendForExternalFightCommand,
        conditionEnvironment = ::conditionEnvironment,
    )


    fun invokeCall(node: JsonValue, frame: Frame): Any? {
        val path = node.field("func").expressionPath()
        val args = evalArguments(node.field("args"), frame)
        if (path != null && ScenarioFightDispatcher.dispatchFightCall(
                path,
                args,
                stage,
                moduleName,
                delayCoordinator::suspendForExternalFightCommand
            )
        ) {
            return null
        }
        val tactical = ScenarioTacticalActionDispatcher.dispatch(path ?: "", node, args, frame, tacticalEnvironment())
        if (tactical != null) return tactical.value
        val stageCall = ScenarioStageCallDispatcher.dispatch(path ?: "", node, args, frame, stageCallEnvironment())
        if (stageCall != null) return stageCall.value
        when (path) {
            "stage.say" -> {
                val sourceText = args.firstOrNull().asText()
                if (!sourceText.startsWith("&")) {
                    dialogueCoordinator.reset()
                    modalController.suspendForInfo(sourceText, ScenarioModalKind.INFO)
                    return null
                }
                dialogueCoordinator.startSay(sourceText)
                return null
            }

            "stage.talk" -> {
                dialogueCoordinator.startTalk(
                    primary = args.intAt(0),
                    fallback = args.intAt(1),
                    text = args.getOrNull(2).asText(),
                    activeCharacterIds = getBattleContext().activeCharacterIds,
                )
                return null
            }

            "stage.choice" -> {
                choiceCoordinator.startChoice(
                    choice = Choice(
                        args.firstOrNull().asText().lineSequence().map(String::trim).filter(String::isNotEmpty)
                            .toList(),
                        args.getOrNull(1)?.asInt()?.takeIf { it >= 0 },
                    ),
                    node = node,
                    frame = frame,
                    moduleName = moduleName,
                )
                return null
            }

            else -> {
                val builtin = ScenarioBuiltinCallDispatcher.dispatch(
                    path = path ?: "",
                    node = node,
                    args = args,
                    frame = frame,
                    env = ScenarioBuiltinCallEnvironment(
                        functions = functions,
                        unhandledCalls = unhandledCalls,
                        jumpToLabel = ::jumpToLabel,
                        pushFunction = ::pushFunction,
                        eval = ::eval,
                    ),
                )
                return builtin?.value ?: 0
            }
        }
    }
}
