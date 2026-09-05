package com.jojo.game

/**
 * data class  `Dialogue`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class Dialogue(val speakerId: String?, val text: String)

/** HallLayer.choice(text, face) supplies the portrait used by ChooseLayer. */
data class Choice(val options: List<String>, val faceId: Int? = null)

sealed interface ScenarioCommand {
    /**
     * data class  `LoadBackground`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class LoadBackground(val backgroundId: Int, val variant: Int) : ScenarioCommand

    /**
     * data class  `SetEventName`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class SetEventName(val name: String) : ScenarioCommand

    /**
     * data class  `ShowUnit`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ShowUnit(val unitId: Int, val x: Int, val y: Int, val direction: Int) : ScenarioCommand

    /**
     * data class  `MoveUnit`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class MoveUnit(val unitId: Int, val x: Int, val y: Int, val direction: Int) : ScenarioCommand

    /**
     * data class  `SetUnitAction`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class SetUnitAction(val unitId: Int, val action: Int) : ScenarioCommand

    /**
     * data class  `DialogueLine`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class DialogueLine(val dialogue: Dialogue) : ScenarioCommand

    /**
     * data class  `Choose`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Choose(val choice: Choice) : ScenarioCommand
}

/**
 * data class  `ScenarioTimeline`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioTimeline(val moduleName: String, val commands: List<ScenarioCommand>)

/**
 * data class  `ScriptedUnitAction`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScriptedUnitAction(
    val unitId: Int,
    val action: Int,
    /** -1 means retain current direction, matching BattleUnit.setAction. */
    val direction: Int = -1,
    val loop: Boolean = false,
    /** Non-stand, non-loop clips resume the source Script on FINISHED. */
    val awaitsFinishedCallback: Boolean = action > 0 && !loop,
)
