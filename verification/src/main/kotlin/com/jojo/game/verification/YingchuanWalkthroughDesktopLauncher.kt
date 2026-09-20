package com.jojo.game.verification

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.application.runtime.RuntimeBattleCommand
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeBattleFrame
import com.jojo.game.application.runtime.RuntimeGridPoint
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.RuntimeScreenProbe
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.domain.battle.Faction
import java.io.File
import java.security.MessageDigest
import kotlin.math.abs

/** Records a short, production-input Yingchuan walkthrough for visual review. */
object YingchuanWalkthroughDesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = File(args.firstOrNull() ?: "verification/build/verification/yingchuan-walkthrough")
            .absoluteFile
        val maxSimulationSeconds = args.getOrNull(1)?.toFloat() ?: 30f
        require(maxSimulationSeconds in 1f..1800f) { "walkthrough duration must be 1..1800 simulated seconds" }
        val timeScale = args.getOrNull(2)?.toFloat() ?: 1f
        require(timeScale in .25f..8f) { "walkthrough time scale must be .25..8" }
        val captureMode = args.getOrNull(3) ?: "semantic-walkthrough"
        require(captureMode in setOf(
            "semantic-walkthrough", "first-normal-combat", "next-normal-actions", "enemy-first-combat", "enemy-settlement",
            "first-round-end", "round2-handoff", "single-player-action",
        )) {
            "unknown walkthrough capture mode: $captureMode"
        }
        require(captureMode == "semantic-walkthrough" || timeScale == 1f) {
            "$captureMode requires normal speed"
        }
        require(captureMode != "single-player-action" || maxSimulationSeconds == 180f) {
            "single-player-action requires exactly 180 simulated seconds"
        }
        outputDirectory.mkdirs()
        val trace = File(outputDirectory, "yingchuan-manual-trace.json")
        val options = VerificationDesktopLaunchOptions.parse(
            arrayOf(
                "--battle",
                "--scenario=S_00",
                "--full-battle-trace=${trace.absolutePath}",
                "--full-battle-time-scale=$timeScale",
                "--full-battle-max-sim-seconds=$maxSimulationSeconds",
                "--full-battle-seed=1000",
                "--full-battle-math-seed=305419896",
            ),
        )
        val baseConfiguration = options.toGameConfiguration()
        val driver = YingchuanWalkthroughDriver(captureMode)
        val recorder = WalkthroughRecorder(outputDirectory, driver, maxSimulationSeconds, timeScale, captureMode)
        val configuration = baseConfiguration.copy(runtimeBattleDriver = driver, runtimeScreenObserver = recorder)
        val game = JojoGame(configuration)
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Yingchuan manual walkthrough")
            setWindowedMode(1280, 688)
            setWindowPosition(0, 0)
            setForegroundFPS(60)
            setIdleFPS(60)
            useVsync(true)
        }
        Lwjgl3Application(RecordingApplication(game, recorder), window)
    }
}

private class RecordingApplication(
    private val game: JojoGame,
    private val recorder: WalkthroughRecorder,
) : ApplicationListener {
    override fun create() = game.create()
    override fun resize(width: Int, height: Int) = game.resize(width, height)
    override fun render() {
        try {
            game.render()
        } catch (failure: Throwable) {
            recorder.finish()
            throw failure
        }
    }
    override fun pause() = game.pause()
    override fun resume() = game.resume()
    override fun dispose() {
        try {
            recorder.finish()
        } finally {
            game.dispose()
        }
    }
}

