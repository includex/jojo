// Game
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx

/** ScenarioCatalog: 애플리케이션에 포함된 복원 시나리오 모듈을 나열한다. */
object ScenarioCatalog {
    /**
     * `moduleEntry` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val moduleEntry = Regex("""^  \"([A-Za-z0-9_]+)\": \{$""", RegexOption.MULTILINE)


    /**
     * `moduleNames`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moduleNames(): List<String> = moduleEntry
        .findAll(Gdx.files.internal("scenarios/manifest.json").readString("UTF-8"))
        .map { it.groupValues[1] }
        .sorted()
        .toList()


    /**
     * `rModuleNames`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun rModuleNames(): List<String> = moduleNames().filter { it.matches(Regex("R_\\d+")) }


    /**
     * `sModuleNames`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun sModuleNames(): List<String> = moduleNames().filter { it.matches(Regex("S_\\d+")) }


    /**
     * `verifyEmbeddedSources`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
