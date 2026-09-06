package com.jojo.game.verification

import com.jojo.game.GameDataCatalog

/** 독립된 카탈로그 검사를 정해진 마커 순서로 조합한다. */
internal class ScenarioBatchVerificationSuite {
/** 전체 시나리오 검사를 실행하고 마커 목록을 반환한다. */
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
