package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * class  `BattleEquipmentExperienceParityTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleEquipmentExperienceParityTest {
    private data class Award(
        val recipient: String,
        val opponent: String,
        val harm: Int,
        val kind: BattleEquipmentExperienceKind,
    )

    private fun awardSink(into: MutableList<Award>) = { recipient: BattleUnit, opponent: BattleUnit, harm: Int, kind: BattleEquipmentExperienceKind ->
        into += Award(recipient.id, opponent.id, harm, kind)
        emptyList<CampaignEquipmentExperienceResult>()
    }

    @Test
    fun `no-harm status magic awards caster weapon EXP but not target armor EXP`() {
        val awards = mutableListOf<Award>()
        val weaken = GameDataCatalog.MagicProfile(
            7, "정신 약화", 7, 0,
            GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 1, 0, 4, 6,
        )
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "caster", "책사", Faction.PLAYER, 0, 0,
                    magicPoints = 10, maxMagicPoints = 10, magic = listOf(weaken),
                ),
                BattleUnit("victim", "적", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
            onEquipmentExperienceAward = awardSink(awards),
        )

        assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "victim", weaken.id))

        assertEquals(
            listOf(Award("caster", "victim", 1, BattleEquipmentExperienceKind.WEAPON)),
            awards,
        )
    }

    @Test
    fun `physical miss and explicit zero harm both settle one point equipment boundaries`() {
        val missedAwards = mutableListOf<Award>()
        val miss = Battle(
            units = listOf(
                BattleUnit("a", "remote", Faction.PLAYER, 0, 0, remoteAttack = true, skills = mapOf(92 to 0)),
                BattleUnit("t", "immune", Faction.ENEMY, 1, 0, skills = mapOf(48 to 0), attackOffsets = emptySet()),
            ),
            events = emptyList(),
            onEquipmentExperienceAward = awardSink(missedAwards),
        )

        assertEquals(false, assertIs<TacticalActionResult.Attack>(miss.attack("a", "t")).hit)
        assertEquals(
            setOf(
                Award("a", "t", 1, BattleEquipmentExperienceKind.WEAPON),
                Award("t", "a", 1, BattleEquipmentExperienceKind.ARMOR),
            ),
            missedAwards.toSet(),
        )

        val guardedAwards = mutableListOf<Award>()
        val guard = Battle(
            units = listOf(
                BattleUnit("a", "attacker", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0)),
                BattleUnit("t", "guard", Faction.ENEMY, 1, 0, attackOffsets = emptySet()),
            ),
            events = emptyList(),
            onEquipmentExperienceAward = awardSink(guardedAwards),
        )

        assertIs<TacticalActionResult.Attack>(guard.attack("a", "t", damage = 0))
        assertEquals(
            setOf(
                Award("a", "t", 1, BattleEquipmentExperienceKind.WEAPON),
                Award("t", "a", 1, BattleEquipmentExperienceKind.ARMOR),
            ),
            guardedAwards.toSet(),
        )
    }

    @Test
    fun `physical splash max merges weapon EXP but keeps each armor recipient boundary`() {
        val awards = mutableListOf<Award>()
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "a", "attacker", Faction.PLAYER, 0, 0,
                    level = 2, attack = 100, morale = 1, critical = 1,
                    skills = mapOf(92 to 0, 226 to 0), attackEffectOffsets = setOf(0 to 1),
                ),
                BattleUnit("high", "high", Faction.ENEMY, 1, 0, level = 3, defense = 1, hitPoints = 500, maxHitPoints = 500),
                BattleUnit("low", "low", Faction.ENEMY, 1, 1, level = 1, defense = 1, hitPoints = 500, maxHitPoints = 500),
            ),
            events = emptyList(),
            onEquipmentExperienceAward = awardSink(awards),
        )

        assertIs<TacticalActionResult.Attack>(battle.attack("a", "high"))

        assertEquals(1, awards.count { it.recipient == "a" && it.kind == BattleEquipmentExperienceKind.WEAPON })
        val weapon = awards.single { it.recipient == "a" && it.kind == BattleEquipmentExperienceKind.WEAPON }
        assertEquals("high", weapon.opponent)
        assertEquals(3, weapon.harm)
        assertEquals(
            mapOf("high" to 3, "low" to 4),
            awards.filter { it.kind == BattleEquipmentExperienceKind.ARMOR }.associate { it.recipient to it.harm },
        )
        assertEquals(
            setOf("high", "low"),
            awards.filter { it.kind == BattleEquipmentExperienceKind.ARMOR }.map { it.recipient }.toSet(),
        )
    }

    @Test
    fun `physical civil attacker omits weapon EXP but its defender still receives armor EXP`() {
        val awards = mutableListOf<Award>()
        val battle = Battle(
            listOf(
                BattleUnit("civil", "문관", Faction.PLAYER, 0, 0, armType = 1, skills = mapOf(92 to 0)),
                BattleUnit("target", "적", Faction.ENEMY, 1, 0, attackOffsets = emptySet()),
            ),
            emptyList(),
            onEquipmentExperienceAward = awardSink(awards),
        )

        assertIs<TacticalActionResult.Attack>(battle.attack("civil", "target", damage = 1))
        assertEquals(listOf(Award("target", "civil", 4, BattleEquipmentExperienceKind.ARMOR)), awards)
    }

    @Test
    fun `offensive magic records equipment and does not turn a kill into a kill EXP reward`() {
        val fire = GameDataCatalog.MagicProfile(
            0, "화계", 0, 0,
            GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 0, 100, 0, 0,
        )
        val awards = mutableListOf<Award>()
        val caster = BattleUnit(
            "caster", "책사", Faction.PLAYER, 0, 0,
            level = 1, spirit = 100, morale = 1, magic = listOf(fire),
        )
        val victim = BattleUnit(
            "victim", "적", Faction.ENEMY, 1, 0,
            level = 1, spirit = 1, morale = 1, hitPoints = 1, maxHitPoints = 1,
        )
        val battle = Battle(
            listOf(caster, victim), emptyList(),
            onEquipmentExperienceAward = awardSink(awards),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "victim", fire.id))

        assertEquals(true, result.targets.single().defeated)
        assertEquals(9, caster.experience) // pre-damage count_exp: 8 + max(1, 0)
        assertEquals(
            setOf(BattleEquipmentExperienceKind.WEAPON, BattleEquipmentExperienceKind.ARMOR),
            awards.map { it.kind }.toSet(),
        )
        assertEquals(setOf(3, 4), awards.map { it.harm }.toSet())
    }

    @Test
    fun `offensive magic miss still records one weapon and armor EXP`() {
        val fire = GameDataCatalog.MagicProfile(
            0, "화계", 0, 0,
            GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 0, 100, 0, 0,
        )
        val awards = mutableListOf<Award>()
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magic = listOf(fire)),
                BattleUnit("victim", "적", Faction.ENEMY, 1, 0, skills = mapOf(17 to 0)), // CLMY
            ),
            emptyList(),
            onEquipmentExperienceAward = awardSink(awards),
        )

        assertEquals(false, assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "victim", fire.id)).targets.single().hit)
        assertEquals(
            setOf(
                Award("caster", "victim", 1, BattleEquipmentExperienceKind.WEAPON),
                Award("victim", "caster", 1, BattleEquipmentExperienceKind.ARMOR),
            ),
            awards.toSet(),
        )
    }
}
