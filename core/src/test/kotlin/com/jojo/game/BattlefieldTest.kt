package com.jojo.game
import com.jojo.game.domain.battle.Battlefield

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * class  `BattlefieldTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattlefieldTest {
    @Test
    fun `initial duplicates replace in place and additions retain stable order`() {
        val first = unit("first")
        val second = unit("second")
        val replacement = unit("first", tileX = 2)
        val battlefield = Battlefield(listOf(first, second, replacement))

        assertEquals(listOf("first", "second"), battlefield.activeMap.keys.toList())
        assertSame(replacement, battlefield.activeUnit("first"))

        val third = unit("third")
        battlefield.add(third)

        assertEquals(listOf("first", "second", "third"), battlefield.activeMap.keys.toList())
        assertFailsWith<IllegalStateException> { battlefield.add(unit("first", tileX = 4)) }
        assertEquals(listOf("first", "second", "third"), battlefield.activeMap.keys.toList())
    }

    @Test
    fun `defeat removes tactical occupancy and retains the presentation unit`() {
        val victim = unit("victim", tileX = 3)
        val battlefield = Battlefield(listOf(victim))

        battlefield.defeat(victim.id)

        assertFalse(victim.id in battlefield.activeMap)
        assertEquals(null, battlefield.unitAt(3, 0))
        assertSame(victim, battlefield.presentationUnit(victim.id))
        assertEquals(listOf(victim), battlefield.pendingPresentationUnits().toList())
    }

    @Test
    fun `hide clear and restore preserve the unit restoration invariants`() {
        val survivor = unit("survivor")
        val defeated = unit("defeated", tileX = 1).apply {
            hitPoints = 0
            maxHitPoints = 80
            magicPoints = 0
            maxMagicPoints = 30
            statuses[BattleStatus.POISON] = 2
            attributeLifts[BattleAttribute.ATTACK] = -1
            attributeLiftRounds[BattleAttribute.ATTACK] = 2
            hasMoved = true
            hasActed = true
            retreatFlag = true
        }
        val discarded = unit("discarded", tileX = 2)
        val battlefield = Battlefield(listOf(survivor, defeated, discarded))
        battlefield.defeat(defeated.id)
        battlefield.defeat(discarded.id)

        battlefield.hideForPresentation(defeated.id)
        battlefield.clearRetained(discarded.id)
        val restored = battlefield.restore(defeated.id)

        assertSame(defeated, restored)
        assertEquals(listOf("survivor", "defeated"), battlefield.activeMap.keys.toList())
        assertTrue(battlefield.pendingPresentationUnits().isEmpty())
        assertEquals(80, defeated.hitPoints)
        assertEquals(30, defeated.magicPoints)
        assertTrue(defeated.statuses.isEmpty())
        assertTrue(defeated.attributeLifts.isEmpty())
        assertTrue(defeated.attributeLiftRounds.isEmpty())
        assertFalse(defeated.hasMoved)
        assertTrue(defeated.hasActed)
        assertFalse(defeated.retreatFlag)
        assertTrue(defeated.visible)
        assertEquals(null, battlefield.presentationUnit(discarded.id))
    }

    @Test
    fun `restore leaves nondefeated mutable battle state intact`() {
        val retained = unit("retained").apply {
            hitPoints = 7
            magicPoints = 3
            statuses[BattleStatus.SILENCE] = 2
            attributeLifts[BattleAttribute.DEFENSE] = 1
            attributeLiftRounds[BattleAttribute.DEFENSE] = 3
            hasMoved = true
            hasActed = true
            retreatFlag = true
            visible = false
        }
        val battlefield = Battlefield(listOf(retained))
        battlefield.defeat(retained.id)

        battlefield.restore(retained.id)

        assertEquals(7, retained.hitPoints)
        assertEquals(3, retained.magicPoints)
        assertEquals(mapOf(BattleStatus.SILENCE to 2), retained.statuses)
        assertEquals(mapOf(BattleAttribute.DEFENSE to 1), retained.attributeLifts)
        assertEquals(mapOf(BattleAttribute.DEFENSE to 3), retained.attributeLiftRounds)
        assertTrue(retained.hasMoved)
        assertTrue(retained.hasActed)
        assertFalse(retained.retreatFlag)
        assertTrue(retained.visible)
    }

    @Test
    fun `topology snapshot restores active and retained insertion order`() {
        val first = unit("first")
        val second = unit("second")
        val third = unit("third")
        val fourth = unit("fourth")
        val battlefield = Battlefield(listOf(first, second, third))
        battlefield.defeat(second.id)
        val snapshot = battlefield.snapshotTopology()

        battlefield.defeat(first.id)
        battlefield.clearRetained(second.id)
        battlefield.add(fourth)
        battlefield.restoreTopology(snapshot, listOf(first, second, third, fourth).associateBy { it.id })

        assertEquals(listOf("first", "third"), battlefield.activeMap.keys.toList())
        assertEquals(listOf("second"), battlefield.pendingPresentationUnits().map { it.id })
        assertEquals(listOf("first", "third", "second"), battlefield.allPresentationUnits().map { it.id })
    }

    @Test
    fun `active map blocks structural mutation and remains a live view`() {
        val first = unit("first")
        val battlefield = Battlefield(listOf(first))
        @Suppress("UNCHECKED_CAST")
        val exported = battlefield.activeMap as MutableMap<String, BattleUnit>

        assertFailsWith<UnsupportedOperationException> { exported.remove(first.id) }
        assertFailsWith<UnsupportedOperationException> { exported.clear() }

        battlefield.add(unit("second"))
        assertEquals(listOf("first", "second"), exported.keys.toList())
    }

    @Test
    fun `retained duplicate keeps active lookup priority and is overwritten by later defeat`() {
        val retained = unit("same", tileX = 1)
        val active = unit("same", tileX = 2)
        val battlefield = Battlefield(listOf(retained))
        battlefield.defeat(retained.id)
        battlefield.add(active)

        assertSame(active, battlefield.presentationUnit("same"))
        assertSame(retained, battlefield.pendingPresentationUnits().single())
        assertEquals(listOf(active), battlefield.allPresentationUnits())
        assertSame(active, battlefield.restore("same"))
        assertSame(retained, battlefield.pendingPresentationUnits().single())

        battlefield.defeat("same")

        assertTrue(battlefield.activeMap.isEmpty())
        assertSame(active, battlefield.pendingPresentationUnits().single())
    }

    @Test
    fun `duplicate topology restore uses the retained-wins memento object for both memberships`() {
        val active = unit("same", tileX = 1)
        val retained = unit("same", tileX = 2)
        val battlefield = Battlefield(listOf(retained))
        battlefield.defeat("same")
        battlefield.add(active)
        val snapshot = battlefield.snapshotTopology()

        // Battle runtime mementos are keyed active-first then retained, so the
        // retained object wins when a duplicate ID exists in both topologies.
        battlefield.restoreTopology(snapshot, mapOf("same" to retained))

        assertSame(retained, battlefield.activeUnit("same"))
        assertSame(retained, battlefield.pendingPresentationUnits().single())
    }

    private fun unit(id: String, tileX: Int = 0): BattleUnit = BattleUnit(
        id = id,
        name = id,
        faction = Faction.PLAYER,
        tileX = tileX,
        tileY = 0,
        hitPoints = 10,
        maxHitPoints = 10,
        magicPoints = 5,
        maxMagicPoints = 5,
    )
}
