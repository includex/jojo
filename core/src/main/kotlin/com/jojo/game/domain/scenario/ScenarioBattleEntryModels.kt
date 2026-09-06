// Scenario
package com.jojo.game.domain.scenario

data class ScenarioJoinBattleLimit(
    val minimum: Int,
    val maximum: Int,
    val requiredUnitIds: List<Int>,
    val excludedUnitIds: List<Int>,
)

data class ScenarioBattleEntryPlan(
    val selectionLimit: ScenarioJoinBattleLimit,
    val directBattleRoster: List<Int>?,
)

data class ScenarioJoinEquipment(
    val unitId: Int,
    val weapon: Int,
    val weaponLevel: Int,
    val armor: Int,
    val armorLevel: Int,
    val auxiliary: Int,
)

/** ScenarioRewardRequest: 전투 종료 후 캠페인에 적용할 금전·아이템·경험치 보상 요청을 나타낸다. */
data class ScenarioRewardRequest(
    val bonusMoney: Int = 0,
    val items: List<Int> = emptyList(),
    val end: Boolean = false,
)

data class ScenarioUnitHideRequest(
    val unitId: Int,
    val hideType: Int,
    val battleUnitId: String? = null,
    val resumesScript: Boolean = true,
    val showsRetireMessage: Boolean = hideType == 1,
)

data class ScenarioUnitShowRequest(
    val unitId: Int,
    val x: Int = -1,
    val y: Int = -1,
    val direction: Int = -1,
    val flags: Int = 0,
)

data class ScenarioUnitPostsRequest(
    val unitId: Int,
    val oldAvatarId: Int,
    val newAvatarId: Int,
    val pausesScript: Boolean,
)
