package com.jojo.game.verification

import com.jojo.game.GameDataCatalog

/** Composes independent catalog checks while preserving the established marker order. */
internal class ScenarioBatchVerificationSuite {
/**
 * 공개 메서드 `verify`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `List<String>`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

    fun verify(): List<String> {
        val scenarioResult = ScenarioCatalogVerifier().verify()
        val gameData = GameDataCatalog.load()
        val battleVerifier = BattleCatalogVerifier(gameData)
        return buildList {
            add(scenarioResult.marker)
            add(BattleAvatarResourceVerifier(gameData).verify())
            addAll(battleVerifier.dataDiagnostics())
            add(YingchuanRouteVerifier(gameData).verify())
            val battleResult = battleVerifier.verify(scenarioResult.unhandledCalls)
            add(battleResult.marker)
            add(battleResult.astGapMarker)
        }
    }
}
