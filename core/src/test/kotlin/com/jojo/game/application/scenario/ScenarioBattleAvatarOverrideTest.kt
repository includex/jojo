package com.jojo.game.application.scenario

import com.jojo.game.application.battle.BattleScenarioFactory
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.infrastructure.data.GameDataCatalog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ScenarioBattleAvatarOverrideTest {
    private fun opening(module: String): ScenarioInterpreter {
        val runtime = ScenarioInterpreter.load(module, CampaignState().also { it.reset() })
        runtime.start("scene0")
        var steps = 0
        while (runtime.state != PlaybackState.COMPLETE) {
            check(++steps < 4000)
            when (runtime.state) {
                PlaybackState.DIALOGUE -> runtime.advanceDialogue()
                PlaybackState.CHOICE -> { runtime.selectChoice(0); runtime.confirmChoice() }
                PlaybackState.DELAY -> runtime.skipDelay()
                PlaybackState.MODAL -> runtime.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
        return runtime
    }

    @Test fun `Lin override belongs only to first battle instance`() {
        val first = opening("S_90").stage.battleUnits.values
        val later = opening("S_91").stage.battleUnits.values
        assertEquals(622, first.single { it.characterId == 512 }.avatarId)
        assertNull(later.single { it.characterId == 512 }.avatarId)
        val projected = BattleScenarioFactory.fromScriptedUnits(first)
        assertEquals(622, projected.units.values.single { it.characterId == 512 }.avatarId)
        assertEquals(601, GameDataCatalog.load().unitProfile(512)?.mapAvatar)
    }
}
