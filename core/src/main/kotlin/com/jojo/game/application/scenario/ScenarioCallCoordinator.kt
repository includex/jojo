// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

/**
 * `ScenarioCallCoordinator` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal class ScenarioCallCoordinator(
    /**
     * `moduleName` (String,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val moduleName: String,
    /**
     * `functions` (Map<String, RuntimeFunction>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val functions: Map<String, RuntimeFunction>,
    /**
     * `campaign` (CampaignState,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val campaign: CampaignState,
    /**
     * `stage` (ScenarioStage,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val stage: ScenarioStage,
    /**
     * `modalController` (ScenarioModalController,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val modalController: ScenarioModalController,
    /**
     * `dialogueCoordinator` (ScenarioDialogueCoordinator,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val dialogueCoordinator: ScenarioDialogueCoordinator,
    /**
     * `choiceCoordinator` (ScenarioChoiceCoordinator,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val choiceCoordinator: ScenarioChoiceCoordinator,
    /**
     * `delayCoordinator` (ScenarioDelayCoordinator,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val delayCoordinator: ScenarioDelayCoordinator,
    /**
     * `callStack` (ScenarioCallStack,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val callStack: ScenarioCallStack,
    /**
     * `randomGenerator` (ScenarioRandomGenerator,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val randomGenerator: ScenarioRandomGenerator,
    /**
     * `vars` (MutableMap<Int, Any?>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val vars: MutableMap<Int, Any?>,
    /**
     * `gvars` (MutableMap<Int, Any?>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val gvars: MutableMap<Int, Any?>,
    /**
     * `pvars` (MutableMap<Int, Any?>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val pvars: MutableMap<Int, Any?>,
    /**
     * `globalVariables` (MutableMap<String, Any?>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val globalVariables: MutableMap<String, Any?>,
    /**
     * `randomTrace` (MutableList<ScenarioRandomTrace>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val randomTrace: MutableList<ScenarioRandomTrace>,
    /**
     * `unhandledCalls` (MutableMap<String, Int>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val unhandledCalls: MutableMap<String, Int>,
    /**
     * `getBattleContext` (() -> ScenarioBattleScriptContext,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val getBattleContext: () -> ScenarioBattleScriptContext,
    /**
     * `isExternalBattlePresentation` (() -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val isExternalBattlePresentation: () -> Boolean,
    /**
     * `isStagePresentationSkipped` (() -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val isStagePresentationSkipped: () -> Boolean,
    /**
     * `onEnd` (() -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onEnd: () -> Unit,
    /**
     * `onSetState` ((PlaybackState) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onSetState: (PlaybackState) -> Unit,
    /**
     * `resolveStageUnitReference` ((Int, Int) -> ScenarioUnitReference?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val resolveStageUnitReference: (Int, Int) -> ScenarioUnitReference?,
) {
    /**
     * `expressionEnvironment` (ScenarioExpressionEnvironment): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val expressionEnvironment: ScenarioExpressionEnvironment = ScenarioExpressionEnvironment(
        vars = vars,
        gvars = gvars,
        pvars = pvars,
        globalVariables = globalVariables,
        invokeCall = ::invokeCall,
    )


    /**
     * `eval`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun eval(node: JsonValue, frame: Frame): Any? =
        ScenarioExpressionEvaluator.eval(node, frame, expressionEnvironment)


    /**
     * `evalBoolean`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun evalBoolean(node: JsonValue, frame: Frame): Boolean =
        ScenarioExpressionEvaluator.evalBoolean(node, frame, expressionEnvironment)


    /**
     * `assign`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun assign(target: JsonValue, value: Any?, frame: Frame) =
        ScenarioExpressionEvaluator.assign(target, value, frame, expressionEnvironment)


    /**
     * `evalArguments`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun evalArguments(args: JsonValue, frame: Frame): List<Any?> =
        ScenarioExpressionEvaluator.evalArguments(args, frame, expressionEnvironment)


    /**
     * `pushFunction`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun pushFunction(name: String, label: String? = null) =
        callStack.pushFunction(name, label, functions, moduleName)


    /**
     * `jumpToLabel`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun jumpToLabel(label: String) = callStack.jumpToLabel(label, functions)

    /**
     * `unitReference`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun unitReference(node: JsonValue, frame: Frame): ScenarioUnitReference? {
        val function = node.field("func")
        if (function.typeName() != "Attribute") return null
        return eval(function.field("value"), frame) as? ScenarioUnitReference
    }

    /**
     * `headReference`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun headReference(node: JsonValue, frame: Frame): HeadReference? {
        val function = node.field("func")
        if (function.typeName() != "Attribute") return null
        return eval(function.field("value"), frame) as? HeadReference
    }

    /**
     * `conditionEnvironment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun conditionEnvironment(): ScenarioConditionEnvironment = ScenarioConditionEnvironment(
        gvars = gvars,
        pvars = pvars,
        battleContext = getBattleContext(),
        stageUnitAttribute = stage::unitAttribute,
    )

    /**
     * `tacticalEnvironment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun tacticalEnvironment(): ScenarioTacticalEnvironment = ScenarioTacticalEnvironment(
        stage = stage,
        battleContext = getBattleContext(),
        externalBattlePresentation = isExternalBattlePresentation(),
        suspendFor = delayCoordinator::suspendFor,
        resolveStageUnitReference = resolveStageUnitReference,
        unitReference = ::unitReference,
        headReference = ::headReference,
    )

    /**
     * `stageCallEnvironment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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


    /**
     * `invokeCall`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
