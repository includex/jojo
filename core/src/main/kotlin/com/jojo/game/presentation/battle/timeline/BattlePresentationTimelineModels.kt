// Battle
package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleUnitMoveTimeline
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.PhysicalBackMove
import com.jojo.game.presentation.battle.render.MovementCameraTickCursor

/** 유닛 행동 애니메이션 종류: 공격·특수기·피격·사망 스프라이트 흐름을 구분한다. */
internal enum class UnitAnimationKind { ATTACK, SPECIAL, HIT, DEATH }

/** 유닛 행동 애니메이션: 행동 대상, 방향, 원본 프레임과 표시 시간 범위를 정의한다. */
internal data class UnitActionAnimation(
    val unitId: String,
    val kind: UnitAnimationKind,
    val direction: Int,
    val startedAt: Float,
    val endsAt: Float,
    val sourceAction: Int = 6,
)

/** 유닛 이동 애니메이션: 이동 경로·원본 시간선·카메라 틱 진행 상태를 보관한다. */
internal data class UnitMoveAnimation(
    val unitId: String,
    val path: List<Pair<Int, Int>>,
    val timeline: BattleUnitMoveTimeline.Timeline,
    val startedAt: Float,
    val cameraTickCursor: MovementCameraTickCursor = MovementCameraTickCursor(),
) {
    /** 종료 시점: 원본 이동 시간선의 유휴 구간까지 포함한 끝 시간을 계산한다. */
    val endsAt: Float get() = startedAt + timeline.idleAt
}

/** 마법 효과 애니메이션: 효과 식별자·대상·재생 범위와 효과음 재생 여부를 보관한다. */
internal data class MagicEffectAnimation(
    val effectId: Int,
    val targetIds: List<String>,
    val startedAt: Float,
    val endsAt: Float,
    var soundPlayed: Boolean = false,
)

/** 넉백 이동 애니메이션: 피격 유닛의 후퇴 경로와 재생 시간 범위를 정의한다. */
internal data class BackMoveAnimation(
    val unitId: String,
    val move: PhysicalBackMove,
    val startedAt: Float,
    val endsAt: Float,
)

/** 피해 수치 애니메이션: 체력·기력 변화량과 화면 노출 시간 범위를 정의한다. */
internal data class HarmNumberAnimation(val amount: Int, val isHp: Boolean, val startedAt: Float, val endsAt: Float)

/** 시간 지정 전투 변경: 화면 애니메이션 시점에 실행할 상태 변경 작업을 보관한다. */
internal data class TimedBattleMutation(val at: Float, val mutation: () -> Unit)

/** 전투 패배 판별기: 주인공 유닛 우선 규칙과 아군 생존 규칙으로 패배 여부를 계산한다. */
internal object BattleScreenLoseCondition {
    /** 패배 판별: 주인공 전투 유닛이 있으면 그 체력을 우선하고, 없으면 아군 생존 여부를 사용한다. */
    fun defeated(units: Collection<BattleUnit>, mineMasterBattleId: String?): Boolean {
        mineMasterBattleId?.let { masterId ->
            units.firstOrNull { it.id == masterId }?.let { return it.hitPoints < 1 }
        }
        return units.none { it.faction == Faction.PLAYER && it.hitPoints > 0 }
    }
}

/** 피격 방향 일정 관리자: 피격 시작·종료 시점의 방향 전환과 복구 작업을 예약한다. */
internal object BattleScreenHitReactionDirectionScheduler {
    /** 방향 복구 예약: 원본 행동이 방향을 고정하지 않을 때만 현재 피격 상태인지 확인하고 이전 방향을 복원한다. */
    fun schedule(
        sourceAction: Int,
        reactionDirection: Int,
        previousDirection: Int?,
        startsAt: Float,
        endsAt: Float,
        schedule: (Float, () -> Unit) -> Unit,
        isCurrentReaction: () -> Boolean,
        setDirection: (Int) -> Unit,
    ) {
        schedule(startsAt) { setDirection(reactionDirection) }
        if (sourceAction != 26 && previousDirection != null) {
            schedule(endsAt) {
                if (isCurrentReaction()) setDirection(previousDirection)
            }
        }
    }
}

/** 사망 대기 애니메이션: 유닛 퇴장에 필요한 방향·원본 행동·표시 시간·퇴장 문구 여부를 정의한다. */
internal data class PendingDeathAnimation(
    val unitId: String,
    val direction: Int,
    val sourceAction: Int,
    val duration: Float,
    val showRetireMessage: Boolean,
)
/** 턴 사망 단계: 스크립트 전·숨김·스크립트 후의 사망 처리 위치를 구분한다. */
internal enum class TurnDeathStage { NONE, PRE_SCRIPT, HIDING, POST_SCRIPT }

/** 진행 중인 유닛 사망 상태: 대기 사망 정보와 종료 시각·원래 체력을 함께 보관한다. */
internal data class ActiveUnitDeath(
    val pending: PendingDeathAnimation,
    val endsAt: Float,
    val originalHp: Int,
)
