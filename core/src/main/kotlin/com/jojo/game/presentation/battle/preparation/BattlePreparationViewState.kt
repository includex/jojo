// Battle
package com.jojo.game.presentation.battle.preparation

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.BattleAvatarResolver

/** BattlePreparationViewState: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
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

/** BattlePreparationUnitView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
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
    /** `data` (GameDataCatalog): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val data: GameDataCatalog,
    /** `unitAttribute` ((unitId: Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val unitAttribute: (unitId: Int, attribute: Int, default: Int) -> Int,
) {
    /** 선택한 유닛 ID 목록을 준비 화면 모델로 변환합니다. */
    fun units(ids: List<Int>): List<BattlePreparationUnitView> = ids.mapNotNull(::unit)

    /**
     * `unit`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
        /**
         * `ATTR_POSTS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ATTR_POSTS = 17
        /**
         * `ATTR_LEVEL` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ATTR_LEVEL = 18
        /**
         * `ATTR_EXP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ATTR_EXP = 19
    }
}
