package com.jojo.game.application.scenario

import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.infrastructure.data.ScenarioDialogueTextCatalog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ScenarioHostApiTest {
    private fun emptyInterpreter() = ScenarioInterpreter("HOST_TEST", emptyMap(), CampaignState())

    @Test
    fun `host dialogue works without an AST frame and resolves stable and legacy keys`() {
        val runtime = emptyInterpreter()
        runtime.hostApi.say("&0\nHost dialogue")
        assertEquals(PlaybackState.DIALOGUE, runtime.state)
        assertEquals("Host dialogue", runtime.currentDialogue?.text)

        val stable = ScenarioDialogueTextCatalog.text("R_00.scene1.dialogue0001")
        assertEquals(stable, ScenarioDialogueTextCatalog.text("R_00:scene1:247"))
        runtime.hostApi.say("@dialogue:R_00.scene1.dialogue0001")
        assertEquals(stable, runtime.currentDialogueSourceText)
        assertFailsWith<IllegalStateException> { runtime.hostApi.say("@dialogue:unknown.stable.id") }
    }

    @Test
    fun `host delay keeps source tick timing without an AST frame`() {
        val runtime = emptyInterpreter()
        runtime.hostApi.delay(3)
        assertEquals(PlaybackState.DELAY, runtime.state)
        runtime.update(9f)
        assertEquals(PlaybackState.DELAY, runtime.state)
        repeat(3) { runtime.update(0.1f) }
        assertEquals(PlaybackState.COMPLETE, runtime.state)
    }

    @Test
    fun `host choice records supplied source and one based result without an AST frame`() {
        val runtime = emptyInterpreter()
        val site = ScenarioCallSite("LUA_TEST", "scene1", 17)
        runtime.hostApi.choice(listOf("first", "second"), null, site)
        assertEquals(PlaybackState.CHOICE, runtime.state)
        assertNull(runtime.hostApi.choiceResult)
        runtime.selectChoice(1)
        runtime.confirmChoice()
        assertEquals(2, runtime.hostApi.choiceResult)
        assertEquals(ScenarioChoiceTrace("LUA_TEST", "scene1", 17, 1, 2), runtime.choiceTrace.single())
    }

    @Test
    fun `host rejects empty choice before changing playback`() {
        val runtime = emptyInterpreter()
        assertFailsWith<IllegalArgumentException> {
            runtime.hostApi.choice(emptyList(), null, ScenarioCallSite("LUA_TEST", "scene1", 18))
        }
        assertEquals(PlaybackState.COMPLETE, runtime.state)
        assertNull(runtime.currentChoice)
        assertNull(runtime.hostApi.choiceResult)
    }
}
