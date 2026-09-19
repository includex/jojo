package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.presentation.shared.dialogue.DialogueMessage
import com.jojo.game.presentation.shared.dialogue.DialogueRevealTiming
import com.jojo.game.presentation.shared.dialogue.DialogueSession
import com.jojo.game.presentation.shared.dialogue.DialogueSessionInput
import com.jojo.game.presentation.shared.dialogue.DialogueSessionTransition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Controlled DialogueLayer auto-close output replayed through the production source-policy session. */
class DialogueAutoSourceFixtureTest {
    private fun fixture() = JsonReader().parse(requireNotNull(
        javaClass.classLoader.getResourceAsStream("parity/dialogue-auto-source.json"),
    ))

    @Test
    fun `natural and manual source auto close steps match production session`() {
        val fixture = fixture()
        assertFixtureContract(fixture)

        val natural = fixture.case("glyph-completion-registers-auto-same-update")
        val naturalSession = sourceSession("대")
        replayUpdates(natural, naturalSession, compareText = true)

        val manual = fixture.case("manual-reveal-outside-update-next-update-prime")
        val manualSession = sourceSession("대")
        manualSession.update(0f, autoAdvanceEnabled = true) // Snapshot source flag and prime glyph timer.
        assertEquals(DialogueSessionTransition.TextRevealed, manualSession.dispatch(DialogueSessionInput.Confirm))
        replayUpdates(manual, manualSession, compareText = true)
    }

    @Test
    fun `disabled and cancelled source auto close never fire`() {
        val fixture = fixture()

        // These source cases call _enabledAutoClose/_disAutoClose on a controlled empty UI closure.
        // The shared session has no direct timer hook, so equivalent public dialogue operations create the timer.
        val disabled = fixture.case("disabled-flag-no-timer")
        val disabledSession = sourceSession("대")
        disabledSession.update(0f, autoAdvanceEnabled = false)
        disabledSession.update(.1f, autoAdvanceEnabled = false) // Natural completion with flag snapshot disabled.
        for (delta in disabled.floatDeltas()) {
            assertEquals(DialogueSessionTransition.Ignored, disabledSession.update(delta, autoAdvanceEnabled = true))
        }

        val cancel = fixture.case("cancel-auto-close")
        val cancelSession = sourceSession("대")
        cancelSession.update(0f, autoAdvanceEnabled = true)
        cancelSession.dispatch(DialogueSessionInput.Confirm)
        val deltas = cancel.floatDeltas()
        assertEquals(DialogueSessionTransition.Ignored, cancelSession.update(deltas[0], autoAdvanceEnabled = true))
        assertEquals(DialogueSessionTransition.Ignored, cancelSession.update(deltas[1], autoAdvanceEnabled = true))
        assertEquals("production _disAutoClose", cancel.get("steps").get(2).getString("operation"))
        cancelSession.clear()
        assertEquals(DialogueSessionTransition.Ignored, cancelSession.update(deltas[2], autoAdvanceEnabled = true))
        assertTrue(cancel.get("steps").get(3).get("events").isEmpty)
    }

    @Test
    fun `second click cancels old timer and primes the next source page`() {
        val case = fixture().case("second-click-cancels-auto-and-advances")
        val deltas = case.floatDeltas()
        val session = sourceSession("A")
        session.update(0f, autoAdvanceEnabled = true)
        session.dispatch(DialogueSessionInput.Confirm)

        assertStep(case.get("steps").get(0), session, session.update(deltas[0], true), 0)
        assertEquals(DialogueSessionTransition.AdvanceDialogue, session.dispatch(DialogueSessionInput.Confirm))
        session.presentDialogue(DialogueMessage(2, null, "B"))
        assertStep(case.get("steps").get(1), session, DialogueSessionTransition.AdvanceDialogue, 1)
        assertStep(case.get("steps").get(2), session, session.update(deltas[1], true), 2)
        assertStep(case.get("steps").get(3), session, session.update(deltas[2], true), 3)
    }

    private fun replayUpdates(case: JsonValue, session: DialogueSession, compareText: Boolean) {
        val steps = case.get("steps")
        for ((index, delta) in case.floatDeltas().withIndex()) {
            val transition = session.update(delta, autoAdvanceEnabled = true)
            val step = steps.get(index)
            if (compareText) assertStep(step, session, transition, index)
            val sourceClosed = step.get("events").any { it.getString("kind") == "closeCallback" }
            assertEquals(sourceClosed, transition == DialogueSessionTransition.AutoAdvance, "${case.name} close at step $index")
        }
    }

    private fun assertStep(step: JsonValue, session: DialogueSession, transition: DialogueSessionTransition, index: Int) {
        assertEquals(step.getString("text"), session.view.dialogueVisibleText, "visible text at step $index")
        val remaining = step.get("remaining")
        val expectedComplete = remaining == null || remaining.isNull || remaining.asString().isEmpty()
        assertEquals(expectedComplete, session.view.textComplete, "completion at step $index")
        if (step.get("events").any { it.getString("kind") == "closeCallback" }) {
            assertEquals(DialogueSessionTransition.AutoAdvance, transition, "source close callback mapping at step $index")
        }
    }

    private fun sourceSession(text: String) = DialogueSession(
        dialogueRevealTiming = DialogueRevealTiming.COCOS_CALLBACK_TIMER,
    ).also { it.presentDialogue(DialogueMessage(1, null, text)) }

    private fun JsonValue.floatDeltas(): List<Float> = get("deltas").map { value ->
        val sourceDelta = value.asDouble()
        assertEquals(sourceDelta, sourceDelta.toFloat().toDouble(), "$name delta Float roundtrip")
        sourceDelta.toFloat()
    }

    private fun JsonValue.case(name: String): JsonValue =
        get("cases").first { it.getString("name") == name }

    private fun assertFixtureContract(fixture: JsonValue) {
        assertEquals("controlled-source-dialogue-auto-close-v1", fixture.getString("contract"))
        assertTrue(fixture.getBoolean("controlledEvidence"))
        assertFalse(fixture.getBoolean("naturalRun"))
        assertEquals(1, fixture.getInt("autoCloseFlag"))
        assertEquals(1.6, fixture.getDouble("autoCloseDelaySeconds"), 0.0)
        assertEquals(.1f.toDouble(), fixture.getDouble("floatPointOne"), 0.0)
        assertEquals("continuation-page-state", fixture.get("mockScope").getString("kind"))
    }
}
