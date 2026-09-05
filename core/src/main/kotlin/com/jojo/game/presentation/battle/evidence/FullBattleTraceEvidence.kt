package com.jojo.game.presentation.battle.evidence

import com.jojo.game.BattleOutcome
import com.jojo.game.FullBattleTraceConfig
import com.jojo.game.FullBattleTraceDeadline
import com.jojo.game.FullBattleTraceRecorder
import com.jojo.game.NaturalBattleTransition
import com.jojo.game.PlaybackState
import com.jojo.game.domain.scenario.ScenarioMapObject
import com.jojo.game.presentation.battle.BattleEvidenceRecorder
import com.jojo.game.presentation.battle.BattleEvidenceView

/** Value-only map row handed off by BattleScreen before evidence is serialized. */
internal data class FullBattleTraceMapObject(
    val objectId: Int,
    val terrainId: Int,
    val x: Int,
    val y: Int,
    val enabled: Boolean,
)

/** Value-only copy of a single source Stage.setObjects invocation. */
internal data class FullBattleTraceMapObjectsCall(
    val enabled: Boolean,
    val terrainId: Int,
    val objects: List<FullBattleTraceMapObjectCall>,
)

internal data class FullBattleTraceMapObjectCall(val objectId: Int, val x: Int, val y: Int)
internal data class FullBattleTraceMapSnapshot(val revision: Int, val json: String)

/** Immutable barrier observation; the coordinator never reaches back into BattleScreen. */
internal data class FullBattleTraceDriveSnapshot(
    val elapsed: Float,
    val outcome: BattleOutcome?,
    val scriptState: PlaybackState,
    val traceBarrierOpen: Boolean,
    val lossSceneActive: Boolean,
    val callbackPending: Boolean,
    val scriptEnded: Boolean,
    val endProcessStarted: Boolean,
)

/** Immutable finish projection. Its JSON is intentionally owned outside the screen. */
internal data class FullBattleTraceFinishSnapshot(
    val scenario: String,
    val requestedScenario: String,
    val round: Int,
    val camp: Int,
    val ended: Boolean,
    val outcome: BattleOutcome?,
    val seededUnitIds: List<Int>,
    val loadedMapIndex: Int,
    val mapName: String,
    val mapWidth: Int,
    val mapHeight: Int,
)

internal data class FullBattleTraceFinishResult(val outputPath: String, val frameCount: Long)

/**
 * Stateful full-battle evidence boundary. It owns every trace-only cursor,
 * signature, terminal-frame counter and completion barrier; BattleScreen only
 * supplies immutable observations from its live presentation state.
 */
