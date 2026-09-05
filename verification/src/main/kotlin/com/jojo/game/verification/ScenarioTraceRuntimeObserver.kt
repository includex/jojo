package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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

object ScenarioTraceRuntimeObserverFactory {
    fun create(arguments: List<String>): RuntimeScreenObserver = ScenarioTraceRuntimeObserver(
        ScenarioTracePlan(
            choices = option(arguments, "--verify-choice-script=")
                ?.takeIf(String::isNotBlank)?.split(',')?.map { it.trim().toInt() }.orEmpty(),
            allowPendingChoiceAfterScript = arguments.contains("--verify-stop-at-choice"),
            choiceTracePath = option(arguments, "--choice-trace="),
            randomTracePath = option(arguments, "--random-trace="),
            stopAfterRandomTraceCount = option(arguments, "--verify-stop-after-random-count=")?.toInt(),
        ),
    )

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
