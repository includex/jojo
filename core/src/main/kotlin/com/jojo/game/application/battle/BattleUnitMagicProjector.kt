// Battle
package com.jojo.game.application.battle

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.battle.magic.BattleMagicProfileValue
import com.jojo.game.domain.campaign.CampaignState

/** BattleUnitMagicProjector: 직위·시나리오·스킬에서 얻은 마법을 전투 유닛용 마법 목록으로 투영한다. */
internal class BattleUnitMagicProjector(
    /**
     * `catalog` (GameDataCatalog?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val catalog: GameDataCatalog?,
    /**
     * `campaign` (CampaignState?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val campaign: CampaignState?,
    /**
     * `skills` (Map<Int, Int>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val skills: Map<Int, Int>,
) {
    /**
     * `project`: 필요한 객체나 결과를 생성한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun project(
        characterId: Int,
        battleProfile: GameDataCatalog.BattleProfile?,
    ): List<BattleMagicProfileValue> = learnedMagic(characterId, battleProfile)
        .map(::upgradeCastArea)
        .map(::upgradeEffectArea)
        .map { it.toBattleMagicProfile() }

    /**
     * `learnedMagic`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun learnedMagic(
        characterId: Int,
        battleProfile: GameDataCatalog.BattleProfile?,
    ): List<GameDataCatalog.MagicProfile> = buildList {
        addAll(battleProfile?.magic.orEmpty())
        campaign?.extraMagic?.values
            ?.filter { it.unitId == characterId && it.learnLevel <= (battleProfile?.level ?: 1) }
            ?.mapNotNull { catalog?.magicProfile(it.magicId) }
            ?.forEach { addIfMissing(it) }
        /**
         * `xhcl` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val xhcl = skills.skillValue(244) ?: return@buildList
        catalog?.allMagicProfiles()?.filter { it.belongsTo(xhcl) }?.forEach { addIfMissing(it) }
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<GameDataCatalog.MagicProfile>.addIfMissing(magic: GameDataCatalog.MagicProfile) {
        if (none { it.id == magic.id }) add(magic)
    }

    /**
     * `upgradeCastArea`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun upgradeCastArea(magic: GameDataCatalog.MagicProfile): GameDataCatalog.MagicProfile {
        if (skills.skillValue(259) == null) return magic
        val upgraded = magic.hitArea.upgradeId.let { catalog?.hitAreaProfile(it) } ?: return magic
        return magic.copy(hitArea = upgraded)
    }

    /**
     * `upgradeEffectArea`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun upgradeEffectArea(magic: GameDataCatalog.MagicProfile): GameDataCatalog.MagicProfile {
        if (skills.skillValue(264) == null) return magic
        return catalog?.upgradedEffectArea(magic.effectAreaId)
            ?.let { (id, offsets) -> magic.copy(effectAreaId = id, effectOffsets = offsets) }
            ?: magic
    }

    /**
     * `GameDataCatalog`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun GameDataCatalog.MagicProfile.belongsTo(families: Int): Boolean =
        (families and 1 != 0 && type in 0..3) ||
            (families and 2 != 0 && type in 7..10) ||
            (families and 4 != 0 && (type in 7..10 || type in 15..18)) ||
            (families and 8 != 0 && type == 19) ||
            (families and 16 != 0 && (type in 11..14 || type == 27)) ||
            (families and 32 != 0 && type == 23) ||
            (families and 64 != 0 && type == 24) ||
            (families and 128 != 0 && type == 25)

    /**
     * `Map`: 입력을 규칙에 따라 계산·변환한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun Map<Int, Int>.skillValue(id: Int): Int? =
        get(id)?.and(255)?.takeIf { it != 255 }
}