internal class FullBattleTraceEvidenceSession(
    private val config: FullBattleTraceConfig,
    private val recorder: FullBattleTraceRecorder,
) {
    private val deadline = FullBattleTraceDeadline(config.maxSimulationSeconds)
    private var lastDriverAt = Float.NEGATIVE_INFINITY
    private var terminalFrames = 0
    private var mapObjectRevision = 0
    private var mapObjectSignature: String? = null
    private var mapObjectsCallCursor = 0
    private var finished = false
    private var finishAfterFrame: String? = null

    fun requestFinish(reason: String) {
        if (!finished) finishAfterFrame = reason
    }

    fun consumeFinishAfterFrame(): String? = finishAfterFrame.also { finishAfterFrame = null }

    /** Preserves the old source order: deadline, cadence, barriers, then terminal stability. */
    fun drive(snapshot: FullBattleTraceDriveSnapshot) {
        if (finished) return
        deadline.timeoutReason(snapshot.elapsed, snapshot.outcome != null)?.let(::requestFinish) ?: run {
            if (snapshot.elapsed - lastDriverAt < config.driverIntervalSeconds) return
            lastDriverAt = snapshot.elapsed
            if (snapshot.traceBarrierOpen) return
            val outcome = snapshot.outcome ?: return
            if (NaturalBattleTransition.campaignLossTraceReadyToFlush(
                    exitOnFinish = config.exitOnFinish,
                    outcome = outcome,
                    loseSceneActive = snapshot.lossSceneActive,
                    scriptState = snapshot.scriptState,
                    callbackPending = snapshot.callbackPending,
                    scriptEnded = snapshot.scriptEnded,
                )
            ) return requestFinish("battle-end")
            if (!config.exitOnFinish) return
            if (NaturalBattleTransition.fullTraceTerminalReady(
                    snapshot.scriptState,
                    snapshot.callbackPending,
                    snapshot.scriptEnded,
                    snapshot.endProcessStarted,
                )
            ) terminalFrames++ else terminalFrames = 0
            if (terminalFrames >= 3) requestFinish("battle-end")
        }
    }

    fun mapSnapshot(objects: Collection<FullBattleTraceMapObject>): FullBattleTraceMapSnapshot {
        val rows = objects.asSequence().filter(FullBattleTraceMapObject::enabled)
            .sortedWith(compareBy<FullBattleTraceMapObject>({ it.x }, { it.y }, { it.objectId }, { it.terrainId }))
            .joinToString(",") { "[${it.objectId},${it.terrainId},${it.x},${it.y}]" }
        val signature = "[$rows]"
        return if (signature != mapObjectSignature) {
            mapObjectSignature = signature
            FullBattleTraceMapSnapshot(++mapObjectRevision, signature)
        } else FullBattleTraceMapSnapshot(mapObjectRevision, "null")
    }

    /** Drains each append-only source callback once, before the ordinary RAF row. */
    fun mapObjectCallObservations(calls: List<FullBattleTraceMapObjectsCall>): List<String> = buildList {
        while (mapObjectsCallCursor < calls.size) {
            val call = calls[mapObjectsCallCursor++]
            val entries = call.objects.joinToString(";") { "${it.objectId},${it.x},${it.y}" }
            add("transition:objects:${if (call.enabled) 1 else 0}:${call.terrainId}:$entries")
        }
    }

    fun nextFrame(elapsed: Float, advanceFrame: Boolean): Long =
        if (advanceFrame) recorder.nextFrame(elapsed) else recorder.upcomingFrame()

    fun record(view: BattleEvidenceView) {
        recorder.addFrame(BattleEvidenceRecorder.frame(view))
    }

    fun recordInput(context: String) {
        recorder.recordInput(context)
    }

    fun finish(reason: String, snapshot: FullBattleTraceFinishSnapshot): FullBattleTraceFinishResult? {
        if (finished) return null
        finished = true
        val expectedScript = "Game/data/RS/${snapshot.scenario}"
        val scenarioEvidence =
            "{\"requestedScenario\":\"${FullBattleTraceRecorder.escape(snapshot.requestedScenario)}\",\"expectedScript\":\"${FullBattleTraceRecorder.escape(expectedScript)}\",\"loadedScript\":\"${FullBattleTraceRecorder.escape(expectedScript)}\",\"route\":\"JojoGame.showBattleSandbox\",\"seededUnitIds\":[${snapshot.seededUnitIds.joinToString(",")}],\"loadBgCalls\":[{\"mapIndex\":${snapshot.loadedMapIndex}}],\"loadedMap\":{\"mapIndex\":${snapshot.loadedMapIndex},\"textureName\":\"${FullBattleTraceRecorder.escape(snapshot.mapName)}\",\"width\":${snapshot.mapWidth},\"height\":${snapshot.mapHeight}}}"
        val summary =
            "{\"scenario\":\"${FullBattleTraceRecorder.escape(snapshot.scenario)}\",\"gameScenario\":$scenarioEvidence,\"round\":${snapshot.round},\"camp\":${snapshot.camp},\"end\":${snapshot.ended},\"frameCount\":${recorder.recordedRowCount},\"outcome\":${snapshot.outcome?.let { "\"$it\"" } ?: "null"}}"
        recorder.write(reason, summary)
        return FullBattleTraceFinishResult(config.outputPath, recorder.recordedRowCount)
    }

    fun exitsOnFinish(): Boolean = config.exitOnFinish
}
