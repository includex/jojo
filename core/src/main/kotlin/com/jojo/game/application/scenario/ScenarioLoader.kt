// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.application.scenario.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/**
 * `ScenarioLoader` 싱글턴 객체: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object ScenarioLoader {


    /**
     * `load`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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


    /**
     * `labelEntrypoints`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun labelEntrypoints(statements: List<JsonValue>): Map<String, List<JsonValue>> {
        val result = linkedMapOf<String, List<JsonValue>>()


        /**
         * `scan`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

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
