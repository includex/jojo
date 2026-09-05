package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BattleUnitPresentationTest {
    @Test
    fun `BattleUnit refStateAnime maps MB JZ HL ZD in original status order`() {
        val unit = BattleUnit(
            "u", "u", Faction.PLAYER, 0, 0,
            statuses = linkedMapOf(BattleStatus.POISON to 2, BattleStatus.CONFUSION to 1, BattleStatus.PARALYSIS to 3),
        )

        // MB (paralysis) and HL (confusion) are the first two active source
        // states; later ZD does not enter the two-frame status clip.
        assertEquals(listOf(0, 2), unit.stateAnimation.current()!!.textureIndices)
        unit.statuses.remove(BattleStatus.PARALYSIS)
        unit.refStateAnime()
        assertEquals(listOf(2, 3), unit.stateAnimation.current()!!.textureIndices)
    }

    private fun unit() = BattleUnit("u", "unit", Faction.PLAYER, 0, 0, hitPoints = 50, maxHitPoints = 100, magicPoints = 4, maxMagicPoints = 10)

    @Test
    fun `BattleUnit HP MP and bar follow source clamp semantics`() {
        val unit = unit()
        unit.setCurHp(150)
        assertEquals(100, unit.hitPoints)
        assertEquals(1f, unit.hpBarProgress)
        unit.addHpcur(-200, keepAlive = true)
        assertEquals(1, unit.hitPoints)
        assertEquals(.01f, unit.hpBarProgress)
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
        unit.refAttributeStatusIcons()

        assertEquals(BattleUnit.AttributeStatusIcon(active = true, down = true), unit.attributeStatusIcons[BattleAttribute.ATTACK])
        assertEquals(BattleUnit.AttributeStatusIcon(active = true, down = false), unit.attributeStatusIcons[BattleAttribute.DEFENSE])
        assertEquals(BattleUnit.AttributeStatusIcon(active = false, down = false), unit.attributeStatusIcons[BattleAttribute.SPIRIT])
        assertEquals(BattleAttribute.entries.toSet(), unit.attributeStatusIcons.keys)
    }

    @Test
    fun `BattleUnit harm number mirrors source HP MP placement colors and replacement`() {
        val unit = unit()
        unit.showHarmNum(hpAdd = -18)
        assertEquals(BattleUnit.HarmNumber(18, true, -24), unit.harmNumber)
        unit.showHarmNum(mpAdd = 3)
        assertEquals(BattleUnit.HarmNumber(3, false, 24), unit.harmNumber)
        unit.clsHarmNum()
        assertNull(unit.harmNumber)
    }
}
