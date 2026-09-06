// Verification
package com.jojo.game.verification
import com.jojo.game.application.scenario.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.infrastructure.data.EncryptedGameDataCodec
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.infrastructure.data.ScenarioCatalog

/** ScenarioCatalogVerificationResult: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class ScenarioCatalogVerificationResult(
    /** marker: 시나리오 검증 성공 표식을 담는다. */
    val marker: String,
    /** unhandledCalls: 처리되지 않은 호출별 횟수를 담는다. */
    val unhandledCalls: Map<String, Int>,
)

/** ScenarioCatalogVerifier: 패키지된 게임 리소스로 모든 캠페인 시나리오를 실행하고 소진한다. */
internal class ScenarioCatalogVerifier {
    /** verify: 시나리오 카탈로그와 관련 게임 데이터 표를 검증한다. */
    fun verify(): ScenarioCatalogVerificationResult {
        val modules = ScenarioCatalog.rModuleNames()
        check(modules.isNotEmpty()) { "R 시나리오가 번들에 없습니다." }
        verifyGameDataTables()

        var sceneOneCount = 0
        var dialogueCount = 0
        var choiceCount = 0
        var modalCount = 0
        var completedCount = 0
        var fullyPlayedCount = 0
        var mappedEventScenes = 0
        val unhandledCalls = linkedMapOf<String, Int>()
        modules.forEach { module ->
            val runtime = ScenarioInterpreter.load(module)
            if ("scene1" !in runtime.functionNames) return@forEach
            sceneOneCount++
            runtime.start("scene1")
            ScenarioRuntimeDrain.settleTimedDelay(runtime)
            if (runtime.stage.backgroundId != 0) {
                check(Gdx.files.internal("maps/${runtime.stage.backgroundId}.jpg").exists()) {
                    "$module 이벤트 배경이 없습니다: ${runtime.stage.backgroundId}"
                }
                mappedEventScenes++
            }
            when (runtime.state) {
                PlaybackState.DIALOGUE -> dialogueCount++
                PlaybackState.CHOICE -> choiceCount++
                PlaybackState.MODAL -> modalCount++
                PlaybackState.COMPLETE -> completedCount++
                PlaybackState.DELAY -> error("$module timed Hall state did not settle")
            }
            ScenarioRuntimeDrain.toCompletion(
                runtime,
                chooseGameStart = true,
                failureMessage = "$module 이벤트 재생이 종료되지 않았습니다.",
            )
            fullyPlayedCount++
            mergeUnhandled(unhandledCalls, runtime.unhandledCalls)
        }
        check(sceneOneCount == modules.size) { "scene1 누락: ${modules.size - sceneOneCount}개" }
        check(dialogueCount + choiceCount + modalCount + completedCount == modules.size) {
            "시나리오 기동 수 집계가 일치하지 않습니다."
        }
        return ScenarioCatalogVerificationResult(
            marker = "VERIFY_ALL_SCENARIOS_OK: ${modules.size} R scenarios booted; " +
                "first settled states dialogue=$dialogueCount choice=$choiceCount modal=$modalCount complete=$completedCount; " +
                "$fullyPlayedCount completed one full input branch, $mappedEventScenes loaded Mmap backgrounds; " +
                "${TABLE_NAMES.size} gameplay tables decoded",
            unhandledCalls = unhandledCalls,
        )
    }

    /** verifyGameDataTables: 게임 데이터 표를 복호화하고 JSON 구조를 검증한다. */
    private fun verifyGameDataTables() {
        TABLE_NAMES.forEach { name ->
            val payload = EncryptedGameDataCodec.decode(Gdx.files.internal("maps/data/$name.bin").readBytes())
                ?: error("$name.bin 복호화 또는 MD5 검증에 실패했습니다.")
            check(JsonReader().parse(payload) != null) { "$name.bin JSON을 읽지 못했습니다." }
        }
    }

    private companion object {
        /**
         * `TABLE_NAMES` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val TABLE_NAMES = listOf(
            "unit", "arms", "posts", "unitPostsSkill", "magic", "item",
            "itemSkills", "config", "hitarea", "effarea", "defineSkill",
        )
    }
}

/** mergeUnhandled: 미처리 호출 집계를 대상 맵에 누적한다. */
internal fun mergeUnhandled(target: MutableMap<String, Int>, source: Map<String, Int>) {
    source.forEach { (call, count) -> target[call] = (target[call] ?: 0) + count }
}
