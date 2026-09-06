// Test
package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.presentation.battle.unit.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** BattleUnitPresentationTest: BattleUnitPresentation의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleUnitPresentationTest {
    @Test
    fun `BattleUnit refStateAnime maps MB JZ HL ZD in original status order`() {
        val unit = BattleUnit(
            "u", "u", Faction.PLAYER, 0, 0,
            statuses = linkedMapOf(BattleStatus.POISON to 2, BattleStatus.CONFUSION to 1, BattleStatus.PARALYSIS to 3),
        )
        val store = BattleUnitPresentationStore()

        // 테스트 근거: 연출 프레임과 콜백 처리 순서을 검증한다.
        assertEquals(listOf(0, 2), store.stateFor(unit).stateAnimation.current()!!.textureIndices)
        unit.statuses.remove(BattleStatus.PARALYSIS)
        assertEquals(listOf(2, 3), store.stateFor(unit).stateAnimation.current()!!.textureIndices)
    }

    private fun unit() = BattleUnit("u", "unit", Faction.PLAYER, 0, 0, hitPoints = 50, maxHitPoints = 100, magicPoints = 4, maxMagicPoints = 10)

    @Test
    fun `BattleUnit HP MP and bar follow source clamp semantics`() {
        val unit = unit()
        val store = BattleUnitPresentationStore()
        unit.setCurHp(150)
        assertEquals(100, unit.hitPoints)
        assertEquals(1f, store.stateFor(unit).hpBarProgress)
        unit.addHpcur(-200, keepAlive = true)
        assertEquals(1, unit.hitPoints)
        assertEquals(.01f, store.stateFor(unit).hpBarProgress)
        unit.setCurMp(-20)
        assertEquals(0, unit.magicPoints)
        unit.addMpcur(30)
        assertEquals(10, unit.magicPoints)
    }

    @Test
    fun `BattleUnit refStatus exposes all six source attribute icons and their two frames`() {
        val unit = unit()
        val store = BattleUnitPresentationStore()
        unit.attributeLifts[BattleAttribute.ATTACK] = -1
        unit.attributeLifts[BattleAttribute.DEFENSE] = 1
        val state = store.stateFor(unit)

        assertEquals(BattleUnitPresentationState.AttributeStatusIcon(active = true, down = true), state.attributeStatusIcons[BattleAttribute.ATTACK])
        assertEquals(BattleUnitPresentationState.AttributeStatusIcon(active = true, down = false), state.attributeStatusIcons[BattleAttribute.DEFENSE])
        assertEquals(BattleUnitPresentationState.AttributeStatusIcon(active = false, down = false), state.attributeStatusIcons[BattleAttribute.SPIRIT])
        assertEquals(BattleAttribute.entries.toSet(), state.attributeStatusIcons.keys)
    }

    @Test
    fun `BattleUnit harm number mirrors source HP MP placement colors and replacement`() {
        val unit = unit()
        val state = BattleUnitPresentationStore().stateFor(unit)
        state.showHarmNumber(hpAdd = -18)
        assertEquals(BattleUnitPresentationState.HarmNumber(18, true, -24), state.harmNumber)
        state.showHarmNumber(mpAdd = 3)
        assertEquals(BattleUnitPresentationState.HarmNumber(3, false, 24), state.harmNumber)
        state.clearHarmNumber()
        assertNull(state.harmNumber)
    }
}
