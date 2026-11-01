package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

/** Choices owned by content/game.yaml and compiled into the packaged resource. */
data class GamePackage(
    val id: String,
    val title: String,
    val startModule: String,
    val startScene: String,
    val resumeScene: String,
    val protagonistId: Int,
    val saveNamespace: String,
    val defaultBattleModule: String,
    val battleModules: Map<String, String>,
) {
    fun battleModule(scenario: String): String =
        battleModules[scenario] ?: scenario.takeIf { it in battleModules.values } ?: defaultBattleModule

    fun returnModule(battle: String): String =
        battleModules.entries.firstOrNull { it.value == battle }?.key
            ?: battle.takeIf { it in battleModules.keys } ?: startModule

    companion object {
        private val packaged: GamePackage by lazy {
            GamePackage::class.java.classLoader.getResourceAsStream("game-package.json")?.use { stream ->
                return@lazy parse(stream.bufferedReader(Charsets.UTF_8).readText())
            }
            val files = Gdx.files
            require(files != null && files.internal("game-package.json").exists()) {
                "Missing compiled game-package.json; compile content/game.yaml into core resources"
            }
            parse(files.internal("game-package.json").readString("UTF-8"))
        }

        /** The classpath is available when launch configuration is built, before Gdx initializes. */
        fun load(): GamePackage = packaged

        fun parse(json: String): GamePackage {
            val root = JsonReader().parse(json)
            val routes = linkedMapOf<String, String>()
            var route = root.get("battleModules")?.child
            while (route != null) {
                routes[route.name] = route.asString()
                route = route.next
            }
            return GamePackage(
                root.getString("id"), root.getString("title"),
                root.getString("startModule"), root.getString("startScene"),
                root.getString("resumeScene"), root.getInt("protagonistId"),
                root.getString("saveNamespace"), root.getString("defaultBattleModule"), routes,
            )
        }
    }
}
