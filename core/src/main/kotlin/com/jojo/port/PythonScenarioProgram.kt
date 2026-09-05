package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import java.util.ArrayDeque

sealed interface ScriptStep {
    data class Command(val command: ScenarioCommand) : ScriptStep
    data class PromptChoice(val variable: String, val choice: Choice) : ScriptStep
    data class AssignInt(val variable: String, val value: Int) : ScriptStep
    data class Conditional(val variable: String, val expected: Int, val whenTrue: List<ScriptStep>, val whenFalse: List<ScriptStep>) : ScriptStep
}

data class ScenarioProgram(val moduleName: String, val steps: List<ScriptStep>, val displayText: List<String>)

/**
 * Restricted source-level Python executor. It currently models the control
 * flow used by dialogue sections: assignments, choice result variables and
 * equality branches. Unsupported game calls are intentionally skipped until
 * their matching Stage API is implemented, rather than being faked.
 */
object PythonScenarioProgram {
    fun load(moduleName: String = "R_00", functionName: String = "scene1"): ScenarioProgram {
        val payload = JsonReader().parse(Gdx.files.internal("scenario-ast/$moduleName.json"))
        val module = payload.get("ast")
        val function = module.field("body").children().firstOrNull {
            it.typeName() == "FunctionDef" && it.field("name").asString() == functionName
        } ?: error("$moduleName.py에서 $functionName 함수를 찾지 못했습니다.")
        val text = mutableListOf<String>()
        val steps = compileStatements(function.field("body").children().toList(), text)
        require(steps.any { it is ScriptStep.PromptChoice }) { "$moduleName:$functionName 선택지를 찾지 못했습니다." }
        return ScenarioProgram(moduleName, steps, text)
    }

    private fun compileStatements(statements: List<JsonValue>, text: MutableList<String>): List<ScriptStep> = buildList {
        statements.forEach { statement ->
            when (statement.typeName()) {
                "Expr" -> commandsFromCall(statement.field("value"), text).forEach { add(ScriptStep.Command(it)) }
                "Assign" -> compileAssignment(statement, text)?.let(::add)
                "If" -> compileConditional(statement, text)?.let(::add)
            }
        }
    }

    private fun compileAssignment(statement: JsonValue, text: MutableList<String>): ScriptStep? {
        val value = statement.field("value")
        val target = statement.field("targets").children().firstOrNull()?.takeIf { it.typeName() == "Name" }?.field("id")?.asString()
        if (value.typeName() == "Call" && value.field("func").expressionPath() == "stage.choice" && target != null) {
            val choice = Choice(value.field("args").children().toList().stringAt(0).lineSequence().map(String::trim).filter(String::isNotEmpty).toList())
            text += choice.options
            return ScriptStep.PromptChoice(target, choice)
        }
        if (target != null && value.typeName() == "Constant") {
            val raw = value.field("value").asString()
            raw.toIntOrNull()?.let { return ScriptStep.AssignInt(target, it) }
        }
        return null
    }

    private fun compileConditional(statement: JsonValue, text: MutableList<String>): ScriptStep.Conditional? {
        val test = statement.field("test")
        if (test.typeName() != "Compare") return null
        val left = test.field("left")
        val operator = test.field("ops").children().firstOrNull()?.typeName()
        val comparator = test.field("comparators").children().firstOrNull()
        if (left.typeName() != "Name" || operator != "Eq" || comparator?.typeName() != "Constant") return null
        val expected = comparator.field("value").asString().toIntOrNull() ?: return null
        return ScriptStep.Conditional(
            left.field("id").asString(),
            expected,
            compileStatements(statement.field("body").children().toList(), text),
            compileStatements(statement.field("orelse").children().toList(), text)
        )
    }

