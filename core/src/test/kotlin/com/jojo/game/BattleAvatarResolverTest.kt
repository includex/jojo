// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.BattleAvatarResolver

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/** BattleAvatarResolverTest: BattleAvatarResolver의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleAvatarResolverTest {
    @Test
    fun `every original unit rank and camp resolves a battle avatar id`() {
        val data = GameDataCatalog.load()
        data.allUnitIds().forEach { characterId ->
            val profile = requireNotNull(data.unitProfile(characterId))
            val postsCases = if (profile.posts < 60) {
                val base = profile.posts - profile.posts % 3
                base..base + 2
            } else profile.posts..profile.posts
            postsCases.forEach { posts ->
                val armId = if (posts < 60) posts / 3 else posts - 40
                Faction.entries.forEach { faction ->
                    assertNotNull(BattleAvatarResolver.resolve(data, characterId, posts, armId, faction))
                }
            }
        }
    }

    @Test
    fun `reinforcements use the enemy avatar slot like source camp clamp`() {
        val data = GameDataCatalog.load()
        val characterId = data.allUnitIds().first()
        val profile = requireNotNull(data.unitProfile(characterId))
        val posts = profile.posts
        val armId = if (posts < 60) posts / 3 else posts - 40

        assertEquals(
            BattleAvatarResolver.resolve(data, characterId, posts, armId, Faction.ENEMY),
            BattleAvatarResolver.resolve(data, characterId, posts, armId, Faction.REINFORCEMENTS),
        )
    }
}
