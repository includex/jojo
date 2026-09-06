// Scenario
package com.jojo.game.domain.scenario

/** PlaybackState: 시나리오 실행이 대화·선택·지연·모달·완료 중 어느 입력 지점에 있는지 나타낸다. */
enum class PlaybackState { DIALOGUE, CHOICE, DELAY, MODAL, COMPLETE }

/**
 * `ScenarioUnitFaction` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

enum class ScenarioUnitFaction { FRIEND, ENEMY, MINE }

/**
 * `ScenarioBattleUnit` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioBattleUnit(
    val instanceId: Int,
    val characterId: Int,
    val faction: ScenarioUnitFaction,
    val x: Int,
    val y: Int,
    val authoredX: Boolean = true,
    val authoredY: Boolean = true,
    val direction: Int = 2,
    val level: Int = 0,
    val reinforcement: Boolean = false,
    var hidden: Boolean = false,
    var ai: Int = 0,
    var aiTargetId: Int = -1,
    var aiTargetX: Int = 0,
    var aiTargetY: Int = 0,
    var deathMessageEnabled: Boolean = faction == ScenarioUnitFaction.MINE,
    val battleSlot: Int = BattleSlotLayout.slotFor(faction, instanceId),
)

/**
 * `ScenarioBattleUnit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
 */

val ScenarioBattleUnit.battleId: String
    get() = BattleSlotLayout.battleId(faction, battleSlot)

/**
 * `ScenarioBattleUnit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
 */

val ScenarioBattleUnit.stageKey: String
    get() = BattleSlotLayout.stageKey(faction, battleSlot)

/** ScenarioMapObjectsCall: 스크립트가 지도 오브젝트를 생성·이동·숨김 처리할 때 전달하는 명령 값이다. */
data class ScenarioMapObjectsCall(
    val enabled: Boolean,
    val terrainId: Int,
    val objects: List<Object>,
) {
    /**
     * `Object` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Object(val objectId: Int, val x: Int, val y: Int)
}

/**
 * `ScriptedAttackAction` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScriptedAttackAction(val attackerId: Int, val targetId: Int, val flag: Int)
