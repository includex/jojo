package com.jojo.game
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * class  `BattleMovementRouteTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleMovementRouteTest {
    @Test
    fun `authored battle move uses source weighted AStar without committing the unit`() {
        val terrain = BattleTerrainGrid(5, 5, List(5) { IntArray(5) })
        val actor = BattleUnit(
            "mine-0", "actor", Faction.PLAYER, 0, 0,
            terrainMovementCosts = mapOf(0 to 1), characterId = 0,
        )
        val battle = Battle(listOf(actor), emptyList(), terrain = terrain)

        assertEquals(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2), battle.scriptedMovePath(0, 2, 2))
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

        // Source QUN_XIONG order checks below the occupied seed first.
        assertEquals(2 to 3, battle.scriptedMovePath(0, 2, 2)?.last())
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
            // Deliberately unordered input: source traversal order comes from
            // hitarea[QUN_XIONG].ps, not the caller's Set implementation.
            movementOffsets = hashSetOf(1 to 0, 0 to -1, -1 to 0, 0 to 1),
        )

        assertIs<TacticalActionResult.Success>(battle.moveUnit("u", 2, 2))
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

        assertTrue(1 to 0 in battle.reachableTiles("u"))
        assertTrue(2 to 0 !in battle.reachableTiles("u"))
        assertIs<TacticalActionResult.Rejected>(battle.moveUnit("u", 2, 0))
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

        // BattleScreen.canMovePoints keeps the friendly x=1 node in psHash,
        // allowing its child x=2.  The public selection area omits x=1 but
        // the legal destination x=2 retains that exact parent route.
        assertTrue(2 to 0 in battle.reachableTiles("u"))
        assertIs<TacticalActionResult.Success>(battle.moveUnit("u", 2, 0))
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

        // The current turn attacks guard at (1,0). Once that real action
        // removes it, staging (4,0) is not reachable in one move but is
        // reachable on the following movement turn. The probe mutates none
        // of the actor/guard state while proving that bounded route.
        assertTrue(!battle.canEnterTilesIgnoringEnemyWithinMoves(
            "escort", "guard", 0 to 0, setOf(4 to 0), moves = 1,
        ))
        assertTrue(battle.canEnterTilesIgnoringEnemyWithinMoves(
            "escort", "guard", 0 to 0, setOf(4 to 0), moves = 2,
        ))
        assertEquals(0 to 0, battle.units.getValue("escort").tileX to battle.units.getValue("escort").tileY)
        assertEquals(1 to 0, battle.units.getValue("guard").tileX to battle.units.getValue("guard").tileY)
    }

    @Test
    fun `canMovePoints stops expansion after entering enemy-near tile unless TJYD is present`() {
        val terrain = BattleTerrainGrid(4, 2, listOf(intArrayOf(0, 0, 0, 0), intArrayOf(0, 0, 0, 0)))
/**
 * 공개 메서드 `unit`
 *
 * ### 파라미터
- `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
- `skills` (`Map<Int, Int> = emptyMap(`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

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

        assertTrue(1 to 0 in blocked.reachableTiles("u"))
        assertTrue(2 to 0 !in blocked.reachableTiles("u"))
        assertTrue(2 to 0 in bypass.reachableTiles("u"))
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

        assertTrue(1 to 0 !in normal.reachableTiles("u"))
        assertTrue(2 to 0 in ignoresTerrain.reachableTiles("u"))
    }

    @Test
    fun `canMovePoints has no psAry for source paralysis state`() {
        val battle = Battle(
            units = listOf(BattleUnit("u", "u", Faction.PLAYER, 0, 0, statuses = linkedMapOf(BattleStatus.PARALYSIS to 1))),
            events = emptyList(),
        )

        assertTrue(battle.reachableTiles("u").isEmpty())
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

        assertIs<TacticalActionResult.Success>(battle.moveUnit("u", 2, 0))
        // Source canMovePoints processes offsets FIFO: down first, then
        // right.  The more economical direct route is retained as the parent
        // chain supplied to BattleScreen.unitMove/BattleUnit.move2.
        assertEquals(listOf(0 to 0, 1 to 0, 2 to 0), battle.lastMovePath("u"))
        assertEquals(2, battle.units.getValue("u").tileX)
        assertEquals(0, battle.units.getValue("u").tileY)
    }
}
