// Battle Trace Test
package com.jojo.game.presentation.battle.trace

import com.jojo.game.application.runtime.BattleRuntimeProbe
import com.jojo.game.application.runtime.BattleRuntimeSnapshot
import com.jojo.game.application.runtime.RuntimeGridPoint
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 전투 화면 probe 투영기가 화면 상태와 화면 좌표를 런타임 조회 계약에 보존하는지 확인한다. */
class BattleRuntimeScreenProbeProjectorTest {
    /** 투영: 메뉴·자동 전투·명령 좌표와 선택 상태가 결과 probe에 그대로 기록되는지 확인한다. */
    @Test
    fun `화면 상태를 런타임 probe로 조립한다`() {
        val probe = BattleRuntimeScreenProbeProjector.project(
            BattleRuntimeScreenProbeInput(
                scenario = "S_00", playback = PlaybackState.DIALOGUE, outcome = null,
                bootstrapComplete = true, initialScene1Started = true, resultScene1Started = false,
                scene2Started = false, rewardOpen = false, winConditionsOpen = true, savePromptOpen = false,
                losePromptOpen = false, loseTitle = 1 to 2, playerMoveCommitted = true, campaignStage = 3,
                turnPhase = "PLAYER", battleMenuOpen = true, battleCommandOpen = true,
                battleTargetSelectionOpen = false, magickListOpen = true, magicTargetSelection = false,
                commandWait = 3 to 4, menuEndRound = 5 to 6, battleMenuButton = 7 to 8,
                autoBattleToggle = 9 to 10, autoBattleConfirm = 11 to 12, autoBattleOverlay = "PROMPT",
                autoBattleChecked = true, collocation = false, committedPlayerMove = "unit-1",
                selectedChoice = 2, selectedUnitId = "unit-2",
            ),
            emptyBattleProbe,
        )

        assertEquals("S_00", probe.scenario)
        assertEquals(3 to 4, probe.commandWaitScreenX to probe.commandWaitScreenY)
        assertEquals("unit-1", probe.committedPlayerMove)
        assertEquals("unit-2", probe.selectedUnitId)
        assertTrue(probe.battleMenuOpen)
    }

    /** 빈 전장 probe: 투영기 테스트에 필요한 최소 런타임 전장 질의를 제공한다. */
    private val emptyBattleProbe = object : BattleRuntimeProbe {
        override val snapshot = BattleRuntimeSnapshot(1, Faction.PLAYER, emptyList())
        override fun reachableTiles(unitId: String) = emptySet<RuntimeGridPoint>()
        override fun canEnterTilesIgnoringEnemyWithinMoves(
            unitId: String, ignoredEnemyId: String, start: RuntimeGridPoint,
            targetTiles: Set<RuntimeGridPoint>, moves: Int,
        ) = false
        override fun physicalDamagePreview(attackerId: String, targetId: String) = 0
        override fun screenPoint(tile: RuntimeGridPoint) = tile
        override fun projectWorldPoint(x: Float, y: Float) = RuntimeGridPoint(x.toInt(), y.toInt())
    }
}
