// Test
package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.magic.MagicEnvironment
import com.jojo.game.domain.battle.magic.MagicResolver
import com.jojo.game.domain.battle.magic.BattleMagicHitAreaValue
import com.jojo.game.domain.battle.magic.BattleMagicProfile
import com.jojo.game.domain.battle.magic.BattleMagicProfileValue
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** MagicResolverTest: MagicResolver의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class MagicResolverTest {

    private fun unit(
        id: String,
        faction: Faction = Faction.PLAYER,
        attack: Int = 100,
        defense: Int = 50,
        spirit: Int = 100,
        level: Int = 10,
        hitPoints: Int = 100,
        maxHitPoints: Int = 100,
        magicPoints: Int = 50,
        maxMagicPoints: Int = 50,
        tileX: Int = 2,
        tileY: Int = 2,
        magic: List<BattleMagicProfile> = emptyList(),
        statuses: Map<BattleStatus, Int> = emptyMap(),
    ) = BattleUnit(
        id = id,
        name = id,
        faction = faction,
        tileX = tileX,
        tileY = tileY,
        attack = attack,
        defense = defense,
        spirit = spirit,
        level = level,
        hitPoints = hitPoints,
        maxHitPoints = maxHitPoints,
        magicPoints = magicPoints,
        maxMagicPoints = maxMagicPoints,
        magic = magic,
        statuses = statuses.toMutableMap(),
    )

    private fun magic(
        id: Int = 1,
        name: String = "Fireball",
        type: Int = 0,
        category: Int = 0,
        target: Int = 0, // 0 = enemy, 1 = ally, 2 = weather/self, 3 = any
        expendMp: Int = 10,
        power: Int = 100,
        offsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, 0 to -1, -1 to 0),
    ) = BattleMagicProfileValue(
        id = id,
        name = name,
        type = type,
        category = category,
        target = target,
        expendMp = expendMp,
        power = power,
        harmType = 0,
        effectAreaId = 0,
        hitArea = BattleMagicHitAreaValue(0, offsets, false),
        effectOffsets = emptySet(),
    )

    private fun createEnv(
        units: List<BattleUnit>,
        weatherRef: Array<BattleWeather> = arrayOf(BattleWeather.CLEAR),
        defeatedList: MutableList<String> = mutableListOf(),
    ): MagicEnvironment {
        val prob = BattleProbabilityResolver(java.util.Random(0), null)
        return MagicEnvironment(
            probabilityResolver = prob,
            units = { units },
            pendingPresentationUnits = { emptyList() },
            unitAt = { x, y -> units.firstOrNull { it.tileX == x && it.tileY == y } },
            areAllied = { a, b -> a.faction == b.faction },
            weather = { weatherRef[0] },
            setWeather = { weatherRef[0] = it },
            terrain = null,
            terrainMagicFlags = emptyMap(),
            activeFaction = { Faction.PLAYER },
            isBattleEnded = { false },
            statusDuration = { _, _ -> 3 },
            resolveCriticalSpeech = { _, _ -> null },
            battleExperience = { _, _, _ -> 10 },
            equipmentExperienceAmount = { _, _, _, _ -> 5 },
            notifyBattleExperience = { _, _ -> },
            notifyEquipmentExperienceAward = { _, _, _, _ -> },
            notifyUnitDefeated = { _, _ -> },
            onDefeat = { defeatedList.add(it) },
        )
    }

    @Test
    fun `castMagic rejects when attacker is silenced or has insufficient MP`() {
        val fireSpell = magic(id = 1, expendMp = 20)
        val attacker = unit("hero", magicPoints = 10, magic = listOf(fireSpell))
        val enemy = unit("enemy", faction = Faction.ENEMY, tileX = 2, tileY = 3)
        val env = createEnv(listOf(attacker, enemy))

        val resultMp = MagicResolver.castMagic("hero", "enemy", 1, env = env)
        assertIs<TacticalActionResult.Rejected>(resultMp)
        assertEquals("MP가 부족합니다.", resultMp.reason)

        attacker.addMpcur(30)
        attacker.statuses[BattleStatus.SILENCE] = 2
        val resultSilence = MagicResolver.castMagic("hero", "enemy", 1, env = env)
        assertIs<TacticalActionResult.Rejected>(resultSilence)
        assertEquals("현재 상태에서는 전략을 사용할 수 없습니다.", resultSilence.reason)
    }

    @Test
    fun `castMagic weather spell alters environment weather`() {
        val rainSpell = magic(id = 58, name = "Rain", target = 2, expendMp = 10)
        val caster = unit("shaman", magic = listOf(rainSpell))
        val weatherRef = arrayOf(BattleWeather.CLEAR)
        val env = createEnv(listOf(caster), weatherRef = weatherRef)

        val result = MagicResolver.castMagic("shaman", "shaman", 58, env = env)
        assertIs<TacticalActionResult.Magic>(result)
        assertEquals(BattleWeather.HEAVY_RAIN, weatherRef[0])
        assertEquals(40, caster.magicPoints)
    }

    @Test
    fun `castMagic heals ally and updates HP`() {
        val healSpell = magic(id = 19, name = "Heal", type = 19, category = 13, target = 1, expendMp = 10, power = 50)
        val caster = unit("priest", magic = listOf(healSpell))
        val injuredAlly = unit("ally", hitPoints = 40, maxHitPoints = 100, tileX = 2, tileY = 3)
        val env = createEnv(listOf(caster, injuredAlly))

        val result = MagicResolver.castMagic("priest", "ally", 19, env = env)
        assertIs<TacticalActionResult.Magic>(result)
        val targetResult = result.targets.first()
        assertTrue(targetResult.healing > 0)
        assertTrue(injuredAlly.hitPoints > 40)
    }

    @Test
    fun `castMagic offensive magic defeats lethal target`() {
        val fireSpell = magic(id = 1, name = "Fire", type = 0, power = 100)
        val caster = unit("mage", magic = listOf(fireSpell))
        val weakEnemy = unit("enemy", faction = Faction.ENEMY, hitPoints = 1, tileX = 2, tileY = 3)
        val defeated = mutableListOf<String>()
        val env = createEnv(listOf(caster, weakEnemy), defeatedList = defeated)

        val result = MagicResolver.castMagic("mage", "enemy", 1, env = env)
        assertIs<TacticalActionResult.Magic>(result)
        val targetResult = result.targets.first()
        assertTrue(targetResult.hit)
        assertTrue(targetResult.defeated)
        assertEquals(listOf("enemy"), defeated)
    }

    @Test
    fun `castMagicAt teleports caster to target coordinate`() {
        val teleportSpell = BattleMagicProfileValue(
            id = 77,
            name = "Teleport",
            type = 37,
            category = 35,
            target = 0,
            expendMp = 20,
            power = 0,
            harmType = 4,
            effectAreaId = 0,
            hitArea = BattleMagicHitAreaValue(0, setOf(3 to 3), false),
            effectOffsets = emptySet(),
        )
        val caster = unit("mage", tileX = 0, tileY = 0, magic = listOf(teleportSpell))
        val env = createEnv(listOf(caster))

        val result = MagicResolver.castMagicAt("mage", 3, 3, 77, env = env)
        assertIs<TacticalActionResult.Magic>(result)
        assertEquals(3, caster.tileX)
        assertEquals(3, caster.tileY)
        assertEquals(30, caster.magicPoints)
        assertTrue(caster.hasActed)
    }
}
