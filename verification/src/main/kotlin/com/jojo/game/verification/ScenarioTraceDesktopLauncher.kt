package com.jojo.game.verification

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.application.runtime.GameEntryPoint
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.ScenarioRunConfiguration

/** Verification-owned LWJGL entry point for deterministic scenario fixtures. */
object ScenarioTraceDesktopLauncher {
    @JvmStatic fun main(args: Array<String>) {
        val options = args.toList()
        fun value(prefix: String) = options.firstOrNull { it.startsWith(prefix) }?.removePrefix(prefix)
        fun ints(prefix: String) = value(prefix)?.takeIf(String::isNotBlank)?.split(',')?.map { it.trim().toInt() }.orEmpty()
        fun map(prefix: String) = value(prefix)?.takeIf(String::isNotBlank)?.split(',')?.associate { token ->
            token.trim().split(':', limit = 2).let { it[0].toInt() to it[1].toInt() }
        }.orEmpty()
        val positions = value("--verify-positions=")?.takeIf(String::isNotBlank)?.split(',')?.associate { token ->
            token.split(':').let { it[0].toInt() to (it[1].toInt() to it[2].toInt()) }
        }.orEmpty()
        val campPositions = value("--verify-camp-positions=")?.takeIf(String::isNotBlank)?.split(',')
            ?.map { it.split(':').map(String::toInt) }?.groupBy({ it[0] }, { it[1] to it[2] }).orEmpty()
        val attributes = value("--verify-attributes=")?.takeIf(String::isNotBlank)?.split(',')
            ?.map { it.split(':').map(String::toInt) }?.groupBy({ it[0] }, { it[1] to it[2] })
            ?.mapValues { (_, values) -> values.associate { it } }.orEmpty()
        val unitAttributes = value("--verify-unit-attrs=")?.takeIf(String::isNotBlank)?.split(',')?.map { token ->
            token.split(':').let { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
        }.orEmpty()
        val scenario = value("--scenario=")?.uppercase() ?: "R_00"
        val observer = ScenarioTraceRuntimeObserverFactory.create(options)
        val configuration = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo scenario trace verification"); setWindowedMode(1280, 688); setWindowPosition(0, 0)
            setForegroundFPS(60); setIdleFPS(60); setPauseWhenMinimized(false); setPauseWhenLostFocus(false); disableAudio(true)
        }
        Lwjgl3Application(JojoGame(GameLaunchConfiguration(
            entryPoint = GameEntryPoint.SCENARIO,
            initialScenario = scenario,
            initialScenarioExplicit = value("--scenario=") != null,
            scenarioRun = ScenarioRunConfiguration(
                globals = map("--verify-globals="), unitAttributes = unitAttributes, variables = map("--verify-vars="),
                ambition = value("--verify-ambition=")?.toInt(), randomSequence = ints("--verify-random="),
                infoTransferRandomSequence = ints("--verify-info-random="), battleRound = value("--verify-round=")?.toInt() ?: 1,
                battleCamp = value("--verify-camp=")?.toInt() ?: 1, battleAttributes = attributes, battlePositions = positions,
                battlePositionsByCamp = campPositions, battleEnemyDefeated = options.contains("--verify-win"),
                startScene = value("--verify-scene=") ?: "scene1", startLabel = value("--verify-label="),
                stopAfterRandomTrace = options.contains("--verify-stop-after-random"),
                stopAfterRandomTraceCount = value("--verify-stop-after-random-count=")?.toInt(),
            ),
            runtimeScreenObserver = observer, automatedRun = true,
        )), configuration)
    }
}
