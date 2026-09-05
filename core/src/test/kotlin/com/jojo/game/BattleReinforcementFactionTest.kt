package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `BattleReinforcementFactionTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleReinforcementFactionTest {
    @Test
    fun `reinforcements retain enemy-side allegiance without collapsing their camp`() {
        assertTrue(Faction.REINFORCEMENTS.isEnemySide())
        assertFalse(Faction.REINFORCEMENTS.isPlayerSide())

        val battle = Battle(
            units = listOf(
                BattleUnit("mine", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("friend", "우군", Faction.FRIEND, 0, 1),
                BattleUnit("enemy", "적군", Faction.ENEMY, 2, 1),
                BattleUnit("reinforcement", "적 증원", Faction.REINFORCEMENTS, 1, 0),
            ),
            events = emptyList(),
        )

        assertNull(battle.outcome(), "enemy reinforcements prevent premature player victory")
        assertTrue(battle.attack("mine", "reinforcement") !is TacticalActionResult.Rejected)

        val friendTurn = Battle(
            units = listOf(
                BattleUnit("mine", "아군", Faction.PLAYER, 3, 0),
                BattleUnit("friend", "우군", Faction.FRIEND, 0, 0),
                BattleUnit("reinforcement", "적 증원", Faction.REINFORCEMENTS, 1, 0),
            ),
            events = emptyList(),
        )
        friendTurn.advanceToNextCamp()
        assertTrue(friendTurn.attack("friend", "reinforcement") !is TacticalActionResult.Rejected)
    }

    @Test
    fun `reinforcements participate in enemy-side outcome accounting`() {
        val playerAndReinforcement = Battle(
            units = listOf(
                BattleUnit("mine", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("reinforcement", "적 증원", Faction.REINFORCEMENTS, 1, 0),
            ),
            events = emptyList(),
        )
        assertNull(playerAndReinforcement.outcome())

        val reinforcementOnly = Battle(
            units = listOf(BattleUnit("reinforcement", "적 증원", Faction.REINFORCEMENTS, 1, 0)),
            events = emptyList(),
        )
        assertEquals(BattleOutcome.ENEMY_VICTORY, reinforcementOnly.outcome())
    }

    @Test
    fun `reinforcement camp is a distinct fourth AI pass`() {
        val enemy = BattleUnit("enemy", "적군", Faction.ENEMY, 0, 1)
        val reinforcement = BattleUnit("reinforcement", "적 증원", Faction.REINFORCEMENTS, 0, 0)
        val battle = Battle(
            units = listOf(BattleUnit("mine", "아군", Faction.PLAYER, 4, 0), enemy, reinforcement),
            events = emptyList(),
        )

        assertEquals(Faction.FRIEND, battle.advanceToNextCamp().activeFaction)
        assertEquals(Faction.ENEMY, battle.advanceToNextCamp().activeFaction)
        assertEquals(Faction.REINFORCEMENTS, battle.advanceToNextCamp().activeFaction)
        battle.prepareActiveCampOperation()
        battle.resolveAiTurn()

        assertFalse(enemy.hasActed, "ordinary enemies belong only to the preceding ENEMY pass")
        assertTrue(reinforcement.hasActed)
        battle.settleActiveCampEnd()
        assertEquals(Faction.PLAYER, battle.advanceToNextCamp().activeFaction)
        assertFalse(enemy.hasActed, "reinforcement completion clears XD for the full enemy side before crossing")
        assertFalse(reinforcement.hasActed)
    }

    @Test
    fun `friend completion clears XD for the full player side before camp crossing`() {
        val mine = BattleUnit("mine", "아군", Faction.PLAYER, 0, 0, hasActed = true)
        val friend = BattleUnit("friend", "우군", Faction.FRIEND, 1, 0, hasActed = true)
        val battle = Battle(
            units = listOf(mine, friend, BattleUnit("enemy", "적군", Faction.ENEMY, 2, 0)),
            events = emptyList(),
        )

        battle.advanceToNextCamp() // PLAYER -> FRIEND: same side, no clear.
        assertTrue(mine.hasActed)
        assertTrue(friend.hasActed)
        battle.settleActiveCampEnd() // source ctrl_mine f() clears the allied side before restore.
        battle.advanceToNextCamp() // FRIEND -> ENEMY: `_setOper` only changes camp.

        assertFalse(mine.hasActed)
        assertFalse(friend.hasActed)
    }

    @Test
    fun `scenario yj marker survives factory conversion as reinforcement camp`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(
            ScenarioUnitFaction.MINE,
            listOf(mapOf("i" to 0, "id" to 1, "x" to 0, "y" to 0)),
        )
        stage.createBattleUnits(
            ScenarioUnitFaction.ENEMY,
            listOf(
                mapOf("i" to 0, "id" to 10, "x" to 1, "y" to 0),
                mapOf("i" to 1, "id" to 11, "x" to 2, "y" to 0, "yj" to 1),
            ),
        )
        val battle = BattleScenarioFactory.fromScriptedUnits(stage.battleUnits.values)

        assertEquals(Faction.ENEMY, battle.units.getValue("enemy-0").faction)
        assertEquals(Faction.REINFORCEMENTS, battle.units.getValue("enemy-1").faction)
    }
}
