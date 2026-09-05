package com.jojo.game
import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattlePropertyItem
import com.jojo.game.domain.battle.BattlePropertyResolver

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `BattlePropertyResolverTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattlePropertyResolverTest {

    private fun unit(
        hp: Int = 50,
        maxHp: Int = 100,
        mp: Int = 20,
        maxMp: Int = 50,
        statuses: Set<BattleStatus> = emptySet(),
    ) = BattleUnit(
        id = "u",
        name = "u",
        faction = Faction.PLAYER,
        tileX = 0,
        tileY = 0,
        hitPoints = hp,
        maxHitPoints = maxHp,
        magicPoints = mp,
        maxMagicPoints = maxMp,
        statuses = statuses.associateWith { 1 }.toMutableMap(),
    )

    private fun prop(itemType: Int, value: Int, name: String = "TestItem") = BattlePropertyItem(
        id = 1,
        name = name,
        itemType = itemType,
        value = value,
    )

    @Test
    fun `hp recovery property recovers hp and clamps to max`() {
        val target = unit(hp = 80, maxHp = 100)
        var consumed = false
        val result = BattlePropertyResolver.applyProperty(
            item = prop(26, 50),
            target = target,
            consume = { consumed = true; true },
        )
        assertNotNull(result)
        assertTrue(consumed)
        assertEquals(100, target.hitPoints)
        assertEquals("HP 20 회복", result.effect)

        // If HP is already full, returns null and does not consume
        consumed = false
        val fullResult = BattlePropertyResolver.applyProperty(
            item = prop(26, 50),
            target = target,
            consume = { consumed = true; true },
        )
        assertNull(fullResult)
        assertEquals(false, consumed)
    }

    @Test
    fun `mp recovery property recovers mp and clamps to max`() {
        val target = unit(mp = 10, maxMp = 50)
        var consumed = false
        val result = BattlePropertyResolver.applyProperty(
            item = prop(27, 30),
            target = target,
            consume = { consumed = true; true },
        )
        assertNotNull(result)
        assertTrue(consumed)
        assertEquals(40, target.magicPoints)
        assertEquals("MP 30 회복", result.effect)
    }

    @Test
    fun `status cure items cure respective statuses`() {
        val target = unit(statuses = setOf(BattleStatus.CONFUSION, BattleStatus.POISON))
        val result = BattlePropertyResolver.applyProperty(
            item = prop(28, 0), // confusion cure
            target = target,
            consume = { true },
        )
        assertNotNull(result)
        assertEquals("혼란 치료", result.effect)
        assertTrue(BattleStatus.CONFUSION !in target.statuses)
        assertTrue(BattleStatus.POISON in target.statuses)
    }

    @Test
    fun `item 32 cures all statuses`() {
        val target = unit(statuses = setOf(BattleStatus.CONFUSION, BattleStatus.POISON, BattleStatus.PARALYSIS))
        val result = BattlePropertyResolver.applyProperty(
            item = prop(32, 0),
            target = target,
            consume = { true },
        )
        assertNotNull(result)
        assertEquals("모든 이상 상태 치료", result.effect)
        assertTrue(target.statuses.isEmpty())
    }

    @Test
    fun `permanent hp and mp increase applies and triggers callback`() {
        val target = unit(hp = 50, maxHp = 100, mp = 20, maxMp = 50)
        var notifiedHp = false
        var notifiedMp = false

        val hpResult = BattlePropertyResolver.applyProperty(
            item = prop(42, 10),
            target = target,
            consume = { true },
            notifyPermanentProperty = { _, _ -> notifiedHp = true },
        )
        assertNotNull(hpResult)
        assertEquals(110, target.maxHitPoints)
        assertEquals(60, target.hitPoints)
        assertTrue(notifiedHp)

        val mpResult = BattlePropertyResolver.applyProperty(
            item = prop(43, 5),
            target = target,
            consume = { true },
            notifyPermanentProperty = { _, _ -> notifiedMp = true },
        )
        assertNotNull(mpResult)
        assertEquals(55, target.maxMagicPoints)
        assertEquals(25, target.magicPoints)
        assertTrue(notifiedMp)
    }
}
