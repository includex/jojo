// Verification
package com.jojo.game.verification.trace

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.application.battle.NaturalBattleTransition
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.domain.scenario.ScenarioMapObject
import com.jojo.game.application.runtime.RuntimeBattleTraceView

/** FullBattleTraceMapObject: 증거 직렬화 전에 BattleScreen이 전달하는 값 전용 지도 행이다. */
internal data class FullBattleTraceMapObject(
    /** objectId: 객체 식별자 값을 보관한다. */
    val objectId: Int,
    /** terrainId: 지형 식별자 값을 보관한다. */
    val terrainId: Int,
    /** x: X 좌표 값을 보관한다. */
    val x: Int,
    /** y: Y 좌표 값을 보관한다. */
    val y: Int,
    /** enabled: 활성화 여부 여부를 나타낸다. */
    val enabled: Boolean,
)

/** FullBattleTraceMapObjectsCall: 원본 Stage.setObjects 호출 하나를 값만으로 복사한 기록이다. */
internal data class FullBattleTraceMapObjectsCall(
    /** enabled: 활성화 여부 여부를 나타낸다. */
    val enabled: Boolean,
    /** terrainId: 지형 식별자 값을 보관한다. */
    val terrainId: Int,
    /** objects: 객체 목록 상태를 검증 흐름에 전달한다. */
    val objects: List<FullBattleTraceMapObjectCall>,
)

/** FullBattleTraceMapObjectCall: 전장 객체 설정 호출의 객체 식별자와 좌표를 기록한다. */
internal data class FullBattleTraceMapObjectCall(val objectId: Int, val x: Int, val y: Int)
/** FullBattleTraceMapSnapshot: 특정 리비전의 전장 객체 지도를 직렬화한 불변 기록이다. */
internal data class FullBattleTraceMapSnapshot(val revision: Int, val json: String)

/** FullBattleTraceDriveSnapshot: 불변 장벽 관찰값이며 코디네이터는 BattleScreen을 다시 참조하지 않는다. */
internal data class FullBattleTraceDriveSnapshot(
    /** elapsed: 경과 시간 상태를 검증 흐름에 전달한다. */
    val elapsed: Float,
    /** outcome: 검증 결과를 담는다. */
    val outcome: BattleOutcome?,
    /** scriptState: 현재 검증 상태를 담는다. */
    val scriptState: PlaybackState,
    /** traceBarrierOpen: 검증 추적 결과를 담는다. */
    val traceBarrierOpen: Boolean,
    /** lossSceneActive: 패배 장면 표시 여부 여부를 나타낸다. */
    val lossSceneActive: Boolean,
    /** callbackPending: 콜백 대기 여부 상태를 검증 흐름에 전달한다. */
    val callbackPending: Boolean,
    /** scriptEnded: 스크립트 종료 여부 여부를 나타낸다. */
    val scriptEnded: Boolean,
    /** endProcessStarted: 종료 처리 시작 여부 상태를 검증 흐름에 전달한다. */
    val endProcessStarted: Boolean,
)

/** FullBattleTraceFinishSnapshot: 불변 종료 투영값이며 JSON 처리는 의도적으로 화면 밖에서 담당한다. */
internal data class FullBattleTraceFinishSnapshot(
    /** scenario: 검증 시나리오 경로를 담는다. */
    val scenario: String,
    /** requestedScenario: 검증 시나리오 경로를 담는다. */
    val requestedScenario: String,
    /** round: 라운드 값을 보관한다. */
    val round: Int,
    /** camp: 진영 상태를 검증 흐름에 전달한다. */
    val camp: Int,
    /** ended: 종료 여부 여부를 나타낸다. */
    val ended: Boolean,
    /** outcome: 검증 결과를 담는다. */
    val outcome: BattleOutcome?,
    /** seededUnitIds: 검증 대상 무장 정보를 담는다. */
    val seededUnitIds: List<Int>,
    /** loadedMapIndex: 불러온 지도 인덱스 값을 보관한다. */
    val loadedMapIndex: Int,
    /** mapName: 지도 이름 상태를 검증 흐름에 전달한다. */
    val mapName: String,
    /** mapWidth: 지도 너비 값을 보관한다. */
    val mapWidth: Int,
    /** mapHeight: 지도 높이 값을 보관한다. */
    val mapHeight: Int,
)

/** FullBattleTraceFinishResult: 추적 기록 파일의 출력 경로와 최종 프레임 수를 전달한다. */
internal data class FullBattleTraceFinishResult(val outputPath: String, val frameCount: Long)

