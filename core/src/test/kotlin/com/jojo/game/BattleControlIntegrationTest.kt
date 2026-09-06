// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.command.*
import com.jojo.game.domain.battle.BattleTerrainGrid

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** BattleControlIntegrationTest: BattleControlIntegration의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleControlIntegrationTest {
    @Test
    fun `AI batch stops immediately when an actor produces terminal outcome`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 1, 0, hitPoints = 1, maxHitPoints = 1),
                BattleUnit("first", "첫 적", Faction.ENEMY, 0, 0, attack = 100,
                    skills = mapOf(92 to 0, 226 to 0)),
                BattleUnit("second", "둘째 적", Faction.ENEMY, 5, 0, attack = 100,
                    skills = mapOf(92 to 0, 226 to 0)),
            ),
            events = emptyList(),
        )
        battle.roundLifecycle.endTurn()

        val result = battle.ai.resolveTurn()

        assertEquals(BattleOutcome.ENEMY_VICTORY, battle.outcome())
        assertEquals(1, result.attacks)
        assertEquals(false, battle.units.getValue("second").hasActed)
        assertEquals(5, battle.units.getValue("second").tileX)
    }

    @Test
    fun `Control AI subtracts original magic MP score cost before choosing an action`() {
        val paralysis = GameDataCatalog.MagicProfile(
            id = 1, name = "마비", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 0, harmType = 4, category = 10,
        )
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 1, 0),
                // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
                BattleUnit("enemy", "적군", Faction.ENEMY, 0, 0, attackOffsets = emptySet(),
                    magicPoints = 1, maxMagicPoints = 1, magic = listOf(paralysis)),
            ),
            events = emptyList(),
        )
        battle.roundLifecycle.endTurn()

        assertEquals(AiTurnResult(moves = 0, attacks = 0, holds = 1), battle.ai.resolveTurn())
        assertEquals(1, battle.units.getValue("enemy").magicPoints)
        assertEquals(null, battle.units.getValue("player").statuses[BattleStatus.PARALYSIS])
    }

    @Test
    fun `Control AI excludes same camp units from enemy strategy splash scoring`() {
        val moraleDown = GameDataCatalog.MagicProfile(
            id = 25, name = "탈진", type = 10, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(0 to 1)), effectAreaId = 0,
            effectOffsets = setOf(-1 to 0, 1 to 0), expendMp = 6, power = 28,
            harmType = 4, category = ControlScoring.Category.JDSQ,
        )
        val poison = GameDataCatalog.MagicProfile(
            id = 33, name = "독연", type = 16, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(0 to 1)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 8, power = 70,
            harmType = 1, category = ControlScoring.Category.ZD,
        )
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "대상", Faction.PLAYER, 0, 1, hitPoints = 96, maxHitPoints = 96, spirit = 21, armType = 2),
                BattleUnit("caster", "곽가", Faction.ENEMY, 0, 0, ai = ControlAi.ACTIVE, movement = 0, attackOffsets = emptySet(),
                    magicPoints = 55, maxMagicPoints = 55, spirit = 71, level = 3, magic = listOf(moraleDown, poison)),
                // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
                BattleUnit("ally-left", "아군1", Faction.ENEMY, -1, 1, armType = 2),
                BattleUnit("ally-right", "아군2", Faction.ENEMY, 1, 1, armType = 2),
            ),
            events = emptyList(),
        )
        battle.roundLifecycle.endTurn()

        val result = battle.ai.resolveTurn(maxUnits = 1)
        assertEquals(1, result.attacks, "result=$result actor=${battle.lastAiUnitResolution?.actorId} trace=${battle.traceActions}")
        assertEquals(47, battle.units.getValue("caster").magicPoints)
        assertTrue(battle.traceActions.any { it.contains("magic=33") })
    }

    @Test
    fun `move magic AI skips physical targets that can attack it unless WFJGJ clears flag two`() {
/** battle: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun battle(skills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 1, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 0, 0, ai = ControlAi.MOVE_MAGIC, aiTargetX = 0, aiTargetY = 0, skills = skills),
            ),
            events = emptyList(),
        )

        val cautious = battle(emptyMap())
        cautious.roundLifecycle.endTurn()
        assertEquals(0, cautious.ai.resolveTurn().attacks)
        assertEquals(100, cautious.units.getValue("player").hitPoints)

        val wfJgj = battle(mapOf(226 to 1))
        wfJgj.roundLifecycle.endTurn()
        assertEquals(1, wfJgj.ai.resolveTurn().attacks)
        assertTrue(wfJgj.units.getValue("player").hitPoints < 100)
    }

    @Test
    fun `friend magic does not receive enemy-only minimum damage`() {
        val weakMagic = GameDataCatalog.MagicProfile(
            id = 1, name = "약한 전략", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 1, harmType = 1, category = 0,
        )
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0),
                BattleUnit("friend", "우군", Faction.FRIEND, 0, 0, spirit = 1, morale = 1_000, magicPoints = 1, maxMagicPoints = 100, magic = listOf(weakMagic)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, spirit = 100, morale = 1, hitPoints = 100, maxHitPoints = 100),
            ),
            events = emptyList(),
        )

        battle.roundLifecycle.endTurn()
        val result = battle.combat.castMagic("friend", "enemy", 1)

        assertEquals(1, (result as TacticalActionResult.Magic).targets.single().damage)
    }

    @Test
    fun `AI_USE thirteen excludes strategy from AI selection`() {
        val strategy = GameDataCatalog.MagicProfile(
            id = 71, name = "화계", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 255, harmType = 1, category = 0,
            // 테스트 근거: 전투 계산·난수 소비·경계값 (MAGIC_ATTR_NAME, AIUSE)을 검증한다.
            condition = 0, aiUse = 13,
        )
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 1, 0, spirit = 1, morale = 1),
                BattleUnit("enemy", "적군", Faction.ENEMY, 0, 0, ai = ControlAi.ACTIVE, magicPoints = 3, maxMagicPoints = 3, spirit = 100, morale = 100, magic = listOf(strategy)),
            ),
            events = emptyList(),
            initialWeather = BattleWeather.HEAVY_RAIN,
        )

        battle.roundLifecycle.endTurn()
        val result = battle.ai.resolveTurn()

        assertEquals(1, result.attacks)
        assertEquals(3, battle.units.getValue("enemy").magicPoints)
    }


    @Test
    fun `designated attack AI becomes active when its original target no longer exists`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 3, aiTargetCharacterId = 99),
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0),
            ),
            events = emptyList(),
        )

        battle.roundLifecycle.endTurn()
        assertEquals(1, battle.ai.resolveTurn().moves)
        assertEquals(1, battle.units.getValue("enemy").ai)
        assertEquals(3, battle.units.getValue("enemy").tileX)
    }

    @Test
    fun `designated follow AI becomes passive when already within source distance`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("leader", "주장", Faction.ENEMY, 1, 0, characterId = 9, ai = 2),
                BattleUnit("follower", "부장", Faction.ENEMY, 0, 0, ai = 5, aiTargetCharacterId = 9),
                BattleUnit("player", "아군", Faction.PLAYER, 5, 0),
            ),
            events = emptyList(),
        )

        battle.roundLifecycle.endTurn()
        battle.ai.resolveTurn()
        assertEquals(0, battle.units.getValue("follower").ai)
        assertEquals(0, battle.units.getValue("follower").tileX)
    }
    @Test
    fun `AI Control process1 holds paralyzed unit before scoring or attacking`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, statuses = linkedMapOf(BattleStatus.PARALYSIS to 2)),
            ),
            events = emptyList(),
        )
        battle.roundLifecycle.endTurn() // PLAYER → ENEMY

        val result = battle.ai.resolveTurn()

        assertEquals(AiTurnResult(moves = 0, attacks = 0, holds = 1), result)
        assertEquals(100, battle.units.getValue("player").hitPoints)
        // 테스트 근거: 전투 계산·난수 소비·경계값 (HOLD)을 검증한다.
        assertEquals(ControlAi.PASSIVE, battle.units.getValue("enemy").ai)
        assertTrue(battle.units.getValue("enemy").hasActed)
    }

    @Test
    fun `BattleScreen ai2 holds confused unit before Control process1`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, statuses = linkedMapOf(BattleStatus.CONFUSION to 2)),
            ),
            events = emptyList(),
        )
        battle.roundLifecycle.endTurn()

        assertEquals(AiTurnResult(moves = 0, attacks = 0, holds = 1), battle.ai.resolveTurn())
        assertEquals(ControlAi.PASSIVE, battle.units.getValue("enemy").ai)
        assertTrue(battle.units.getValue("enemy").hasActed)
    }

    @Test
    fun `AI Control invokes scored decision when process1 does not hold`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0, hitPoints = 500, maxHitPoints = 500),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, skills = mapOf(92 to 0)),
            ),
            events = emptyList(),
        )
        battle.roundLifecycle.endTurn()

        val result = battle.ai.resolveTurn()

        assertTrue(result.attacks == 1 || result.moves == 1)
    }

    @Test
    fun `designated attack AI applies source GJZDWJ target bonus`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("first", "첫 대상", Faction.PLAYER, 0, 0, hitPoints = 500, maxHitPoints = 500, characterId = 1),
                BattleUnit("designated", "지정 대상", Faction.PLAYER, 2, 0, hitPoints = 500, maxHitPoints = 500, characterId = 2),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, ai = ControlAi.ATTACK_UNIT, aiTargetCharacterId = 2),
            ),
            events = emptyList(),
        )

        battle.roundLifecycle.endTurn()
        battle.ai.resolveTurn()

        assertEquals(500, battle.units.getValue("first").hitPoints)
        assertTrue(battle.units.getValue("designated").hitPoints < 500)
    }

    @Test
    fun `AI Control surrounding uses original QUN XIONG cardinal four cells then attacks in place`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("north", "북", Faction.PLAYER, 2, 1),
                BattleUnit("east", "동", Faction.PLAYER, 3, 2),
                BattleUnit("west", "서", Faction.PLAYER, 1, 2),
                BattleUnit("south", "남", Faction.PLAYER, 2, 3),
                BattleUnit("enemy", "적군", Faction.ENEMY, 2, 2),
            ),
            events = emptyList(),
        )
        battle.roundLifecycle.endTurn()

        // 테스트 근거: 전투 계산·난수 소비·경계값 (HOLD)을 검증한다.
        assertEquals(AiTurnResult(0, 1, 0), battle.ai.resolveTurn())
        assertEquals(ControlAi.PASSIVE, battle.units.getValue("enemy").ai)
    }

    @Test
    fun `retreat destination keeps persistent AI until the next controller entry`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 5, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 0, 0, ai = 6, aiTargetX = 1, aiTargetY = 0),
            ),
            events = emptyList(),
        )
        battle.roundLifecycle.endTurn()
        battle.ai.resolveTurn()
        assertEquals(6, battle.units.getValue("enemy").ai)
    }

    @Test
    fun `retreat AI attacks the first enemy blocking its source AStar five route`() {
        val terrain = BattleTerrainGrid(5, 1, listOf(intArrayOf(0, 0, 0, 0, 0)))
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "friend", "신비", Faction.FRIEND, 0, 0,
                    movement = 1, attack = 100, critical = 100,
                    ai = ControlAi.RETREAT_TO, aiTargetX = 4, aiTargetY = 0,
                    characterId = 117, terrainMovementCosts = mapOf(0 to 1),
                ),
                BattleUnit(
                    "blocker", "적군", Faction.ENEMY, 2, 0,
                    hitPoints = 500, maxHitPoints = 500, defense = 100, critical = 1,
                    remoteAttack = true, attackOffsets = setOf(0 to 2, 0 to -2, 2 to 0, -2 to 0),
                    characterId = 236, terrainMovementCosts = mapOf(0 to 1),
                ),
            ),
            events = emptyList(),
            terrain = terrain,
        )
        battle.selectVerificationFaction(Faction.FRIEND)

        assertEquals(AiTurnResult(1, 1, 0), battle.ai.resolveTurn())
        assertEquals(1 to 0, battle.units.getValue("friend").tileX to battle.units.getValue("friend").tileY)
        assertTrue(battle.units.getValue("blocker").hitPoints < 500)
        assertEquals(ControlAi.RETREAT_TO, battle.units.getValue("friend").ai)
    }

    @Test
    fun `designated bonus cannot revive a non-positive source attack score`() {
        val terrain = BattleTerrainGrid(2, 1, listOf(intArrayOf(0, 0)))
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "friend", "신비", Faction.FRIEND, 0, 0,
                    hitPoints = 23, maxHitPoints = 105,
                    attack = 26, defense = 47, movement = 0, armType = 1,
                    ai = ControlAi.ATTACK_UNIT, aiTargetCharacterId = 211,
                    characterId = 117, terrainMovementCosts = mapOf(0 to 1),
                ),
                BattleUnit(
                    "blocker", "보병", Faction.ENEMY, 1, 0,
                    hitPoints = 119, maxHitPoints = 119,
                    attack = 500, defense = 53,
                    characterId = 211, terrainMovementCosts = mapOf(0 to 1),
                ),
            ),
            events = emptyList(),
            terrain = terrain,
        )
        battle.selectVerificationFaction(Faction.FRIEND)

        assertTrue(battle.ai.previewAttackValue("friend", "blocker") < 1)
        assertEquals(AiTurnResult(0, 0, 1), battle.ai.resolveTurn())
        assertEquals(0 to 0, battle.units.getValue("friend").tileX to battle.units.getValue("friend").tileY)
        assertEquals(119, battle.units.getValue("blocker").hitPoints)
        assertEquals(ControlAi.ATTACK_UNIT, battle.units.getValue("friend").ai)
    }

    @Test
    fun `retreat AI follows source AStar detour instead of Manhattan nearest tile`() {
        val terrain = BattleTerrainGrid(7, 5, List(5) { IntArray(7) })
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0),
                BattleUnit(
                    "enemy", "원상", Faction.ENEMY, 6, 2,
                    movement = 3, ai = ControlAi.RETREAT_TO, aiTargetX = 0, aiTargetY = 2,
                    terrainMovementCosts = mapOf(0 to 1),
                ),
            ),
            events = emptyList(),
            terrain = terrain,
            blockedTiles = setOf(3 to 0, 3 to 1, 3 to 2, 3 to 3),
        )

        battle.roundLifecycle.endTurn()
        battle.ai.resolveTurn()

        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        assertEquals(5 to 4, battle.units.getValue("enemy").tileX to battle.units.getValue("enemy").tileY)
    }

    @Test
    fun `retreat AI finds an empty psHash detour by terrain cost rather than BFS order`() {
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        val terrain = BattleTerrainGrid(7, 5, List(5) { y ->
            IntArray(7) { x -> if (x == 6 && y == 4) 1 else 0 }
        })
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("blocker", "아군", Faction.ENEMY, 5, 4, ai = ControlAi.HOLD),
                BattleUnit(
                    "enemy", "원상", Faction.ENEMY, 6, 2,
                    movement = 3, ai = ControlAi.RETREAT_TO, aiTargetX = 0, aiTargetY = 2,
                    terrainMovementCosts = mapOf(0 to 1, 1 to 2),
                ),
            ),
            events = emptyList(),
            terrain = terrain,
            blockedTiles = setOf(3 to 0, 3 to 1, 3 to 2, 3 to 3),
        )

        battle.roundLifecycle.endTurn()
        battle.ai.resolveTurn()

        assertEquals(5 to 3, battle.units.getValue("enemy").tileX to battle.units.getValue("enemy").tileY)
    }

    @Test
    fun `wounded AI uses original resumeHP terrain before normal scoring`() {
        val terrain = BattleTerrainGrid(5, 1, listOf(intArrayOf(0, 18, 0, 0, 0)))
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 0, 0, hitPoints = 10, maxHitPoints = 100, terrainMovementCosts = mapOf(0 to 1, 18 to 1)),
            ),
            events = emptyList(),
            terrain = terrain,
            terrainResumeRates = mapOf(18 to 50),
        )
        battle.roundLifecycle.endTurn()
        battle.ai.resolveTurn()
        val enemy = battle.units.getValue("enemy")
        assertEquals(1, enemy.tileX)
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        assertEquals(ControlAi.PASSIVE, enemy.ai)
    }

    @Test
    fun `wounded enemy retreats to own master when no resume terrain exists`() {
        val terrain = BattleTerrainGrid(6, 1, listOf(intArrayOf(0, 0, 0, 0, 0, 0)))
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 5, 0),
                BattleUnit("master", "주장", Faction.ENEMY, 3, 0, ai = 2, terrainMovementCosts = mapOf(0 to 1)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 0, 0, hitPoints = 10, maxHitPoints = 100, terrainMovementCosts = mapOf(0 to 1)),
            ),
            events = emptyList(), terrain = terrain, enemyMasterUnitId = "master",
        )
        battle.roundLifecycle.endTurn()
        battle.ai.resolveTurn()
        assertEquals(ControlAi.PASSIVE, battle.units.getValue("enemy").ai)
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        assertEquals(2, battle.units.getValue("enemy").tileX)
    }
}
