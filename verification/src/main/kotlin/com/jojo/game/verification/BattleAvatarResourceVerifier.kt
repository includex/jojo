package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.jojo.game.BattleAvatarResolver
import com.jojo.game.Faction
import com.jojo.game.GameDataCatalog

/** Verifies every resolved battle avatar against the packaged sprite families. */
internal class BattleAvatarResourceVerifier(private val data: GameDataCatalog) {
/**
 * 공개 메서드 `verify`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `String`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

    fun verify(): String {
        var checks = 0
        val missing = mutableListOf<String>()
        data.allUnitIds().forEach { characterId ->
            val profile = checkNotNull(data.unitProfile(characterId))
            val postsCases = if (profile.posts < 60) {
                val base = profile.posts - profile.posts % 3
                (base..base + 2).toList()
            } else listOf(profile.posts)
            postsCases.forEach { posts ->
                val armId = if (posts < 60) posts / 3 else posts - 40
                Faction.entries.forEach { faction ->
                    val avatar = checkNotNull(BattleAvatarResolver.resolve(data, characterId, posts, armId, faction))
                    val absentFamilies = listOf("mov", "atk", "spc").filter { family ->
                        sequenceOf("maps/units/${family}2/$avatar.png", "maps/units/$family/$avatar.png")
                            .none { Gdx.files.internal(it).exists() }
                    }
                    if (absentFamilies.isNotEmpty()) {
                        missing += "$characterId/$posts/$faction=>$avatar:${absentFamilies.joinToString()}"
                    }
                    checks++
                }
            }
        }
        check(missing.isEmpty()) { "fAvatarGroup atlas 누락: ${missing.take(12)} (${missing.size})" }
        return "BATTLE_AVATAR_CONFORMANCE_OK: checks=$checks unitIds=${data.allUnitIds().size}"
    }
}
