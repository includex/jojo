// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.application.scenario.ScenarioStage

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** UnitSetPostsContractTest: Unit과 BattleUnit의 setPosts가 유지해야 하는 원본 직위 변경 계약을 집중 검증한다. */
class UnitSetPostsContractTest {
    @Test
    fun `model setPosts preserves flags 2 8 and Mine phase refresh semantics`() {
        val data = GameDataCatalog.load()
        val profile = assertNotNull(data.unitProfile(0))
        val campaign = CampaignState().also {
            it.setUnitAttribute(0, 16, 1) // Unit.isMine()
            it.setUnitAttribute(0, 17, profile.posts)
            it.setUnitAttribute(0, 18, 3)
            it.globalVariables[4094] = 1 // ZZCS/SJCS source gate
            it.setUnitAttribute(0, 2, -999)
        }

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        val same = assertNotNull(campaign.setUnitPosts(0, profile.posts, flags = 2, data = data))
        assertFalse(same.postsWritten)
        assertEquals(emptyList(), same.cacheRefreshOrder)
        assertEquals(
            data.unitLevelDerivedAttributes(0, profile.posts, 3, mine = true, campaign),
            same.derivedAttributes,
        )

        // 테스트 근거: 전투 계산·난수 소비·경계값 (POSTS)을 검증한다.
        val skipped = assertNotNull(campaign.setUnitPosts(0, profile.posts + 1, flags = 8, data = data))
        assertTrue(skipped.postsWritten)
        assertEquals(listOf("postsSkills", "magic"), skipped.cacheRefreshOrder)
        assertEquals(emptyMap(), skipped.derivedAttributes)
        assertEquals(profile.posts + 1, campaign.unitAttribute(0, 17))
    }

    @Test
    fun `stage BattleUnit defaults 19 and only changed avatar pauses callback`() {
        val data = GameDataCatalog.load()
        val profile = assertNotNull(data.unitProfile(0))
        val campaign = CampaignState().also { it.setUnitAttribute(0, 17, profile.posts) }
        val stage = ScenarioStage(campaign).also {
            it.battleUnits["mine-0"] = ScenarioBattleUnit(0, 0, ScenarioUnitFaction.MINE, 4, 5)
        }
        val promoted = profile.posts + 1

        val battleChange = assertNotNull(stage.setBattleUnitPosts(0, promoted, data = data))
        assertEquals(19, battleChange.flags)
        assertTrue(stage.lastBattleUnitPostsRequiresPause)
        val reload = assertNotNull(stage.consumeUnitPostsRequest())
        assertTrue(reload.pausesScript)
        assertEquals(0, reload.unitId)
        assertTrue(reload.oldAvatarId != reload.newAvatarId)

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (POSTS)을 검증한다.
        stage.setBattleUnitPosts(0, promoted, data = data)
        assertFalse(stage.lastBattleUnitPostsRequiresPause)
        assertNull(stage.consumeUnitPostsRequest())

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        val modelChange = assertNotNull(stage.setModelUnitPosts(0, profile.posts, data = data))
        assertEquals(3, modelChange.flags)
        assertNull(stage.consumeUnitPostsRequest())
    }

    @Test
    fun `live projection refreshes post arm abilities skills and magic without tactical reset`() {
        val magic = GameDataCatalog.load().allMagicProfiles().first()
        val live = BattleUnit(
            id = "mine-0", name = "before", faction = Faction.PLAYER, tileX = 3, tileY = 4,
            posts = 0, armId = 0, attack = 1, defense = 2, spirit = 3, critical = 4, morale = 5,
            skills = mapOf(1 to 1), magic = emptyList(), movement = 2,
        ).also { it.hitPoints = 7; it.magicPoints = 6; it.hasActed = true }
        val refreshed = live.copy(
            posts = 1, armId = 7, armType = 2, remoteAttack = true,
            attack = 11, defense = 12, spirit = 13, critical = 14, morale = 15,
            maxHitPoints = 90, maxMagicPoints = 40, movement = 6,
            skills = mapOf(9 to 8), magic = listOf(magic), attackOffsets = setOf(2 to 0),
        )

        live.refreshPostsDerivedState(refreshed, refreshAbilityPhase = true)

        assertEquals(listOf(1, 7, 11, 12, 13, 14, 15, 6),
            listOf(live.posts, live.armId, live.attack, live.defense, live.spirit, live.critical, live.morale, live.movement))
        assertEquals(mapOf(9 to 8), live.skills)
        assertEquals(listOf(magic), live.magic)
        assertEquals(setOf(2 to 0), live.attackOffsets)
        assertEquals(listOf(3, 4, 7, 6, true), listOf(live.tileX, live.tileY, live.hitPoints, live.magicPoints, live.hasActed))
    }

    @Test
    fun `non Mine setPosts keeps stored ability phase while refreshing post skill magic and movement`() {
        val magic = GameDataCatalog.load().allMagicProfiles().first()
        val live = BattleUnit(
            id = "enemy-0", name = "enemy", faction = Faction.ENEMY, tileX = 1, tileY = 2,
            posts = 0, attack = 41, defense = 42, spirit = 43, critical = 44, morale = 45,
            movement = 3, skills = mapOf(1 to 1), magic = emptyList(),
        )
        val promoted = live.copy(
            posts = 1, attack = 91, defense = 92, spirit = 93, critical = 94, morale = 95,
            movement = 6, skills = mapOf(2 to 2), magic = listOf(magic),
        )

        live.refreshPostsDerivedState(promoted, refreshAbilityPhase = false)

        assertEquals(listOf(1, 41, 42, 43, 44, 45, 6),
            listOf(live.posts, live.attack, live.defense, live.spirit, live.critical, live.morale, live.movement))
        assertEquals(mapOf(2 to 2), live.skills)
        assertEquals(listOf(magic), live.magic)
    }
}
