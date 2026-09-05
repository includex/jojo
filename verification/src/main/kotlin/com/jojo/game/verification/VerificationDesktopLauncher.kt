package com.jojo.game.verification

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.FullBattleTraceConfig
import com.jojo.game.GameEntryPoint
import com.jojo.game.GameLaunchConfiguration
import com.jojo.game.JojoGame
import com.jojo.game.RenderCaptureConfiguration
import com.jojo.game.ScenarioRunConfiguration
import com.jojo.game.VerificationConfiguration
import com.jojo.game.verification.preparation.VerificationBattlePreparationDriver
import com.jojo.game.verification.title.VerificationTitleStartupDriver

/** Verification-owned desktop entry point for capture, trace and scripted-run flags. */
object VerificationDesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val options = VerificationDesktopLaunchOptions.parse(args)
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo verification")
            setWindowedMode(1280, 688)
            setWindowPosition(0, 0)
            setForegroundFPS(60)
            useVsync(options.fullBattleTrace == null)
            if (options.fullBattleTrace != null) setIdleFPS(60)
        }
        Lwjgl3Application(JojoGame(options.toGameConfiguration()), window)
    }
}

/** Parser is deliberately owned by verification so desktop production has no test CLI surface. */
internal data class VerificationDesktopLaunchOptions(
    val scenario: String,
    val battleReturnScenario: String?,
    val explicitScenario: Boolean,
    val battle: Boolean,
    val scenarioRun: ScenarioRunConfiguration,
    val verification: VerificationConfiguration,
    val capture: RenderCaptureConfiguration,
    val fullBattleTrace: FullBattleTraceConfig?,
    val yingchuanEntryFlowTracePath: String?,
) {
    fun toGameConfiguration(): GameLaunchConfiguration {
        val artifactObserver = VerificationArtifactObserver(capture)
        return GameLaunchConfiguration(
            entryPoint = when {
                battle || fullBattleTrace != null -> GameEntryPoint.BATTLE
                capture.state == null && yingchuanEntryFlowTracePath == null && !explicitScenario && scenarioRun == ScenarioRunConfiguration() -> GameEntryPoint.TITLE
                else -> GameEntryPoint.SCENARIO
            },
            initialScenario = scenario,
            battleReturnScenario = battleReturnScenario,
            initialScenarioExplicit = explicitScenario,
            scenarioRun = scenarioRun,
            verification = verification,
            capture = capture,
            runtimeArtifactObserver = artifactObserver,
            runtimeScreenObserver = artifactObserver,
            runtimeBattleDriver = VerificationBattleDriver(capture.state),
            runtimeBattleReferenceAssets = VerificationBattleReferenceAssets(),
            runtimeBattlePresentation = VerificationBattlePresentation.from(capture.state),
            runtimeBattlePreparationDriver = VerificationBattlePreparationDriver(capture.state),
            runtimeScenarioDriver = VerificationScenarioDriver(capture.state),
            runtimeTitleStartupDriver = VerificationTitleStartupDriver(capture.state),
            fullBattleTrace = fullBattleTrace,
            yingchuanEntryFlowTracePath = yingchuanEntryFlowTracePath,
            automatedRun = true,
        )
    }

    companion object {
        fun parse(args: Array<String>): VerificationDesktopLaunchOptions {
            val values = args.toList()
            fun value(prefix: String) = values.firstOrNull { it.startsWith(prefix) }?.removePrefix(prefix)
            fun ints(prefix: String) = value(prefix)?.takeIf(String::isNotBlank)?.split(',')?.map { it.trim().toInt() }.orEmpty()
            fun pairs(prefix: String) = value(prefix)?.takeIf(String::isNotBlank)?.split(',')?.associate { token ->
                token.trim().split(':', limit = 2).let { it[0].toInt() to it[1].toInt() }
            }.orEmpty()
            val scenario = value("--scenario=")?.uppercase() ?: "R_00"
            val startScene = value("--verify-scene=") ?: "scene1"
            require(startScene.matches(Regex("scene[0-9]+"))) { "--verify-scene must be a source scene function such as scene2" }
            val battleAttributes = value("--verify-attributes=")?.takeIf(String::isNotBlank)?.split(',')
                ?.map { it.trim().split(':', limit = 3).map(String::toInt) }
                ?.groupBy({ it[0] }, { it[1] to it[2] })?.mapValues { (_, entries) -> entries.toMap() }.orEmpty()
            val battlePositions = value("--verify-positions=")?.takeIf(String::isNotBlank)?.split(',')?.associate { token ->
                token.trim().split(':', limit = 3).map(String::toInt).let { it[0] to (it[1] to it[2]) }
            }.orEmpty()
            val campPositions = value("--verify-camp-positions=")?.takeIf(String::isNotBlank)?.split(',')
                ?.map { it.trim().split(':', limit = 3).map(String::toInt) }
                ?.groupBy({ it[0] }, { it[1] to it[2] }).orEmpty()
            val unitAttributes = value("--verify-unit-attrs=")?.takeIf(String::isNotBlank)?.split(',')?.map { token ->
                token.trim().split(':', limit = 3).map(String::toInt).let { Triple(it[0], it[1], it[2]) }
            }.orEmpty()
            val random = ints("--verify-random=")
            require(random.all { it in 0..100 }) { "--verify-random values must be 0..100" }
            val tracePath = value("--full-battle-trace=")
            val fullBattleTrace = tracePath?.let { output ->
                FullBattleTraceConfig(
                    outputPath = output,
                    scenario = scenario.replaceFirst("R_", "S_"),
                    toolSeed = value("--full-battle-seed=")?.toInt() ?: 1000,
                    mathSeed = value("--full-battle-math-seed=")?.toLong() ?: 0x12345678L,
                    timeScale = value("--full-battle-time-scale=")?.toFloat() ?: 8f,
                    maxSimulationSeconds = value("--full-battle-max-sim-seconds=")?.toFloat() ?: 1800f,
                    exitOnFinish = true,
                )
            }
            val offset = value("--map-sample-offset=")?.split(':')?.let {
                require(it.size == 2) { "--map-sample-offset uses x:y" }; it[0].toFloat() to it[1].toFloat()
            }
            return VerificationDesktopLaunchOptions(
                scenario = scenario,
                battleReturnScenario = value("--battle-return=")?.uppercase(),
                explicitScenario = value("--scenario=") != null,
                battle = "--battle" in values,
                scenarioRun = ScenarioRunConfiguration(
                    globals = pairs("--verify-globals="),
                    unitAttributes = unitAttributes,
                    variables = pairs("--verify-vars="),
                    ambition = value("--verify-ambition=")?.toInt(),
                    randomSequence = random,
                    infoTransferRandomSequence = ints("--verify-info-random="),
                    battleRound = value("--verify-round=")?.toInt() ?: 1,
                    battleCamp = value("--verify-camp=")?.toInt() ?: 1,
                    battleAttributes = battleAttributes,
                    battlePositions = battlePositions,
                    battlePositionsByCamp = campPositions,
                    battleEnemyDefeated = "--verify-win" in values,
                    startScene = startScene,
                    startLabel = value("--verify-label="),
                    stopAfterRandomTrace = "--verify-stop-after-random" in values,
                    stopAfterRandomTraceCount = value("--verify-stop-after-random-count=")?.toInt()?.also {
                        require(it > 0) { "--verify-stop-after-random-count must be positive" }
                    },
                ),
                verification = VerificationConfiguration(
                    battle = "--verify-battle" in values,
                    scriptedBattle = "--verify-scripted-battle" in values,
                ),
                capture = RenderCaptureConfiguration(
                    screenshotPath = value("--capture="), rawCapturePath = value("--capture-raw="),
                    mapTextureDumpPath = value("--dump-map-texture="), mapDither = value("--map-dither="),
                    mapFilter = value("--map-filter="), mapSampler = value("--map-sampler="), mapSampleOffset = offset,
                    compositionTracePath = value("--composition-trace="), renderEventLogPath = value("--render-event-log="),
                    state = value("--capture-state="),
                ),
                fullBattleTrace = fullBattleTrace,
                yingchuanEntryFlowTracePath = value("--yingchuan-entry-flow-trace="),
            )
        }
    }
}
