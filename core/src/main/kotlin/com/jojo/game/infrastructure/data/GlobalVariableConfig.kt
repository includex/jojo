package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.domain.scenario.GlobalVariableDeclaration
import com.jojo.game.domain.scenario.GlobalVariableStore

/** Loads the validated YAML compilation product, independent of a particular game's variable names. */
object GlobalVariableConfig {
    private val packaged by lazy {
        val path = "global-variables.json"
        val json = GlobalVariableConfig::class.java.classLoader.getResourceAsStream(path)?.use {
            it.bufferedReader(Charsets.UTF_8).readText()
        } ?: requireNotNull(Gdx.files) { "Missing compiled $path; run :core:processResources" }
            .internal(path).readString("UTF-8")
        parse(json)
    }
    fun load(): List<GlobalVariableDeclaration> = packaged
    fun parse(json: String): List<GlobalVariableDeclaration> {
        val root = JsonReader().parse(json)
        require(root.getInt("version") == 1) { "Unsupported global variable schema version" }
        val variables = root.get("variables")
        require(variables != null && variables.isObject)
        val declarations = variables.map { value ->
            val default = value.get("default")
            val type = value.getString("type")
            val defaultValue: Any = when (type) {
                "int" -> { require(default.isLong); requireNotNull(default.asString().toIntOrNull()) }
                "bool" -> { require(default.isBoolean); default.asBoolean() }
                else -> error("Unsupported variable type $type")
            }
            GlobalVariableDeclaration(value.name, type, defaultValue, value.getString("description"),
                value.get("legacyId")?.let { require(it.isLong); requireNotNull(it.asString().toIntOrNull()) })
        }
        GlobalVariableStore(declarations) // Validate uniqueness, defaults and aliases before use.
        return declarations
    }
}
