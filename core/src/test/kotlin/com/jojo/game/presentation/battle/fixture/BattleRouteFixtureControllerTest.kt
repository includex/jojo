// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import kotlin.test.Test
import kotlin.test.assertEquals

/** 전투 경로 fixture 조정기가 라운드 카드 경로별 표시값을 유지하는지 확인한다. */
class BattleRouteFixtureControllerTest {
    /** 최종·적군·기본 라운드 경로가 원본 카드의 숫자 표시 규칙을 선택하는지 확인한다. */
    @Test
    fun `라운드 경로별 카드 표시값을 계산한다`() {
        assertEquals(
            BattleRouteFixtureController.RoundCard(9, 8),
            BattleRouteFixtureController.roundCard(RuntimeBattleRoute.ROUND_FINAL, 8),
        )
        assertEquals(
            BattleRouteFixtureController.RoundCard(null, 8),
            BattleRouteFixtureController.roundCard(RuntimeBattleRoute.ROUND_ENEMY, 8),
        )
        assertEquals(
            BattleRouteFixtureController.RoundCard(2, 2),
            BattleRouteFixtureController.roundCard(null, 2),
        )
    }
}
