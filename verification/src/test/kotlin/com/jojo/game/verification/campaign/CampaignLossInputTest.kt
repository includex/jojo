package com.jojo.game.verification.campaign

import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * class  `CampaignLossInputTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class CampaignLossInputTest {
    @Test fun `loss recovery pointer remains unavailable until Lose prompt owns input`() {
        val logoOnly = lossState(promptOpen = false)
        assertNull(productionLossRecoveryPointer(logoOnly))

        val prompt = lossState(promptOpen = true)
        assertEquals(844 to 296, productionLossRecoveryPointer(prompt))
    }

    private fun lossState(promptOpen: Boolean) = CampaignE2eBattleState(
        scenario = "S_01", playback = PlaybackState.COMPLETE, outcome = BattleOutcome.ENEMY_VICTORY,
        initialScene1Started = true, resultScene1Started = true, scene2Started = false,
        rewardOpen = false, winConditionsOpen = false, savePromptOpen = false,
        losePromptOpen = promptOpen, loseTitleScreenX = 844, loseTitleScreenY = 296,
        playerMoveCommitted = true, campaignStage = 2, round = 4, activeFaction = Faction.ENEMY,
        turnPhase = "FINISHED", battleMenuOpen = false,
        battleCommandOpen = false, battleTargetSelectionOpen = false, selectedUnit = false,
        manualMoveInput = null, manualAttackInput = null, magickListOpen = false,
        magicTargetSelection = false, manualMagicInput = null,
        commandWaitScreenX = 0, commandWaitScreenY = 0, menuEndRoundScreenX = 0,
        menuEndRoundScreenY = 0, battleMenuButtonScreenX = 0, battleMenuButtonScreenY = 0,
        autoBattleToggleScreenX = 0, autoBattleToggleScreenY = 0,
        autoBattleConfirmScreenX = 0, autoBattleConfirmScreenY = 0,
        manualMoveDebug = "", autoBattleOverlay = "NONE",
        autoBattleChecked = false, collocation = false, committedPlayerMove = null,
        selectedChoice = 0, guidedAuthoredRoute = false,
    )
}
