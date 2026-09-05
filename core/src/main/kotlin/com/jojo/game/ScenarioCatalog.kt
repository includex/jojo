package com.jojo.game

import com.badlogic.gdx.Gdx

/** Lists the restored scenario modules embedded in the LibGDX application. */
object ScenarioCatalog {
    private val moduleEntry = Regex("""^  \"([A-Za-z0-9_]+)\": \{$""", RegexOption.MULTILINE)

    /**
     * 공개 메서드 `moduleNames`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<String>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun moduleNames(): List<String> = moduleEntry
        .findAll(Gdx.files.internal("scenarios/manifest.json").readString("UTF-8"))
        .map { it.groupValues[1] }
        .sorted()
        .toList()

    /**
     * 공개 메서드 `rModuleNames`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<String>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rModuleNames(): List<String> = moduleNames().filter { it.matches(Regex("R_\\d+")) }

    /**
     * 공개 메서드 `sModuleNames`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<String>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun sModuleNames(): List<String> = moduleNames().filter { it.matches(Regex("S_\\d+")) }

    /**
     * 공개 메서드 `verifyEmbeddedSources`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
