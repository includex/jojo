// Scenario
package com.jojo.game.domain.scenario

/**
 * `ScenarioJoinBattleLimit` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioJoinBattleLimit(
    val minimum: Int,
    val maximum: Int,
    val requiredUnitIds: List<Int>,
    val excludedUnitIds: List<Int>,
)

/**
 * `ScenarioBattleEntryPlan` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioBattleEntryPlan(
    val selectionLimit: ScenarioJoinBattleLimit,
    val directBattleRoster: List<Int>?,
)

/**
 * `ScenarioJoinEquipment` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

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

/**
 * `ScenarioUnitHideRequest` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioUnitHideRequest(
    val unitId: Int,
    val hideType: Int,
    val battleUnitId: String? = null,
    val resumesScript: Boolean = true,
    val showsRetireMessage: Boolean = hideType == 1,
)

/**
 * `ScenarioUnitShowRequest` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioUnitShowRequest(
    val unitId: Int,
    val x: Int = -1,
    val y: Int = -1,
    val direction: Int = -1,
    val flags: Int = 0,
)

/**
 * `ScenarioUnitPostsRequest` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioUnitPostsRequest(
    val unitId: Int,
    val oldAvatarId: Int,
    val newAvatarId: Int,
    val pausesScript: Boolean,
)
