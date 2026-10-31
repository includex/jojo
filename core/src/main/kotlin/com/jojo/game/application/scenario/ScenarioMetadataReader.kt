package com.jojo.game.application.scenario

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.jojo.engine.script.children
import com.jojo.engine.script.expressionPath
import com.jojo.engine.script.field
import com.jojo.engine.script.typeName
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit

/** Reads static deployment metadata used when starting a battle directly. */
object ScenarioMetadataReader {
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

    private fun JsonValue.walk(): Sequence<JsonValue> = sequence {
        yield(this@walk)
        children().forEach { child -> yieldAll(child.walk()) }
    }

    private fun JsonValue.intListValues(): List<Int> {
        check(typeName() == "List") { "setJoinBattle 명단은 정적 List여야 합니다: ${typeName()}" }
        return field("elts").children().map { it.asIntValue() }.toList()
    }

    private fun JsonValue.asIntValue(): Int {
        check(typeName() == "Constant") { "정수 상수가 필요합니다: ${typeName()}" }
        return field("value").asString().toInt()
    }
}
