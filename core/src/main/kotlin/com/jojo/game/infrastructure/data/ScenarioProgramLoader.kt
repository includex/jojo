// Infrastructure
package com.jojo.game.infrastructure.data
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.*
import com.jojo.game.domain.scenario.ScenarioScript
import com.jojo.game.domain.scenario.ScriptStep

/** AST 리소스를 실행 가능한 시나리오 스크립트로 변환한다. */
object ScenarioProgramLoader {
    /** 지정한 모듈과 함수의 AST를 읽어 시나리오 단계로 컴파일한다. */
    fun load(moduleName: String = "R_00", functionName: String = "scene1"): ScenarioScript {
        val payload = JsonReader().parse(Gdx.files.internal("scenario-ast/$moduleName.json"))
        val module = payload.get("ast")
        val function = module.field("body").children().firstOrNull {
            it.typeName() == "FunctionDef" && it.field("name").asString() == functionName
        } ?: error("$moduleName.py에서 $functionName 함수를 찾지 못했습니다.")
        val text = mutableListOf<String>()
        val steps = compileStatements(function.field("body").children().toList(), text)
        require(steps.any { it is ScriptStep.PromptChoice }) { "$moduleName:$functionName 선택지를 찾지 못했습니다." }
        return ScenarioScript(moduleName, steps, text)
    }

    private fun compileStatements(statements: List<JsonValue>, text: MutableList<String>): List<ScriptStep> =
        buildList {
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
        val target =
            statement.field("targets").children().firstOrNull()?.takeIf { it.typeName() == "Name" }?.field("id")
                ?.asString()
        if (value.typeName() == "Call" && value.field("func").expressionPath() == "stage.choice" && target != null) {
            val choice = Choice(
                value.field("args").children().toList().stringAt(0).lineSequence().map(String::trim)
                    .filter(String::isNotEmpty).toList()
            )
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
            "stage.showUnit" -> listOf(
                ScenarioCommand.ShowUnit(
                    args.intAt(0),
                    args.intAt(1),
                    args.intAt(2),
                    args.intAt(3)
                )
            )

            "stage.showUnits" -> showUnitCommands(args.firstOrNull())
            "stage.unitsMove" -> unitMoveCommands(args.firstOrNull())
            "stage.unit().move" -> listOf(
                ScenarioCommand.MoveUnit(
                    node.field("func").field("value").unitId(),
                    args.intAt(0),
                    args.intAt(1),
                    args.intAt(2)
                )
            )

            "stage.unit().setAction" -> listOf(
                ScenarioCommand.SetUnitAction(
                    node.field("func").field("value").unitId(),
                    args.intAt(0)
                )
            )

            "stage.say" -> listOf(ScenarioCommand.DialogueLine(toDialogue(args.stringAt(0).also { text += it })))
            else -> emptyList()
        }
    }

    private fun showUnitCommands(node: JsonValue?): List<ScenarioCommand> {
        if (node?.typeName() != "List") return emptyList()
        return node.field("elts").children().mapNotNull { entry ->
            val values = entry.listValues()
            values.takeIf { it.size >= 3 }?.let {
                ScenarioCommand.ShowUnit(
                    it[0].asIntValue(),
                    it[1].asIntValue(),
                    it[2].asIntValue(),
                    it.getOrNull(3)?.asIntValue() ?: 0
                )
            }
        }.toList()
    }

    private fun unitMoveCommands(node: JsonValue?): List<ScenarioCommand> {
        if (node?.typeName() != "List") return emptyList()
        return node.field("elts").children().mapNotNull { entry ->
            val values = entry.listValues()
            val unitId = values.firstOrNull()?.unitId() ?: return@mapNotNull null
            values.takeIf { it.size >= 3 }?.let {
                ScenarioCommand.MoveUnit(
                    unitId,
                    it[1].asIntValue(),
                    it[2].asIntValue(),
                    it.getOrNull(3)?.asIntValue() ?: 0
                )
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

    private fun JsonValue.listValues(): List<JsonValue> =
        if (typeName() == "List") field("elts").children().toList() else emptyList()

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
