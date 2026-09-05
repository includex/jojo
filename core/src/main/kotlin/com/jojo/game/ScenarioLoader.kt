package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

internal object ScenarioLoader {

    fun load(moduleName: String, campaign: CampaignState = CampaignState()): ScenarioInterpreter {
        val resourceName = "scenario-ast/$moduleName.json"
        val payloadText = ScenarioInterpreter::class.java.classLoader.getResourceAsStream(resourceName)
            ?.use { it.reader(Charsets.UTF_8).readText() }
            ?: Gdx.files.internal(resourceName).readString("UTF-8")
        val payload = JsonReader().parse(payloadText)
        val module = payload.get("ast")
        val functions = module.field("body").children()
            .filter { it.typeName() == "FunctionDef" }
            .associate { node ->
                val statements = node.field("body").children().toList()
                val labels = statements.mapIndexedNotNull { index, statement ->
                    val call = statement.takeIf { it.typeName() == "Expr" }?.field("value")
                    if (call?.typeName() == "Call" && call.field("func").expressionPath() == "label") {
                        call.field("args").children().firstOrNull()?.field("value")?.asString()?.let { it to index }
                    } else null
                }.toMap()
                node.field("name").asString() to RuntimeFunction(
                    node.field("name").asString(),
                    statements,
                    labels,
                    labelEntrypoints(statements),
                )
            }
        return ScenarioInterpreter(moduleName, functions, campaign)
    }

    fun labelEntrypoints(statements: List<JsonValue>): Map<String, List<JsonValue>> {
        val result = linkedMapOf<String, List<JsonValue>>()
        fun scan(block: List<JsonValue>) {
            block.forEachIndexed { index, statement ->
                val call = statement.takeIf { it.typeName() == "Expr" }?.field("value")
                if (call?.typeName() == "Call" && call.field("func").expressionPath() == "label") {
                    call.field("args").children().firstOrNull()?.field("value")?.asString()?.let { label ->
                        result.putIfAbsent(label, block.drop(index + 1))
                    }
                }
                when (statement.typeName()) {
                    "If" -> {
                        scan(statement.field("body").children().toList())
                        scan(statement.field("orelse").children().toList())
                    }
                    "For" -> scan(statement.field("body").children().toList())
                }
            }
        }
        scan(statements)
        return result
    }
}
