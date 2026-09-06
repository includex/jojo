// Scenario
package com.jojo.game.domain.scenario

/** ScenarioFightCommand: 스크립트가 전투 시작·승패·보상 흐름에 전달하는 도메인 명령의 공통 타입이다. */
sealed class ScenarioFightCommand(open val fightId: Long) {
    /**
     * `Start` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Start(
        /**
         * `fightId` (Long,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val fightId: Long,
        /**
         * `firstUnitId` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val firstUnitId: Int,
        /**
         * `secondUnitId` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val secondUnitId: Int,
        /**
         * `backgroundIndex` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val backgroundIndex: Int,
        /**
         * `previousBackgroundSound` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val previousBackgroundSound: Int,
    ) : ScenarioFightCommand(fightId)

    /**
     * `ShowUnit` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class ShowUnit(
        /**
         * `fightId` (Long,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val fightId: Long,
        /**
         * `mine` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mine: Boolean,
        /**
         * `text` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val text: String,
        /**
         * `entryAction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val entryAction: Int,
    ) : ScenarioFightCommand(fightId)

    /**
     * `ShowStart` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class ShowStart(override val fightId: Long) : ScenarioFightCommand(fightId)
    /**
     * `SetAction` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class SetAction(override val fightId: Long, val mine: Boolean, val action: Int) : ScenarioFightCommand(fightId)
    /**
     * `Say` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Say(override val fightId: Long, val mine: Boolean, val text: String, val flag: Boolean) : ScenarioFightCommand(fightId)
    /**
     * `Attack2` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Attack2(override val fightId: Long, val mine: Boolean, val style: Int, val defended: Boolean) : ScenarioFightCommand(fightId)
    /**
     * `Attack1` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Attack1(override val fightId: Long, val mine: Boolean, val style: Int, val critical: Boolean) : ScenarioFightCommand(fightId)
    /**
     * `Death` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Death(override val fightId: Long, val enemy: Boolean) : ScenarioFightCommand(fightId)
    /**
     * `End` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class End(override val fightId: Long) : ScenarioFightCommand(fightId)
}
