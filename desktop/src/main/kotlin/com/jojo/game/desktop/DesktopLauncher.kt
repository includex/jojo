package com.jojo.game.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.GameEntryPoint
import com.jojo.game.GameLaunchConfiguration
import com.jojo.game.JojoGame
import com.jojo.game.RenderCaptureConfiguration
import com.jojo.game.ScenarioRunConfiguration
import com.jojo.game.VerificationConfiguration

/**
 * object  `DesktopLauncher`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object DesktopLauncher {
    @JvmStatic
/**
 * 공개 메서드 `main`
 *
 * ### 파라미터
- `args` (`Array<String>`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

    fun main(args: Array<String>) {
        val scenario = args.firstOrNull { it.startsWith("--scenario=") }
            ?.substringAfter('=')
            ?.uppercase()
            ?: "R_00"
        val battleReturnScenario = args.firstOrNull { it.startsWith("--battle-return=") }
            ?.substringAfter('=')
            ?.uppercase()
        val hasExplicitScenario = args.any { it.startsWith("--scenario=") }
        val screenshotPath = args.firstOrNull { it.startsWith("--capture=") }?.substringAfter('=')
        val rawCapturePath = args.firstOrNull { it.startsWith("--capture-raw=") }?.substringAfter('=')
        val mapTextureDumpPath = args.firstOrNull { it.startsWith("--dump-map-texture=") }?.substringAfter('=')
        val mapDither = args.firstOrNull { it.startsWith("--map-dither=") }?.substringAfter('=')
        val mapFilter = args.firstOrNull { it.startsWith("--map-filter=") }?.substringAfter('=')
        val mapSampler = args.firstOrNull { it.startsWith("--map-sampler=") }?.substringAfter('=')
        val mapSampleOffset = args.firstOrNull { it.startsWith("--map-sample-offset=") }
            ?.substringAfter('=')
            ?.split(':')
            ?.let { values -> require(values.size == 2) { "--map-sample-offset uses x:y" }; values[0].toFloat() to values[1].toFloat() }
        val compositionTracePath = args.firstOrNull { it.startsWith("--composition-trace=") }?.substringAfter('=')
        val renderEventLogPath = args.firstOrNull { it.startsWith("--render-event-log=") }?.substringAfter('=')
        val screenshotState = args.firstOrNull { it.startsWith("--capture-state=") }?.substringAfter('=')
        val fullBattleTracePath = args.firstOrNull { it.startsWith("--full-battle-trace=") }?.substringAfter('=')
        val yingchuanEntryFlowTracePath = args.firstOrNull { it.startsWith("--yingchuan-entry-flow-trace=") }?.substringAfter('=')
        val fullBattleTraceConfig = fullBattleTracePath?.let { output ->
            com.jojo.game.FullBattleTraceConfig(
                outputPath = output,
                scenario = scenario.replaceFirst("R_", "S_"),
                toolSeed = args.firstOrNull { it.startsWith("--full-battle-seed=") }?.substringAfter('=')?.toInt() ?: 1000,
                mathSeed = args.firstOrNull { it.startsWith("--full-battle-math-seed=") }?.substringAfter('=')?.toLong() ?: 0x12345678L,
                timeScale = args.firstOrNull { it.startsWith("--full-battle-time-scale=") }?.substringAfter('=')?.toFloat() ?: 8f,
                maxSimulationSeconds = args.firstOrNull { it.startsWith("--full-battle-max-sim-seconds=") }?.substringAfter('=')?.toFloat() ?: 1800f,
                exitOnFinish = true,
            )
        }
        val choiceScript = args.firstOrNull { it.startsWith("--verify-choice-script=") }
            ?.substringAfter('=')
            ?.takeIf(String::isNotBlank)
            ?.split(',')
            ?.map { it.trim().toInt() }
            ?: emptyList()
        val globals = args.firstOrNull { it.startsWith("--verify-globals=") }
            ?.substringAfter('=')
            ?.takeIf(String::isNotBlank)
            ?.split(',')
            ?.associate { entry ->
                val (id, value) = entry.trim().split(':', limit = 2)
                id.toInt() to value.toInt()
            }
            ?: emptyMap()
        val variables = args.firstOrNull { it.startsWith("--verify-vars=") }
            ?.substringAfter('=')
            ?.takeIf(String::isNotBlank)
            ?.split(',')
            ?.associate { entry ->
                val (id, value) = entry.trim().split(':', limit = 2)
                id.toInt() to value.toInt()
            }
            ?: emptyMap()
        val ambition = args.firstOrNull { it.startsWith("--verify-ambition=") }?.substringAfter('=')?.toInt()
        val randomSequence = args.firstOrNull { it.startsWith("--verify-random=") }
            ?.substringAfter('=')
            ?.takeIf(String::isNotBlank)
            ?.split(',')
            ?.map { it.trim().toInt().also { value -> require(value in 0..100) { "--verify-random values must be 0..100" } } }
            ?: emptyList()
        val battleRound = args.firstOrNull { it.startsWith("--verify-round=") }?.substringAfter('=')?.toInt() ?: 1
        val battleCamp = args.firstOrNull { it.startsWith("--verify-camp=") }?.substringAfter('=')?.toInt() ?: 1
        val startScene = args.firstOrNull { it.startsWith("--verify-scene=") }?.substringAfter('=') ?: "scene1"
        val startLabel = args.firstOrNull { it.startsWith("--verify-label=") }?.substringAfter('=')
        val choiceTracePath = args.firstOrNull { it.startsWith("--choice-trace=") }?.substringAfter('=')
        val randomTracePath = args.firstOrNull { it.startsWith("--random-trace=") }?.substringAfter('=')
        val infoTransferRandomSequence = args.firstOrNull { it.startsWith("--verify-info-random=") }
            ?.substringAfter('=')?.takeIf(String::isNotBlank)?.split(',')?.map(String::toInt) ?: emptyList()
        val unitAttributes = args.firstOrNull { it.startsWith("--verify-unit-attrs=") }?.substringAfter('=')
            ?.takeIf(String::isNotBlank)?.split(',')?.map { token ->
                token.split(':').let { fields -> require(fields.size == 3) { "--verify-unit-attrs uses unit:attribute:value" }; Triple(fields[0].toInt(), fields[1].toInt(), fields[2].toInt()) }
            } ?: emptyList()
        val stopAfterRandomTraceCount = args.firstOrNull { it.startsWith("--verify-stop-after-random-count=") }
            ?.substringAfter('=')?.toInt()?.also { require(it > 0) { "--verify-stop-after-random-count must be positive" } }
        require(startScene.matches(Regex("scene[0-9]+"))) { "--verify-scene must be a source scene function such as scene2" }
        val enemyDefeated = args.contains("--verify-win")
        val battleAttributes = args.firstOrNull { it.startsWith("--verify-attributes=") }
            ?.substringAfter('=')
            ?.takeIf(String::isNotBlank)
            ?.split(',')
            ?.map { entry -> entry.trim().split(':', limit = 3).map(String::toInt) }
            ?.groupBy({ it[0] }, { it[1] to it[2] })
            ?.mapValues { (_, values) -> values.associate { it } }
            ?: emptyMap()
        val battlePositions = args.firstOrNull { it.startsWith("--verify-positions=") }
            ?.substringAfter('=')
            ?.takeIf(String::isNotBlank)
            ?.split(',')
            ?.associate { entry ->
                val (id, x, y) = entry.trim().split(':', limit = 3).map(String::toInt)
                id to (x to y)
            }
            ?: emptyMap()
        val battlePositionsByCamp = args.firstOrNull { it.startsWith("--verify-camp-positions=") }
            ?.substringAfter('=')
            ?.takeIf(String::isNotBlank)
            ?.split(',')
            ?.map { entry -> entry.trim().split(':', limit = 3).map(String::toInt) }
            ?.groupBy({ it[0] }, { it[1] to it[2] })
            ?: emptyMap()
        val automatedRun = args.any { argument ->
            argument.startsWith("--verify") ||
                argument.startsWith("--capture") ||
                argument.startsWith("--dump-map-texture=") ||
                argument.startsWith("--render-event-log=") ||
                argument.startsWith("--composition-trace=") ||
                argument.startsWith("--full-battle-trace=") ||
                argument.startsWith("--yingchuan-entry-flow-trace=") ||
                argument.startsWith("--choice-trace=") ||
                argument.startsWith("--random-trace=")
        }
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("삼국지 조조전")
            // Electron BrowserWindow is 1280×720 including its native title
            // bar; Cocos capturePage renders the 1280×688 game canvas.
            setWindowedMode(1280, 688)
            // LibGDX centres an unspecified window by querying GLFW's
            // primary monitor.  macOS can transiently return a null monitor
            // after a long sequence of short-lived verification processes,
            // causing a launcher NPE before the fixture runs.  Automated
            // routes do not need centring, so use an explicit position and
            // keep ordinary interactive launches centred.
            if (automatedRun) setWindowPosition(0, 0)
            // Full-battle/campaign trace windows contain no framebuffer
            // evidence.  On macOS, an occluded vsynced GLFW window can be
            // throttled to roughly one frame per second, which made `--jobs`
            // batches effectively serial and could hit the process timeout
            // before the simulation deadline.  Preserve a deterministic
            // 60-frame cadence for both foreground and occluded trace windows;
            // visual capture routes keep compositor-synchronised VSync.
            val logicTraceRun = fullBattleTracePath != null
            useVsync(!logicTraceRun)
            if (logicTraceRun) setIdleFPS(60)
            setForegroundFPS(60)
        }.also { configuration ->
            Lwjgl3Application(
                JojoGame(GameLaunchConfiguration(
                    entryPoint = when {
                        args.contains("--battle") || fullBattleTraceConfig != null -> GameEntryPoint.BATTLE
                        screenshotState == null && yingchuanEntryFlowTracePath == null && !args.contains("--verify") && !hasExplicitScenario && choiceScript.isEmpty() && globals.isEmpty() && variables.isEmpty() && randomSequence.isEmpty() && startScene == "scene1" -> GameEntryPoint.TITLE
                        else -> GameEntryPoint.SCENARIO
                    },
                    initialScenario = scenario,
                    battleReturnScenario = battleReturnScenario,
                    initialScenarioExplicit = hasExplicitScenario,
                    scenarioRun = ScenarioRunConfiguration(
                        choices = choiceScript,
                        allowPendingChoiceAfterScript = args.contains("--verify-stop-at-choice"),
                        globals = globals,
                        unitAttributes = unitAttributes,
                        variables = variables,
                        ambition = ambition,
                        randomSequence = randomSequence,
                        infoTransferRandomSequence = infoTransferRandomSequence,
                        battleRound = battleRound,
                        battleCamp = battleCamp,
                        battleAttributes = battleAttributes,
                        battlePositions = battlePositions,
                        battlePositionsByCamp = battlePositionsByCamp,
                        battleEnemyDefeated = enemyDefeated,
                        startScene = startScene,
                        startLabel = startLabel,
                        choiceTracePath = choiceTracePath,
                        randomTracePath = randomTracePath,
                        stopAfterRandomTrace = args.contains("--verify-stop-after-random"),
                        stopAfterRandomTraceCount = stopAfterRandomTraceCount,
                    ),
                    verification = VerificationConfiguration(
                        scenario = args.contains("--verify"),
                        battle = args.contains("--verify-battle"),
                        firstBranch = args.contains("--verify-branch"),
                        alternateBranch = args.contains("--verify-branch-2"),
                        scriptedBattle = args.contains("--verify-scripted-battle"),
                    ),
                    capture = RenderCaptureConfiguration(
                        screenshotPath = screenshotPath,
                        rawCapturePath = rawCapturePath,
                        mapTextureDumpPath = mapTextureDumpPath,
                        mapDither = mapDither,
                        mapFilter = mapFilter,
                        mapSampler = mapSampler,
                        mapSampleOffset = mapSampleOffset,
                        compositionTracePath = compositionTracePath,
                        renderEventLogPath = renderEventLogPath,
                        state = screenshotState,
                    ),
                    fullBattleTrace = fullBattleTraceConfig,
                    yingchuanEntryFlowTracePath = yingchuanEntryFlowTracePath,
                    automatedRun = automatedRun,
                )),
                configuration
            )
        }
    }
}
