// Verification
package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.jojo.game.infrastructure.data.ScenarioCatalog
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.RuntimeScreenProbe
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState
import java.nio.file.Files
import java.nio.file.Path

/** ScenarioTraceRuntimeObserver: 결정적인 시나리오 선택과 추적 파일을 담당하는 검증 전용 실행기이다. core는 불변 [ScenarioRuntimeProbe]만 발행하고, 입력은 설치된 운영 입력 처리기로 전달한다. */
class ScenarioTraceRuntimeObserver(private val plan: ScenarioTracePlan) : RuntimeScreenObserver {
    /** choiceStep: 선택 단계 번호를 담는다. */
    private var choiceStep = 0
    /** finished: 검증 흐름 종료 여부를 담는다. */
    private var finished = false

    /** keepsScenarioOpen: 검증 시나리오 식별자를 담는다. */
    override val keepsScenarioOpen: Boolean = true

    /** update: 현재 검증 상태를 입력에 맞게 갱신한다. */
    override fun update(delta: Float, screen: RuntimeScreenProbe) {
        if (finished || screen !is ScenarioRuntimeProbe) return
        when (screen.playback) {
            PlaybackState.CHOICE -> choose(screen)
            PlaybackState.COMPLETE -> finish(screen)
            else -> pressEnter()
        }
        if (plan.stopAfterRandomTraceCount?.let { screen.randomTrace.size >= it } == true) finish(screen)
    }

    /** choose: 검증 입력 선택을 적용해 다음 상태로 진행한다. */
    private fun choose(screen: ScenarioRuntimeProbe) {
        if (choiceStep >= plan.choices.size) {
            check(plan.allowPendingChoiceAfterScript || (plan.choices.isEmpty() && plan.randomTracePath != null)) {
                "${screen.module} reached an unconsumed choice"
            }
            finish(screen)
            return
        }
        val expected = plan.choices[choiceStep]
        require(expected in screen.options.indices) {
            "${screen.module} choice step $choiceStep selected $expected, options=${screen.options.size}"
        }
        if (screen.selectedChoice != expected) {
            pressEnter(if (expected > screen.selectedChoice) Input.Keys.DOWN else Input.Keys.UP)
        } else {
            pressEnter(Input.Keys.ENTER)
            choiceStep++
        }
    }

    /** finish: 검증 흐름을 종료하고 사용한 리소스를 정리한다. */
    private fun finish(screen: ScenarioRuntimeProbe) {
        if (finished) return
        check(choiceStep == plan.choices.size || plan.allowPendingChoiceAfterScript) {
            "${screen.module} completed before scripted choice $choiceStep"
        }
        plan.choiceTracePath?.let { ScenarioTraceJson.writeChoices(it, screen) }
        plan.randomTracePath?.let { ScenarioTraceJson.writeRandom(it, screen) }
        Gdx.app.log("JojoGame", "VERIFY_SCENARIO_TRACE_OK: ${screen.module} choices=$choiceStep draws=${screen.randomDrawCount}")
        finished = true
        Gdx.app.exit()
    }

    /** pressEnter: 검증 입력 선택을 적용해 다음 상태로 진행한다. */
    private fun pressEnter(key: Int = Input.Keys.ENTER) {
        checkNotNull(Gdx.input.inputProcessor) { "scenario trace has no production input processor" }.keyDown(key)
    }
}

/** ScenarioTracePlan: 검증 추적 데이터와 증거를 표현하는 타입이다. */
data class ScenarioTracePlan(
    /** choices: 검증 대상 목록을 담는다. */
    val choices: List<Int> = emptyList(),
    /** allowPendingChoiceAfterScript: 검증 실행 조건을 나타낸다. */
    val allowPendingChoiceAfterScript: Boolean = false,
    /** choiceTracePath: 검증 산출물을 저장할 경로를 담는다. */
    val choiceTracePath: String? = null,
    /** randomTracePath: 검증 산출물을 저장할 경로를 담는다. */
    val randomTracePath: String? = null,
    /** stopAfterRandomTraceCount: 검증 추적 결과를 담는다. */
    val stopAfterRandomTraceCount: Int? = null,
)

