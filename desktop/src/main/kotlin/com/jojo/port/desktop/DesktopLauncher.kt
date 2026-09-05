package com.jojo.port.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.port.JojoGame

object DesktopLauncher {
    @JvmStatic
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
        val campaignE2eTracePath = args.firstOrNull { it.startsWith("--campaign-e2e-trace=") }?.substringAfter('=')
        val campaignE2eStopPoint = args.firstOrNull { it.startsWith("--campaign-e2e-stop=") }
            ?.substringAfter('=')
            ?.split(':', limit = 2)
            ?.let { fields ->
                require(fields.size == 2 && fields[0].matches(Regex("R_[0-9]{2}"))) {
                    "--campaign-e2e-stop uses R_NN:sceneIndex"
                }
                com.jojo.port.CampaignE2eStopPoint(fields[0], fields[1].toInt())
            }
        val fullBattleTraceConfig = fullBattleTracePath?.let { output ->
            com.jojo.port.FullBattleTraceConfig(
                outputPath = output,
                scenario = scenario.replaceFirst("R_", "S_"),
                toolSeed = args.firstOrNull { it.startsWith("--full-battle-seed=") }?.substringAfter('=')?.toInt() ?: 1000,
                mathSeed = args.firstOrNull { it.startsWith("--full-battle-math-seed=") }?.substringAfter('=')?.toLong() ?: 0x12345678L,
                timeScale = args.firstOrNull { it.startsWith("--full-battle-time-scale=") }?.substringAfter('=')?.toFloat() ?: 8f,
                maxSimulationSeconds = args.firstOrNull { it.startsWith("--full-battle-max-sim-seconds=") }?.substringAfter('=')?.toFloat() ?: 1800f,
                exitOnFinish = campaignE2eTracePath == null,
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
                argument.startsWith("--campaign-e2e-trace=") ||
                argument.startsWith("--yingchuan-entry-flow-trace=") ||
                argument.startsWith("--choice-trace=") ||
                argument.startsWith("--random-trace=")
        }
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("삼국지 조조전 · LibGDX Port")
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
            val logicTraceRun = fullBattleTracePath != null || campaignE2eTracePath != null
            useVsync(!logicTraceRun)
            if (logicTraceRun) setIdleFPS(60)
            setForegroundFPS(60)
        }.also { configuration ->
            Lwjgl3Application(
                JojoGame(
                    verifyMode = args.contains("--verify"),
                    battleVerifyMode = args.contains("--verify-battle"),
                    scenarioBranchVerifyMode = args.contains("--verify-branch"),
                    alternateScenarioBranchVerifyMode = args.contains("--verify-branch-2"),
                    scenarioChoiceScript = choiceScript,
                    scenarioAllowPendingChoiceAfterScript = args.contains("--verify-stop-at-choice"),
                    scenarioGlobals = globals,
                    scenarioVariables = variables,
                    scenarioAmbition = ambition,
                    scenarioRandomSequence = randomSequence,
                    scenarioBattleRound = battleRound,
                    scenarioBattleCamp = battleCamp,
                    scenarioBattleAttributes = battleAttributes,
                    scenarioBattlePositions = battlePositions,
                    scenarioBattlePositionsByCamp = battlePositionsByCamp,
                    scenarioBattleEnemyDefeated = enemyDefeated,
                    scenarioStartScene = startScene,
                    scenarioStartLabel = startLabel,
                    scenarioChoiceTracePath = choiceTracePath,
                    scenarioRandomTracePath = randomTracePath,
                    scenarioStopAfterRandomTrace = args.contains("--verify-stop-after-random"),
                    scenarioStopAfterRandomTraceCount = stopAfterRandomTraceCount,
                    scenarioInfoTransferRandomSequence = infoTransferRandomSequence,
                    scenarioUnitAttributes = unitAttributes,
                    initialScenario = scenario,
                    battleReturnScenario = battleReturnScenario,
                    initialScenarioExplicit = hasExplicitScenario,
                    // A bare desktop launch opens the title screen, but the
                    // documented `--verify` command must enter R_00 so its
                    // self-check can complete and exit (including from the
                    // packaged app image).
                    // A capture state is an explicit deterministic route.  It
                    // must not be shadowed by the ordinary bare-launch title
                    // default (otherwise every --capture-state fixture was
                    // captured from the title screen).
                    startAtTitle = campaignE2eTracePath != null || (screenshotState == null && yingchuanEntryFlowTracePath == null && !args.contains("--verify") && !hasExplicitScenario && choiceScript.isEmpty() && globals.isEmpty() && variables.isEmpty() && randomSequence.isEmpty() && startScene == "scene1"),
                    startAtBattle = args.contains("--battle") || (fullBattleTraceConfig != null && campaignE2eTracePath == null),
                    allScenariosVerifyMode = args.contains("--verify-all-scenarios"),
                    scriptedBattleVerifyMode = args.contains("--verify-scripted-battle"),
                    screenshotPath = screenshotPath,
                    rawCapturePath = rawCapturePath,
                    mapTextureDumpPath = mapTextureDumpPath,
                    mapDither = mapDither,
                    mapFilter = mapFilter,
                    mapSampler = mapSampler,
                    mapSampleOffset = mapSampleOffset,
                    compositionTracePath = compositionTracePath,
                    renderEventLogPath = renderEventLogPath,
                    screenshotState = screenshotState,
                    fullBattleTraceConfig = fullBattleTraceConfig,
                    yingchuanEntryFlowTracePath = yingchuanEntryFlowTracePath,
                    campaignE2eTraceConfig = campaignE2eTracePath?.let {
                        val stopPoint = campaignE2eStopPoint ?: com.jojo.port.CampaignE2eStopPoint()
                        com.jojo.port.CampaignE2eTraceConfig(
                            outputPath = it,
                            maxSeconds = if (campaignE2eStopPoint == null) 900f else 3600f,
                            stopAt = stopPoint,
                            requireYingchuanBootstrapContract = campaignE2eStopPoint == null,
                        )
                    },
                    automatedRun = automatedRun,
                ),
                configuration
            )
        }
    }
}
