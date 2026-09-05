package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleStateJournalTest {
    @Test
    fun `journal commands own turn economy and route progress`() {
        val journal = BattleStateJournal(BattleWeather.CLEAR, 10, 20, setOf(2 to 3))

        journal.setRound(4)
        journal.setActiveFaction(Faction.ENEMY)
        journal.setWeather(BattleWeather.HEAVY_RAIN)
        journal.setPlayerMoney(7)
        journal.setEnemyMoney(23)
        journal.recordMove("u", listOf(0 to 0, 1 to 0), 2)
        journal.mutableTraceActions().add("attack")

        assertEquals(4, journal.round)
        assertEquals(Faction.ENEMY, journal.activeFaction)
        assertEquals(BattleWeather.HEAVY_RAIN, journal.weather)
        assertEquals(7 to 23, journal.playerMoney to journal.enemyMoney)
        assertEquals(listOf(0 to 0, 1 to 0), journal.lastMovePath("u"))
        assertEquals(listOf("attack"), journal.traceActionsSnapshot())
        assertEquals(setOf(2 to 3), journal.blockedTiles())
    }

    @Test
    fun `journal queues upgrades and stages effects in lifecycle order`() {
        val journal = BattleStateJournal(BattleWeather.CLEAR, 0, 0, emptySet())
        val effects = mutableListOf<String>()

        journal.recordStagedHitSideEffects(mutableListOf<() -> Unit>({ effects += "hit" }))
        journal.stageHitSideEffect { effects += "next" }
        journal.recordStagedCompletionSideEffects(mutableListOf<() -> Unit>({ effects += "completion" }))
        journal.stageCompletionSideEffect { effects += "last" }

        assertTrue(journal.hasStagedCompletionSideEffects())
        journal.stagedHitSideEffects()!!.forEach { it() }
        journal.stagedCompletionSideEffects()!!.forEach { it() }
        assertEquals(listOf("hit", "next", "completion", "last"), effects)

        journal.clearBlockedTiles()
        journal.addBlockedTiles(listOf(4 to 4))
        assertFalse((2 to 3) in journal.blockedTiles())
        assertEquals(setOf(4 to 4), journal.blockedTiles())
    }
}
