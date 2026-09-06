// Scenario
package com.jojo.game.domain.scenario

/** ScenarioFightCommand: 스크립트가 전투 시작·승패·보상 흐름에 전달하는 도메인 명령의 공통 타입이다. */
sealed class ScenarioFightCommand(open val fightId: Long) {
    data class Start(
        override val fightId: Long,
        val firstUnitId: Int,
        val secondUnitId: Int,
        val backgroundIndex: Int,
        val previousBackgroundSound: Int,
    ) : ScenarioFightCommand(fightId)

    data class ShowUnit(
        override val fightId: Long,
        val mine: Boolean,
        val text: String,
        val entryAction: Int,
    ) : ScenarioFightCommand(fightId)

    data class ShowStart(override val fightId: Long) : ScenarioFightCommand(fightId)
    data class SetAction(override val fightId: Long, val mine: Boolean, val action: Int) : ScenarioFightCommand(fightId)
    data class Say(override val fightId: Long, val mine: Boolean, val text: String, val flag: Boolean) : ScenarioFightCommand(fightId)
    data class Attack2(override val fightId: Long, val mine: Boolean, val style: Int, val defended: Boolean) : ScenarioFightCommand(fightId)
    data class Attack1(override val fightId: Long, val mine: Boolean, val style: Int, val critical: Boolean) : ScenarioFightCommand(fightId)
    data class Death(override val fightId: Long, val enemy: Boolean) : ScenarioFightCommand(fightId)
    data class End(override val fightId: Long) : ScenarioFightCommand(fightId)
}
