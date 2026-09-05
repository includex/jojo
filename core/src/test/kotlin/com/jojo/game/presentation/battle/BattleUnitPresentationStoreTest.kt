package com.jojo.game.presentation.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

class BattleUnitPresentationStoreTest {
    @Test
    fun `screen-owned store creates isolated state and synchronizes derived HP status and lifts`() {
        val unit = BattleUnit("unit", "unit", Faction.PLAYER, 0, 0, hitPoints = 80, maxHitPoints = 100)
        val firstScreen = BattleUnitPresentationStore()
        val secondScreen = BattleUnitPresentationStore()

        val firstState = firstScreen.stateFor(unit)
        val secondState = secondScreen.stateFor(unit)
        assertNotSame(firstState, secondState)

        unit.setHpcur(25)
        unit.statuses[BattleStatus.POISON] = 2
        unit.attributeLifts[BattleAttribute.ATTACK] = -1

        val synchronized = firstScreen.stateFor(unit)
        assertEquals(.25f, synchronized.hpBarProgress)
        assertEquals(listOf(3, 3), synchronized.stateAnimation.current()?.textureIndices)
        assertTrue(synchronized.attributeStatusIcons.getValue(BattleAttribute.ATTACK).down)
        assertEquals(.8f, secondState.hpBarProgress)
    }

    @Test
    fun `unchanged projection preserves hidden state until authored refresh`() {
        val unit = BattleUnit(
            "unit", "unit", Faction.PLAYER, 0, 0,
            statuses = linkedMapOf(BattleStatus.PARALYSIS to 2),
        )
        val store = BattleUnitPresentationStore()
        val state = store.stateFor(unit)

        state.setStateAnimationVisible(false)
        assertFalse(store.stateFor(unit).stateAnimation.current()!!.active)
        unit.attributeLifts[BattleAttribute.ATTACK] = 1
        assertFalse(store.stateFor(unit).stateAnimation.current()!!.active)
        assertTrue(store.refresh(unit).stateAnimation.current()!!.active)
    }

    @Test
    fun `projection cleanup releases removed units and screen disposal clears the store`() {
        val unit = BattleUnit("unit", "unit", Faction.PLAYER, 0, 0)
        val store = BattleUnitPresentationStore()
        val first = store.stateFor(unit)

        store.synchronize(emptyList())
        assertNotSame(first, store.stateFor(unit))

        val retained = store.stateFor(unit)
        store.clear()
        assertNotSame(retained, store.stateFor(unit))
    }
}
