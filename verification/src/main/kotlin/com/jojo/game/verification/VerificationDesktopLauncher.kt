// Verification
package com.jojo.game.verification

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.application.runtime.GameEntryPoint
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.RenderCaptureConfiguration
import com.jojo.game.application.runtime.ScenarioRunConfiguration
import com.jojo.game.application.runtime.VerificationConfiguration
import com.jojo.game.application.runtime.BattleTraceRuntimeConfig
import com.jojo.game.verification.preparation.VerificationBattlePreparationDriver
import com.jojo.game.verification.title.VerificationTitleStartupDriver

/** VerificationDesktopLauncher: 캡처·추적·스크립트 실행 옵션을 처리하는 검증 전용 데스크톱 진입점이다. */
object VerificationDesktopLauncher {
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

/** VerificationDesktopLaunchOptions: 운영 데스크톱에 테스트 CLI가 노출되지 않도록 검증 모듈이 옵션을 해석한다. */
internal data class VerificationDesktopLaunchOptions(
    /** scenario: 검증 시나리오 식별자를 담는다. */
    val scenario: String,
    /** battleReturnScenario: 검증 시나리오 식별자를 담는다. */
    val battleReturnScenario: String?,
    /** explicitScenario: 검증 시나리오 식별자를 담는다. */
    val explicitScenario: Boolean,
    /** battle: 전투 실행 여부를 담는다. */
    val battle: Boolean,
    /** scenarioRun: 검증 시나리오 식별자를 담는다. */
    val scenarioRun: ScenarioRunConfiguration,
    /** verification: 검증 실행 설정을 담는다. */
    val verification: VerificationConfiguration,
    /** capture: 캡처 실행 여부를 담는다. */
    val capture: RenderCaptureConfiguration,
    /** fullBattleTrace: 검증 추적 결과를 담는다. */
    val fullBattleTrace: BattleTraceRuntimeConfig?,
    /** fullBattleTraceOutputPath: 검증 산출물을 저장할 경로를 담는다. */
    val fullBattleTraceOutputPath: String?,
    /** yingchuanEntryFlowTracePath: 검증 산출물을 저장할 경로를 담는다. */
    val yingchuanEntryFlowTracePath: String?,
) {
    /** toGameConfiguration: 검증 입력을 처리하고 관련 상태를 갱신한다. */
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
            runtimeBattlePresentation = VerificationBattlePresentation.from(capture.state),
            runtimeBattlePreparationDriver = VerificationBattlePreparationDriver(capture.state),
            runtimeScenarioDriver = VerificationScenarioDriver(capture.state),
            runtimeTitleStartupDriver = VerificationTitleStartupDriver(capture.state),
            runtimeBattleObserver = fullBattleTrace?.let { VerificationBattleObserver(requireNotNull(fullBattleTraceOutputPath), it) },
            battleTraceRuntime = fullBattleTrace,
            yingchuanEntryFlowTracePath = yingchuanEntryFlowTracePath,
            automatedRun = true,
        )
    }

    companion object {
        /** parse: 외부 입력을 검증용 값으로 변환한다. */
        fun parse(args: Array<String>): VerificationDesktopLaunchOptions {
            val values = args.toList()
            /** value: 검증 입력을 처리하고 관련 상태를 갱신한다. */
            fun value(prefix: String) = values.firstOrNull { it.startsWith(prefix) }?.removePrefix(prefix)
            /** ints: 검증 입력을 처리하고 관련 상태를 갱신한다. */
            fun ints(prefix: String) = value(prefix)?.takeIf(String::isNotBlank)?.split(',')?.map { it.trim().toInt() }.orEmpty()
            /** pairs: 검증 입력을 처리하고 관련 상태를 갱신한다. */
            fun pairs(prefix: String) = value(prefix)?.takeIf(String::isNotBlank)?.split(',')?.associate { token ->
                token.trim().split(':', limit = 2).let { it[0].toInt() to it[1].toInt() }
            }.orEmpty()
            /**
             * `scenario` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val scenario = value("--scenario=")?.uppercase() ?: "R_00"
            /**
             * `startScene` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val startScene = value("--verify-scene=") ?: "scene1"
            require(startScene.matches(Regex("scene[0-9]+"))) { "--verify-scene must be a source scene function such as scene2" }
            /**
             * `battleAttributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val battleAttributes = value("--verify-attributes=")?.takeIf(String::isNotBlank)?.split(',')
                ?.map { it.trim().split(':', limit = 3).map(String::toInt) }
                ?.groupBy({ it[0] }, { it[1] to it[2] })?.mapValues { (_, entries) -> entries.toMap() }.orEmpty()
            /**
             * `battlePositions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val battlePositions = value("--verify-positions=")?.takeIf(String::isNotBlank)?.split(',')?.associate { token ->
                token.trim().split(':', limit = 3).map(String::toInt).let { it[0] to (it[1] to it[2]) }
            }.orEmpty()
            /**
             * `campPositions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val campPositions = value("--verify-camp-positions=")?.takeIf(String::isNotBlank)?.split(',')
                ?.map { it.trim().split(':', limit = 3).map(String::toInt) }
                ?.groupBy({ it[0] }, { it[1] to it[2] }).orEmpty()
            /**
             * `unitAttributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val unitAttributes = value("--verify-unit-attrs=")?.takeIf(String::isNotBlank)?.split(',')?.map { token ->
                token.trim().split(':', limit = 3).map(String::toInt).let { Triple(it[0], it[1], it[2]) }
            }.orEmpty()
            /**
             * `random` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val random = ints("--verify-random=")
            require(random.all { it in 0..100 }) { "--verify-random values must be 0..100" }
            /**
             * `tracePath` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val tracePath = value("--full-battle-trace=")
            /**
             * `fullBattleTrace` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val fullBattleTrace = tracePath?.let { output ->
                BattleTraceRuntimeConfig(
                    scenario = scenario.replaceFirst("R_", "S_"),
                    toolSeed = value("--full-battle-seed=")?.toInt() ?: 1000,
                    mathSeed = value("--full-battle-math-seed=")?.toLong() ?: 0x12345678L,
                    timeScale = value("--full-battle-time-scale=")?.toFloat() ?: 8f,
                    maxSimulationSeconds = value("--full-battle-max-sim-seconds=")?.toFloat() ?: 1800f,
                    exitOnFinish = true,
                )
            }
            /**
             * `offset` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
                    mapDither = value("--map-dither="),
                    mapFilter = value("--map-filter="), mapSampler = value("--map-sampler="), mapSampleOffset = offset,
                    compositionTracePath = value("--composition-trace="), renderEventLogPath = value("--render-event-log="),
                    state = value("--capture-state="),
                ),
                fullBattleTrace = fullBattleTrace,
                fullBattleTraceOutputPath = tracePath,
                yingchuanEntryFlowTracePath = value("--yingchuan-entry-flow-trace="),
            )
        }
    }
}
