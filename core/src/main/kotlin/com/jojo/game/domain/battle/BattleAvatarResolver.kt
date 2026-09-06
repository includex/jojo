// Battle
package com.jojo.game.domain.battle

import com.jojo.game.infrastructure.data.GameDataCatalog

/** 전투 캐릭터와 진영에 맞는 아바타 번호를 계산합니다. */
object BattleAvatarResolver {
    /** 캐릭터·직위·무기·진영 정보를 원본 아바타 선택 규칙으로 변환합니다. */
    fun resolve(
        data: GameDataCatalog,
        characterId: Int,
        posts: Int,
        armId: Int,
        faction: Faction,
    ): Int? {
        val profile = data.unitProfile(characterId) ?: return null
        var specialAvatar = profile.battleAvatar
        val armAvatarType = data.armProfile(armId)?.battleAvatarType ?: -1
        if (specialAvatar != 0 && profile.battleAvatarType != -1 && profile.battleAvatarType != armAvatarType) {
            specialAvatar = 0
        }
        if (specialAvatar != 0) {
            return if (specialAvatar <= 32) {
                3 * (79 + specialAvatar) + 1 + if (posts < 60) posts % 3 else 0
            } else specialAvatar - 32 + 336
        }
        val camp = when (faction) {
            Faction.PLAYER -> 0
            Faction.FRIEND -> 1
            Faction.ENEMY -> 2
            // 원본 규칙은 적 진영보다 큰 진영을 적 진영으로 취급합니다.
            Faction.REINFORCEMENTS -> 2
        }
        return posts * 3 + 1 + camp
    }
}
