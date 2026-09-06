// Scenario
package com.jojo.game.domain.scenario


/** 단순화된 시나리오 스크립트의 실행 단계를 나타낸다. */
sealed interface ScriptStep {
    /** 시나리오 명령을 실행한다. */
    data class Command(val command: ScenarioCommand) : ScriptStep

    /** 선택지 입력을 기다린다. */
    data class PromptChoice(val variable: String, val choice: Choice) : ScriptStep

    /** 정수 변수를 대입한다. */
    data class AssignInt(val variable: String, val value: Int) : ScriptStep

    /** 변수 값에 따라 다음 단계 목록을 선택한다. */
    data class Conditional(
        /**
         * `variable` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val variable: String,
        /**
         * `expected` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val expected: Int,
        /**
         * `whenTrue` (List<ScriptStep>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val whenTrue: List<ScriptStep>,
        /**
         * `whenFalse` (List<ScriptStep>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val whenFalse: List<ScriptStep>
    ) : ScriptStep
}

/** 실행 가능한 시나리오 단계와 표시 문자열을 묶는다. */
data class ScenarioScript(val moduleName: String, val steps: List<ScriptStep>, val displayText: List<String>)