/** ScenarioVerificationKind: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
private enum class ScenarioVerificationKind { SMOKE, FIRST_BRANCH, ALTERNATE_BRANCH }

/** ScenarioRuntimeVerificationObserver: 기존 시나리오 스모크·분기 경로의 검증과 프로세스 종료를 담당한다. */
private class ScenarioRuntimeVerificationObserver(
    /** kind: 검증 대상의 종류를 담는다. */
    private val kind: ScenarioVerificationKind,
) : RuntimeScreenObserver {
    /** selectedBranch: 선택된 분기 식별자를 담는다. */
    private var selectedBranch = false
    /** finished: 검증 흐름 종료 여부를 담는다. */
    private var finished = false
    /** selectionAttempts: 선택 시도 횟수를 담는다. */
    private var selectionAttempts = 0

    /** keepsScenarioOpen: 검증 시나리오 식별자를 담는다. */
    override val keepsScenarioOpen: Boolean = true

    /** update: 현재 검증 상태를 입력에 맞게 갱신한다. */
    override fun update(delta: Float, screen: RuntimeScreenProbe) {
        if (finished || screen !is ScenarioRuntimeProbe) return
        when (kind) {
            ScenarioVerificationKind.SMOKE -> verifySmoke(screen)
            ScenarioVerificationKind.FIRST_BRANCH -> verifyBranch(screen, 0, "내가 남자로 태어났을 때부터")
            ScenarioVerificationKind.ALTERNATE_BRANCH -> verifyBranch(screen, 1, "간웅이라고? 지금 단정 짓기엔 너무 이르지 않나")
        }
    }

    /** verifySmoke: 검증 조건을 실행하고 실패한 계약을 보고한다. */
    private fun verifySmoke(screen: ScenarioRuntimeProbe) {
        if (screen.playback == PlaybackState.COMPLETE) error("${screen.module} completed before the smoke evidence appeared")
        val dialogueText = screen.dialogueText
        if (screen.backgroundId == 0 || screen.unitIds.isEmpty() || dialogueText.isNullOrBlank()) return
        check(ScenarioCatalog.verifyEmbeddedSources() == 119) { "Expected 119 restored scenarios" }
        if (screen.module == "R_00") {
            check(screen.unitIds.containsAll(listOf(0, 157, 181, 182))) { "R_00 grouped unit commands were not executed" }
            check(dialogueText.contains("대장님")) { "R_00 dialogue did not match" }
            check(Gdx.files.internal("maps/heads/181.png").exists()) { "R_00 speaker portrait was not bundled" }
            check(Gdx.files.internal("maps/2.jpg").exists()) { "R_00 loadBg source image was not bundled" }
        }
        finish("VERIFY_SCENARIO_OK: ${screen.module} runtime loaded")
    }

    /** verifyBranch: 검증 조건을 실행하고 실패한 계약을 보고한다. */
    private fun verifyBranch(screen: ScenarioRuntimeProbe, expectedChoice: Int, expectedDialogue: String) {
        if (!selectedBranch && screen.playback == PlaybackState.CHOICE) {
            require(expectedChoice in screen.options.indices) { "${screen.module} branch option $expectedChoice is unavailable" }
            if (screen.selectedChoice != expectedChoice) {
                check(selectionAttempts++ < screen.options.size) { "${screen.module} could not select branch option $expectedChoice" }
                press(if (expectedChoice > screen.selectedChoice) Input.Keys.DOWN else Input.Keys.UP)
                return
            }
            press(Input.Keys.ENTER)
            selectedBranch = true
            return
        }
        val selectedOptionRecorded = screen.choiceTrace.lastOrNull()?.option == expectedChoice
        val reachedBranchJoin = screen.dialogueText?.contains("그래서 그런 거구나") == true
        if (selectedBranch && (screen.dialogueText?.contains(expectedDialogue) == true ||
                (selectedOptionRecorded && reachedBranchJoin))) {
            finish("VERIFY_SCENARIO_BRANCH_OK: ${screen.module} option=$expectedChoice")
            return
        }
        check(screen.playback != PlaybackState.COMPLETE) { "${screen.module} completed before branch dialogue" }
        press(Input.Keys.ENTER)
    }

    /** finish: 검증 흐름을 종료하고 사용한 리소스를 정리한다. */
    private fun finish(message: String) {
        if (finished) return
        finished = true
        Gdx.app.log("JojoGame", message)
        Gdx.app.exit()
    }

    /** press: 검증 입력 선택을 적용해 다음 상태로 진행한다. */
    private fun press(key: Int) {
        checkNotNull(Gdx.input.inputProcessor) { "scenario verification has no production input processor" }.keyDown(key)
    }
}