private class WalkthroughRecorder(
    private val outputDirectory: File,
    private val driver: YingchuanWalkthroughDriver,
    private val maxSimulationSeconds: Float,
    private val timeScale: Float,
    private val captureMode: String,
) : RuntimeScreenObserver {
    private data class PendingCapture(
        val key: String,
        val frame: Long,
        val elapsedSeconds: Double,
        val width: Int,
        val height: Int,
        val rgba: ByteArray,
        val state: JsonValue,
    )

    private val captures = mutableListOf<PendingCapture>()
    private val capturedKeys = linkedSetOf<String>()
    private val transitions = JsonValue(JsonValue.ValueType.array)
    private var previousSignature: String? = null
    private var elapsedSeconds = 0.0
    private var frame = 0L
    private var finished = false
    private var combatAnchorSeconds: Double? = null
    private var nextCombatCapture = 0
    private val nextActionAnchors = mutableMapOf<Int, Double>()
    private val nextActionCaptureCounts = mutableMapOf<Int, Int>()
    private var enemyArrivalAnchorSeconds: Double? = null
    private var nextEnemyCapture = 0
    private val previousFirstRoundEnemyHasActed = mutableMapOf<Int, Boolean>()
    private var round2PreviousPlayback: PlaybackState? = null
    private var round2FirstDialogueObserved = false
    private var round2FirstDialogueClosed = false
    private var round2FirstDialogueClosedAt: Double? = null
    private var round2FirstPostDialogueOffset = 0
    private var round2Unit3DialogueObserved = false
    private var round2Unit3DialogueClosed = false
    private var round2Unit3DialogueClosedAt: Double? = null
    private var round2Unit3PostDialogueOffset = 0
    private var round2NewUnitsVisible = false
    private var round2NewUnitPositionChanged = false
    private var previousSingleActionPhase: String? = null

    override fun update(delta: Float, screen: RuntimeScreenProbe) {
        elapsedSeconds += delta.toDouble()
        frame += 1
        val probe = screen as? BattleRuntimeScreenProbe ?: return
        val signature = listOf(
            probe.playback.name,
            probe.bootstrapComplete,
            probe.round,
            probe.activeFaction.name,
            probe.turnPhase,
            probe.selectedUnitId,
            probe.playerMoveCommitted,
            probe.committedPlayerMove,
            probe.battleCommandOpen,
            probe.battleTargetSelectionOpen,
            probe.battleMenuOpen,
            probe.autoBattleOverlay,
            probe.winConditionsOpen,
            probe.outcome?.toString(),
        ).joinToString("|")
        if (signature != previousSignature) {
            transitions.addChild(stateRow(probe, null))
            previousSignature = signature
        }

        when (captureMode) {
            "first-normal-combat" -> captureFirstNormalCombat(probe)
            "next-normal-actions" -> captureNextNormalActions(probe)
            "enemy-first-combat" -> captureEnemyFirstCombat(probe)
            "enemy-settlement" -> captureEnemySettlement(probe)
            "first-round-end" -> captureFirstRoundEnd(probe)
            "round2-handoff" -> captureRound2Handoff(probe)
            "single-player-action" -> captureSinglePlayerAction(probe)
            else -> {
                semanticKeys(probe).firstOrNull { it !in capturedKeys }?.let { key ->
                    if (captures.size < MAX_CAPTURES) capture(key, probe)
                }
            }
        }
    }

    private fun captureFirstNormalCombat(probe: BattleRuntimeScreenProbe) {
        if (combatAnchorSeconds == null && probe.bootstrapComplete && probe.turnPhase == "AI" &&
            probe.activeFaction == Faction.FRIEND
        ) {
            combatAnchorSeconds = elapsedSeconds
        }
        val anchor = combatAnchorSeconds ?: return
        if (nextCombatCapture >= MAX_CAPTURES) return
        if (elapsedSeconds + 1e-9 < anchor + nextCombatCapture * COMBAT_CAPTURE_INTERVAL_SECONDS) return
        val ordinal = nextCombatCapture++
        capture("first-normal-combat-${ordinal.toString().padStart(2, '0')}", probe)
    }

    private fun captureNextNormalActions(probe: BattleRuntimeScreenProbe) {
        val destinations = mapOf(211 to (9 to 16), 234 to (10 to 17))
        destinations.forEach { (characterId, destination) ->
            val unit = probe.battle.snapshot.units.firstOrNull { it.characterId == characterId }
            if (unit != null && unit.x == destination.first && unit.y == destination.second) {
                nextActionAnchors.putIfAbsent(characterId, elapsedSeconds)
            }
        }
        for (characterId in listOf(211, 234)) {
            val anchor = nextActionAnchors[characterId] ?: continue
            val index = nextActionCaptureCounts[characterId] ?: 0
            if (index >= NEXT_ACTION_OFFSETS_SECONDS.size) continue
            if (elapsedSeconds + 1e-9 < anchor + NEXT_ACTION_OFFSETS_SECONDS[index]) continue
            capture("next-normal-$characterId-${index.toString().padStart(2, '0')}", probe)
            nextActionCaptureCounts[characterId] = index + 1
            return
        }
    }

    private fun captureEnemyFirstCombat(probe: BattleRuntimeScreenProbe) {
        val unit = probe.battle.snapshot.units.firstOrNull { it.characterId == 474 }
        if (enemyArrivalAnchorSeconds == null && unit != null && unit.x == 9 && unit.y == 17) {
            enemyArrivalAnchorSeconds = elapsedSeconds
        }
        val anchor = enemyArrivalAnchorSeconds ?: return
        if (nextEnemyCapture >= ENEMY_CAPTURE_OFFSETS_SECONDS.size) return
        if (elapsedSeconds + 1e-9 < anchor + ENEMY_CAPTURE_OFFSETS_SECONDS[nextEnemyCapture]) return
        val ordinal = nextEnemyCapture++
        capture("enemy-first-combat-${ordinal.toString().padStart(2, '0')}", probe)
    }

    private fun captureEnemySettlement(probe: BattleRuntimeScreenProbe) {
        val unit = probe.battle.snapshot.units.firstOrNull { it.characterId == 477 }
        if (enemyArrivalAnchorSeconds == null && unit != null && unit.x == 11 && unit.y == 15) {
            enemyArrivalAnchorSeconds = elapsedSeconds
        }
        val anchor = enemyArrivalAnchorSeconds ?: return
        if (nextEnemyCapture >= MAX_CAPTURES) return
        if (elapsedSeconds < anchor + 2.5 + nextEnemyCapture * .25) return
        capture("enemy-settlement-${(nextEnemyCapture++).toString().padStart(2, '0')}", probe)
    }

    /**
     * Records state boundaries at the end of the first enemy round. A unit row is captured when
     * its domain `hasActed` value first commits; this is not labelled as an attack animation frame.
     * The reinforcement camp is recorded only if it survives as an observable post-render state.
     */
    private fun captureFirstRoundEnd(probe: BattleRuntimeScreenProbe) {
        val tracked = probe.battle.snapshot.units.filter { it.characterId in FIRST_ROUND_END_CHARACTER_IDS }
        if (probe.round == 1 && probe.activeFaction == Faction.ENEMY) {
            tracked.forEach { unit ->
                val characterId = unit.characterId ?: return@forEach
                val previous = previousFirstRoundEnemyHasActed[characterId]
                if (previous == false && unit.hasActed) {
                    captureOnce("round1-enemy-action-committed-$characterId", probe)
                }
                previousFirstRoundEnemyHasActed[characterId] = unit.hasActed
            }
        } else if (probe.round <= 1) {
            tracked.forEach { unit ->
                unit.characterId?.let { previousFirstRoundEnemyHasActed.putIfAbsent(it, unit.hasActed) }
            }
        }

        if (probe.round == 1 && probe.activeFaction == Faction.REINFORCEMENTS) {
            captureOnce("round1-reinforcements-camp-observed", probe)
        }
        if (probe.round == 2) captureOnce("round2-start-observed", probe)
        if (probe.round == 2 && probe.playback == PlaybackState.DIALOGUE) {
            captureOnce("round2-start-dialogue", probe)
        }
        if (probe.round == 2 && probe.turnPhase == "CAMP_CARD") {
            captureOnce("round2-camp-card-banner", probe)
        }
    }

    private fun captureOnce(key: String, probe: BattleRuntimeScreenProbe): Boolean {
        if (key in capturedKeys || captures.size >= MAX_CAPTURES) return false
        capture(key, probe)
        return true
    }

    /** Captures only post-render states exposed by the production battle probe during round two. */
    private fun captureRound2Handoff(probe: BattleRuntimeScreenProbe) {
        if (probe.round < 2) {
            round2PreviousPlayback = probe.playback
            return
        }

        val previousPlayback = round2PreviousPlayback
        if (!round2FirstDialogueObserved && probe.round == 2 && probe.playback == PlaybackState.DIALOGUE) {
            round2FirstDialogueObserved = true
            captureOnce("round2-first-dialogue-observed", probe)
        }
        if (round2FirstDialogueObserved && !round2FirstDialogueClosed &&
            previousPlayback == PlaybackState.DIALOGUE && probe.playback != PlaybackState.DIALOGUE
        ) {
            round2FirstDialogueClosed = true
            round2FirstDialogueClosedAt = elapsedSeconds
        }
        round2FirstDialogueClosedAt?.let { closedAt ->
            if (round2FirstPostDialogueOffset < ROUND2_POST_DIALOGUE_OFFSETS_SECONDS.size &&
                elapsedSeconds + 1e-9 >= closedAt + ROUND2_POST_DIALOGUE_OFFSETS_SECONDS[round2FirstPostDialogueOffset]
            ) {
                val offset = ROUND2_POST_DIALOGUE_OFFSET_LABELS[round2FirstPostDialogueOffset]
                captureOnce("round2-first-dialogue-closed-after-${offset}s", probe)
                round2FirstPostDialogueOffset += 1
            }
        }

        val unit3 = probe.battle.snapshot.units.firstOrNull { it.characterId == 3 }
        if (!round2Unit3DialogueObserved && unit3?.x == 8 && unit3.y == 5 &&
            probe.playback == PlaybackState.DIALOGUE
        ) {
            round2Unit3DialogueObserved = true
            captureOnce("round2-unit-3-destination-dialogue-observed", probe)
        }
        if (round2Unit3DialogueObserved && !round2Unit3DialogueClosed &&
            previousPlayback == PlaybackState.DIALOGUE && probe.playback != PlaybackState.DIALOGUE
        ) {
            round2Unit3DialogueClosed = true
            round2Unit3DialogueClosedAt = elapsedSeconds
        }
        round2Unit3DialogueClosedAt?.let { closedAt ->
            if (round2Unit3PostDialogueOffset < ROUND2_POST_DIALOGUE_OFFSETS_SECONDS.size &&
                elapsedSeconds + 1e-9 >= closedAt + ROUND2_POST_DIALOGUE_OFFSETS_SECONDS[round2Unit3PostDialogueOffset]
            ) {
                val offset = ROUND2_POST_DIALOGUE_OFFSET_LABELS[round2Unit3PostDialogueOffset]
                captureOnce("round2-unit-3-dialogue-closed-after-${offset}s", probe)
                round2Unit3PostDialogueOffset += 1
            }
        }

        val newUnits = probe.battle.snapshot.units.filter { it.characterId in ROUND2_NEW_CHARACTER_IDS }
        if (!round2NewUnitsVisible && ROUND2_NEW_CHARACTER_IDS.all { characterId ->
                newUnits.any { it.characterId == characterId && it.visible }
            }
        ) {
            round2NewUnitsVisible = true
            captureOnce("round2-units-0-258-259-all-visible", probe)
        }
        if (round2NewUnitsVisible && !round2NewUnitPositionChanged && newUnits.any { unit ->
                unit.characterId?.let { characterId ->
                    ROUND2_NEW_STARTS[characterId]?.let { start -> start != (unit.x to unit.y) }
                } == true
            }
        ) {
            round2NewUnitPositionChanged = true
            captureOnce("round2-units-0-258-259-position-change-observed", probe)
        }
        if (ROUND2_NEW_DESTINATIONS.all { (characterId, destination) ->
                newUnits.any { it.characterId == characterId && it.visible && (it.x to it.y) == destination }
            }
        ) {
            captureOnce("round2-units-0-258-259-destinations-observed", probe)
        }
        if (probe.winConditionsOpen) captureOnce("round2-win-conditions-open", probe)
        if (probe.round == 2 && probe.turnPhase == "PLAYER_INPUT") {
            captureOnce("round2-player-input-observed", probe)
            if (probe.selectedUnitId != null && "round2-player-input-unit-selected" !in capturedKeys) {
                captureOnce("round2-player-input-unit-selected", probe)
            }
        }
        round2PreviousPlayback = probe.playback
    }


    /** Captures each post-render boundary of the dedicated round-two player action. */
    private fun captureSinglePlayerAction(probe: BattleRuntimeScreenProbe) {
        val phase = driver.singlePlayerActionPhase
        if (phase == previousSingleActionPhase) return
        previousSingleActionPhase = phase
        val key = when (phase) {
            "READY" -> "single-action-round2-input-ready"
            "SELECT_SENT" -> "single-action-unit-0-selected"
            "MOVE_SENT" -> "single-action-move-to-11-5-sent"
            "COMMAND_READY" -> "single-action-command-open"
            "ATTACK_COMMAND_SENT" -> "single-action-attack-targeting-open"
            "TARGET_SENT" -> "single-action-target-484-input-sent"
            "COMPLETE" -> "single-action-settlement-complete-terminal"
            "FAILED" -> "single-action-failed"
            else -> return
        }
        captureOnce(key, probe)
    }

    private fun semanticKeys(probe: BattleRuntimeScreenProbe): List<String> = buildList {
        if (probe.playback == PlaybackState.DIALOGUE) add("first-dialogue")
        if (probe.bootstrapComplete && probe.playback != PlaybackState.DIALOGUE) add("map-ready")
        if (probe.selectedUnitId != null) add("player-selected")
        if (probe.playerMoveCommitted || probe.committedPlayerMove != null) add("player-moving")
        if (probe.battleTargetSelectionOpen) add("attack-targeting")
        if (probe.battleCommandOpen) add("command-menu")
        if (probe.battleMenuOpen) add("battle-menu")
        if (probe.autoBattleOverlay == "PROMPT") add("end-turn-prompt")
        if (probe.turnPhase == "AI") add("turn-ai")
        if (probe.turnPhase == "PLAYER_INPUT") add("turn-player-input")
        if (probe.round > 1) add("later-round")
        if (probe.outcome != null) add("outcome-${probe.outcome.toString().lowercase()}")
        if (driver.lastAction?.startsWith("attack-") == true) add("player-attack")
        if (timeScale == 1f && elapsedSeconds >= 3.8) add("scripted-hit-hold")
        val checkpoint = (elapsedSeconds / 5.0).toInt() * 5
        if (timeScale == 1f && checkpoint in 10..30) {
            add("checkpoint-${checkpoint.toString().padStart(2, '0')}")
        }
    }

    private fun capture(key: String, probe: BattleRuntimeScreenProbe) {
        val width = Gdx.graphics.backBufferWidth
        val height = Gdx.graphics.backBufferHeight
        val rgba = ScreenUtils.getFrameBufferPixels(0, 0, width, height, false)
        capturedKeys += key
        captures += PendingCapture(key, frame, elapsedSeconds, width, height, rgba, stateRow(probe, key))
        Gdx.app.log("JojoGame", "YINGCHUAN_WALKTHROUGH_CAPTURE key=$key frame=$frame")
    }

    private fun stateRow(probe: BattleRuntimeScreenProbe, key: String?): JsonValue =
        JsonValue(JsonValue.ValueType.`object`).apply {
            key?.let { addChild("key", JsonValue(it)) }
            addChild("frame", JsonValue(frame))
            addChild("elapsedSeconds", JsonValue(elapsedSeconds))
            addChild("playback", JsonValue(probe.playback.name))
            addChild("bootstrapComplete", JsonValue(probe.bootstrapComplete))
            addChild("round", JsonValue(probe.round.toLong()))
            addChild("activeFaction", JsonValue(probe.activeFaction.name))
            addChild("turnPhase", JsonValue(probe.turnPhase))
            addChild("selectedUnitId", probe.selectedUnitId?.let(::JsonValue) ?: JsonValue(JsonValue.ValueType.nullValue))
            addChild("playerPresentationReady", JsonValue(probe.playerPresentationReady))
            addChild("singlePlayerActionPhase", JsonValue(driver.singlePlayerActionPhase))
            addChild("playerMoveCommitted", JsonValue(probe.playerMoveCommitted))
            addChild("committedPlayerMove", probe.committedPlayerMove?.let(::JsonValue) ?: JsonValue(JsonValue.ValueType.nullValue))
            addChild("battleCommandOpen", JsonValue(probe.battleCommandOpen))
            addChild("battleTargetSelectionOpen", JsonValue(probe.battleTargetSelectionOpen))
            addChild("battleMenuOpen", JsonValue(probe.battleMenuOpen))
            addChild("autoBattleOverlay", JsonValue(probe.autoBattleOverlay))
            addChild("winConditionsOpen", JsonValue(probe.winConditionsOpen))
            addChild("outcome", probe.outcome?.toString()?.let(::JsonValue) ?: JsonValue(JsonValue.ValueType.nullValue))
            val units = JsonValue(JsonValue.ValueType.array)
            probe.battle.snapshot.units.filter { it.visible }.forEach { unit ->
                units.addChild(JsonValue(JsonValue.ValueType.`object`).apply {
                    addChild("id", JsonValue(unit.id))
                    addChild("characterId", unit.characterId?.let { JsonValue(it.toLong()) }
                        ?: JsonValue(JsonValue.ValueType.nullValue))
                    addChild("faction", JsonValue(unit.effectiveFaction.name))
                    addChild("x", JsonValue(unit.x.toLong()))
                    addChild("y", JsonValue(unit.y.toLong()))
                    addChild("hitPoints", JsonValue(unit.hitPoints.toLong()))
                    addChild("hasActed", JsonValue(unit.hasActed))
                })
            }
            addChild("units", units)
        }

    fun finish() {
        if (finished) return
        finished = true
        outputDirectory.mkdirs()
        val captureRows = JsonValue(JsonValue.ValueType.array)
        captures.forEachIndexed { index, capture ->
            val fileName = "walkthrough-${(index + 1).toString().padStart(2, '0')}-${capture.key}.png"
            val target = File(outputDirectory, fileName)
            writeTopDownPng(capture, target)
            captureRows.addChild(capture.state.apply {
                addChild("file", JsonValue(fileName))
                addChild("width", JsonValue(capture.width.toLong()))
                addChild("height", JsonValue(capture.height.toLong()))
                addChild("origin", JsonValue("top-left"))
                addChild("rgbaSha256", JsonValue(sha256(capture.rgba)))
                addChild("pngSha256", JsonValue(sha256(target.readBytes())))
            })
        }
        val manifest = JsonValue(JsonValue.ValueType.`object`).apply {
            addChild("contract", JsonValue("yingchuan-production-input-walkthrough-v1"))
            addChild("scenario", JsonValue("S_00"))
            addChild("driverClassName", JsonValue(driver.javaClass.name))
            addChild("driverInputs", driver.inputJournal())
            addChild("singlePlayerActionPhase", JsonValue(driver.singlePlayerActionPhase))
            addChild("singlePlayerActionComplete", JsonValue(driver.singlePlayerActionComplete))
            addChild("singlePlayerActionFailure", driver.singlePlayerActionFailure?.let(::JsonValue)
                ?: JsonValue(JsonValue.ValueType.nullValue))
            addChild("singlePlayerActionRequestedAction", driver.singlePlayerActionRequestedActionJson())
            addChild("singlePlayerActionReachableTiles", driver.singlePlayerActionReachableTilesJson())
            addChild("singlePlayerActionTargetHitPointsBefore", driver.singlePlayerActionTargetHitPointsBefore?.let {
                JsonValue(it.toLong())
            } ?: JsonValue(JsonValue.ValueType.nullValue))
            addChild("singlePlayerActionTargetHitPointsAfter", driver.singlePlayerActionTargetHitPointsAfter?.let {
                JsonValue(it.toLong())
            } ?: JsonValue(JsonValue.ValueType.nullValue))
            addChild("timeScale", JsonValue(timeScale.toDouble()))
            addChild("captureMode", JsonValue(captureMode))
            addChild("maxSimulationSeconds", JsonValue(maxSimulationSeconds.toDouble()))
            addChild("frameCount", JsonValue(frame))
            addChild("elapsedSeconds", JsonValue(elapsedSeconds))
            addChild("captureLimit", JsonValue(MAX_CAPTURES.toLong()))
            addChild("captures", captureRows)
            addChild("transitions", transitions)
        }
        File(outputDirectory, "yingchuan-walkthrough.json").writeText(
            manifest.prettyPrint(JsonWriter.OutputType.json, 120),
        )
    }

    private fun writeTopDownPng(capture: PendingCapture, target: File) {
        val bottomUp = Pixmap(capture.width, capture.height, Pixmap.Format.RGBA8888)
        bottomUp.pixels.put(capture.rgba).flip()
        val topDown = Pixmap(capture.width, capture.height, Pixmap.Format.RGBA8888)
        for (y in 0 until capture.height) for (x in 0 until capture.width) {
            topDown.drawPixel(x, capture.height - 1 - y, bottomUp.getPixel(x, y))
        }
        bottomUp.dispose()
        PixmapIO.writePNG(com.badlogic.gdx.files.FileHandle(target), topDown)
        topDown.dispose()
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }

    private companion object {
        const val MAX_CAPTURES = 12
        const val COMBAT_CAPTURE_INTERVAL_SECONDS = .3
        val NEXT_ACTION_OFFSETS_SECONDS = doubleArrayOf(0.0, .5, 1.0, 1.5, 2.2, 3.2)
        val ENEMY_CAPTURE_OFFSETS_SECONDS = doubleArrayOf(0.0, .15, .3, .6, 1.0, 1.5, 2.2, 3.0, 4.0, 5.0, 6.0, 7.0)
        val FIRST_ROUND_END_CHARACTER_IDS = setOf(484, 485, 475, 476)
        val ROUND2_NEW_CHARACTER_IDS = setOf(0, 258, 259)
        val ROUND2_NEW_STARTS = mapOf(0 to (7 to 0), 258 to (6 to 0), 259 to (5 to 0))
        // Scenario requests unit 0 at (10,6), but source findEmptyPos observes 484 occupying it
        // and resolves the actual destination to (10,5).
        val ROUND2_NEW_DESTINATIONS = mapOf(0 to (10 to 5), 258 to (9 to 7), 259 to (10 to 7))
        val ROUND2_POST_DIALOGUE_OFFSETS_SECONDS = doubleArrayOf(.15, 1.2)
        val ROUND2_POST_DIALOGUE_OFFSET_LABELS = arrayOf("0.15", "1.20")
    }
}

