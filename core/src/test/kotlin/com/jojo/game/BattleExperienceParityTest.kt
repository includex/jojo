// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.application.battle.Battle

import com.jojo.game.application.battle.BattleScenarioFactory
import com.jojo.game.application.scenario.ScenarioStage

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** BattleExperienceParityTest: BattleExperienceParity의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleExperienceParityTest {
    @Test
    fun `level-up keeps initially equipped default item levels while refreshing effective abilities`() {
        val data = GameDataCatalog.load()
        val campaign = CampaignState().apply {
            setUnitAttribute(0, 16, 1)
            setUnitAttribute(0, 17, 0)
            setUnitAttribute(0, 18, 4)
            setUnitAttribute(0, 19, 96)
        }
        ScenarioStage(campaign).joinUnit(0)
        val mine = ScenarioBattleUnit(0, 0, ScenarioUnitFaction.MINE, 0, 0, level = 3)
        val enemy = ScenarioBattleUnit(0, 5, ScenarioUnitFaction.ENEMY, 1, 0, level = 8)
        val battle = BattleScenarioFactory.fromScriptedUnits(
            listOf(mine, enemy),
            gameDataCatalog = data,
            campaign = campaign,
            terrain = BattleTerrainGrid(2, 1, listOf(intArrayOf(0, 0))),
        )
        battle.units.getValue(mine.battleId).skills += mapOf(92 to 0, 226 to 0)

        assertIs<TacticalActionResult.Attack>(battle.combat.attack(mine.battleId, enemy.battleId, damage = 1))

        assertEquals(5, campaign.unitAttribute(0, 18))
        assertEquals(14, campaign.unitAttribute(0, 19))
        assertEquals(56, campaign.unitAttribute(0, 2))
        assertEquals(64, campaign.unitAttribute(0, 3))
        assertEquals(66, battle.units.getValue(mine.battleId).attack)
        assertEquals(74, battle.units.getValue(mine.battleId).defense)
        assertEquals(
            CampaignEquipment(2, 1, 2, 1, 1, weaponExperience = 3),
            campaign.inventory.equipment.getValue(0),
        )
    }

    @Test
    fun `no-harm status magic still settles caster EXP`() {
        val weaken = GameDataCatalog.MagicProfile(
            7, "정신 약화", 7, 0,
            GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 1, 0, 4, 6,
        )
        val caster = BattleUnit(
            "caster", "책사", Faction.PLAYER, 0, 0,
            magicPoints = 10, maxMagicPoints = 10, level = 3, magic = listOf(weaken),
        )
        val victim = BattleUnit("victim", "적", Faction.ENEMY, 1, 0, level = 1)
        val battle = Battle(listOf(caster, victim), emptyList())

        assertIs<TacticalActionResult.Magic>(battle.combat.castMagic(caster.id, victim.id, weaken.id))

        assertEquals(6, caster.experience)
    }

    @Test
    fun `physical miss still settles attacker EXP through zero harm attack3`() {
        val attacker = BattleUnit(
            "attacker", "궁수", Faction.PLAYER, 0, 0,
            level = 1, remoteAttack = true, skills = mapOf(92 to 0, 226 to 0),
        )
        val target = BattleUnit(
            "target", "적", Faction.ENEMY, 1, 0,
            level = 3, skills = mapOf(48 to 0), attackOffsets = emptySet(),
        )
        val battle = Battle(listOf(attacker, target), emptyList())

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack(attacker.id, target.id))

        assertEquals(false, result.hit)
        assertEquals(0, result.physicalPasses.single().targets.single().resolvedHarm)
        assertEquals(12, attacker.experience)
    }

    @Test
    fun `offensive magic settles target EXP even when its accumulated hit misses`() {
        val fire = GameDataCatalog.MagicProfile(
            0, "화계", 0, 0,
            GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 0, 100, 0, 0,
        )
        val caster = BattleUnit(
            "caster", "책사", Faction.PLAYER, 0, 0,
            level = 1, experience = 88, spirit = 1, morale = 1,
            magic = listOf(fire),
        )
        val victim = BattleUnit(
            "victim", "적", Faction.ENEMY, 1, 0,
            level = 3, spirit = 100, morale = 100,
            skills = mapOf(17 to 0), // CLMY: guaranteed magic miss after countRate.
        )
        val battle = Battle(listOf(caster, victim), emptyList())

        val result = assertIs<TacticalActionResult.Magic>(battle.combat.castMagic(caster.id, victim.id, fire.id))

        assertEquals(false, result.targets.single().hit)
        assertEquals(2, caster.level)
        assertEquals(0, caster.experience)
    }

    @Test
    fun `S22 unit 217 settles 12 48 12 12 12 12 then attacks with level two profile`() {
        val data = GameDataCatalog.load()
        val unit217 = ScenarioBattleUnit(
            instanceId = 0,
            characterId = 217,
            faction = ScenarioUnitFaction.FRIEND,
            x = 0,
            y = 0,
            level = 0,
        )
        val levelThreeTargets = (0..6).map { instance ->
            ScenarioBattleUnit(
                instanceId = instance,
                characterId = 5,
                faction = ScenarioUnitFaction.ENEMY,
                x = 1,
                y = 0,
                level = 2,
            )
        }
        val battle = BattleScenarioFactory.fromScriptedUnits(
            listOf(unit217) + levelThreeTargets,
            gameDataCatalog = data,
            terrain = BattleTerrainGrid(2, 1, listOf(intArrayOf(0, 0))),
        )
        val actor = battle.units.getValue(unit217.battleId)
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택 (GJJDMZ, GJMFJ)을 검증한다.
        actor.skills = actor.skills + mapOf(92 to 0, 226 to 0)
        battle.selectVerificationFaction(Faction.FRIEND)

        levelThreeTargets.take(4).forEachIndexed { index, scriptedTarget ->
            val target = battle.units.getValue(scriptedTarget.battleId)
            target.skills = target.skills + (92 to 0)
            if (index == 1) target.setHpcur(1)
            assertIs<TacticalActionResult.Attack>(battle.combat.attack(actor.id, target.id, damage = 1))
            actor.hasActed = false
        }

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(84, actor.experience)
        assertEquals(1, actor.level)
        battle.roundLifecycle.endTurn()
        assertEquals(Faction.ENEMY, battle.activeFaction)

        levelThreeTargets.slice(4..5).forEachIndexed { index, scriptedAttacker ->
            val attacker = battle.units.getValue(scriptedAttacker.battleId)
            attacker.skills = attacker.skills + (92 to 0)
            val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack(attacker.id, actor.id, damage = 1))
            assertEquals(true, result.counterDamage > 0)
            assertEquals(6, attacker.experience)
            assertEquals(if (index == 0) 96 else 8, actor.experience)
        }

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(2, actor.level)
        assertEquals(8, actor.experience)
        assertEquals(44, actor.attack)
        assertEquals(46 + 10, actor.defense)
        assertEquals(41, actor.spirit)
        assertEquals(34, actor.critical)
        assertEquals(34, actor.morale)
        assertEquals(128, actor.maxHitPoints)
        assertEquals(12, actor.maxMagicPoints)

        val finalAttacker = battle.units.getValue(levelThreeTargets[6].battleId)
        finalAttacker.skills = finalAttacker.skills + (92 to 0)
        finalAttacker.terrainImpacts = finalAttacker.terrainImpacts + (0 to 110)
        actor.skills = actor.skills + (92 to 0)
        actor.terrainImpacts = actor.terrainImpacts + (0 to 100)
        assertEquals(57, finalAttacker.defense)
        assertEquals(121, finalAttacker.maxHitPoints)
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택 (S22, ENEMY, PLAYER)을 검증한다.
        val aiActor = actor.copy(id = "ai-217", faction = Faction.ENEMY, tileX = 0, tileY = 0)
        val aiTarget = finalAttacker.copy(
            id = "ai-5", faction = Faction.PLAYER, tileX = 1, tileY = 0,
            attackOffsets = emptySet(),
        )
        val aiBattle = Battle(
            listOf(aiActor, aiTarget), emptyList(),
            terrain = BattleTerrainGrid(2, 1, listOf(intArrayOf(0, 0))),
        )
        assertEquals(10, aiBattle.ai.previewAttackValue(aiActor.id, aiTarget.id))

        // 테스트 근거: 연출 프레임과 콜백 처리 순서을 검증한다.
        val finalResult = assertIs<TacticalActionResult.Attack>(
            aiBattle.combat.forcedAttack(aiActor.id, aiTarget.id),
        )
        assertEquals(10, finalResult.damage)
    }

    @Test
    fun `Mine kill persists one capped EXP grant without duplicate defeat reward`() {
        val data = GameDataCatalog.load()
        val profile = requireNotNull(data.unitProfile(217))
        val campaign = CampaignState().also {
            it.setUnitAttribute(217, 16, 1)
            it.setUnitAttribute(217, 17, profile.posts)
            it.setUnitAttribute(217, 18, 1)
            it.setUnitAttribute(217, 19, 52)
        }
        val mine = ScenarioBattleUnit(0, 217, ScenarioUnitFaction.MINE, 0, 0, level = 0)
        val enemy = ScenarioBattleUnit(0, 5, ScenarioUnitFaction.ENEMY, 1, 0, level = 2)
        val battle = BattleScenarioFactory.fromScriptedUnits(
            listOf(mine, enemy), gameDataCatalog = data, campaign = campaign,
        )
        val attacker = battle.units.getValue(mine.battleId)
        val target = battle.units.getValue(enemy.battleId)
        attacker.skills = attacker.skills + mapOf(92 to 0, 226 to 0)
        target.setHpcur(1)

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack(attacker.id, target.id, damage = 1))

        assertEquals(true, result.defeated)
        assertEquals(2, attacker.level)
        assertEquals(0, attacker.experience)
        assertEquals(2, campaign.unitAttribute(217, 18))
        assertEquals(0, campaign.unitAttribute(217, 19))
        assertEquals(44, attacker.attack)
        assertEquals(34, attacker.critical)
    }
}
