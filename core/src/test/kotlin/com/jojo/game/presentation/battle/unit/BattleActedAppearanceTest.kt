package com.jojo.game.presentation.battle.unit

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BattleActedAppearanceTest {
    private fun input(hp: Int = 100) = BattleUnitPresentationState.DefaultActionInput(
        visible = true, hitPoints = hp, maxHitPoints = 100, famous = false,
        hasActed = true, poisoned = false, paralyzed = false,
    )

    @Test
    fun `committed action keeps its old idle until the unit default callback`() {
        val unit = BattleUnit("u", "u", Faction.FRIEND, 0, 0)
        val store = BattleUnitPresentationStore()
        store.stateFor(unit).deferActedAppearance(unit.hasActed)
        unit.hasActed = true
        store.synchronize(listOf(unit))
        assertTrue(unit.hasActed, "only the appearance is deferred, not the battle command")
        assertEquals(0, store.stateFor(unit).defaultAction(input()).action)
        // Damage continues to affect the default pose during the settlement.
        assertEquals(9, store.stateFor(unit).defaultAction(input(hp = 10)).action)
        store.stateFor(unit).applyActedAppearance()
        assertEquals(39, store.stateFor(unit).defaultAction(input()).action)
    }

    @Test
    fun `another unit default callback does not release the actor appearance`() {
        val actor = BattleUnit("actor", "actor", Faction.FRIEND, 0, 0)
        val target = BattleUnit("target", "target", Faction.ENEMY, 0, 1)
        val store = BattleUnitPresentationStore()
        store.stateFor(actor).deferActedAppearance(false)
        store.stateFor(target).applyActedAppearance()
        assertEquals(0, store.stateFor(actor).defaultAction(input()).action)
        store.refresh(actor).applyActedAppearance()
        assertEquals(39, store.stateFor(actor).defaultAction(input()).action)
        assertEquals(39, store.refresh(actor).defaultAction(input()).action)
    }

    @Test
    fun `settlement without visible operations releases deferred appearances`() {
        val store = BattleUnitPresentationStore()
        val unit = BattleUnit("u", "u", Faction.FRIEND, 0, 0)
        store.stateFor(unit).deferActedAppearance(false)
        store.applyDeferredActedAppearances()
        assertEquals(39, store.stateFor(unit).defaultAction(input()).action)
    }
}
