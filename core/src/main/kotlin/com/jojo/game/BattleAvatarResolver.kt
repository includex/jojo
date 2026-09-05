package com.jojo.game

/** Exact static-data branch of Model.fAvatarGroup, shared by game and fixtures. */
object BattleAvatarResolver {
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
            // Model.fAvatarGroup clamps every camp above ENEMY to ENEMY.
            Faction.REINFORCEMENTS -> 2
        }
        return posts * 3 + 1 + camp
    }
}
