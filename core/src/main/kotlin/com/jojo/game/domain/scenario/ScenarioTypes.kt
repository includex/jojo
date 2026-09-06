// Scenario
package com.jojo.game.domain.scenario

import com.jojo.game.domain.battle.*

/** 화자와 본문으로 구성된 대사이다. */
data class Dialogue(val speakerId: String?, val text: String)

/** 선택지와 선택 화면에 표시할 인물 초상을 묶는다. */
data class Choice(val options: List<String>, val faceId: Int? = null)

/** 시나리오 재생기가 처리하는 화면·유닛 명령이다. */
sealed interface ScenarioCommand {
    /** 배경과 변형 값을 설정한다. */
    data class LoadBackground(val backgroundId: Int, val variant: Int) : ScenarioCommand

    /** 현재 이벤트 이름을 설정한다. */
    data class SetEventName(val name: String) : ScenarioCommand

    /** 유닛을 지정 좌표와 방향으로 표시한다. */
    data class ShowUnit(val unitId: Int, val x: Int, val y: Int, val direction: Int) : ScenarioCommand

    /** 유닛을 지정 좌표와 방향으로 이동한다. */
    data class MoveUnit(val unitId: Int, val x: Int, val y: Int, val direction: Int) : ScenarioCommand

    /** 유닛의 연출 동작을 설정한다. */
    data class SetUnitAction(val unitId: Int, val action: Int) : ScenarioCommand

    /** 대사를 표시한다. */
    data class DialogueLine(val dialogue: Dialogue) : ScenarioCommand

    /** 선택지를 표시하고 입력을 기다린다. */
    data class Choose(val choice: Choice) : ScenarioCommand
}

/** 시나리오 명령의 재생 순서를 표현한다. */
data class ScenarioTimeline(val moduleName: String, val commands: List<ScenarioCommand>)

/** 스크립트가 요청한 유닛 연출 동작을 표현한다. */
data class ScriptedUnitAction(
    val unitId: Int,
    val action: Int,
    /** -1이면 현재 방향을 유지한다. */
    val direction: Int = -1,
    val loop: Boolean = false,
    /** 대기·반복이 아닌 동작은 완료 후 스크립트를 재개한다. */
    val awaitsFinishedCallback: Boolean = action > 0 && !loop,
)
