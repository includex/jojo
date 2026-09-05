package com.jojo.game.verification

import com.jojo.game.GameDataCatalog

/** Composes independent catalog checks while preserving the established marker order. */
internal class ScenarioBatchVerificationSuite {
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
