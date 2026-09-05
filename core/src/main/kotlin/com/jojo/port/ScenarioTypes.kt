package com.jojo.port

data class Dialogue(val speakerId: String?, val text: String)
/** HallLayer.choice(text, face) supplies the portrait used by ChooseLayer. */
data class Choice(val options: List<String>, val faceId: Int? = null)

sealed interface ScenarioCommand {
    data class LoadBackground(val backgroundId: Int, val variant: Int) : ScenarioCommand
    data class SetEventName(val name: String) : ScenarioCommand
    data class ShowUnit(val unitId: Int, val x: Int, val y: Int, val direction: Int) : ScenarioCommand
    data class MoveUnit(val unitId: Int, val x: Int, val y: Int, val direction: Int) : ScenarioCommand
    data class SetUnitAction(val unitId: Int, val action: Int) : ScenarioCommand
    data class DialogueLine(val dialogue: Dialogue) : ScenarioCommand
    data class Choose(val choice: Choice) : ScenarioCommand
}

data class ScenarioTimeline(val moduleName: String, val commands: List<ScenarioCommand>)

data class ScriptedUnitAction(
    val unitId: Int,
    val action: Int,
    /** -1 means retain current direction, matching BattleUnit.setAction. */
    val direction: Int = -1,
    val loop: Boolean = false,
    /** Non-stand, non-loop clips resume the source Script on FINISHED. */
    val awaitsFinishedCallback: Boolean = action > 0 && !loop,
)
