package com.jojo.game

import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/**
 * Reads the JSON AST generated directly from each restored Python 3.9 source
 * file. This avoids fragile line parsing and keeps the original .py files in
 * the application bundle as the source of truth.
 */
/**
 * object  `ScenarioMetadataReader`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object ScenarioMetadataReader {
    /**
     * Returns the last authored HallLayer.setJoinBattle contract in an R
     * module.  Direct full-battle verification skips the Hall scenes, so it
     * must reconstruct this production prerequisite instead of inventing a
     * generic 0..14 roster.
     */
    /**
     * 공개 메서드 `loadLastJoinBattleLimit`
     *
     * ### 파라미터
    - `moduleName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `ScenarioJoinBattleLimit?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun loadLastJoinBattleLimit(moduleName: String): ScenarioJoinBattleLimit? {
        val payload = JsonReader().parse(Gdx.files.internal("scenario-ast/$moduleName.json"))
        return payload.get("ast").walk()
            .filter { it.isObject && it.getString("type", "") == "Call" }
            .filter { it.get("fields")?.get("func")?.expressionPath() == "stage.setJoinBattle" }
            .mapNotNull { call ->
                val args = call.field("args").children().toList()
                val minimum = args.getOrNull(0)?.asIntValue() ?: return@mapNotNull null
                val maximum = args.getOrNull(1)?.asIntValue() ?: return@mapNotNull null
                val required = args.getOrNull(2)?.intListValues().orEmpty()
                val excluded = args.getOrNull(3)?.intListValues().orEmpty()
                ScenarioJoinBattleLimit(minimum, maximum, required, excluded)
            }
            .lastOrNull()
    }

    /**
     * 공개 메서드 `loadFirstInteractiveSegment`
     *
     * ### 파라미터
    - `moduleName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `functionName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `ScenarioTimeline`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun loadFirstInteractiveSegment(moduleName: String, functionName: String): ScenarioTimeline {
        val payload = JsonReader().parse(Gdx.files.internal("scenario-ast/$moduleName.json"))
        val module = payload.get("ast")
        val function = module.field("body").children().firstOrNull {
            it.typeName() == "FunctionDef" && it.field("name").asString() == functionName
        } ?: error("$moduleName.py에서 $functionName 함수를 찾지 못했습니다.")

        val commands = mutableListOf<ScenarioCommand>()
        for (statement in function.field("body").children()) {
            if (appendStatement(statement, commands)) break
        }
        require(commands.any { it is ScenarioCommand.DialogueLine }) { "$moduleName:$functionName 대사를 찾지 못했습니다." }
        require(commands.lastOrNull() is ScenarioCommand.Choose) { "$moduleName:$functionName 첫 선택지를 찾지 못했습니다." }
        return ScenarioTimeline(moduleName, commands)
    }

    /** Returns true once a choice blocks the first interactive segment. */
    private fun appendStatement(statement: JsonValue, commands: MutableList<ScenarioCommand>): Boolean {
        val expression = when (statement.typeName()) {
            "Expr" -> statement.field("value")
            "Assign" -> statement.field("value")
            else -> return false
        }
        if (expression.typeName() != "Call") return false

        val path = expression.field("func").expressionPath()
        val args = expression.field("args").children().toList()
        when (path) {
            "stage.loadBg" -> commands += ScenarioCommand.LoadBackground(args.intAt(0), args.intAt(1))
            "stage.setEventName" -> commands += ScenarioCommand.SetEventName(args.stringAt(0))
            "stage.showUnit" -> commands += ScenarioCommand.ShowUnit(
                args.intAt(0),
                args.intAt(1),
                args.intAt(2),
                args.intAt(3)
            )

            "stage.showUnits" -> appendShowUnits(args.firstOrNull(), commands)
            "stage.unitsMove" -> appendUnitMoves(args.firstOrNull(), commands)
            "stage.unit().move" -> {
                val unitId = expression.field("func").field("value").unitId()
                commands += ScenarioCommand.MoveUnit(unitId, args.intAt(0), args.intAt(1), args.intAt(2))
            }

            "stage.unit().setAction" -> {
                val unitId = expression.field("func").field("value").unitId()
                commands += ScenarioCommand.SetUnitAction(unitId, args.intAt(0))
            }

            "stage.say" -> commands += ScenarioCommand.DialogueLine(toDialogue(args.stringAt(0)))
            "stage.choice" -> {
                commands += ScenarioCommand.Choose(
                    Choice(
                        args.stringAt(0).lineSequence().map(String::trim).filter(String::isNotEmpty).toList()
                    )
                )
                return true
            }
        }
        return false
    }

    private fun appendShowUnits(node: JsonValue?, commands: MutableList<ScenarioCommand>) {
        if (node?.typeName() != "List") return
        node.field("elts").children().forEach { entry ->
            val values = entry.listValues()
            if (values.size >= 3) {
                commands += ScenarioCommand.ShowUnit(
                    values[0].asIntValue(),
                    values[1].asIntValue(),
                    values[2].asIntValue(),
                    values.getOrNull(3)?.asIntValue() ?: 0
                )
            }
        }
    }

    private fun appendUnitMoves(node: JsonValue?, commands: MutableList<ScenarioCommand>) {
        if (node?.typeName() != "List") return
        node.field("elts").children().forEach { entry ->
            val values = entry.listValues()
            val unitId = values.firstOrNull()?.unitId() ?: return@forEach
            if (values.size >= 3) {
                commands += ScenarioCommand.MoveUnit(
                    unitId, values[1].asIntValue(), values[2].asIntValue(), values.getOrNull(3)?.asIntValue() ?: 0
                )
            }
        }
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

    private fun JsonValue.walk(): Sequence<JsonValue> = sequence {
        yield(this@walk)
        children().forEach { child -> yieldAll(child.walk()) }
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

    private fun JsonValue.intListValues(): List<Int> {
        check(typeName() == "List") { "setJoinBattle 명단은 정적 List여야 합니다: ${typeName()}" }
        return listValues().map { it.asIntValue() }
    }

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
