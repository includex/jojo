// Verification
package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.jojo.game.domain.battle.BattleAvatarResolver
import com.jojo.game.domain.battle.Faction
import com.jojo.game.infrastructure.data.GameDataCatalog

/** BattleAvatarResourceVerifier: 해석된 모든 전투 아바타가 패키지 스프라이트 계열과 일치하는지 검증한다. */
internal class BattleAvatarResourceVerifier(private val data: GameDataCatalog) {
    /** verify: 전투 아바타 리소스 검증 결과를 반환한다. */
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
