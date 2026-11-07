package com.jojo.game

import com.jojo.game.application.battle.Battle
import com.jojo.game.domain.battle.*
import kotlin.test.*

class BattleMeditationTest {
    @Test fun `meditation survives other camps and recovers once on the next own turn`() {
        val caster = BattleUnit("caster", "임충", Faction.PLAYER, 0, 0,
            hitPoints = 40, maxHitPoints = 100, magicPoints = 0, maxMagicPoints = 50,
            statuses = linkedMapOf(BattleStatus.MEDITATION to 1))
        val battle = Battle(listOf(caster, BattleUnit("enemy", "적", Faction.ENEMY, 5, 5)), emptyList())
        repeat(3) {
            battle.roundLifecycle.advanceToNextCamp()
            battle.roundLifecycle.settleActiveCampStart()
            assertEquals(1, caster.statuses[BattleStatus.MEDITATION])
            assertEquals(40, caster.hitPoints)
            assertEquals(0, caster.magicPoints)
        }
        battle.roundLifecycle.advanceToNextCamp()
        battle.roundLifecycle.settleActiveCampStart()
        assertFalse(BattleStatus.MEDITATION in caster.statuses)
        assertEquals(50, caster.hitPoints)
        assertEquals(10, caster.magicPoints)
        battle.roundLifecycle.settleActiveCampStart()
        assertEquals(50, caster.hitPoints)
        assertEquals(10, caster.magicPoints)
    }

    @Test fun `recovery caps at maxima and never revives the dead`() {
        val unit = BattleUnit("u", "u", Faction.PLAYER, 0, 0,
            hitPoints = 99, maxHitPoints = 100, magicPoints = 49, maxMagicPoints = 50,
            statuses = linkedMapOf(BattleStatus.MEDITATION to 1))
        BattleMeditation.recover(unit)
        assertEquals(100, unit.hitPoints)
        assertEquals(50, unit.magicPoints)
        unit.hitPoints = 0
        unit.magicPoints = 0
        unit.statuses[BattleStatus.MEDITATION] = 1
        BattleMeditation.recover(unit)
        assertEquals(0, unit.hitPoints)
        assertEquals(0, unit.magicPoints)
    }
}
