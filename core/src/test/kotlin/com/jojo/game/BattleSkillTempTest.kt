package com.jojo.game

import java.util.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BattleSkillTempTest {
    private class ValuesRandom(private vararg val values: Int) : Random() {
        private var index = 0
        override fun nextInt(bound: Int): Int = values[index++].mod(bound)
    }
    @Test
    fun `BattleScreen temporary values retain source reset policies`() {
        val temp = BattleSkillTemp { skill ->
            when (skill) {
                1 -> BattleSkillTemp.ResetType.NONE
                2 -> BattleSkillTemp.ResetType.NEXT_ROUND
                else -> BattleSkillTemp.ResetType.RESET
            }
        }
        temp.set("u", 1, 10, 4)
        temp.set("u", 2, 20, 4)
        temp.set("u", 3, 30, 4)
        temp.reset(previousRound = 4)
        assertEquals(10, temp.value("u", 1))
        assertEquals(20, temp.value("u", 2))
        assertEquals(0, temp.value("u", 3))
        temp.reset(previousRound = 5)
        assertEquals(10, temp.value("u", 1))
        assertEquals(0, temp.value("u", 2))
    }

    @Test
    fun `CHGJ adds prior charge on active attack then defender accumulates one`() {
        fun battle(charge: Int) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(26 to 5)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1, skills = mapOf(26 to 4)),
            ), events = emptyList(),
        ).also { it.setSkillTemp("a", 26, charge) }

        val ordinary = battle(0)
        val charged = battle(2)
        val ordinaryResult = assertIs<TacticalActionResult.Attack>(ordinary.attack("a", "t"))
        val chargedResult = assertIs<TacticalActionResult.Attack>(charged.attack("a", "t"))
        assertEquals(10, chargedResult.damage - ordinaryResult.damage)
        assertEquals(1, charged.skillTemp("t", 26))
    }

    @Test
    fun `XU_SHI contributes once and clears its source temporary value`() {
        fun battle(stored: Int) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(243 to 7)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1),
            ), events = emptyList(),
        ).also { it.setSkillTemp("a", 243, stored) }

        val ordinary = battle(0)
        val charged = battle(3)
        val ordinaryResult = assertIs<TacticalActionResult.Attack>(ordinary.attack("a", "t"))
        val chargedResult = assertIs<TacticalActionResult.Attack>(charged.attack("a", "t"))
        assertEquals(21, chargedResult.damage - ordinaryResult.damage)
        assertEquals(0, charged.skillTemp("a", 243))
    }

    @Test
    fun `CHGJ also accumulates on a CTGJ splash defender`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, attackEffectAreaId = 4),
                BattleUnit("t", "주 대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1),
                BattleUnit("s", "범위 대상", Faction.ENEMY, 2, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1, skills = mapOf(26 to 3)),
            ), events = emptyList(),
        )
        assertIs<TacticalActionResult.Attack>(battle.attack("a", "t"))
        assertEquals(1, battle.skillTemp("s", 26))
    }

    @Test
    fun `CFGJ uses BattleScreen unitMove path node count minus one`() {
        fun battle(attackerX: Int, targetX: Int) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, attackerX, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(25 to 7)),
                BattleUnit("t", "대상", Faction.ENEMY, targetX, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1),
            ), events = emptyList(),
        )
        val unmoved = battle(0, 1)
        val moved = battle(0, 2)
        assertIs<TacticalActionResult.Success>(moved.moveUnit("a", 1, 0))
        val normal = assertIs<TacticalActionResult.Attack>(unmoved.attack("a", "t"))
        val charged = assertIs<TacticalActionResult.Attack>(moved.attack("a", "t"))
        assertEquals(7, charged.damage - normal.damage)
    }

    @Test
    fun `count_attackHarm direct line and fixed skill additions match source arithmetic`() {
        fun battle(attackerSkills: Map<Int, Int>, targetSkills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, movement = 6, critical = 1, morale = 1, skills = attackerSkills),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, hitPoints = 400, maxHitPoints = 500, defense = 20, critical = 1, morale = 1, direction = 1, skills = targetSkills),
            ), events = emptyList(),
        )
        val plain = battle(emptyMap(), emptyMap())
        val modified = battle(mapOf(234 to 5, 184 to 0, 114 to 9), mapOf(6 to 4, 118 to 3, 245 to 0, 247 to 2, 275 to 8))
        val base = assertIs<TacticalActionResult.Attack>(plain.attack("a", "t"))
        val result = assertIs<TacticalActionResult.Attack>(modified.attack("a", "t"))
        // These are source rate points: +5(WU_BIAN) +55(QJTJ) +9(JQGJ)
        // -4(BA_HAI) -3(JQWLSH) -20(XZDD) +6(XLGJ) -2(ZHONG_ZHUANG).
        assertEquals(25, result.damage - base.damage)
    }

    @Test
    fun `count_attackHarm diagonal reductions follow JSXXSH and JUAN_WU`() {
        fun battle(skills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, attackOffsets = setOf(1 to 1)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 1, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1, skills = skills),
            ), events = emptyList(),
        )
        val plain = battle(emptyMap())
        val guarded = battle(mapOf(121 to 7, 132 to 11))
        val base = assertIs<TacticalActionResult.Attack>(plain.attack("a", "t"))
        val result = assertIs<TacticalActionResult.Attack>(guarded.attack("a", "t"))
        assertEquals(-11, result.damage - base.damage)
    }

    @Test
    fun `MRSP2 applies its inclusive random percentage before fixed additions`() {
        fun battle(skills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = skills),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1, attackOffsets = emptySet()),
            // countRate consumes no Model.random; MRSP2 reads the first
            // value directly through random(0, 5).
            ), events = emptyList(), random = ValuesRandom(2),
        )
        val plain = battle(emptyMap())
        val boosted = battle(mapOf(292 to 0))
        val base = assertIs<TacticalActionResult.Attack>(plain.attack("a", "t"))
        val result = assertIs<TacticalActionResult.Attack>(boosted.attack("a", "t"))
        // Base 56; source roll 10 + 2 makes it floor(56 * 112 / 100) = 62.
        assertEquals(6, result.damage - base.damage)
    }

    @Test
    fun `MRSP2 applies its flag-random percentage to strategy damage`() {
        val fire = GameDataCatalog.MagicProfile(
            0, "화계", 0, 0,
            GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 0, 100, 0, 0,
        )
        fun battle(skills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit(
                    "a", "책사", Faction.PLAYER, 0, 0,
                    spirit = 100, critical = 1, morale = 1, magic = listOf(fire), skills = skills,
                ),
                BattleUnit(
                    "t", "대상", Faction.ENEMY, 1, 0,
                    spirit = 1, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500,
                ),
            ),
            events = emptyList(),
            random = ValuesRandom(2),
        )
        val base = assertIs<TacticalActionResult.Magic>(battle(emptyMap()).castMagic("a", "t", 0))
        val boosted = assertIs<TacticalActionResult.Magic>(battle(mapOf(292 to 0)).castMagic("a", "t", 0))

        // Base 59; source roll 10 + 2 makes floor(59 * 112 / 100) = 66.
        assertEquals(7, boosted.targets.single().damage - base.targets.single().damage)
    }

    @Test
    fun `JDGJ counts every existing BU_BING unit without a camp filter`() {
        fun battle(skill: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = skill),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1, attackOffsets = emptySet()),
                BattleUnit("near", "인접", Faction.PLAYER, 0, 1, hitPoints = 500, maxHitPoints = 500),
            ), events = emptyList(),
        )
        val plain = battle(emptyMap())
        val linked = battle(mapOf(109 to 4))
        val base = assertIs<TacticalActionResult.Attack>(plain.attack("a", "t"))
        val result = assertIs<TacticalActionResult.Attack>(linked.attack("a", "t"))
        assertEquals(8, result.damage - base.damage)
    }

    @Test
    fun `JFGJ and JFGJ2 use target mov_final including weather and paralysis`() {
        fun battle(weather: BattleWeather, targetSkills: Map<Int, Int>, paralyzed: Boolean) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(110 to 3, 312 to 10)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, movement = 6, critical = 1, morale = 1, attackOffsets = emptySet(), skills = targetSkills,
                    statuses = if (paralyzed) linkedMapOf(BattleStatus.PARALYSIS to 2) else linkedMapOf()),
            ), events = emptyList(), initialWeather = weather,
        )
        val windy = battle(BattleWeather.WINDY, emptyMap(), paralyzed = false)
        val rainBypass = battle(BattleWeather.HEAVY_RAIN, mapOf(268 to 0), paralyzed = false)
        val paralyzed = battle(BattleWeather.WINDY, emptyMap(), paralyzed = true)
        val windyDamage = assertIs<TacticalActionResult.Attack>(windy.attack("a", "t")).damage
        val bypassDamage = assertIs<TacticalActionResult.Attack>(rainBypass.attack("a", "t")).damage
        val paralysisDamage = assertIs<TacticalActionResult.Attack>(paralyzed.attack("a", "t")).damage
        // Rate is applied to base damage before fixed additions and truncates.
        assertEquals(5, windyDamage - bypassDamage)
        assertEquals(14, paralysisDamage - windyDamage)
    }

    @Test
    fun `HMGJ applies original back side and front direction rates`() {
        fun damage(attackerDirection: Int, targetX: Int, targetY: Int, skill: Boolean): Int {
            val battle = Battle(
                units = listOf(
                    BattleUnit("a", "공격", Faction.PLAYER, 1, 1, attack = 80, critical = 1, morale = 1, direction = attackerDirection,
                        attackOffsets = setOf(targetX - 1 to targetY - 1), skills = if (skill) mapOf(104 to 9) else emptyMap()),
                    BattleUnit("t", "대상", Faction.ENEMY, targetX, targetY, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1, attackOffsets = emptySet()),
                ), events = emptyList(),
            )
            return assertIs<TacticalActionResult.Attack>(battle.attack("a", "t")).damage
        }
        // Incoming down(2): attacker faces down -> 1/3; faces right -> 1/2; faces up -> full.
        assertEquals(1, damage(2, 1, 2, true) - damage(2, 1, 2, false))
        assertEquals(2, damage(1, 1, 2, true) - damage(1, 1, 2, false))
        assertEquals(5, damage(0, 1, 2, true) - damage(0, 1, 2, false))
    }

    @Test
    fun `TJGJ is suppressed when countAtkHarm has a valid CTGJ target`() {
        fun battle(withAreaTarget: Boolean) = Battle(
            units = buildList {
                add(BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(126 to 13), attackEffectAreaId = 4))
                add(BattleUnit("t", "주 대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, critical = 1, morale = 1, attackOffsets = emptySet()))
                if (withAreaTarget) add(BattleUnit("s", "범위 대상", Faction.ENEMY, 2, 0, hitPoints = 500, maxHitPoints = 500, defense = 20, attackOffsets = emptySet()))
            }, events = emptyList(),
        )
        val noCt = assertIs<TacticalActionResult.Attack>(battle(false).attack("a", "t"))
        val withCt = assertIs<TacticalActionResult.Attack>(battle(true).attack("a", "t"))
        assertEquals(7, noCt.damage - withCt.damage)
    }
}
