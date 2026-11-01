package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.domain.rules.*

/** Authored declarations; game-specific Kotlin modules are supplied at the composition root. */
data class GameRuleConfig(
    val resources: List<ExtensionResourceDeclaration>,
    val rules: List<RuleDefinition>,
) {
    fun createEngine(random: RuleRandom, modules: List<RuleModule> = emptyList()): RuleEngine {
        val engine = RuleEngine(
            listOf(RecoveryRuleHandler, ResourceRegenerationRuleHandler, DamageModifierRuleHandler) +
                modules.flatMap { it.handlers }, random,
        )
        engine.validate(rules)
        rules.filter { it.handlerId == ResourceRegenerationRuleHandler.id }.forEach { rule ->
            require(resources.any { it.resourceKey == rule.parameters["resource"] }) {
                "Rule ${rule.id} references an undeclared resource"
            }
        }
        return engine
    }

    companion object {
        private val packaged: GameRuleConfig by lazy {
            val path = "game-rules.json"
            val json = GameRuleConfig::class.java.classLoader.getResourceAsStream(path)?.use {
                it.bufferedReader(Charsets.UTF_8).readText()
            } ?: requireNotNull(Gdx.files) { "Missing compiled $path; run :core:processResources" }
                .internal(path).readString("UTF-8")
            parse(json)
        }
        fun load(): GameRuleConfig = packaged

        fun parse(json: String): GameRuleConfig {
            val root = JsonReader().parse(json)
            require(root.getInt("version") == RULE_CONTRACT_VERSION) { "Unsupported game rule contract version" }
            val resources = root.get("resources").map { value ->
                ExtensionResourceDeclaration(value.getString("namespace"), value.getString("key"),
                    value.getInt("defaultValue"), value.getInt("minValue"), value.getInt("maxValue"),
                    value.getBoolean("perCharacter"))
            }
            require(resources.map { it.resourceKey }.distinct().size == resources.size) { "Duplicate resource declaration" }
            val rules = root.get("rules").map { value ->
                val parameters = linkedMapOf<String, String>()
                value.get("parameters").forEach { parameters[it.name] = it.asString() }
                RuleDefinition(value.getString("id"), RuleEvent.valueOf(value.getString("event")),
                    value.getString("handlerId"), value.getInt("priority"), parameters, value.getInt("version"))
            }
            return GameRuleConfig(resources, rules)
        }
    }
}
