// Battle Evidence Test
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.overlay.RoundLayer
import kotlin.test.Test
import kotlin.test.assertEquals

/** evidence coordinator가 화면 Port를 기존 projector 입력과 보드 정책으로 조립하는지 검증한다. */
class BattleRenderEventEvidenceCoordinatorTest {
    @Test
    fun `win route projection preserves phase flags and board policy`() {
        val coordinator = BattleRenderEventEvidenceCoordinator(port())

        val projection = coordinator.projection(
            "battle-win-condition-full",
            BattleRenderEventEvidenceCoordinator.RouteState(
                battleInit = false,
                dialogueBlend = false,
                winConditionRoute = RuntimeBattleRoute.WIN_FULL,
                itemUpgrade = false,
                reward = false,
            ),
        )

        assertEquals(-320f, projection.boardLeft)
        assertEquals(1728f, projection.boardBottom)
        assertEquals(96f, projection.boardTile)
        assertEquals(BattleRenderEventProjectionWinRoute.FULL, projection.input.winConditionRoute)
        assertEquals("battle-win-condition-full", projection.input.phase)
        assertEquals("승리 조건", projection.input.winConditions?.first)
    }

    private fun port() = object : BattleRenderEventEvidenceCoordinator.Port {
        override fun unitInputs() = emptyList<BattleRenderEventProjectionUnitInput>()
        override fun dialogueMarker() = null
        override fun dialogue() = null
        override fun winConditions(route: BattleRenderEventProjectionWinRoute) =
            if (route == BattleRenderEventProjectionWinRoute.FULL) {
                BattleRenderEventProjectionWinConditionsInput("승리 조건", "본문")
            } else {
                null
            }

        override fun itemUpgrade() = null
        override fun reward() = null
        override fun roundView(): RoundLayer.View? = null
        override fun usePropertyView(): BattleUsePropertyRenderEventView = error("not used by projection")
        override fun magickView(): BattleMagickRenderEventView = error("not used by projection")
        override fun jiqiRates(): List<Int>? = null
    }
}
