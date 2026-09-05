package com.jojo.game.application.battle

import com.jojo.game.GameDataCatalog
import com.jojo.game.domain.battle.magic.BattleMagicProfileValue
import com.jojo.game.domain.campaign.CampaignState

/** Resolves post, campaign, and skill-granted spells for a projected unit. */
internal class BattleUnitMagicProjector(
    private val catalog: GameDataCatalog?,
    private val campaign: CampaignState?,
    private val skills: Map<Int, Int>,
) {
    fun project(
        characterId: Int,
        battleProfile: GameDataCatalog.BattleProfile?,
    ): List<BattleMagicProfileValue> = learnedMagic(characterId, battleProfile)
        .map(::upgradeCastArea)
        .map(::upgradeEffectArea)
        .map { it.toBattleMagicProfile() }

    private fun learnedMagic(
        characterId: Int,
        battleProfile: GameDataCatalog.BattleProfile?,
    ): List<GameDataCatalog.MagicProfile> = buildList {
        addAll(battleProfile?.magic.orEmpty())
        campaign?.extraMagic?.values
            ?.filter { it.unitId == characterId && it.learnLevel <= (battleProfile?.level ?: 1) }
            ?.mapNotNull { catalog?.magicProfile(it.magicId) }
            ?.forEach { addIfMissing(it) }
        val xhcl = skills.skillValue(244) ?: return@buildList
        catalog?.allMagicProfiles()?.filter { it.belongsTo(xhcl) }?.forEach { addIfMissing(it) }
    }

    private fun MutableList<GameDataCatalog.MagicProfile>.addIfMissing(magic: GameDataCatalog.MagicProfile) {
        if (none { it.id == magic.id }) add(magic)
    }

    private fun upgradeCastArea(magic: GameDataCatalog.MagicProfile): GameDataCatalog.MagicProfile {
        if (skills.skillValue(259) == null) return magic
        val upgraded = magic.hitArea.upgradeId.let { catalog?.hitAreaProfile(it) } ?: return magic
        return magic.copy(hitArea = upgraded)
    }

    private fun upgradeEffectArea(magic: GameDataCatalog.MagicProfile): GameDataCatalog.MagicProfile {
        if (skills.skillValue(264) == null) return magic
        return catalog?.upgradedEffectArea(magic.effectAreaId)
            ?.let { (id, offsets) -> magic.copy(effectAreaId = id, effectOffsets = offsets) }
            ?: magic
    }

    private fun GameDataCatalog.MagicProfile.belongsTo(families: Int): Boolean =
        (families and 1 != 0 && type in 0..3) ||
            (families and 2 != 0 && type in 7..10) ||
            (families and 4 != 0 && (type in 7..10 || type in 15..18)) ||
            (families and 8 != 0 && type == 19) ||
            (families and 16 != 0 && (type in 11..14 || type == 27)) ||
            (families and 32 != 0 && type == 23) ||
            (families and 64 != 0 && type == 24) ||
            (families and 128 != 0 && type == 25)

    private fun Map<Int, Int>.skillValue(id: Int): Int? =
        get(id)?.and(255)?.takeIf { it != 255 }
}
