// Test
package com.jojo.game.application.runtime

import com.jojo.game.domain.battle.Faction
import kotlin.test.Test
import kotlin.test.assertEquals

class BattleRuntimeProbeFactoryTest {
    @Test
    fun `factory preserves immutable snapshot and delegates read-only queries`() {
        val snapshot = BattleRuntimeSnapshot(round = 2, activeFaction = Faction.PLAYER, units = emptyList())
        val probe = BattleRuntimeProbeFactory(
            initialSnapshot = snapshot,
            reachable = { setOf(RuntimeGridPoint(1, 2)) },
            canEnter = { _, _, _, _, moves -> moves == 2 },
            damagePreview = { _, _ -> 7 },
            screenPointQuery = { RuntimeGridPoint(it.x + 10, it.y + 20) },
            projectWorldPointQuery = { x, y -> RuntimeGridPoint(x.toInt(), y.toInt()) },
        ).create()

        assertEquals(snapshot, probe.snapshot)
        assertEquals(setOf(RuntimeGridPoint(1, 2)), probe.reachableTiles("u"))
        assertEquals(7, probe.physicalDamagePreview("a", "b"))
        assertEquals(RuntimeGridPoint(11, 22), probe.screenPoint(RuntimeGridPoint(1, 2)))
        assertEquals(RuntimeGridPoint(4, 5), probe.projectWorldPoint(4.5f, 5.5f))
    }
}
