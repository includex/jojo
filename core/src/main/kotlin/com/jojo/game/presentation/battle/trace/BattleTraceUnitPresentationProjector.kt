// Battle
package com.jojo.game.presentation.battle.trace

import com.jojo.game.application.runtime.RuntimeBattleTracePoint
import com.jojo.game.application.runtime.RuntimeBattleTraceSpriteInput
import com.jojo.game.application.runtime.RuntimeBattleTraceUnitInput
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleUnitMoveTimeline
import com.jojo.game.presentation.battle.unit.ScriptedUnitVisual
import com.jojo.game.presentation.battle.timeline.UnitActionAnimation
import com.jojo.game.presentation.battle.timeline.UnitMoveAnimation
import com.jojo.game.presentation.battle.unit.UnitSpriteFrame

/** 전투 추적 유닛 표현 입력: 화면에서 계산한 유닛 애니메이션과 지형 표시 정보를 한 번에 전달한다. */
internal data class BattleTraceUnitPresentationInput(
    val unit: BattleUnit,
    val now: Float,
    val movement: UnitMoveAnimation?,
    val action: UnitActionAnimation?,
    val hitReaction: UnitActionAnimation?,
    val death: UnitActionAnimation?,
    val scriptedVisual: ScriptedUnitVisual?,
    val terrainId: Int,
    val visualTile: Pair<Float, Float>,
    val defaultAction: Int,
    val idleStartedAt: Float,
)

/** 전투 추적 유닛 표현 투영기: 화면 애니메이션 상태를 검증 가능한 런타임 추적 유닛으로 변환한다. */
internal object BattleTraceUnitPresentationProjector {
    /** 투영: 이동·피격·사망·스크립트 애니메이션의 우선순위를 반영해 추적 프레임을 만든다. */
    fun project(
        input: BattleTraceUnitPresentationInput,
        spriteFrame: (action: Int, direction: Int, elapsed: Float, loop: Boolean) -> UnitSpriteFrame?,
    ): RuntimeBattleTraceUnitInput {
        val unit = input.unit
        val movement = input.movement?.takeIf { it.unitId == unit.id && input.now < it.endsAt }
        val movementSample = movement?.let {
            BattleUnitMoveTimeline.sample(it.path, it.timeline, input.now - it.startedAt)
        }
        val active = input.action?.takeIf { it.unitId == unit.id && input.now < it.endsAt }
            ?: input.hitReaction?.takeIf { input.now in it.startedAt..<it.endsAt }
            ?: input.death?.takeIf { input.now in it.startedAt..<it.endsAt }
        val action = if (movement != null) 20 else active?.sourceAction ?: input.scriptedVisual?.action ?: input.defaultAction
        val direction = movementSample?.direction ?: active?.direction ?: unit.direction
        val animationTime = (input.now - (movement?.startedAt ?: active?.startedAt ?: input.scriptedVisual?.startedAt ?: input.idleStartedAt)).coerceAtLeast(0f)
        val sprite = spriteFrame(action, direction, animationTime, movement != null)
        return RuntimeBattleTraceUnitInput(
            unit.id.substringAfterLast('-').toIntOrNull() ?: -1, unit.characterId ?: -1, unit.type().ordinal,
            unit.tileX, unit.tileY, unit.hitPoints, unit.magicPoints, direction, action, unit.visible, unit.hasActed,
            unit.ai, unit.aiValue, animationTime,
            sprite?.let { RuntimeBattleTraceSpriteInput(it.sourceY, it.sourceWidth, it.sourceHeight) },
            listOf(unit.attack, unit.defense, unit.spirit, unit.critical, unit.morale), unit.level, unit.posts,
            unit.armId, unit.experience, unit.attackOffsets.map { RuntimeBattleTracePoint(it.first, it.second) },
            unit.terrainImpacts[input.terrainId] ?: 100,
            (0..7).map { unit.rateAccumulators[it] ?: 0 }, listOf(7, 43, 197, 262, 276).map { unit.skills[it]?.and(255) ?: 255 },
            BattleAttribute.entries.take(6).map { unit.attributeLifts[it] ?: 0 },
            BattleAttribute.entries.take(6).map { unit.attributeLiftRounds[it] ?: 0 },
            BattleStatus.PARALYSIS in unit.statuses, unit.statuses[BattleStatus.PARALYSIS] ?: 0,
            BattleStatus.SILENCE in unit.statuses, unit.statuses[BattleStatus.SILENCE] ?: 0,
            BattleStatus.CONFUSION in unit.statuses, unit.statuses[BattleStatus.CONFUSION] ?: 0,
            BattleStatus.POISON in unit.statuses, unit.statuses[BattleStatus.POISON] ?: 0,
            BattleStatus.LOST in unit.statuses, unit.statuses[BattleStatus.LOST] ?: 0,
            unit.actionStatusRound, input.visualTile.first, input.visualTile.second,
        )
    }
}
