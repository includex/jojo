package com.jojo.game
import com.jojo.game.presentation.battle.timeline.*
import com.jojo.game.domain.battle.*

import java.util.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `BattleAttackSequenceTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleAttackSequenceTest {
    @Test
    fun `ZYSH redirects landed harm after primary and before splash settlement`() {
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "attacker", "공격자", Faction.PLAYER, 0, 0,
                    attack = 100, skills = mapOf(92 to 0, 226 to 0),
                ),
                BattleUnit(
                    "defender", "전가", Faction.ENEMY, 1, 0,
                    defense = 1, hitPoints = 200, maxHitPoints = 200,
                    skills = mapOf(277 to 100),
                    attackOffsets = linkedSetOf(-1 to 0, 1 to 0),
                ),
                BattleUnit(
                    "recipient", "대신 맞는 아군", Faction.PLAYER, 2, 0,
                    hitPoints = 200, maxHitPoints = 200,
                ),
            ),
            events = emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "defender"))
        val targets = result.physicalPasses.single().targets

        assertEquals(listOf("defender", "recipient"), targets.map { it.targetId })
        assertEquals(0, targets[0].resolvedHarm)
        assertTrue(targets[1].resolvedHarm > 0)
        assertEquals(200, battle.units.getValue("defender").hitPoints)
        assertTrue(battle.units.getValue("recipient").hitPoints < 200)
    }

    private class FixedRandom(private vararg val values: Int) : Random() {
        private var index = 0
        override fun nextInt(bound: Int): Int = values[minOf(index++, values.lastIndex)].mod(bound)
    }
    private class CountingRandom : Random() {
        var calls = 0
        override fun nextInt(bound: Int): Int = (if (++calls == 1) 0 else 100).mod(bound)
    }
    private class ZeroCountingRandom : Random() {
        var calls = 0
        override fun nextInt(bound: Int): Int { calls++; return 0 }
    }

    @Test
    fun `ZDSY uses its self property after damage without consuming enemy inventory`() {
        var automaticConsumes = 0
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0)),
                BattleUnit("defender", "자동회복", Faction.ENEMY, 1, 0, skills = mapOf(284 to 200)),
            ),
            events = emptyList(),
            propertyItems = mapOf(200 to BattlePropertyItem(200, "자동회복약", 26, 50)),
            consumeAutomaticProperty = { automaticConsumes++ },
            random = FixedRandom(100, 0, 100),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(30, result.damage)
        assertEquals("HP 30 회복", result.automaticProperty?.effect)
        assertEquals(100, battle.units.getValue("defender").hitPoints)
        assertEquals(0, automaticConsumes) // source only pushProperty for MINE.
    }

    @Test
    fun `property type 34 follows source ZL spirit mapping`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("user", "사용자", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "적", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
            propertyItems = mapOf(701 to BattlePropertyItem(701, "지력약", 34, 1)),
            consumeProperty = { true },
        )

        battle.combat.useProperty("user", "user", 701)

        val unit = battle.units.getValue("user")
        assertEquals(1, unit.attributeLifts[BattleAttribute.SPIRIT])
        assertNull(unit.attributeLifts[BattleAttribute.DEFENSE])
    }

    @Test
    fun `ZDGJ includes its source equal-rate boundary and MPFY does not discard the landed state`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 272 to 50)),
                BattleUnit("defender", "마법 방어", Faction.ENEMY, 1, 0, magicPoints = 40, maxMagicPoints = 40, skills = mapOf(2 to 0)),
            ),
            events = emptyList(),
            // physical hit, then ZDGJ's inclusive `<= 50` check.
            random = FixedRandom(0, 50),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(0, result.damage)
        assertEquals(30, result.mpShieldDamage)
        assertEquals(100, battle.units.getValue("defender").hitPoints)
        assertEquals(3, battle.units.getValue("defender").statuses[BattleStatus.POISON])
    }

    @Test
    fun `continuous attack reuses one source getAtkStatus batch without a second status roll`() {
        val random = ZeroCountingRandom()
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 144 to 100, 270 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0),
            ), events = emptyList(), random = random,
        )

        battle.combat.attack("attacker", "defender", damage = 10)

        // countRate owns continuous/hit gauges; only the one MBGJ status
        // roll consumes Model.random, and the second attack reuses it.
        assertEquals(1, random.calls)
        assertEquals(2, battle.units.getValue("defender").statuses[BattleStatus.PARALYSIS])
    }

    @Test
    fun `BJBLJ critical changes BattleScreen attack loop from one pass to two`() {
        val battle = Battle(
            units = listOf(
                // Low morale makes the ordinary SJL countRate pass fail;
                // GJJDMZ guarantees each physical hit and ZMYJGJ guarantees
                // the critical that activates BJBLJ.
                BattleUnit("attacker", "필살", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 270 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, morale = 100,
                    hitPoints = 500, maxHitPoints = 500),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        assertEquals(true, result.critical)
        assertEquals(true, result.followUpDamage > 0)
    }

    @Test
    fun `critical speech follows source checkCrit alternating gate per attacker`() {
        val speech = GameDataCatalog.CriticalSpeechProfile(listOf("필살 대사"), randomized = false)
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "필살", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 270 to 0), criticalSpeech = speech),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, morale = 100,
                    hitPoints = 500, maxHitPoints = 500),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        assertEquals("필살 대사", result.physicalPasses[0].criticalSpeech)
        assertNull(result.physicalPasses[1].criticalSpeech)
        assertEquals(2, battle.units.getValue("attacker").criticalSpeechChecks)
    }

    @Test
    fun `generic critical speech consumes source default random after harm calculation`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "필살", Faction.PLAYER, 0, 0,
                    skills = mapOf(92 to 0, 226 to 0, 270 to 0),
                    criticalSpeech = GameDataCatalog.CriticalSpeechProfile(
                        listOf("첫째", "둘째", "셋째"), randomized = true,
                    )),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500),
            ),
            events = emptyList(),
            random = FixedRandom(2),
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        assertEquals("셋째", result.physicalPasses.single().criticalSpeech)
    }

    @Test
    fun `physical and magic criticals share one source checkCrit counter`() {
        val magic = GameDataCatalog.MagicProfile(
            10, "회오리", 3, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 0, 100, 0, 0,
        )
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "필살", Faction.PLAYER, 0, 0, spirit = 100, morale = 100,
                    skills = mapOf(92 to 0, 226 to 0, 270 to 0), magic = listOf(magic),
                    criticalSpeech = GameDataCatalog.CriticalSpeechProfile(listOf("공용 필살"), randomized = false)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, spirit = 1, morale = 1,
                    hitPoints = 1_000, maxHitPoints = 1_000),
            ),
            events = emptyList(),
        )

        val physical = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack
        battle.units.getValue("attacker").hasActed = false
        val strategy = battle.combat.castMagic("attacker", "defender", 10) as TacticalActionResult.Magic

        assertEquals("공용 필살", physical.physicalPasses.single().criticalSpeech)
        assertEquals(listOf(null), strategy.criticalSpeeches)
        assertEquals(2, battle.units.getValue("attacker").criticalSpeechChecks)
    }

    @Test
    fun `each BJBLJ pass runs its own MPFY cap and records MP_ADD`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "필살", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 270 to 0)),
                BattleUnit("defender", "MP 방어", Faction.ENEMY, 1, 0, morale = 100,
                    hitPoints = 1_000, maxHitPoints = 1_000, magicPoints = 1_000, maxMagicPoints = 1_000,
                    skills = mapOf(2 to 0)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        assertEquals(0, result.damage)
        assertEquals(0, result.followUpDamage)
        assertEquals(true, result.mpShieldDamage > 0)
        assertEquals(true, result.followUpMpShieldDamage > 0)
        assertEquals(1_000 - result.mpShieldDamage - result.followUpMpShieldDamage, battle.units.getValue("defender").magicPoints)
        assertEquals(1_000, battle.units.getValue("defender").hitPoints)
    }

    @Test
    fun `each BJBLJ pass independently applies defender JQFY`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "필살", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 270 to 0)),
                BattleUnit("defender", "금전 방어", Faction.ENEMY, 1, 0, morale = 100,
                    hitPoints = 1_000, maxHitPoints = 1_000, skills = mapOf(125 to 1)),
            ),
            events = emptyList(),
            initialEnemyMoney = 10_000,
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        assertEquals(1, result.damage)
        assertEquals(1, result.followUpDamage)
        assertEquals(998, battle.units.getValue("defender").hitPoints)
        assertEquals(true, result.moneyShieldSpent > 1)
    }

    @Test
    fun `each BJBLJ pass resolves source QXL then FTSH`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "흡수", Faction.PLAYER, 0, 0,
                    hitPoints = 300, maxHitPoints = 1_000, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 270 to 0, 298 to 0)),
                BattleUnit("defender", "반사", Faction.ENEMY, 1, 0,
                    hitPoints = 1_000, maxHitPoints = 1_000, morale = 100, skills = mapOf(40 to 100)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        assertEquals(true, result.followUpDamage > 0)
        assertEquals(result.damage + result.followUpDamage, result.qxlHealing)
        assertEquals(result.damage + result.followUpDamage, result.recoilDamage)
        assertEquals(300, battle.units.getValue("attacker").hitPoints)
    }

    @Test
    fun `each BJBLJ pass independently transfers source XSJQ money`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "약탈", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 237 to 1, 270 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0,
                    hitPoints = 1_000, maxHitPoints = 1_000, morale = 100),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        val transferred = result.damage + result.followUpDamage
        assertEquals(transferred, result.playerMoneyDelta)
        assertEquals(-transferred, result.enemyMoneyDelta)
        assertEquals(transferred, battle.playerMoney)
        assertEquals(-transferred, battle.enemyMoney)
    }

    @Test
    fun `each BJBLJ pass applies source TPGJ from the target current position`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "밀치기", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 221 to 0, 226 to 0, 270 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0,
                    hitPoints = 1_000, maxHitPoints = 1_000, morale = 100),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        assertEquals(true, result.followUpDamage > 0)
        assertEquals(3, battle.units.getValue("defender").tileX)
        assertEquals(0, battle.units.getValue("defender").tileY)
    }

    @Test
    fun `each BJBLJ pass increments struck unit source CHGJ temp`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "필살", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 270 to 0)),
                BattleUnit("defender", "축적", Faction.ENEMY, 1, 0, morale = 100,
                    hitPoints = 1_000, maxHitPoints = 1_000, skills = mapOf(26 to 1)),
            ),
            events = emptyList(),
        )

        battle.combat.attack("attacker", "defender")

        assertEquals(2, battle.skillTemp("defender", 26))
    }

    @Test
    fun `each BJBLJ pass invokes source automatic ZDSY property`() {
        var uses = 0
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "필살", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 270 to 0)),
                BattleUnit("defender", "자동 회복", Faction.ENEMY, 1, 0, morale = 100,
                    hitPoints = 1_000, maxHitPoints = 1_000, skills = mapOf(284 to 200)),
            ),
            events = emptyList(),
            propertyItems = mapOf(200 to BattlePropertyItem(200, "자동회복약", 26, 1_000)),
            consumeAutomaticProperty = { uses++ },
        )

        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack

        assertEquals(true, result.followUpDamage > 0)
        assertEquals(1_000, battle.units.getValue("defender").hitPoints)
        // Enemy auto-use does not touch the player's item store in source.
        assertEquals(0, uses)
    }

    @Test
    fun `BJBLJ also extends a critical counterattack to its source second pass`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, hitPoints = 500, maxHitPoints = 500,
                    skills = mapOf(92 to 0)),
                BattleUnit("defender", "반격", Faction.ENEMY, 1, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 270 to 0)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 1) as TacticalActionResult.Attack

        assertEquals(true, result.counterCritical)
        assertEquals(true, result.counterFollowUpDamage > 0)
    }

    @Test
    fun `counterattack MPFY applies MP_ADD and skips the source HP branch`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "MP 방어", Faction.PLAYER, 0, 0,
                    hitPoints = 500, maxHitPoints = 500, magicPoints = 10, maxMagicPoints = 10,
                    skills = mapOf(2 to 0)),
                BattleUnit("defender", "반격", Faction.ENEMY, 1, 0, skills = mapOf(92 to 0)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 1) as TacticalActionResult.Attack

        assertEquals(10, result.counterMpShieldDamage)
        assertEquals(0, result.counterDamage)
        assertEquals(0, battle.units.getValue("attacker").magicPoints)
        assertEquals(500, battle.units.getValue("attacker").hitPoints)
    }

    @Test
    fun `counterattack resolves its source QXL and defender FTSH tail`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "반사", Faction.PLAYER, 0, 0, skills = mapOf(40 to 100)),
                BattleUnit("defender", "반격 흡수", Faction.ENEMY, 1, 0,
                    hitPoints = 1_000, maxHitPoints = 1_000, skills = mapOf(92 to 0, 298 to 0)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 1) as TacticalActionResult.Attack

        // The counterattacker took the primary 1-point hit first, so QXL
        // is capped by its one missing HP before FTSH reflects full n.
        assertEquals(1, result.qxlHealing)
        assertEquals(result.counterDamage, result.recoilDamage)
        assertEquals(1_000 - result.counterDamage, battle.units.getValue("defender").hitPoints)
    }

    @Test
    fun `counterattack independently resolves source XSJQ money transfer`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0),
                BattleUnit("defender", "반격 약탈", Faction.ENEMY, 1, 0, skills = mapOf(92 to 0, 237 to 1)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 1) as TacticalActionResult.Attack

        assertEquals(-result.counterDamage, result.playerMoneyDelta)
        assertEquals(result.counterDamage, result.enemyMoneyDelta)
        assertEquals(-result.counterDamage, battle.playerMoney)
        assertEquals(result.counterDamage, battle.enemyMoney)
    }

    @Test
    fun `counterattack target invokes source automatic ZDSY property`() {
        var uses = 0
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "자동 회복", Faction.PLAYER, 0, 0,
                    hitPoints = 1_000, maxHitPoints = 1_000, skills = mapOf(284 to 200)),
                BattleUnit("defender", "반격", Faction.ENEMY, 1, 0, skills = mapOf(92 to 0)),
            ),
            events = emptyList(),
            propertyItems = mapOf(200 to BattlePropertyItem(200, "자동회복약", 26, 1_000)),
            consumeAutomaticProperty = { uses++ },
        )

        battle.combat.attack("attacker", "defender", damage = 1)

        assertEquals(1, uses)
        assertEquals(1_000, battle.units.getValue("attacker").hitPoints)
    }

    @Test
    fun `counterattack applies source TPGJ to its original attacker target`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 1, 0),
                BattleUnit("defender", "밀치기 반격", Faction.ENEMY, 2, 0, skills = mapOf(92 to 0, 221 to 0)),
            ),
            events = emptyList(),
        )

        battle.combat.attack("attacker", "defender", damage = 1)

        assertEquals(0, battle.units.getValue("attacker").tileX)
        assertEquals(0, battle.units.getValue("attacker").tileY)
    }

    @Test
    fun `source countRate guarantees a 100 percent physical hit without a random roll`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(), random = FixedRandom(100),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(true, result.hit)
        assertEquals(30, result.damage)
    }

    @Test
    fun `source physical hit rate truncates 80 over 50 critical to 96 percent`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, critical = 80, skills = mapOf(92 to 0, 226 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, critical = 50),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 1) as TacticalActionResult.Attack

        assertEquals(96, result.hitRate)
    }

    @Test
    fun `source PKDX uses defender lowest final ATT through MOR for physical hit`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "파괴", Faction.PLAYER, 0, 0, critical = 80, skills = mapOf(165 to 0, 226 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0,
                    attack = 40, defense = 20, spirit = 30, critical = 100, morale = 50),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 1) as TacticalActionResult.Attack

        // BattleUnit._pkdx picks 20 (defense), not the target's 100 CRI.
        assertEquals(100, result.hitRate)
    }

    @Test
    fun `source continuous rate truncates 80 over 50 critical to 12 percent`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, critical = 80,
                    skills = mapOf(92 to 0, 226 to 0), rateAccumulators = linkedMapOf(2 to 75)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, critical = 50,
                    rateAccumulators = linkedMapOf(3 to 0)),
            ),
            events = emptyList(),
        )

        // 75 + 12 loses to the opposing 88 gauge.  Rounding to 13 would
        // incorrectly produce the second _attack2 pass at this boundary.
        val result = battle.combat.attack("attacker", "defender") as TacticalActionResult.Attack
        assertEquals(0, result.followUpDamage)
    }

    @Test
    fun `countRate accumulates a 25 percent physical hit on the fourth source attempt`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, critical = 1, skills = mapOf(226 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, critical = 100, hitPoints = 100, maxHitPoints = 100),
            ),
            events = emptyList(),
        )

        val results = buildList {
            repeat(4) {
                add(battle.combat.attack("attacker", "defender", damage = 1) as TacticalActionResult.Attack)
                battle.units.getValue("attacker").hasActed = false
            }
        }

        assertEquals(listOf(false, false, false, true), results.map(TacticalActionResult.Attack::hit))
        assertEquals(99, battle.units.getValue("defender").hitPoints)
        // Source countRate wraps the winner and retains the loser's 75.
        assertEquals(0, battle.units.getValue("attacker").rateAccumulators[0])
        assertEquals(75, battle.units.getValue("defender").rateAccumulators[1])
    }

    @Test
    fun `physical remote immunity overrides result only after source hit gauge updates`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "궁수", Faction.PLAYER, 0, 0, remoteAttack = true, critical = 1, skills = mapOf(226 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, critical = 100, skills = mapOf(48 to 0)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 1) as TacticalActionResult.Attack

        assertEquals(false, result.hit)
        // countAtkHarm calls countRate before FYYJGJ forces the miss.
        assertEquals(25, battle.units.getValue("attacker").rateAccumulators[0])
        assertEquals(0, battle.units.getValue("defender").rateAccumulators[1])
    }

    @Test
    fun `BattleScreen truncUnitData seeds all eight source rate gauges for initial and added units`() {
        val sourceRandom = object : Random() {
            var next = 0
            override fun nextInt(bound: Int): Int = next++.mod(bound)
        }
        val initial = BattleUnit("initial", "초기", Faction.PLAYER, 0, 0)
        val battle = Battle(listOf(initial), emptyList(), random = sourceRandom)

        battle.initializeAllRateGauges()
        assertEquals((0..7).associateWith { it }, initial.rateAccumulators)

        val added = BattleUnit("added", "증원", Faction.PLAYER, 1, 0)
        battle.addUnit(added)
        assertEquals((0..7).associateWith { 8 + it }, added.rateAccumulators)
    }

    @Test
    fun `status duration comes from injected GAME_CFG except source enemy HL MB overrides`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 144 to 100, 127 to 100)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
            statusRoundFor = { if (it == BattleStatus.SILENCE) 1 else 3 },
            random = FixedRandom(0),
        )

        battle.combat.attack("attacker", "defender", damage = 30)

        val statuses = battle.units.getValue("defender").statuses
        assertEquals(2, statuses[BattleStatus.PARALYSIS]) // non-mine MB special case
        assertEquals(1, statuses[BattleStatus.SILENCE]) // GAME_CFG.status[JZ].round
    }

    @Test
    fun `SJZTGJ rolls only statuses absent from ordinary outgoing attack states`() {
        val battle = Battle(
            units = listOf(
                // MBGJ first supplies paralysis; SJZTGJ must not roll MB
                // again, then its JZ roll (71) supplies silence.
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 144 to 100, 204 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
            // getAtkStatus MB roll, then SJZTGJ JZ/HL/ZD. Hit uses countRate.
            random = FixedRandom(0, 71, 0, 71),
        )

        battle.combat.attack("attacker", "defender", damage = 30)

        val states = battle.units.getValue("defender").statuses
        assertEquals(2, states[BattleStatus.PARALYSIS])
        assertEquals(3, states[BattleStatus.SILENCE])
        assertEquals(null, states[BattleStatus.CONFUSION])
        assertEquals(3, states[BattleStatus.POISON]) // source's final 71 is > 70
    }

    @Test
    fun `SJSXGJ uses source rising strict thresholds and skips ordinary debuff slots`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 203 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
            // _attack2 builds SJSXGJ ATT..MOV; hit uses countRate.
            random = FixedRandom(61, 65, 71, 75, 81, 86),
        )

        battle.combat.attack("attacker", "defender", damage = 30)

        val lifts = battle.units.getValue("defender").attributeLifts
        assertEquals(-1, lifts[BattleAttribute.ATTACK])
        assertEquals(null, lifts[BattleAttribute.DEFENSE]) // 65 is not > 65
        assertEquals(-1, lifts[BattleAttribute.SPIRIT])
        assertEquals(null, lifts[BattleAttribute.CRITICAL]) // 75 is not > 75
        assertEquals(-1, lifts[BattleAttribute.MORALE])
        assertEquals(-1, lifts[BattleAttribute.MOVEMENT])
    }

    @Test
    fun `JYWX filters SJSXGJ states after its six source random draws`() {
        val random = CountingRandom()
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 203 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, skills = mapOf(122 to 0)),
            ),
            events = emptyList(), random = random,
        )

        battle.combat.attack("attacker", "defender", damage = 30)

        // The six SJSXGJ rolls are the only random operations; rate gauges
        // handle continuous and hit decisions.
        assertEquals(6, random.calls)
        assertEquals(emptyMap(), battle.units.getValue("defender").attributeLifts)
    }

    @Test
    fun `CLFJ performs configured strategy counter before physical counter without consuming defender action`() {
        val counterMagic = GameDataCatalog.MagicProfile(
            id = 99, name = "반격 책략", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(-1 to 0)),
            effectAreaId = 0, effectOffsets = emptySet(), expendMp = 0, power = 100, harmType = 1, category = 0,
        )
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, hitPoints = 100, maxHitPoints = 100, skills = mapOf(92 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, spirit = 100, skills = mapOf(13 to 99), magic = listOf(counterMagic)),
            ),
            events = emptyList(), random = FixedRandom(0),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals("반격 책략", result.counterMagic?.name)
        assertEquals(99, result.counterMagicId)
        assertEquals(0, result.counterDamage)
        assertEquals(
            emptyList(),
            result.physicalPasses.filter {
                it.kind == PhysicalAttackPassKind.COUNTER || it.kind == PhysicalAttackPassKind.COUNTER_FOLLOW_UP
            },
        )
        assertEquals(false, battle.units.getValue("defender").hasActed)
        assertEquals(true, battle.presentation.presentationUnit("attacker")!!.hitPoints < 100)
    }

    @Test
    fun `CLFJ remains queued after an active miss and retains its zero harm attack target`() {
        val counterMagic = GameDataCatalog.MagicProfile(
            id = 99, name = "반격 책략", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(-1 to 0)),
            effectAreaId = 0, effectOffsets = emptySet(), expendMp = 0, power = 100, harmType = 1, category = 0,
        )
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "attacker", "궁수", Faction.PLAYER, 0, 0,
                    remoteAttack = true,
                ),
                BattleUnit(
                    "defender", "방어", Faction.ENEMY, 1, 0,
                    spirit = 100,
                    skills = mapOf(13 to 99, 48 to 0),
                    magic = listOf(counterMagic),
                ),
            ),
            events = emptyList(),
            random = FixedRandom(0),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(false, result.hit)
        assertEquals("defender", result.physicalPasses.single().primaryTargetId)
        assertEquals(1, result.physicalPasses.single().targets.size)
        assertEquals(0, result.physicalPasses.single().targets.single().resolvedHarm)
        assertEquals(0, result.physicalPasses.single().targets.single().damage)
        assertEquals(99, result.counterMagicId)
        assertEquals("반격 책략", result.counterMagic?.name)
    }

    @Test
    fun `JQFY spends the defending camp money and changes a hit to exactly one HP`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, skills = mapOf(125 to 3)),
            ),
            events = emptyList(),
            initialEnemyMoney = 100,
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(1, result.damage)
        assertEquals(90, result.moneyShieldSpent)
        assertEquals(99, battle.units.getValue("defender").hitPoints)
        assertEquals(10, battle.enemyMoney)
    }

    @Test
    fun `JQFY makes a FRIEND defender spend shared player money`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0),
                BattleUnit("defender", "우군", Faction.FRIEND, 1, 0, skills = mapOf(125 to 3)),
                BattleUnit("attacker", "적", Faction.ENEMY, 0, 0, skills = mapOf(92 to 0, 226 to 0)),
            ),
            events = emptyList(), initialPlayerMoney = 100, initialEnemyMoney = 100,
        )
        battle.roundLifecycle.endTurn() // FRIEND
        battle.roundLifecycle.endTurn() // ENEMY

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(1, result.damage)
        assertEquals(10, battle.playerMoney)
        assertEquals(100, battle.enemyMoney)
    }

    @Test
    fun `XSJQ transfers final source harm between player and enemy money without clamping`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 237 to 2)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
            initialPlayerMoney = 10,
            initialEnemyMoney = 40,
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(60, result.playerMoneyDelta)
        assertEquals(-60, result.enemyMoneyDelta)
        assertEquals(70, battle.playerMoney)
        assertEquals(-20, battle.enemyMoney)
    }

    @Test
    fun `XSJQ from FRIEND transfers to shared player money`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0),
                BattleUnit("attacker", "우군", Faction.FRIEND, 0, 0, skills = mapOf(92 to 0, 226 to 0, 237 to 2)),
                BattleUnit("defender", "적", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(), initialPlayerMoney = 10, initialEnemyMoney = 40,
        )
        battle.roundLifecycle.endTurn() // FRIEND

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(60, result.playerMoneyDelta)
        assertEquals(-60, result.enemyMoneyDelta)
        assertEquals(70, battle.playerMoney)
        assertEquals(-20, battle.enemyMoney)
    }

    @Test
    fun `FTYCGJ removes only abnormal states created by this physical hit`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 144 to 100)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, skills = mapOf(42 to 0)),
            ),
            events = emptyList(),
        )

        battle.combat.attack("attacker", "defender", damage = 30)

        assertEquals(null, battle.units.getValue("defender").statuses[BattleStatus.PARALYSIS])
    }

    @Test
    fun `JYWX filters source physical DOWN attribute states but not the hit`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0, 226 to 0, 170 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, skills = mapOf(122 to 0)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(30, result.damage)
        assertEquals(null, battle.units.getValue("defender").attributeLifts[BattleAttribute.ATTACK])
    }

    @Test
    fun `MPFY caps source harm to MP and skips HP loss`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, magicPoints = 10, maxMagicPoints = 10, skills = mapOf(2 to 0)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(0, result.damage)
        assertEquals(10, result.mpShieldDamage)
        assertEquals(100, battle.units.getValue("defender").hitPoints)
        assertEquals(0, battle.units.getValue("defender").magicPoints)
    }

    @Test
    fun `MPFY break skips source QXL`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, hitPoints = 50, maxHitPoints = 100, skills = mapOf(92 to 0, 226 to 0, 298 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, magicPoints = 10, maxMagicPoints = 10, skills = mapOf(2 to 0)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(0, result.qxlHealing)
        assertEquals(50, battle.units.getValue("attacker").hitPoints)
    }

    @Test
    fun `FTSH inflicts source nonlethal recoil after the final physical harm`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, hitPoints = 5, maxHitPoints = 100, skills = mapOf(92 to 0, 226 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, skills = mapOf(40 to 100)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 30) as TacticalActionResult.Attack

        assertEquals(30, result.recoilDamage)
        assertEquals(1, battle.units.getValue("attacker").hitPoints)
    }

    @Test
    fun `MENG JI and NI FAN apply source block-only harm and abnormal states`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, hitPoints = 100, maxHitPoints = 100, skills = mapOf(92 to 0, 226 to 0)),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0, skills = mapOf(153 to 20, 161 to 30)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "defender", damage = 0) as TacticalActionResult.Attack
        val attacker = battle.presentation.presentationUnit("attacker")!!

        assertEquals(50, result.blockRetaliationDamage)
        assertEquals(50, attacker.hitPoints)
        assertEquals(3, attacker.statuses[BattleStatus.CONFUSION])
        assertEquals(3, attacker.statuses[BattleStatus.PARALYSIS])
    }

    @Test
    fun `continuous CTGJ resolves primary then splash inside every source pass`() {
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "attacker", "연속 범위 공격", Faction.PLAYER, 0, 0,
                    morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 226 to 0, 270 to 0),
                    attackEffectOffsets = setOf(0 to 1),
                ),
                BattleUnit(
                    "primary", "주 대상", Faction.ENEMY, 1, 0,
                    hitPoints = 1_000, maxHitPoints = 1_000, morale = 100,
                ),
                BattleUnit(
                    "splash", "범위 대상", Faction.ENEMY, 1, 1,
                    hitPoints = 1_000, maxHitPoints = 1_000,
                    magicPoints = 1_000, maxMagicPoints = 1_000,
                    skills = mapOf(2 to 0),
                ),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "primary") as TacticalActionResult.Attack

        assertEquals(
            listOf(PhysicalAttackPassKind.ACTIVE, PhysicalAttackPassKind.ACTIVE_FOLLOW_UP),
            result.physicalPasses.map(PhysicalAttackPass::kind),
        )
        assertEquals(
            listOf(listOf("primary", "splash"), listOf("primary", "splash")),
            result.physicalPasses.map { pass -> pass.targets.map(PhysicalAttackTargetResult::targetId) },
        )
        val splashResults = result.physicalPasses.flatMap(PhysicalAttackPass::targets)
            .filter { it.targetId == "splash" }
        assertEquals(2, splashResults.size)
        assertEquals(listOf(0, 0), splashResults.map(PhysicalAttackTargetResult::damage))
        assertEquals(true, splashResults.all { it.mpShieldDamage > 0 })
        assertEquals(2, result.splashTargets.size)
    }

    @Test
    fun `CTGJ target retains its own QXL FTSH and ZDSY callback data`() {
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "attacker", "범위 공격", Faction.PLAYER, 0, 0,
                    hitPoints = 10, maxHitPoints = 1_000,
                    skills = mapOf(92 to 0, 226 to 0, 298 to 0),
                    attackEffectOffsets = setOf(0 to 1),
                ),
                BattleUnit(
                    "primary", "MP 방어", Faction.ENEMY, 1, 0,
                    hitPoints = 1_000, maxHitPoints = 1_000,
                    magicPoints = 1_000, maxMagicPoints = 1_000,
                    skills = mapOf(2 to 0),
                ),
                BattleUnit(
                    "splash", "반사 자동회복", Faction.ENEMY, 1, 1,
                    hitPoints = 1_000, maxHitPoints = 1_000,
                    skills = mapOf(40 to 50, 284 to 200),
                ),
            ),
            events = emptyList(),
            propertyItems = mapOf(200 to BattlePropertyItem(200, "자동회복약", 26, 1_000)),
        )

        val result = battle.combat.attack("attacker", "primary") as TacticalActionResult.Attack
        val primary = result.physicalPasses.single().targets[0]
        val splash = result.physicalPasses.single().targets[1]

        assertEquals(true, primary.mpShieldDamage > 0)
        assertEquals(0, primary.qxlHealing)
        assertEquals(true, splash.qxlHealing > 0)
        assertEquals(true, splash.recoilDamage > 0)
        assertEquals(200, splash.automaticPropertyId)
        assertEquals("자동회복약", splash.automaticProperty?.name)

        val callbackTargets = result.toPhysicalCallbackInvocations().single().targets
        assertEquals(listOf("primary", "splash"), callbackTargets.map(BattlePhysicalCallbackPlan.Target::targetId))
        assertEquals(200, callbackTargets[1].automaticProperty?.itemId)
    }

    @Test
    fun `attack action selection covers every original critical and delay branch`() {
        assertEquals(25, BattleAttackSequence.selectAttackAction(critical = false, attackDelay = false))
        assertEquals(48, BattleAttackSequence.selectAttackAction(critical = false, attackDelay = true))
        assertEquals(21, BattleAttackSequence.selectAttackAction(critical = true, attackDelay = false))
        assertEquals(49, BattleAttackSequence.selectAttackAction(critical = true, attackDelay = true))
    }

}
