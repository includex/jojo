package com.jojo.game.verification.campaign

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LinchongOpeningScenarioTest {
    @Test fun `opening separates home conspiracy palace and sentencing before battle`() {
        val campaign=CampaignState().also { it.reset() }
        val script=ScenarioInterpreter.load("R_90",campaign)
        assertEquals("lua",script.scriptLanguage)
        val backgrounds=linkedSetOf<Int>()
        val dialogue=mutableListOf<String>()
        var mostVisibleUnits=0
        var kneelingSeen=false
        script.start("scene0")
        var steps=0
        while(script.state!=PlaybackState.COMPLETE) {
            check(++steps<4000) { "Opening did not complete: ${script.state}" }
            when(script.state) {
                PlaybackState.DIALOGUE -> {
                    backgrounds+=script.stage.backgroundVariant
                    mostVisibleUnits=maxOf(mostVisibleUnits,script.stage.units.values.count { it.visible })
                    kneelingSeen=kneelingSeen || script.stage.scriptedUnitActions.any { it.unitId==512 && it.action==6 && it.loop }
                    dialogue+=script.currentDialogueSourceText.orEmpty()
                    script.advanceDialogue()
                }
                PlaybackState.DELAY -> script.skipDelay()
                PlaybackState.MODAL -> script.resumeModal()
                PlaybackState.CHOICE -> { script.selectChoice(0);script.confirmChoice() }
                PlaybackState.COMPLETE -> Unit
            }
        }
        assertEquals(listOf(991,992,993,994),backgrounds.toList())
        assertTrue(dialogue.size>=15)
        assertTrue(mostVisibleUnits>=8,"Arrest must surround Lin with six guards and an official")
        assertTrue(kneelingSeen,"Sentencing must include Lin kneeling")
        for (speaker in listOf(512,571,572,573,574)) {
            assertTrue(dialogue.any { it.startsWith("&$speaker\n") },"Missing opening speaker $speaker")
        }
        assertTrue(512 in campaign.joinedUnits)
        assertTrue(dialogue.any { "유배" in it })
    }
}
