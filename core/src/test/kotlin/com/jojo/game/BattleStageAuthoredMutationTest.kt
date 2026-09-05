package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.campaign.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `BattleStageAuthoredMutationTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleStageAuthoredMutationTest {
    @Test
    fun `setMaxRound applies only ZJHH four turn flag and skips equal write`() {
        val stage = ScenarioStage()
        assertTrue(stage.setMaxRound(12))
        assertEquals(12, stage.battleMaxRounds)
        assertFalse(stage.setMaxRound(12))
        assertTrue(stage.setMaxRound(12, enabledFeatures = 8))
        assertEquals(16, stage.battleMaxRounds)
        assertFalse(stage.setMaxRound(12, enabledFeatures = 8))
        assertTrue(stage.setMaxRound(0))
        assertEquals(0, stage.battleMaxRounds)
    }

    @Test
    fun `addLv clamps then writes level derived abilities and cache refresh order`() {
        val data = GameDataCatalog.load()
        val profile = assertNotNull(data.unitProfile(474))
        val campaign = CampaignState().also {
            it.setUnitAttribute(474, 18, 3)
            it.setUnitAttribute(474, 17, profile.posts)
        }
        val before = data.unitLevelDerivedAttributes(474, profile.posts, 3, mine = false, campaign)
        before.forEach { (attribute, value) -> campaign.setUnitAttribute(474, attribute, value) }

        val change = assertNotNull(campaign.addUnitLevels(474, 1, data))
        val growth = data.unitLevelGrowth(474, profile.posts, campaign)
        assertEquals(3, change.oldLevel)
        assertEquals(4, change.newLevel)
        assertEquals(listOf("equipmentSkills", "unitSkills", "postsSkills", "magic"), change.cacheRefreshOrder)
        growth.forEach { (attribute, amount) ->
            assertEquals(before.getValue(attribute) + amount, campaign.unitAttribute(474, attribute))
        }

        campaign.setUnitAttribute(474, 18, data.unitLevelLimit())
        assertNull(campaign.addUnitLevels(474, 99, data))
        assertEquals(data.unitLevelLimit(), campaign.unitAttribute(474, 18))
    }

    @Test
    fun `mine SJCS path fully recomputes and clamps negative ADD attributes`() {
        val data = GameDataCatalog.load()
        val profile = assertNotNull(data.unitProfile(0))
        val campaign = CampaignState().also {
            it.setUnitAttribute(0, 16, 1)
            it.setUnitAttribute(0, 17, profile.posts)
            it.setUnitAttribute(0, 18, 2)
            it.setUnitAttribute(0, 2, -999)
            it.setUnitAttribute(0, 39, -50)
            it.globalVariables[4094] = 1
        }

        val change = assertNotNull(campaign.addUnitLevels(0, 1, data))
        val expected = data.unitLevelDerivedAttributes(0, profile.posts, 3, mine = true, campaign)
        assertEquals(expected, change.attributes)
        assertEquals(expected[2], campaign.unitAttribute(0, 2))
    }

    @Test
    fun `live battle unit consumes rebuilt level abilities skills and magic`() {
        val stale = BattleUnit(
            id = "enemy-1", name = "stale", faction = Faction.ENEMY, tileX = 2, tileY = 3,
            level = 2, attack = 1, defense = 2, spirit = 3, critical = 4, morale = 5,
            skills = mapOf(1 to 2), magic = emptyList(), movement = 1,
        ).also { it.hitPoints = 7; it.magicPoints = 6; it.hasActed = true }
        val refreshed = stale.copy(
            level = 3, maxHitPoints = 90, maxMagicPoints = 40,
            attack = 11, defense = 12, spirit = 13, critical = 14, morale = 15,
            skills = mapOf(9 to 8), magic = listOf(GameDataCatalog.load().allMagicProfiles().first()), movement = 4,
        )

        stale.refreshLevelDerivedState(refreshed)

        assertEquals(listOf(3, 90, 40, 11, 12, 13, 14, 15, 4),
            listOf(stale.level, stale.maxHitPoints, stale.maxMagicPoints, stale.attack, stale.defense,
                stale.spirit, stale.critical, stale.morale, stale.movement))
        assertEquals(mapOf(9 to 8), stale.skills)
        assertEquals(refreshed.magic, stale.magic)
        assertEquals(7, stale.hitPoints)
        assertEquals(6, stale.magicPoints)
        assertTrue(stale.hasActed)
        assertEquals(2, stale.tileX)
        assertEquals(3, stale.tileY)
    }
}
