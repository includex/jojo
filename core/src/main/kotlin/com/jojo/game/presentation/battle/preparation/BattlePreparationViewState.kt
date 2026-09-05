package com.jojo.game.presentation.battle.preparation

import com.jojo.game.GameDataCatalog
import com.jojo.game.BattleAvatarResolver
import com.jojo.game.domain.battle.*

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

/** Maps game data into a detached, immutable rendering model. */
internal class BattlePreparationViewStateFactory(
    private val data: GameDataCatalog,
    private val unitAttribute: (unitId: Int, attribute: Int, default: Int) -> Int,
) {
    /**
     * 공개 메서드 `units`
     *
     * ### 파라미터
    - `ids` (`List<Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<BattlePreparationUnitView>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
