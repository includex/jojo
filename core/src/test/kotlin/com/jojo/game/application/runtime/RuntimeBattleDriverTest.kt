// Test
package com.jojo.game.application.runtime

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals

class RuntimeBattleDriverTest {
    @Test
    fun `driver contract carries immutable frame and commands`() {
        val driver = RuntimeBattleDriver { frame, probe ->
            if (frame.elapsed > 1f && probe.outcome == null) listOf(RuntimeBattleCommand.EndTurn)
            else emptyList()
        }
        val probe = BattleRuntimeScreenProbe(
            scenario = "S_00", playback = PlaybackState.COMPLETE, outcome = null,
            bootstrapComplete = true, initialScene1Started = true, resultScene1Started = false,
            scene2Started = false, rewardOpen = false, winConditionsOpen = false,
            savePromptOpen = false, losePromptOpen = false, loseTitleScreenX = 0,
            loseTitleScreenY = 0, playerMoveCommitted = false, campaignStage = 0,
            turnPhase = "PLAYER_INPUT", battleMenuOpen = false, battleCommandOpen = false,
            battleTargetSelectionOpen = false, magickListOpen = false, magicTargetSelection = false,
            commandWaitScreenX = 0, commandWaitScreenY = 0, menuEndRoundScreenX = 0,
            menuEndRoundScreenY = 0, battleMenuButtonScreenX = 0, battleMenuButtonScreenY = 0,
            autoBattleToggleScreenX = 0, autoBattleToggleScreenY = 0, autoBattleConfirmScreenX = 0,
            autoBattleConfirmScreenY = 0, autoBattleOverlay = "NONE", autoBattleChecked = false,
            collocation = false, committedPlayerMove = null, selectedChoice = -1,
            selectedUnitId = null,
            battle = BattleRuntimeProbeFactory(
                BattleRuntimeSnapshot(1, Faction.PLAYER, emptyList()), { emptySet() },
                { _, _, _, _, _ -> false }, { _, _ -> 0 }, { it }, { x, y -> RuntimeGridPoint(x.toInt(), y.toInt()) },
            ).create(),
        )
        assertEquals(listOf(RuntimeBattleCommand.EndTurn), driver.commands(RuntimeBattleFrame(1f, 2f), probe))
    }
}
