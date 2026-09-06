// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleTerrainGrid

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** BattleMovementRouteTest: BattleMovementRoute의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleMovementRouteTest {
    @Test
    fun `authored battle move uses source weighted AStar without committing the unit`() {
        val terrain = BattleTerrainGrid(5, 5, List(5) { IntArray(5) })
        val actor = BattleUnit(
            "mine-0", "actor", Faction.PLAYER, 0, 0,
            terrainMovementCosts = mapOf(0 to 1), characterId = 0,
        )
        val battle = Battle(listOf(actor), emptyList(), terrain = terrain)

        assertEquals(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2), battle.movement.scriptedMovePath(0, 2, 2))
        assertEquals(0 to 0, actor.tileX to actor.tileY)
    }

    @Test
    fun `authored battle move resolves an occupied destination before AStar`() {
        val terrain = BattleTerrainGrid(4, 4, List(4) { IntArray(4) })
        val actor = BattleUnit(
            "mine-0", "actor", Faction.PLAYER, 0, 0,
            terrainMovementCosts = mapOf(0 to 1), characterId = 0,
        )
        val occupied = BattleUnit("friend", "friend", Faction.FRIEND, 2, 2)
        val battle = Battle(listOf(actor, occupied), emptyList(), terrain = terrain)

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (QUN_XIONG)을 검증한다.
        assertEquals(2 to 3, battle.movement.scriptedMovePath(0, 2, 2)?.last())
        assertEquals(0 to 0, actor.tileX to actor.tileY)
    }

    @Test
    fun `move2 route preserves source down-first ties and exposes first segment direction`() {
        val terrain = BattleTerrainGrid(5, 5, List(5) { IntArray(5) })
        val battle = Battle(
            units = listOf(
                BattleUnit("u", "u", Faction.PLAYER, 0, 0, movement = 4, terrainMovementCosts = mapOf(0 to 1)),
                BattleUnit("enemy", "enemy", Faction.ENEMY, 4, 4),
            ),
            events = emptyList(),
            terrain = terrain,
            // 테스트 근거: 전투 계산·난수 소비·경계값 (QUN_XIONG)을 검증한다.
            movementOffsets = hashSetOf(1 to 0, 0 to -1, -1 to 0, 0 to 1),
        )

        assertIs<TacticalActionResult.Success>(battle.movement.moveUnit("u", 2, 2))
        assertEquals(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2), battle.lastMovePath("u"))
        assertEquals(2, battle.units.getValue("u").direction)
    }

    @Test
    fun `windy weather uses mov_final for both selectable and executable range`() {
        val terrain = BattleTerrainGrid(3, 2, listOf(intArrayOf(0, 0, 0), intArrayOf(0, 0, 0)))
        val battle = Battle(
            units = listOf(
                BattleUnit("u", "u", Faction.PLAYER, 0, 0, movement = 2, terrainMovementCosts = mapOf(0 to 1)),
                BattleUnit("enemy", "enemy", Faction.ENEMY, 2, 1),
            ),
            events = emptyList(), terrain = terrain, initialWeather = BattleWeather.WINDY,
        )

        assertTrue(1 to 0 in battle.movement.reachableTiles("u"))
        assertTrue(2 to 0 !in battle.movement.reachableTiles("u"))
        assertIs<TacticalActionResult.Rejected>(battle.movement.moveUnit("u", 2, 0))
    }

    @Test
    fun `canMovePoints includes a friendly occupied node as a route but excludes a direct enemy`() {
        val terrain = BattleTerrainGrid(4, 1, listOf(intArrayOf(0, 0, 0, 0)))
        val battle = Battle(
            units = listOf(
                BattleUnit("u", "u", Faction.PLAYER, 0, 0, movement = 3, terrainMovementCosts = mapOf(0 to 1)),
                BattleUnit("friend", "friend", Faction.FRIEND, 1, 0),
                BattleUnit("enemy", "enemy", Faction.ENEMY, 3, 0),
            ),
            events = emptyList(), terrain = terrain,
        )

        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        assertTrue(2 to 0 in battle.movement.reachableTiles("u"))
        assertIs<TacticalActionResult.Success>(battle.movement.moveUnit("u", 2, 0))
        assertEquals(listOf(0 to 0, 1 to 0, 2 to 0), battle.lastMovePath("u"))
    }

    @Test
    fun `counterfactual removed guard admits a leader staging tile on the next movement only`() {
        val terrain = BattleTerrainGrid(6, 1, listOf(IntArray(6)))
        val battle = Battle(
            units = listOf(
                BattleUnit("escort", "escort", Faction.PLAYER, 0, 0, movement = 2, terrainMovementCosts = mapOf(0 to 1)),
                BattleUnit("guard", "guard", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(), terrain = terrain,
        )

        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        assertTrue(!battle.movement.canEnterTilesIgnoringEnemyWithinMoves(
            "escort", "guard", 0 to 0, setOf(4 to 0), moves = 1,
        ))
        assertTrue(battle.movement.canEnterTilesIgnoringEnemyWithinMoves(
            "escort", "guard", 0 to 0, setOf(4 to 0), moves = 2,
        ))
        assertEquals(0 to 0, battle.units.getValue("escort").tileX to battle.units.getValue("escort").tileY)
        assertEquals(1 to 0, battle.units.getValue("guard").tileX to battle.units.getValue("guard").tileY)
    }

    @Test
    fun `canMovePoints stops expansion after entering enemy-near tile unless TJYD is present`() {
        val terrain = BattleTerrainGrid(4, 2, listOf(intArrayOf(0, 0, 0, 0), intArrayOf(0, 0, 0, 0)))
/** unit: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun unit(id: String, skills: Map<Int, Int> = emptyMap()) = BattleUnit(
            id, id, Faction.PLAYER, 0, 0, movement = 4,
            terrainMovementCosts = mapOf(0 to 1), skills = skills,
        )
        val blocked = Battle(
            units = listOf(unit("u"), BattleUnit("enemy", "enemy", Faction.ENEMY, 1, 1)),
            events = emptyList(), terrain = terrain,
        )
        val bypass = Battle(
            units = listOf(unit("u", mapOf(220 to 0)), BattleUnit("enemy", "enemy", Faction.ENEMY, 1, 1)),
            events = emptyList(), terrain = terrain,
        )

        assertTrue(1 to 0 in blocked.movement.reachableTiles("u"))
        assertTrue(2 to 0 !in blocked.movement.reachableTiles("u"))
        assertTrue(2 to 0 in bypass.movement.reachableTiles("u"))
    }

    @Test
    fun `CYYD makes impassable terrain cost one in canMovePoints`() {
        val terrain = BattleTerrainGrid(3, 1, listOf(intArrayOf(0, 9, 0)))
        val normal = Battle(
            units = listOf(BattleUnit("u", "u", Faction.PLAYER, 0, 0, movement = 2, terrainMovementCosts = mapOf(0 to 1, 9 to 255))),
            events = emptyList(), terrain = terrain,
        )
        val ignoresTerrain = Battle(
            units = listOf(BattleUnit("u", "u", Faction.PLAYER, 0, 0, movement = 2, terrainMovementCosts = mapOf(0 to 1, 9 to 255), skills = mapOf(29 to 0))),
            events = emptyList(), terrain = terrain,
        )

        assertTrue(1 to 0 !in normal.movement.reachableTiles("u"))
        assertTrue(2 to 0 in ignoresTerrain.movement.reachableTiles("u"))
    }

    @Test
    fun `canMovePoints has no psAry for source paralysis state`() {
        val battle = Battle(
            units = listOf(BattleUnit("u", "u", Faction.PLAYER, 0, 0, statuses = linkedMapOf(BattleStatus.PARALYSIS to 1))),
            events = emptyList(),
        )

        assertTrue(battle.movement.reachableTiles("u").isEmpty())
    }

    @Test
    fun `unitMove retains the same start-inclusive canMovePoints parent route that move2 receives`() {
        val terrain = BattleTerrainGrid(3, 2, listOf(intArrayOf(0, 9, 0), intArrayOf(0, 0, 0)))
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "u", "u", Faction.PLAYER, 0, 0, movement = 3,
                    terrainMovementCosts = mapOf(0 to 1, 9 to 2),
                ),
                BattleUnit("enemy", "enemy", Faction.ENEMY, 2, 1),
            ),
            events = emptyList(),
            terrain = terrain,
        )

        assertIs<TacticalActionResult.Success>(battle.movement.moveUnit("u", 2, 0))
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택 (FIFO)을 검증한다.
        assertEquals(listOf(0 to 0, 1 to 0, 2 to 0), battle.lastMovePath("u"))
        assertEquals(2, battle.units.getValue("u").tileX)
        assertEquals(0, battle.units.getValue("u").tileY)
    }
}
