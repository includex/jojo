package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.presentation.shared.dialogue.DialogueMessage
import com.jojo.game.presentation.shared.dialogue.DialogueRevealTiming
import com.jojo.game.presentation.shared.dialogue.DialogueSession
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Controlled Cocos DialogueLayer timer output replayed through the production dialogue session. */
class DialogueTimerSourceFixtureTest {
    private fun fixture() = JsonReader().parse(requireNotNull(
        javaClass.classLoader.getResourceAsStream("parity/dialogue-timer-source.json"),
    ))

    @Test
    fun `float representable source steps match production dialogue session`() {
        val fixture = fixture()
        assertEquals("controlled-source-dialogue-steps-v1", fixture.getString("contract"))
        assertTrue(fixture.getBoolean("controlledEvidence"))
        assertFalse(fixture.getBoolean("naturalRun"))
        assertEquals(.04f.toDouble(), fixture.getDouble("floatPointZeroFour"), 0.0)
        assertEquals(.1f.toDouble(), fixture.getDouble("floatPointOne"), 0.0)

        for (case in fixture.get("cases")) {
            val deltas = case.get("deltas").map(JsonValue::asDouble)
            val session = sourceSession(fixture.getString("text"))

            for (index in deltas.indices) {
                val sourceDelta = deltas[index]
                assertEquals(sourceDelta, sourceDelta.toFloat().toDouble(), "${case.getString("name")} delta $index Float roundtrip")
                session.update(sourceDelta.toFloat(), autoAdvanceEnabled = false)
                assertStep(case, index, session)
            }
            assertEquals(case.getBoolean("complete"), session.view.textComplete, case.getString("name"))
        }
    }

    private fun sourceSession(text: String) = DialogueSession(
        dialogueRevealTiming = DialogueRevealTiming.COCOS_CALLBACK_TIMER,
    ).also {
        it.presentDialogue(DialogueMessage(revision = 1, speakerId = null, text = text))
    }

    private fun assertStep(case: JsonValue, index: Int, session: DialogueSession) {
        val step = case.get("steps").get(index)
        assertEquals(step.getString("text"), session.view.dialogueVisibleText, "${case.getString("name")} text at step $index")
        assertEquals(!step.getBoolean("handleActive"), session.view.textComplete, "${case.getString("name")} complete at step $index")
    }
}