    private fun commandsFromCall(node: JsonValue, text: MutableList<String>): List<ScenarioCommand> {
        if (node.typeName() != "Call") return emptyList()
        val path = node.field("func").expressionPath()
        val args = node.field("args").children().toList()
        return when (path) {
            "stage.loadBg" -> listOf(ScenarioCommand.LoadBackground(args.intAt(0), args.intAt(1)))
            "stage.setEventName" -> listOf(ScenarioCommand.SetEventName(args.stringAt(0).also { text += it }))
            "stage.showUnit" -> listOf(ScenarioCommand.ShowUnit(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3)))
            "stage.showUnits" -> showUnitCommands(args.firstOrNull())
            "stage.unitsMove" -> unitMoveCommands(args.firstOrNull())
            "stage.unit().move" -> listOf(ScenarioCommand.MoveUnit(node.field("func").field("value").unitId(), args.intAt(0), args.intAt(1), args.intAt(2)))
            "stage.unit().setAction" -> listOf(ScenarioCommand.SetUnitAction(node.field("func").field("value").unitId(), args.intAt(0)))
            "stage.say" -> listOf(ScenarioCommand.DialogueLine(toDialogue(args.stringAt(0).also { text += it })))
            else -> emptyList()
        }
    }

    private fun showUnitCommands(node: JsonValue?): List<ScenarioCommand> {
        if (node?.typeName() != "List") return emptyList()
        return node.field("elts").children().mapNotNull { entry ->
            val values = entry.listValues()
            values.takeIf { it.size >= 3 }?.let {
                ScenarioCommand.ShowUnit(it[0].asIntValue(), it[1].asIntValue(), it[2].asIntValue(), it.getOrNull(3)?.asIntValue() ?: 0)
            }
        }.toList()
    }

    private fun unitMoveCommands(node: JsonValue?): List<ScenarioCommand> {
        if (node?.typeName() != "List") return emptyList()
        return node.field("elts").children().mapNotNull { entry ->
            val values = entry.listValues()
            val unitId = values.firstOrNull()?.unitId() ?: return@mapNotNull null
            values.takeIf { it.size >= 3 }?.let {
                ScenarioCommand.MoveUnit(unitId, it[1].asIntValue(), it[2].asIntValue(), it.getOrNull(3)?.asIntValue() ?: 0)
            }
        }.toList()
    }

    private fun toDialogue(raw: String): Dialogue {
        val match = Regex("""^&(\d+)\n(.*)$""", setOf(RegexOption.DOT_MATCHES_ALL)).matchEntire(raw)
        return if (match == null) Dialogue(null, raw) else Dialogue(match.groupValues[1], match.groupValues[2])
    }

    private fun JsonValue.typeName(): String = getString("type")
    private fun JsonValue.field(name: String): JsonValue = get("fields").get(name)
    private fun JsonValue.children(): Sequence<JsonValue> = sequence {
        var item = child
        while (item != null) {
            yield(item)
            item = item.next
        }
    }
    private fun JsonValue.expressionPath(): String? = when (typeName()) {
        "Name" -> field("id").asString()
        "Attribute" -> field("value").expressionPath()?.plus(".")?.plus(field("attr").asString())
        "Call" -> field("func").expressionPath()?.plus("()")
        else -> null
    }
    private fun JsonValue.unitId(): Int {
        check(typeName() == "Call" && field("func").expressionPath() == "stage.unit") { "stage.unit(id) 호출이 필요합니다." }
        return field("args").children().first().asIntValue()
    }
    private fun JsonValue.listValues(): List<JsonValue> = if (typeName() == "List") field("elts").children().toList() else emptyList()
    private fun JsonValue.asIntValue(): Int {
        check(typeName() == "Constant") { "정수 상수가 필요합니다: ${typeName()}" }
        return field("value").asString().toInt()
    }
    private fun List<JsonValue>.intAt(index: Int): Int = getOrNull(index)?.asIntValue() ?: error("인수 ${index}가 없습니다.")
    private fun List<JsonValue>.stringAt(index: Int): String {
        val value = getOrNull(index) ?: error("인수 ${index}가 없습니다.")
        check(value.typeName() == "Constant") { "문자열 상수가 필요합니다: ${value.typeName()}" }
        return value.field("value").asString()
    }
}

class ProgramPlayback(val program: ScenarioProgram) {
    val stage = ScenarioStage()
    var state: PlaybackState = PlaybackState.COMPLETE
        private set
    var currentDialogue: Dialogue? = null
        private set
    var currentChoice: Choice? = null
        private set
    var selectedChoice: Int = 0
        private set
    var chosenOption: String? = null
        private set
    private var pendingChoiceVariable: String? = null
    private val variables = mutableMapOf<String, Int>()
    private val queue = ArrayDeque<ScriptStep>()

    init {
        queue.addAll(program.steps)
        runUntilInput()
    }

    fun advanceDialogue() {
        check(state == PlaybackState.DIALOGUE) { "대기 중인 대사가 없습니다." }
        currentDialogue = null
        runUntilInput()
    }

    fun selectPrevious() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice - 1, it.size) }
    }

    fun selectNext() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice + 1, it.size) }
    }

    fun confirmChoice() {
        check(state == PlaybackState.CHOICE) { "대기 중인 선택지가 없습니다." }
        val choice = requireNotNull(currentChoice)
        chosenOption = choice.options[selectedChoice]
        variables[requireNotNull(pendingChoiceVariable)] = selectedChoice + 1 // Original scripts use one-based choices.
        currentChoice = null
        pendingChoiceVariable = null
        runUntilInput()
    }

    private fun runUntilInput() {
        while (queue.isNotEmpty()) {
            when (val step = queue.removeFirst()) {
                is ScriptStep.Command -> when (val command = step.command) {
                    is ScenarioCommand.DialogueLine -> {
                        currentDialogue = command.dialogue
                        state = PlaybackState.DIALOGUE
                        return
                    }
                    else -> stage.apply(command)
                }
                is ScriptStep.PromptChoice -> {
                    currentChoice = step.choice
                    selectedChoice = 0
                    pendingChoiceVariable = step.variable
                    state = PlaybackState.CHOICE
                    return
                }
                is ScriptStep.AssignInt -> variables[step.variable] = step.value
                is ScriptStep.Conditional -> prepend(if (variables[step.variable] == step.expected) step.whenTrue else step.whenFalse)
            }
        }
        state = PlaybackState.COMPLETE
    }

    private fun prepend(steps: List<ScriptStep>) {
        steps.asReversed().forEach(queue::addFirst)
    }
}
