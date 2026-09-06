package com.jojo.game.presentation.battle.preparation

import com.jojo.game.GameDataCatalog
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.BattleAvatarResolver

data class BattlePreparationViewState(
    val backgroundId: Int,
    val availableIds: List<Int>,
    val units: List<BattlePreparationUnitView>,
    val selectedIds: List<Int>,
    val requiredIds: List<Int>,
    val requiredSlotCount: Int,
    val minimum: Int,
    val maximum: Int,
    val cursorId: Int?,
    val canStart: Boolean,
    val detailsVisible: Boolean = false,
    val mapVisible: Boolean = false,
    val sortOpen: Boolean = false,
    val battleViewMarkerCount: Int = 0,
)

data class BattlePreparationUnitView(
    val id: Int,
    val name: String,
    val armName: String,
    val level: Int,
    val experience: Int,
    val maxHitPoints: Int,
    val maxMagicPoints: Int,
    val traits: List<Pair<String, Int>>,
    val avatarId: Int?,
    val headId: Int,
)

/** 게임 데이터를 분리된 불변 준비 화면 모델로 변환합니다. */
internal class BattlePreparationViewStateFactory(
    private val data: GameDataCatalog,
    private val unitAttribute: (unitId: Int, attribute: Int, default: Int) -> Int,
) {
    /** 선택한 유닛 ID 목록을 준비 화면 모델로 변환합니다. */
    fun units(ids: List<Int>): List<BattlePreparationUnitView> = ids.mapNotNull(::unit)

    private fun unit(id: Int): BattlePreparationUnitView? {
        val profile = data.unitProfile(id) ?: return null
        val posts = unitAttribute(id, ATTR_POSTS, profile.posts)
        val level = unitAttribute(id, ATTR_LEVEL, profile.level)
        val battle = data.battleProfile(id, level - 1, posts) ?: return null
        val equipment = data.defaultEquipmentBonus(posts, level)
        val rawFace = profile.face
        return BattlePreparationUnitView(
            id = id,
            name = if (profile.famous) profile.name.trim() else profile.name.trim().replace(Regex("\\s*\\d+$"), ""),
            armName = battle.arm.name,
            level = level,
            experience = unitAttribute(id, ATTR_EXP, 0),
            maxHitPoints = battle.maxHitPoints,
            maxMagicPoints = battle.maxMagicPoints,
            traits = listOf(
                "무력" to profile.attack * 2, "민첩성" to profile.critical * 2,
                "지력" to profile.spirit * 2, "운기" to profile.morale * 2,
                "지휘" to profile.defense * 2, "" to 0,
                "공격" to (battle.attack + (equipment?.attack ?: 0)),
                "방어" to (battle.defense + (equipment?.defense ?: 0)),
                "정신" to battle.spirit, "폭발" to battle.critical,
                "사기" to battle.morale, "이동" to battle.movement,
            ),
            avatarId = BattleAvatarResolver.resolve(
                data, id, posts, if (posts < 60) posts / 3 else posts - 40, Faction.PLAYER,
            ),
            headId = if (id == 0 && rawFace <= 3) rawFace + 1 else rawFace + 8,
        )
    }

    private companion object {
        const val ATTR_POSTS = 17
        const val ATTR_LEVEL = 18
        const val ATTR_EXP = 19
    }
}
