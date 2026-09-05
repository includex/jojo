package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * class  `BattleAvatarResolverTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
