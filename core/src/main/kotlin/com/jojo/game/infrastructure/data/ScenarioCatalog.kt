// Game
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx

/** ScenarioCatalog: 애플리케이션에 포함된 복원 시나리오 모듈을 나열한다. */
object ScenarioCatalog {
    private val moduleEntry = Regex("""^  \"([A-Za-z0-9_]+)\": \{$""", RegexOption.MULTILINE)


    fun moduleNames(): List<String> = moduleEntry
        .findAll(Gdx.files.internal("scenarios/manifest.json").readString("UTF-8"))
        .map { it.groupValues[1] }
        .sorted()
        .toList()


    fun rModuleNames(): List<String> = moduleNames().filter { it.matches(Regex("R_\\d+")) }


    fun sModuleNames(): List<String> = moduleNames().filter { it.matches(Regex("S_\\d+")) }


    fun verifyEmbeddedSources(): Int {
        val modules = moduleNames()
        require(modules.isNotEmpty()) { "시나리오 manifest가 비어 있습니다." }
        modules.forEach { module ->
            require(Gdx.files.internal("scenarios/$module.py").exists()) { "$module.py가 번들에 없습니다." }
            require(Gdx.files.internal("scenario-ast/$module.json").exists()) { "$module AST가 번들에 없습니다." }
        }
        return modules.size
    }
}
