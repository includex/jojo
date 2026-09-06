// Test
package com.jojo.game

import com.jojo.game.infrastructure.preferences.InMemoryPreferences

import com.jojo.game.infrastructure.data.CampaignStore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** CampaignStorePersistenceBoundaryTest: 저장 책임 분리 뒤에도 CampaignStore가 유지해야 하는 응용 저장소 계약을 검증한다. */
class CampaignStorePersistenceBoundaryTest {
    @Test
    fun `store restores snapshot and runtime state through its persistence boundary`() {
        val preferences = InMemoryPreferences()
        CampaignStore(preferences).apply {
            state.addMoney(240)
            state.joinedUnits += 8
            recordChoice("R_01", "left")
            enter("R_02")
        }

        val restored = CampaignStore(preferences)
        assertEquals("R_02", restored.snapshot.currentScenario)
        assertEquals("left", restored.snapshot.choices["R_01"])
        assertEquals(240, restored.state.money)
        assertEquals(listOf(8), restored.state.joinedUnits.toList())
    }

    @Test
    fun `numbered slot restoration keeps stage and runtime state`() {
        val preferences = InMemoryPreferences()
        val store = CampaignStore(preferences)
        store.state.addMoney(37)
        store.state.joinedUnits += 3
        store.setStage(4)
        val raw = store.saveSlot(2)

        store.newGame()
        assertTrue(store.restoreSlot(2, raw))

        assertEquals(4, store.snapshot.stage)
        assertEquals(37, store.state.money)
        assertEquals(listOf(3), store.state.joinedUnits.toList())
    }
}