/** FullBattleTraceEvidenceSession: 상태를 보유하는 전체 전투 증거 경계이다. 추적 전용 커서·서명·종료 프레임 수·완료 장벽을 소유하고, BattleScreen은 실행 중 표현 상태의 불변 관찰값만 제공한다. */
internal class FullBattleTraceEvidenceSession(
    /** config: 검증 실행 설정을 담는다. */
    private val config: FullBattleTraceConfig,
    /** recorder: 기록기 상태를 검증 흐름에 전달한다. */
    private val recorder: FullBattleTraceRecorder,
) {
    /** deadline: 제한 시각 상태를 검증 흐름에 전달한다. */
    private val deadline = FullBattleTraceDeadline(config.maxSimulationSeconds)
    /** lastDriverAt: 마지막 드라이버 시각 상태를 검증 흐름에 전달한다. */
    private var lastDriverAt = Float.NEGATIVE_INFINITY
    /** terminalFrames: 종료 프레임 목록 상태를 검증 흐름에 전달한다. */
    private var terminalFrames = 0
    /** mapObjectRevision: 지도 객체 버전 상태를 검증 흐름에 전달한다. */
    private var mapObjectRevision = 0
    /** mapObjectSignature: 지도 객체 서명 상태를 검증 흐름에 전달한다. */
    private var mapObjectSignature: String? = null
    /** mapObjectsCallCursor: 지도 객체 호출 위치 상태를 검증 흐름에 전달한다. */
    private var mapObjectsCallCursor = 0
    /** finished: 완료 여부 여부를 나타낸다. */
    private var finished = false
    /** finishAfterFrame: 프레임 종료 후 완료 여부 상태를 검증 흐름에 전달한다. */
    private var finishAfterFrame: String? = null

    /** requestFinish: 검증 완료를 요청한다. */
    fun requestFinish(reason: String) {
        if (!finished) finishAfterFrame = reason
    }

    /** consumeFinishAfterFrame: 프레임 종료 후 대기 중인 완료 요청을 소비한다. */
    fun consumeFinishAfterFrame(): String? = finishAfterFrame.also { finishAfterFrame = null }

    /** drive: 기존 원본 순서인 제한 시간·주기·장벽·종료 안정성을 유지한다. */
    fun drive(snapshot: FullBattleTraceDriveSnapshot) {
        if (finished) return
        deadline.timeoutReason(snapshot.elapsed, snapshot.outcome != null)?.let(::requestFinish) ?: run {
            if (snapshot.elapsed - lastDriverAt < config.driverIntervalSeconds) return
            lastDriverAt = snapshot.elapsed
            if (snapshot.traceBarrierOpen) return
            val outcome = snapshot.outcome ?: return
            if (NaturalBattleTransition.campaignLossReadyToFlush(
                    exitOnFinish = config.exitOnFinish,
                    outcome = outcome,
                    loseSceneActive = snapshot.lossSceneActive,
                    scriptState = snapshot.scriptState,
                    callbackPending = snapshot.callbackPending,
                    scriptEnded = snapshot.scriptEnded,
                )
            ) return requestFinish("battle-end")
            if (!config.exitOnFinish) return
            if (NaturalBattleTransition.terminalReady(
                    snapshot.scriptState,
                    snapshot.callbackPending,
                    snapshot.scriptEnded,
                    snapshot.endProcessStarted,
                )
            ) terminalFrames++ else terminalFrames = 0
            if (terminalFrames >= 3) requestFinish("battle-end")
        }
    }

    /** mapSnapshot: 지도 상태 스냅샷을 생성한다. */
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

    /** mapObjectCallObservations: 추가 전용 원본 콜백을 일반 RAF 행보다 먼저 한 번씩 소비한다. */
    fun mapObjectCallObservations(calls: List<FullBattleTraceMapObjectsCall>): List<String> = buildList {
        while (mapObjectsCallCursor < calls.size) {
            val call = calls[mapObjectsCallCursor++]
            val entries = call.objects.joinToString(";") { "${it.objectId},${it.x},${it.y}" }
            add("transition:objects:${if (call.enabled) 1 else 0}:${call.terrainId}:$entries")
        }
    }

    /** nextFrame: 다음 프레임 번호를 예약한다. */
    fun nextFrame(elapsed: Float, advanceFrame: Boolean): Long =
        if (advanceFrame) recorder.nextFrame(elapsed) else recorder.upcomingFrame()

    /** record: 검증 이벤트와 산출물을 기록한다. */
    fun record(view: RuntimeBattleTraceView) {
        recorder.addFrame(RuntimeBattleTraceJson.frame(view))
    }

    /** recordInput: 검증 이벤트와 산출물을 기록한다. */
    fun recordInput(context: String) {
        recorder.recordInput(context)
    }

    /** finish: 검증 흐름을 종료하고 후속 상태를 정리한다. */
    fun finish(reason: String, snapshot: FullBattleTraceFinishSnapshot): FullBattleTraceFinishResult? {
        if (finished) return null
        finished = true
        val expectedScript = "Game/data/RS/${snapshot.scenario}"
        val scenarioEvidence =
            "{\"requestedScenario\":\"${RuntimeBattleTraceJson.escape(snapshot.requestedScenario)}\",\"expectedScript\":\"${RuntimeBattleTraceJson.escape(expectedScript)}\",\"loadedScript\":\"${RuntimeBattleTraceJson.escape(expectedScript)}\",\"route\":\"JojoGame.showBattleSandbox\",\"seededUnitIds\":[${snapshot.seededUnitIds.joinToString(",")}],\"loadBgCalls\":[{\"mapIndex\":${snapshot.loadedMapIndex}}],\"loadedMap\":{\"mapIndex\":${snapshot.loadedMapIndex},\"textureName\":\"${RuntimeBattleTraceJson.escape(snapshot.mapName)}\",\"width\":${snapshot.mapWidth},\"height\":${snapshot.mapHeight}}}"
        val summary =
            "{\"scenario\":\"${RuntimeBattleTraceJson.escape(snapshot.scenario)}\",\"gameScenario\":$scenarioEvidence,\"round\":${snapshot.round},\"camp\":${snapshot.camp},\"end\":${snapshot.ended},\"frameCount\":${recorder.recordedRowCount},\"outcome\":${snapshot.outcome?.let { "\"$it\"" } ?: "null"}}"
        recorder.write(reason, summary)
        return FullBattleTraceFinishResult(config.outputPath, recorder.recordedRowCount)
    }

    /** exitsOnFinish: 완료 시 실행기를 종료할지 판정한다. */
    fun exitsOnFinish(): Boolean = config.exitOnFinish
}