/** Uses only the stable runtime probe and production InputProcessor to play the walkthrough. */
private class YingchuanWalkthroughDriver(
    private val captureMode: String,
) : RuntimeBattleDriver {
    private enum class SinglePlayerActionPhase {
        WAIT_ROUND2, READY, SELECT_SENT, MOVE_SENT, COMMAND_READY, ATTACK_COMMAND_SENT, TARGET_SENT, COMPLETE, FAILED,
    }

    private class SinglePlayerActionFailure(message: String) : RuntimeException(message)

    private var nextTapAt = Float.NEGATIVE_INFINITY
    private var seenRound = -1
    private val movedThisTurn = mutableSetOf<String>()
    private val journal = JsonValue(JsonValue.ValueType.array)
    var lastAction: String? = null
        private set
    private var singleActionPhase = SinglePlayerActionPhase.WAIT_ROUND2
    private var singleActorId: String? = null
    private var singleTargetId: String? = null
    var singlePlayerActionTargetHitPointsBefore: Int? = null
        private set
    var singlePlayerActionTargetHitPointsAfter: Int? = null
        private set
    var singlePlayerActionFailure: String? = null
        private set
    private var singlePlayerActionReachableTiles: List<RuntimeGridPoint> = emptyList()
    val singlePlayerActionPhase: String get() = singleActionPhase.name
    val singlePlayerActionComplete: Boolean get() = singleActionPhase == SinglePlayerActionPhase.COMPLETE

    override fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand> {
        if (probe.outcome != null || frame.elapsed < nextTapAt) return emptyList()
        if (probe.playback == PlaybackState.DIALOGUE) {
            tap(frame, "advance-dialogue", probe.battleMenuButtonScreenX, probe.battleMenuButtonScreenY + 200)
            nextTapAt = frame.elapsed + .35f
            return emptyList()
        }
        if (probe.winConditionsOpen) {
            tap(frame, "close-win-conditions", probe.winConditionButtonScreenX, probe.winConditionButtonScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (!probe.bootstrapComplete || probe.collocation) return emptyList()
        if (captureMode == "single-player-action" &&
            (probe.round >= 2 || singleActionPhase != SinglePlayerActionPhase.WAIT_ROUND2)
        ) {
            try {
                driveSinglePlayerAction(frame, probe)
            } catch (_: SinglePlayerActionFailure) {
                // Expected fail-closed validation remains observable until the natural trace timeout.
            }
            return emptyList()
        }
        if (probe.round != seenRound) {
            seenRound = probe.round
            movedThisTurn.clear()
        }
        if (probe.autoBattleOverlay == "PROMPT") {
            tap(frame, "confirm-end-turn", probe.autoBattleConfirmScreenX, probe.autoBattleConfirmScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (probe.autoBattleOverlay != "NONE") return emptyList()
        if (probe.battleCommandOpen) {
            tap(frame, "command-wait", probe.commandWaitScreenX, probe.commandWaitScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (probe.battleMenuOpen) {
            tap(frame, "menu-end-round", probe.menuEndRoundScreenX, probe.menuEndRoundScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (probe.turnPhase != "PLAYER_INPUT") return emptyList()

        val units = probe.battle.snapshot.units
        val enemies = units.filter {
            it.visible && it.hitPoints > 0 && it.effectiveFaction.isEnemyOf(Faction.PLAYER)
        }
        val mine = units.filter {
            it.visible && it.hitPoints > 0 && it.effectiveFaction == Faction.PLAYER && !it.hasActed
        }
        val actor = mine.firstOrNull { it.id !in movedThisTurn || adjacentEnemy(it, enemies) != null }
        if (actor == null) {
            tap(frame, "open-battle-menu", probe.battleMenuButtonScreenX, probe.battleMenuButtonScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (probe.selectedUnitId != actor.id) {
            tapTile(frame, "select-${actor.id}", probe, actor.x, actor.y)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        adjacentEnemy(actor, enemies)?.let { target ->
            tapTile(frame, "attack-${actor.id}-${target.id}", probe, target.x, target.y)
            nextTapAt = frame.elapsed + ACTION_INTERVAL
            return emptyList()
        }
        if (actor.id in movedThisTurn) {
            // A moved unit enters CommandLayer. The branch above commits WAIT; this is only a
            // defensive retry if the command layer has not appeared yet.
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        val nearest = enemies.minByOrNull { abs(it.x - actor.x) + abs(it.y - actor.y) }
        val reachable = probe.battle.reachableTiles(actor.id).filter { tile ->
            units.none { it.visible && it.hitPoints > 0 && it.x == tile.x && it.y == tile.y && it.id != actor.id }
        }
        val destination = nearest?.let { enemy ->
            reachable.minByOrNull { abs(it.x - enemy.x) + abs(it.y - enemy.y) }
        }
        movedThisTurn += actor.id
        if (destination == null || (destination.x == actor.x && destination.y == actor.y)) {
            tap(frame, "open-battle-menu", probe.battleMenuButtonScreenX, probe.battleMenuButtonScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        tapTile(frame, "move-${actor.id}-${destination.x}-${destination.y}", probe, destination.x, destination.y)
        nextTapAt = frame.elapsed + ACTION_INTERVAL
        return emptyList()
    }

    private fun driveSinglePlayerAction(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe) {
        if (singleActionPhase == SinglePlayerActionPhase.COMPLETE || singleActionPhase == SinglePlayerActionPhase.FAILED) return
        if (probe.round > 2) failSingleAction("round advanced past 2 before the fixed player action completed")
        if (frame.elapsed < nextTapAt) return

        val units = probe.battle.snapshot.units
        val actor = units.firstOrNull { it.visible && it.hitPoints > 0 && it.characterId == 0 }
        val destination = SINGLE_PLAYER_ACTION_DESTINATION

        when (singleActionPhase) {
            SinglePlayerActionPhase.WAIT_ROUND2 -> {
                if (probe.round != 2 || probe.turnPhase != "PLAYER_INPUT" || !probe.playerPresentationReady ||
                    probe.playback != PlaybackState.COMPLETE || probe.winConditionsOpen
                ) return
                val readyActor = actor ?: failSingleAction("visible living character 0 was not found at player input")
                singlePlayerActionReachableTiles = probe.battle.reachableTiles(readyActor.id)
                    .sortedWith(compareBy(RuntimeGridPoint::x, RuntimeGridPoint::y))
                if (destination !in singlePlayerActionReachableTiles) {
                    failSingleAction("character 0 cannot reach required destination (11,5)")
                }
                singleActorId = readyActor.id
                singleActionPhase = SinglePlayerActionPhase.READY
            }

            SinglePlayerActionPhase.READY -> {
                val readyActor = actor ?: failSingleAction("character 0 disappeared before selection")
                if (readyActor.id != singleActorId) failSingleAction("character 0 runtime id changed before selection")
                tapRequired(frame, "single-select-character-0", probe.battle.screenPoint(RuntimeGridPoint(readyActor.x, readyActor.y)))
                nextTapAt = frame.elapsed + TAP_INTERVAL
                singleActionPhase = SinglePlayerActionPhase.SELECT_SENT
            }

            SinglePlayerActionPhase.SELECT_SENT -> {
                val selectedActor = actor ?: failSingleAction("character 0 disappeared after selection input")
                if (probe.selectedUnitId != selectedActor.id) return
                if (destination !in probe.battle.reachableTiles(selectedActor.id)) {
                    failSingleAction("required destination (11,5) stopped being reachable after selection")
                }
                tapRequired(frame, "single-move-character-0-to-11-5", probe.battle.screenPoint(destination))
                nextTapAt = frame.elapsed + TAP_INTERVAL
                singleActionPhase = SinglePlayerActionPhase.MOVE_SENT
            }

            SinglePlayerActionPhase.MOVE_SENT -> {
                if (!probe.battleCommandOpen) return
                val movedActor = actor ?: failSingleAction("character 0 disappeared during movement")
                if (movedActor.x != destination.x || movedActor.y != destination.y) {
                    failSingleAction("CommandLayer opened before character 0 reached (11,5): (${movedActor.x},${movedActor.y})")
                }
                nextTapAt = frame.elapsed + COMMAND_OBSERVATION_SECONDS
                singleActionPhase = SinglePlayerActionPhase.COMMAND_READY
            }

            SinglePlayerActionPhase.COMMAND_READY -> {
                if (!probe.battleCommandOpen) failSingleAction("CommandLayer closed before ATTACK input")
                tapRequired(
                    frame,
                    "single-open-attack-command",
                    RuntimeGridPoint(probe.commandAttackScreenX, probe.commandAttackScreenY),
                )
                nextTapAt = frame.elapsed + TAP_INTERVAL
                singleActionPhase = SinglePlayerActionPhase.ATTACK_COMMAND_SENT
            }

            SinglePlayerActionPhase.ATTACK_COMMAND_SENT -> {
                if (!probe.battleTargetSelectionOpen) return
                val attackTarget = units.firstOrNull { it.visible && it.hitPoints > 0 && it.characterId == 484 }
                    ?: failSingleAction("visible living target character 484 was not found")
                if (attackTarget.x != 10 || attackTarget.y != 6) {
                    failSingleAction("target character 484 is not at required tile (10,6): (${attackTarget.x},${attackTarget.y})")
                }
                singleTargetId = attackTarget.id
                singlePlayerActionTargetHitPointsBefore = attackTarget.hitPoints
                tapRequired(frame, "single-attack-character-484", probe.battle.screenPoint(RuntimeGridPoint(10, 6)))
                nextTapAt = frame.elapsed + ACTION_INTERVAL
                singleActionPhase = SinglePlayerActionPhase.TARGET_SENT
            }

            SinglePlayerActionPhase.TARGET_SENT -> {
                val currentActor = units.firstOrNull { it.id == singleActorId }
                    ?: failSingleAction("character 0 disappeared during attack settlement")
                val currentTarget = units.firstOrNull { it.id == singleTargetId }
                singlePlayerActionTargetHitPointsAfter = currentTarget?.hitPoints ?: 0
                if (!currentActor.hasActed || probe.turnPhase != "PLAYER_INPUT" ||
                    !probe.playerPresentationReady || probe.selectedUnitId != null ||
                    probe.battleCommandOpen || probe.battleTargetSelectionOpen
                ) return
                singleActionPhase = SinglePlayerActionPhase.COMPLETE
            }

            SinglePlayerActionPhase.COMPLETE, SinglePlayerActionPhase.FAILED -> Unit
        }
    }

    private fun tapRequired(frame: RuntimeBattleFrame, label: String, point: RuntimeGridPoint) {
        if (!tap(frame, label, point.x, point.y)) failSingleAction("no InputProcessor was available for $label")
    }

    private fun failSingleAction(message: String): Nothing {
        singlePlayerActionFailure = message
        singleActionPhase = SinglePlayerActionPhase.FAILED
        throw SinglePlayerActionFailure(message)
    }

    fun singlePlayerActionRequestedActionJson(): JsonValue = JsonValue(JsonValue.ValueType.`object`).apply {
        addChild("actorCharacterId", JsonValue(0L))
        addChild("destinationX", JsonValue(SINGLE_PLAYER_ACTION_DESTINATION.x.toLong()))
        addChild("destinationY", JsonValue(SINGLE_PLAYER_ACTION_DESTINATION.y.toLong()))
        addChild("command", JsonValue("ATTACK"))
        addChild("targetCharacterId", JsonValue(484L))
        addChild("targetX", JsonValue(10L))
        addChild("targetY", JsonValue(6L))
    }

    fun singlePlayerActionReachableTilesJson(): JsonValue = JsonValue(JsonValue.ValueType.array).also { rows ->
        singlePlayerActionReachableTiles.forEach { tile ->
            rows.addChild(JsonValue(JsonValue.ValueType.`object`).apply {
                addChild("x", JsonValue(tile.x.toLong()))
                addChild("y", JsonValue(tile.y.toLong()))
            })
        }
    }

    fun inputJournal(): JsonValue = journal

    private fun tapTile(
        frame: RuntimeBattleFrame,
        label: String,
        probe: BattleRuntimeScreenProbe,
        x: Int,
        y: Int,
    ) {
        val point = probe.battle.screenPoint(RuntimeGridPoint(x, y))
        tap(frame, label, point.x, point.y)
    }

    private fun tap(frame: RuntimeBattleFrame, label: String, x: Int, y: Int): Boolean {
        val processor = Gdx.input.inputProcessor ?: return false
        lastAction = label
        if (journal.size < MAX_JOURNAL_ROWS) {
            journal.addChild(JsonValue(JsonValue.ValueType.`object`).apply {
                addChild("simulationSeconds", JsonValue(frame.elapsed.toDouble()))
                addChild("action", JsonValue(label))
                addChild("screenX", JsonValue(x.toLong()))
                addChild("screenY", JsonValue(y.toLong()))
            })
        }
        processor.touchDown(x, y, 0, Input.Buttons.LEFT)
        processor.touchUp(x, y, 0, Input.Buttons.LEFT)
        return true
    }

    private fun adjacentEnemy(
        actor: com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot,
        enemies: List<com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot>,
    ) = enemies.firstOrNull { enemy ->
        actor.attackOffsets.any { RuntimeGridPoint(actor.x + it.x, actor.y + it.y) == RuntimeGridPoint(enemy.x, enemy.y) }
    }

    private fun Faction.isEnemyOf(other: Faction): Boolean =
        (this == Faction.ENEMY || this == Faction.REINFORCEMENTS) !=
            (other == Faction.ENEMY || other == Faction.REINFORCEMENTS)

    private companion object {
        val SINGLE_PLAYER_ACTION_DESTINATION = RuntimeGridPoint(11, 5)
        const val COMMAND_OBSERVATION_SECONDS = .2f
        const val TAP_INTERVAL = .4f
        const val ACTION_INTERVAL = 1.5f
        const val MAX_JOURNAL_ROWS = 256
    }
}
