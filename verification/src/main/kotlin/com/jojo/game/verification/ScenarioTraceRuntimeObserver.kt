package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.jojo.game.ScenarioCatalog
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.RuntimeScreenProbe
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState
import java.nio.file.Files
import java.nio.file.Path

/**
 * Verification-owned driver for deterministic scenario choices and trace
 * files. Core publishes immutable [ScenarioRuntimeProbe] snapshots only;
 * this observer sends input through the installed production processor.
 */
class ScenarioTraceRuntimeObserver(private val plan: ScenarioTracePlan) : RuntimeScreenObserver {
    private var choiceStep = 0
    private var finished = false

    override val keepsScenarioOpen: Boolean = true

    override fun update(delta: Float, screen: RuntimeScreenProbe) {
        if (finished || screen !is ScenarioRuntimeProbe) return
        when (screen.playback) {
            PlaybackState.CHOICE -> choose(screen)
            PlaybackState.COMPLETE -> finish(screen)
            else -> pressEnter()
        }
        if (plan.stopAfterRandomTraceCount?.let { screen.randomTrace.size >= it } == true) finish(screen)
    }

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

    private fun pressEnter(key: Int = Input.Keys.ENTER) {
        checkNotNull(Gdx.input.inputProcessor) { "scenario trace has no production input processor" }.keyDown(key)
    }
}

data class ScenarioTracePlan(
    val choices: List<Int> = emptyList(),
    val allowPendingChoiceAfterScript: Boolean = false,
    val choiceTracePath: String? = null,
    val randomTracePath: String? = null,
    val stopAfterRandomTraceCount: Int? = null,
)

private enum class ScenarioVerificationKind { SMOKE, FIRST_BRANCH, ALTERNATE_BRANCH }

/** Assertions and process termination for the legacy scenario smoke/branch routes. */
private class ScenarioRuntimeVerificationObserver(
    private val kind: ScenarioVerificationKind,
) : RuntimeScreenObserver {
    private var selectedBranch = false
    private var finished = false
    private var selectionAttempts = 0

    override val keepsScenarioOpen: Boolean = true

    override fun update(delta: Float, screen: RuntimeScreenProbe) {
        if (finished || screen !is ScenarioRuntimeProbe) return
        when (kind) {
            ScenarioVerificationKind.SMOKE -> verifySmoke(screen)
            ScenarioVerificationKind.FIRST_BRANCH -> verifyBranch(screen, 0, "내가 남자로 태어났을 때부터")
            ScenarioVerificationKind.ALTERNATE_BRANCH -> verifyBranch(screen, 1, "간웅이라고? 지금 단정 짓기엔 너무 이르지 않나")
        }
    }

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

    private fun finish(message: String) {
        if (finished) return
        finished = true
        Gdx.app.log("JojoGame", message)
        Gdx.app.exit()
    }

    private fun press(key: Int) {
        checkNotNull(Gdx.input.inputProcessor) { "scenario verification has no production input processor" }.keyDown(key)
    }
}

object ScenarioTraceRuntimeObserverFactory {
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

    private fun option(arguments: List<String>, prefix: String): String? =
        arguments.firstOrNull { it.startsWith(prefix) }?.removePrefix(prefix)
}

internal object ScenarioTraceJson {
    fun writeChoices(rawPath: String, screen: ScenarioRuntimeProbe) = write(rawPath, "choices", screen.choiceTrace.joinToString(",") {
        "{\"module\":\"${it.module}\",\"function\":\"${it.function}\",\"line\":${it.line},\"option\":${it.option},\"optionCount\":${it.optionCount}}"
    })

    fun writeRandom(rawPath: String, screen: ScenarioRuntimeProbe) = write(rawPath, "random", screen.randomTrace.joinToString(",") {
        "{\"module\":\"${it.module}\",\"function\":\"${it.function}\",\"line\":${it.line},\"value\":${it.value}}"
    })

    private fun write(rawPath: String, field: String, entries: String) {
        val path = Path.of(rawPath)
        path.parent?.let(Files::createDirectories)
        Files.writeString(path, "{\"$field\":[$entries]}\n")
    }
}
