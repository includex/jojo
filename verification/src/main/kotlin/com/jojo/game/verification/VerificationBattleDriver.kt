package com.jojo.game.verification

import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeBattleCommand
import com.jojo.game.application.runtime.RuntimeBattleFrame

/** Verification-owned deterministic input driver for externally named battle runs. */
internal class VerificationBattleDriver(private val state: String?) : RuntimeBattleDriver {
    private var endTurnIssued = false

    override fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand> {
        if (state == "enemy-turn" && !endTurnIssued && probe.turnPhase == "PLAYER_INPUT" && probe.outcome == null) {
            endTurnIssued = true
            return listOf(RuntimeBattleCommand.EndTurn)
        }
        return emptyList()
    }
}
