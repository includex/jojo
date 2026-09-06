// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleTerrainGrid

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** SourceTerrainResumeContractTest: SourceTerrainResumeContract의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class SourceTerrainResumeContractTest {
    @Test
    fun `game config terrain resumeHP is read from original key casing`() {
        val data = GameDataCatalog.load()
        assertEquals(50, data.terrainResumeHp(18)) // 성채
        assertEquals(40, data.terrainResumeHp(19)) // 관문
        assertEquals(0, data.terrainResumeHp(0)) // 평원
        assertEquals(10, data.terrainResumeMp(18))
        assertEquals(8, data.terrainResumeMp(24)) // 보물 창고
    }

    @Test
    fun `state processing restores terrain HP percent and flat MP before enemy acts`() {
        val terrain = BattleTerrainGrid(2, 1, listOf(intArrayOf(0, 18)))
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "player", Faction.PLAYER, 0, 0),
                BattleUnit(
                    "enemy", "enemy", Faction.ENEMY, 1, 0,
                    hitPoints = 20, maxHitPoints = 100,
                    magicPoints = 5, maxMagicPoints = 30,
                ),
            ),
            events = emptyList(),
            terrain = terrain,
            terrainResumeRates = mapOf(18 to 50),
            terrainResumeMp = mapOf(18 to 10),
        )

        battle.roundLifecycle.endTurn()

        assertEquals(Faction.ENEMY, battle.activeFaction)
        assertEquals(70, battle.units.getValue("enemy").hitPoints)
        assertEquals(15, battle.units.getValue("enemy").magicPoints)
    }

    @Test
    fun `battle property table is sourced from every original item not current inventory`() {
        val properties = GameDataCatalog.load().battlePropertyItems()

        assertTrue(properties.isNotEmpty())
        assertTrue(properties.all { it.itemType in 26..37 || it.itemType in 42..43 })
        assertTrue(properties.any { it.itemType == 26 }) // HP recovery, used by ZDSY.
    }
}
