// Battle Trace
package com.jojo.game.presentation.battle.trace

import com.jojo.game.application.runtime.BattleRuntimeProbe
import com.jojo.game.application.runtime.BattleRuntimeProbeFactory
import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.application.runtime.BattleRuntimeSnapshotProjector
import com.jojo.game.application.runtime.BattleTraceRandomStreams
import com.jojo.game.application.runtime.BattleTraceRuntimeConfig
import com.jojo.game.application.runtime.BattleTraceRuntimeSession
import com.jojo.game.application.runtime.RuntimeBattleObserver
import com.jojo.game.application.runtime.RuntimeBattleTraceAiPresentationInput
import com.jojo.game.application.runtime.RuntimeBattleTraceDriverInput
import com.jojo.game.application.runtime.RuntimeBattleTraceFrameInput
import com.jojo.game.application.runtime.RuntimeBattleTraceFrameProjector
import com.jojo.game.application.runtime.RuntimeBattleTraceUnitInput
import com.jojo.game.application.runtime.RuntimeGridPoint
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction

/** 런타임 전투 probe 포트: 화면 구현을 노출하지 않고 자동 구동기에 필요한 전장 조회와 좌표 변환만 제공한다. */
internal interface BattleRuntimeProbePort {
    /** 현재 라운드: probe 스냅샷에 기록할 전투 진행 차수다. */
    val round: Int

    /** 활성 진영: probe 스냅샷에 기록할 현재 행동 진영이다. */
    val activeFaction: Faction

    /** 전장 유닛: 자동 구동기의 판단에 사용할 현재 유닛 집합을 반환한다. */
    fun units(): Collection<BattleUnit>

    /** 이동 가능 칸: 지정 유닛이 현재 도달할 수 있는 격자 위치를 반환한다. */
    fun reachableTiles(unitId: String): Set<RuntimeGridPoint>

    /** 적을 무시한 진입 판정: 자동 이동 검증에 필요한 목적지 도달 가능 여부를 반환한다. */
    fun canEnterTilesIgnoringEnemyWithinMoves(
        unitId: String,
        ignoredEnemyId: String,
        start: RuntimeGridPoint,
        targetTiles: Set<RuntimeGridPoint>,
        moves: Int,
    ): Boolean

    /** 물리 피해 예상값: 자동 전투 명령의 대상 선택에 사용할 피해량을 반환한다. */
    fun physicalDamagePreview(attackerId: String, targetId: String): Int

    /** 격자 화면 좌표: 전장 타일 중심의 실제 화면 좌표를 반환한다. */
    fun screenPoint(tile: RuntimeGridPoint): RuntimeGridPoint

    /** 월드 화면 좌표: 화면 배치 기준점의 실제 화면 좌표를 반환한다. */
    fun projectWorldPoint(x: Float, y: Float): RuntimeGridPoint
}

/** 런타임 trace 조정자: 화면 상태를 불변 trace·probe 계약으로 변환하고 기록 세션의 수명 주기를 관리한다. */
internal class BattleRuntimeTraceCoordinator(
    configuration: BattleTraceRuntimeConfig,
    observer: RuntimeBattleObserver?,
) {
    /**
     * `session` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val session = BattleTraceRuntimeSession(configuration, observer)

    /** 결정적 난수열: 전투가 trace 실행 중일 때 사용할 재현 가능한 난수 공급원이다. */
    val randomSource: BattleTraceRandomStreams get() = session.randomSource

    /** 시간 배율: trace 실행 프레임에 적용할 시뮬레이션 속도다. */
    val timeScale: Float get() = session.timeScale

    /** 종료 요청 여부: trace 완료 시 애플리케이션 종료를 요청할지 나타낸다. */
    val exitOnFinish: Boolean get() = session.exitOnFinish

    /** 완료 여부: 종료 통지가 이미 기록되었는지 나타낸다. */
    val isFinished: Boolean get() = session.isFinished

    /** 프레임 기록: 화면이 조립한 불변 trace 입력에 세션 프레임 번호를 부여해 기록한다. */
    fun recordFrame(input: RuntimeBattleTraceFrameInput, advanceFrame: Boolean) {
        val frame = session.nextFrame(input.elapsed, advanceFrame)
        session.record(RuntimeBattleTraceFrameProjector.project(input.copy(frame = frame)))
    }

    /** AI 연출 투영: 현재 AI 행동 상태를 trace 프레임에 넣을 불변 입력으로 변환한다. */
    fun projectAiPresentation(
        stage: String,
        resolution: com.jojo.game.domain.battle.AiUnitResolution?,
        actorCharacterId: Int,
        targetCharacterId: Int,
        targetHealthBeforeAction: Int,
        hasPendingAction: Boolean,
    ): RuntimeBattleTraceAiPresentationInput? = BattleTraceAiPresentationProjector.project(
        stage, resolution, actorCharacterId, targetCharacterId, targetHealthBeforeAction, hasPendingAction,
    )

    /** 유닛 연출 투영: 화면 애니메이션과 스프라이트 프레임을 trace 유닛 입력으로 고정한다. */
    fun projectUnitPresentation(
        input: BattleTraceUnitPresentationInput,
        spriteFrame: (action: Int, direction: Int, elapsed: Float, loop: Boolean) -> com.jojo.game.presentation.battle.unit.UnitSpriteFrame?,
    ): RuntimeBattleTraceUnitInput = BattleTraceUnitPresentationProjector.project(input, spriteFrame)

    /** 입력 기록: 수락된 사용자·자동 전투 명령 문맥을 현재 trace 실행에 남긴다. */
    fun recordInput(context: String) = session.recordInput(context)

    /** 메뉴 누름 기록: 메뉴의 누름·해제 상태와 월드 좌표를 trace 진단값으로 남긴다. */
    fun recordMenuTap(pressed: Int?, released: Int?, x: Float, y: Float) =
        session.recordMenuTap(pressed, released, x, y)

    /** 자동 구동기 입력: 최근 메뉴·명령 상태를 trace 프레임용 불변 입력으로 고정한다. */
    fun driverInput(
        selectedUnitId: String?,
        commandPhase: String,
        eventMessage: String,
        autoOverlay: String,
    ): RuntimeBattleTraceDriverInput = session.driverInput(
        selectedUnitId, commandPhase, eventMessage, autoOverlay,
    )

    /** trace 종료: 지정 사유로 완료 통지를 한 번만 기록한다. */
    fun finish(reason: String) = session.finish(reason)

}

/** 런타임 probe 조정자: trace 실행 여부와 무관하게 화면 포트를 자동 전투 구동기의 읽기 계약으로 변환한다. */
internal object BattleRuntimeProbeCoordinator {
    /** probe 조립: 화면 포트의 전장 조회와 불변 화면 상태를 자동 구동기 계약으로 변환한다. */
    fun create(input: BattleRuntimeScreenProbeInput, port: BattleRuntimeProbePort): BattleRuntimeScreenProbe {
        val battle = BattleRuntimeProbeFactory(
            initialSnapshot = BattleRuntimeSnapshotProjector.project(port.round, port.activeFaction, port.units()),
            reachable = port::reachableTiles,
            canEnter = port::canEnterTilesIgnoringEnemyWithinMoves,
            damagePreview = port::physicalDamagePreview,
            screenPointQuery = port::screenPoint,
            projectWorldPointQuery = port::projectWorldPoint,
        ).create()
        return BattleRuntimeScreenProbeProjector.project(input, battle)
    }
}
