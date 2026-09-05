package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleUnitDefaultActionTest {
    private fun unit(hp: Int = 100, famous: Boolean = false, acted: Boolean = false, states: Set<BattleStatus> = emptySet()) =
        BattleUnit("u", "u", Faction.PLAYER, 0, 0, hitPoints = hp, maxHitPoints = 100, famous = famous, hasActed = acted,
            statuses = states.associateWithTo(linkedMapOf()) { 1 })

    @Test
    fun `defaultAction selects every normal HP source BRAnime combination`() {
        assertEquals(BattleUnit.DefaultAction(0, true), unit().defaultAction())
        assertEquals(BattleUnit.DefaultAction(39, false), unit(acted = true).defaultAction())
        assertEquals(BattleUnit.DefaultAction(40, true), unit(acted = true, states = setOf(BattleStatus.POISON)).defaultAction())
        assertEquals(BattleUnit.DefaultAction(36, true), unit(states = setOf(BattleStatus.PARALYSIS)).defaultAction())
        assertEquals(BattleUnit.DefaultAction(37, true), unit(states = setOf(BattleStatus.POISON)).defaultAction())
        assertEquals(BattleUnit.DefaultAction(38, true), unit(states = setOf(BattleStatus.POISON, BattleStatus.PARALYSIS)).defaultAction())
    }

    @Test
    fun `defaultAction selects every low HP source BRAnime combination and famous threshold`() {
        assertEquals(BattleUnit.DefaultAction(9, true), unit(hp = 19).defaultAction())
        assertEquals(BattleUnit.DefaultAction(44, false), unit(hp = 19, acted = true).defaultAction())
        assertEquals(BattleUnit.DefaultAction(45, true), unit(hp = 19, acted = true, states = setOf(BattleStatus.POISON)).defaultAction())
        assertEquals(BattleUnit.DefaultAction(41, true), unit(hp = 19, states = setOf(BattleStatus.POISON)).defaultAction())
        assertEquals(BattleUnit.DefaultAction(42, true), unit(hp = 19, states = setOf(BattleStatus.PARALYSIS)).defaultAction())
        assertEquals(BattleUnit.DefaultAction(43, true), unit(hp = 19, states = setOf(BattleStatus.POISON, BattleStatus.PARALYSIS)).defaultAction())
        assertEquals(BattleUnit.DefaultAction(9, true), unit(hp = 39, famous = true).defaultAction())
        assertEquals(BattleUnit.DefaultAction(0, true), unit(hp = 39, famous = false).defaultAction())
    }
}
