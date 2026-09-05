package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnitMemento
import com.jojo.game.presentation.battle.BattleUnitPresentationStore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * class  `BattleUnitMementoTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleUnitMementoTest {
    @Test
    fun `restore returns the same unit with every captured field restored`() {
        val unit = BattleUnit(
            id = "unit",
            name = "유닛",
            faction = Faction.PLAYER,
            tileX = 3,
            tileY = 4,
            hitPoints = 90,
            maxHitPoints = 120,
            magicPoints = 30,
            maxMagicPoints = 40,
            level = 8,
            direction = 1,
            statuses = linkedMapOf(BattleStatus.PARALYSIS to 2),
            attributeLifts = linkedMapOf(BattleAttribute.ATTACK to -1),
            attributeLiftRounds = linkedMapOf(BattleAttribute.ATTACK to 3),
            hasActed = true,
            hasMoved = true,
            visible = false,
            otherNodesVisible = false,
            retreatFlag = true,
            retreatCount = 2,
            ai = 4,
            aiTargetCharacterId = 17,
            aiTargetX = 8,
            aiTargetY = 9,
            aiValue = 71,
            criticalSpeechChecks = 5,
        ).also {
            it.actionStatusRound = 3
            it.rateAccumulators.putAll(linkedMapOf(0 to 11, 7 to 88))
        }
        val memento = BattleUnitMemento.capture(unit)

        unit.tileX = 30
        unit.tileY = 40
        unit.maxHitPoints = 10
        unit.setHpcur(1)
        unit.maxMagicPoints = 2
        unit.setMpcur(1)
        unit.level = 1
        unit.direction = 3
        unit.hasActed = false
        unit.actionStatusRound = 0
        unit.hasMoved = false
        unit.visible = true
        unit.otherNodesVisible = true
        unit.retreatFlag = false
        unit.retreatCount = 0
        unit.ai = 0
        unit.aiTargetCharacterId = -1
        unit.aiTargetX = 0
        unit.aiTargetY = 0
        unit.aiValue = 0
        unit.criticalSpeechChecks = 0
        unit.statuses.clear()
        unit.statuses[BattleStatus.POISON] = 1
        unit.attributeLifts.clear()
        unit.attributeLifts[BattleAttribute.DEFENSE] = 1
        unit.attributeLiftRounds.clear()
        unit.attributeLiftRounds[BattleAttribute.DEFENSE] = 1
        unit.rateAccumulators.clear()
        unit.rateAccumulators[3] = 99

        val restored = memento.restore()
        val presentation = BattleUnitPresentationStore().stateFor(unit)

        assertSame(unit, restored)
        assertEquals(3 to 4, unit.tileX to unit.tileY)
        assertEquals(90, unit.hitPoints)
        assertEquals(120, unit.maxHitPoints)
        assertEquals(30, unit.magicPoints)
        assertEquals(40, unit.maxMagicPoints)
        assertEquals(8, unit.level)
        assertEquals(1, unit.direction)
        assertTrue(unit.hasActed)
        assertEquals(3, unit.actionStatusRound)
        assertTrue(unit.hasMoved)
        assertFalse(unit.visible)
        assertFalse(unit.otherNodesVisible)
        assertTrue(unit.retreatFlag)
        assertEquals(2, unit.retreatCount)
        assertEquals(4, unit.ai)
        assertEquals(17, unit.aiTargetCharacterId)
        assertEquals(8, unit.aiTargetX)
        assertEquals(9, unit.aiTargetY)
        assertEquals(71, unit.aiValue)
        assertEquals(5, unit.criticalSpeechChecks)
        assertEquals(mapOf(BattleStatus.PARALYSIS to 2), unit.statuses)
        assertEquals(mapOf(BattleAttribute.ATTACK to -1), unit.attributeLifts)
        assertEquals(mapOf(BattleAttribute.ATTACK to 3), unit.attributeLiftRounds)
        assertEquals(mapOf(0 to 11, 7 to 88), unit.rateAccumulators)
        assertEquals(.75f, presentation.hpBarProgress)
        assertEquals(listOf(0, 0), presentation.stateAnimation.current()?.textureIndices)
        assertTrue(presentation.attributeStatusIcons.getValue(BattleAttribute.ATTACK).down)
        assertFalse(presentation.attributeStatusIcons.getValue(BattleAttribute.DEFENSE).active)
    }

    @Test
    fun `captured collections are deep copies and preserve the unit identity`() {
        val unit = BattleUnit(
            "unit", "유닛", Faction.ENEMY, 0, 0,
            statuses = linkedMapOf(BattleStatus.SILENCE to 2),
            attributeLifts = linkedMapOf(BattleAttribute.MORALE to 1),
            attributeLiftRounds = linkedMapOf(BattleAttribute.MORALE to 3),
        ).also { it.rateAccumulators[4] = 27 }

        val memento = BattleUnitMemento.capture(unit)
        unit.statuses[BattleStatus.SILENCE] = 1
        unit.attributeLifts[BattleAttribute.MORALE] = -1
        unit.attributeLiftRounds.clear()
        unit.rateAccumulators[4] = 99

        assertSame(unit, memento.unit)
        assertEquals(mapOf(BattleStatus.SILENCE to 2), memento.statuses)
        assertEquals(mapOf(BattleAttribute.MORALE to 1), memento.attributeLifts)
        assertEquals(mapOf(BattleAttribute.MORALE to 3), memento.attributeLiftRounds)
        assertEquals(mapOf(4 to 27), memento.rateAccumulators)
    }
}
