package com.jojo.game.application.scenario

import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals

class LuaPythonScenarioParityTest {
    private fun settle(runtime: ScenarioInterpreter): List<String> {
        val lines = mutableListOf<String>()
        var steps = 0
        while (runtime.state != PlaybackState.COMPLETE) {
            check(++steps < 4000) { "Scenario did not settle: ${runtime.state}" }
            when (runtime.state) {
                PlaybackState.DIALOGUE -> { lines += runtime.currentDialogueSourceText.orEmpty(); runtime.advanceDialogue() }
                PlaybackState.CHOICE -> { runtime.selectChoice(0); runtime.confirmChoice() }
                PlaybackState.DELAY -> runtime.skipDelay()
                PlaybackState.MODAL -> runtime.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
        return lines
    }

    @Test fun `all eight Lua openings match Python placement dialogue and progression`() {
        for (index in 90..97) {
            val old = CampaignState().also { it.reset() }
            val current = CampaignState().also { it.reset() }
            for ((campaign, python) in listOf(old to true, current to false)) {
                val route = if (python) ScenarioLoader.loadPython("R_90", campaign) else ScenarioLoader.load("R_90", campaign)
                route.start("scene0"); settle(route)
            }
            val python = ScenarioLoader.loadPython("S_$index", old)
            val lua = ScenarioLoader.load("S_$index", current)
            assertEquals("lua", lua.scriptLanguage)
            assertEquals("python", python.scriptLanguage)
            python.start("scene0"); lua.start("scene0")
            assertEquals(settle(python), settle(lua), "S_$index opening dialogue")
            fun positions(r: ScenarioInterpreter) = r.stage.battleUnits.values.map { listOf(it.characterId, it.x, it.y, it.direction) }
            assertEquals(positions(python), positions(lua), "S_$index placement")
            assertEquals(python.stage.battleMapIndex, lua.stage.battleMapIndex)
            assertEquals(old.globalVariables, current.globalVariables)
            assertEquals(old.inventory.items, current.inventory.items)
            assertEquals(old.joinedUnits, current.joinedUnits)
        }
    }
}
