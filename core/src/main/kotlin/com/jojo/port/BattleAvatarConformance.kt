package com.jojo.port

import com.badlogic.gdx.Gdx

/** Exhaustive static Model.fAvatarGroup conformance over original UNIT rows. */
object BattleAvatarConformance {
    fun verify() {
        val data = OriginalGameData.load()
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
                    if (absentFamilies.isNotEmpty()) missing += "$characterId/$posts/$faction=>$avatar:${absentFamilies.joinToString()}"
                    checks++
                }
            }
        }
        check(missing.isEmpty()) { "원본 fAvatarGroup atlas 누락: ${missing.take(12)} (${missing.size})" }
        Gdx.app.log("JojoPort", "BATTLE_AVATAR_CONFORMANCE_OK: checks=$checks unitIds=${data.allUnitIds().size}")
    }
}
