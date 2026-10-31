package com.jojo.game

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.Dialogue
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.scenario.ScenarioDialogueSessionAdapter
import com.jojo.game.presentation.shared.dialogue.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RuntimeDialogueSessionAdapterTest {
    private fun runtime() = ScenarioInterpreter("dialogue-test", emptyMap(), CampaignState())

    private fun ScenarioInterpreter.say(text: String) {
        dialogueCoordinator.presentDialogue(Dialogue("1", text))
        state = PlaybackState.DIALOGUE
    }

    @Test fun `battle renders and confirms the same reveal state and resets identical next lines`() {
        val runtime = runtime().apply { say("주군, 서두르셔야 합니다.") }
        val adapter = RuntimeDialogueSessionAdapter()
        adapter.synchronize(runtime)
        adapter.update(.08f, false)
        val partial = adapter.view.dialogueVisibleText
        assertTrue(partial.isNotEmpty())
        adapter.synchronize(runtime)
        assertEquals(partial, adapter.view.dialogueVisibleText)
        assertEquals(DialogueSessionTransition.TextRevealed, adapter.dispatch(DialogueSessionInput.Confirm))
        assertEquals(runtime.currentDialogue?.text, adapter.view.dialogueVisibleText)
        assertEquals(DialogueSessionTransition.AdvanceDialogue, adapter.dispatch(DialogueSessionInput.Confirm))
        runtime.say(runtime.currentDialogue!!.text)
        adapter.synchronize(runtime)
        assertEquals("", adapter.view.dialogueVisibleText)
        assertFalse(adapter.view.textComplete)
    }

    @Test fun `battle auto advance waits a full second after reveal and cancels when disabled`() {
        val runtime = runtime().apply { say("가") }
        val adapter = RuntimeDialogueSessionAdapter()
        adapter.synchronize(runtime)
        assertEquals(DialogueSessionTransition.Ignored, adapter.update(.75f, true))
        assertTrue(adapter.view.textComplete)
        assertEquals(DialogueSessionTransition.Ignored, adapter.update(.99f, true))
        assertEquals(DialogueSessionTransition.AutoAdvance, adapter.update(.01f, true))
        adapter.update(0f, true)
        adapter.update(2f, false)
        assertEquals(DialogueSessionTransition.Ignored, adapter.update(2f, true))
    }

    @Test fun `manual reveal cancels the previous auto advance countdown`() {
        val runtime = runtime().apply { say("주군") }
        val adapter = RuntimeDialogueSessionAdapter()
        adapter.synchronize(runtime)
        adapter.update(.04f, true)
        assertEquals(DialogueSessionTransition.TextRevealed, adapter.dispatch(DialogueSessionInput.Confirm))
        assertEquals(DialogueSessionTransition.Ignored, adapter.update(10f, true))
        assertEquals(DialogueSessionTransition.AutoAdvance, adapter.update(1f, true))
        adapter.resetDialogueReveal()
        assertEquals(DialogueSessionMode.IDLE, adapter.view.mode)
    }

    @Test fun `scenario keeps callback timing while battle accumulates the same frame delta`() {
        val runtime = runtime().apply { say("주군, 서두르셔야 합니다.") }
        val battle = RuntimeDialogueSessionAdapter()
        val scenario = ScenarioDialogueSessionAdapter()
        battle.synchronize(runtime)
        scenario.synchronize(runtime)
        battle.update(.12f, false)
        scenario.update(.12f, false)
        assertTrue(battle.view.dialogueVisibleText.length >= 2)
        assertEquals("", scenario.view.dialogueVisibleText)
        scenario.update(.12f, false)
        assertEquals("주", scenario.view.dialogueVisibleText)
        assertEquals(DialogueSessionTransition.TextRevealed, scenario.dispatch(DialogueSessionInput.Confirm))
        assertEquals(DialogueSessionTransition.AdvanceDialogue, scenario.dispatch(DialogueSessionInput.Confirm))
    }

    @Test fun `runtime-owned info typing stays external through synchronization`() {
        val runtime = runtime()
        val adapter = RuntimeDialogueSessionAdapter()
        runtime.modalController.setModalPresentation("알림 본문", ScenarioModalKind.INFO, 0f)
        adapter.synchronize(runtime)
        val initial = adapter.view.modalVisibleText
        adapter.update(10f, false)
        assertEquals(initial, adapter.view.modalVisibleText)
        runtime.completeModalTyping()
        adapter.synchronize(runtime)
        assertEquals("알림 본문", adapter.view.modalVisibleText)
        assertTrue(adapter.view.textComplete)
        runtime.state = PlaybackState.COMPLETE
        adapter.synchronize(runtime)
        assertEquals(DialogueSessionMode.IDLE, adapter.view.mode)
    }
}
