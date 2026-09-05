package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * class  `BattleOutcomeCoordinatorTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleOutcomeCoordinatorTest {

    @Test
/**
 * 공개 메서드 `testOutcomeCalculations`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

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

        // Player victory when enemy defeated
        units.remove(enemyUnit)
        assertEquals(BattleOutcome.PLAYER_VICTORY, coordinator.outcome())

        // Enemy victory when round limit reached
        round = 10
        assertEquals(BattleOutcome.ENEMY_VICTORY, coordinator.outcome())

        // Scripted outcome overrides everything
        coordinator.setScriptedOutcome(BattleOutcome.PLAYER_VICTORY)
        assertEquals(BattleOutcome.PLAYER_VICTORY, coordinator.outcome())
    }

    @Test
/**
 * 공개 메서드 `testMaxRoundsFeatureFlag`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

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
