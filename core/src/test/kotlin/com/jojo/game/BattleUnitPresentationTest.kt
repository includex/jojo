package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * class  `BattleUnitPresentationTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleUnitPresentationTest {
    @Test
    fun `BattleUnit refStateAnime maps MB JZ HL ZD in original status order`() {
        val unit = BattleUnit(
            "u", "u", Faction.PLAYER, 0, 0,
            statuses = linkedMapOf(BattleStatus.POISON to 2, BattleStatus.CONFUSION to 1, BattleStatus.PARALYSIS to 3),
        )

        // MB (paralysis) and HL (confusion) are the first two active source
        // states; later ZD does not enter the two-frame status clip.
        assertEquals(listOf(0, 2), unit.presentation.stateAnimation.current()!!.textureIndices)
        unit.statuses.remove(BattleStatus.PARALYSIS)
        unit.presentation.refreshStatus(unit.statuses, unit.attributeLifts)
        assertEquals(listOf(2, 3), unit.presentation.stateAnimation.current()!!.textureIndices)
    }

    private fun unit() = BattleUnit("u", "unit", Faction.PLAYER, 0, 0, hitPoints = 50, maxHitPoints = 100, magicPoints = 4, maxMagicPoints = 10)

    @Test
    fun `BattleUnit HP MP and bar follow source clamp semantics`() {
        val unit = unit()
        unit.setCurHp(150)
        assertEquals(100, unit.hitPoints)
        assertEquals(1f, unit.presentation.hpBarProgress)
        unit.addHpcur(-200, keepAlive = true)
        assertEquals(1, unit.hitPoints)
        assertEquals(.01f, unit.presentation.hpBarProgress)
        unit.setCurMp(-20)
        assertEquals(0, unit.magicPoints)
        unit.addMpcur(30)
        assertEquals(10, unit.magicPoints)
    }

    @Test
    fun `BattleUnit refStatus exposes all six source attribute icons and their two frames`() {
        val unit = unit()
        unit.attributeLifts[BattleAttribute.ATTACK] = -1
        unit.attributeLifts[BattleAttribute.DEFENSE] = 1
        unit.presentation.refreshAttributeStatusIcons(unit.attributeLifts)

        assertEquals(BattleUnitPresentationState.AttributeStatusIcon(active = true, down = true), unit.presentation.attributeStatusIcons[BattleAttribute.ATTACK])
        assertEquals(BattleUnitPresentationState.AttributeStatusIcon(active = true, down = false), unit.presentation.attributeStatusIcons[BattleAttribute.DEFENSE])
        assertEquals(BattleUnitPresentationState.AttributeStatusIcon(active = false, down = false), unit.presentation.attributeStatusIcons[BattleAttribute.SPIRIT])
        assertEquals(BattleAttribute.entries.toSet(), unit.presentation.attributeStatusIcons.keys)
    }

    @Test
    fun `BattleUnit harm number mirrors source HP MP placement colors and replacement`() {
        val unit = unit()
        unit.presentation.showHarmNumber(hpAdd = -18)
        assertEquals(BattleUnitPresentationState.HarmNumber(18, true, -24), unit.presentation.harmNumber)
        unit.presentation.showHarmNumber(mpAdd = 3)
        assertEquals(BattleUnitPresentationState.HarmNumber(3, false, 24), unit.presentation.harmNumber)
        unit.presentation.clearHarmNumber()
        assertNull(unit.presentation.harmNumber)
    }
}
