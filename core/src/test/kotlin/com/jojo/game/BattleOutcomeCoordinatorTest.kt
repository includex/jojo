// Test
package com.jojo.game

import com.jojo.game.domain.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** BattleOutcomeCoordinatorTest: BattleOutcomeCoordinator의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleOutcomeCoordinatorTest {

    @Test
/** testOutcomeCalculations: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

    fun testOutcomeCalculations() {
        var round = 1
        var features = 0
        val units = mutableListOf<BattleUnit>()

        val coordinator = BattleOutcomeCoordinator(
            units = { units },
            getRound = { round },
            enabledFeatures = { features },
            initialMaxRounds = 10,
        )

        assertNull(coordinator.outcome())

        val playerUnit = BattleUnit(
            id = "player",
            name = "Player",
            faction = Faction.PLAYER,
            tileX = 0,
            tileY = 0,
            hitPoints = 100,
        )
        val enemyUnit = BattleUnit(
            id = "enemy",
            name = "Enemy",
            faction = Faction.ENEMY,
            tileX = 1,
            tileY = 1,
            hitPoints = 100,
        )
        units.add(playerUnit)
        units.add(enemyUnit)

        assertNull(coordinator.outcome())

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        units.remove(enemyUnit)
        assertEquals(BattleOutcome.PLAYER_VICTORY, coordinator.outcome())

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        round = 10
        assertEquals(BattleOutcome.ENEMY_VICTORY, coordinator.outcome())

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        coordinator.setScriptedOutcome(BattleOutcome.PLAYER_VICTORY)
        assertEquals(BattleOutcome.PLAYER_VICTORY, coordinator.outcome())
    }

    @Test
/** testMaxRoundsFeatureFlag: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

    fun testMaxRoundsFeatureFlag() {
        var features = 0
        val coordinator = BattleOutcomeCoordinator(
            units = { emptyList() },
            getRound = { 1 },
            enabledFeatures = { features },
            initialMaxRounds = 10,
        )

        coordinator.setMaxRounds(12)
        assertEquals(12, coordinator.maxRounds)

        features = BattleOutcomeCoordinator.ENABLED_FEATURE_ZJHH
        coordinator.setMaxRounds(12)
        assertEquals(16, coordinator.maxRounds)

        coordinator.setResolvedMaxRounds(20)
        assertEquals(20, coordinator.maxRounds)
    }
}