/** ScenarioTraceRuntimeObserverFactory: 검증 추적 데이터와 증거를 표현하는 타입이다. */
object ScenarioTraceRuntimeObserverFactory {
    /** create: 검증 시나리오의 초기 상태를 구성한다. */
    fun create(arguments: List<String>): RuntimeScreenObserver {
        val scenarioVerification = when {
            arguments.contains("--verify-scenario-smoke") -> ScenarioVerificationKind.SMOKE
            arguments.contains("--verify-scenario-branch") -> ScenarioVerificationKind.FIRST_BRANCH
            arguments.contains("--verify-scenario-branch-2") -> ScenarioVerificationKind.ALTERNATE_BRANCH
            else -> null
        }
        return scenarioVerification?.let(::ScenarioRuntimeVerificationObserver) ?: ScenarioTraceRuntimeObserver(
            ScenarioTracePlan(
            choices = option(arguments, "--verify-choice-script=")
                ?.takeIf(String::isNotBlank)?.split(',')?.map { it.trim().toInt() }.orEmpty(),
            allowPendingChoiceAfterScript = arguments.contains("--verify-stop-at-choice"),
            choiceTracePath = option(arguments, "--choice-trace="),
            randomTracePath = option(arguments, "--random-trace="),
            stopAfterRandomTraceCount = option(arguments, "--verify-stop-after-random-count=")?.toInt(),
            ),
        )
    }

    /** option: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun option(arguments: List<String>, prefix: String): String? =
        arguments.firstOrNull { it.startsWith(prefix) }?.removePrefix(prefix)
}

/** ScenarioTraceJson: 검증 추적 데이터와 증거를 표현하는 타입이다. */
internal object ScenarioTraceJson {
    /** writeChoices: 검증 산출물을 지정한 경로에 기록한다. */
    fun writeChoices(rawPath: String, screen: ScenarioRuntimeProbe) = write(rawPath, "choices", screen.choiceTrace.joinToString(",") {
        "{\"module\":\"${it.module}\",\"function\":\"${it.function}\",\"line\":${it.line},\"option\":${it.option},\"optionCount\":${it.optionCount}}"
    })

    /** writeRandom: 검증 산출물을 지정한 경로에 기록한다. */
    fun writeRandom(rawPath: String, screen: ScenarioRuntimeProbe) = write(rawPath, "random", screen.randomTrace.joinToString(",") {
        "{\"module\":\"${it.module}\",\"function\":\"${it.function}\",\"line\":${it.line},\"value\":${it.value}}"
    })

    /** write: 검증 이벤트와 결과를 기록한다. */
    private fun write(rawPath: String, field: String, entries: String) {
        val path = Path.of(rawPath)
        path.parent?.let(Files::createDirectories)
        Files.writeString(path, "{\"$field\":[$entries]}\n")
    }
}
