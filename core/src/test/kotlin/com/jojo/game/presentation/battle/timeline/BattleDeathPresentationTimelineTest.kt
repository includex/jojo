package com.jojo.game.presentation.battle.timeline

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleDeathPresentationTimelineTest {
    @Test
    fun `post action deaths run dialogue and animations serially`() {
        val port = RecordingPort(
            deaths = listOf(
                death("first", showMessage = true),
                death("second", showMessage = false),
            ),
        )
        val timeline = BattleDeathPresentationTimeline(port)

        assertTrue(timeline.queuePostAction(port.deaths))
        assertEquals(listOf("focus:first", "dialogue:first"), port.events)

        port.dialogueActive = false
        timeline.tick(1f)
        timeline.tick(2f)
        timeline.tick(2f)
        timeline.tick(3f)

        assertEquals(
            listOf(
                "focus:first",
                "dialogue:first",
                "start:first@1.0",
                "complete:first",
                "focus:second",
                "start:second@2.0",
                "complete:second",
            ),
            port.events,
        )
        assertFalse(timeline.isBusy())
    }

    @Test
    fun `round checkpoint preserves script death script callback order`() {
        val port = RecordingPort(deaths = listOf(death("fallen", showMessage = false)))
        val timeline = BattleDeathPresentationTimeline(port)

        assertFalse(timeline.begin(BattleDeathPresentationTimeline.Checkpoint.ROUND_START))
        assertEquals(listOf("script"), port.events)

        port.scriptComplete = true
        timeline.driveScriptBarrier()
        timeline.tick(1f)
        timeline.tick(2f)
        assertEquals(listOf("script", "focus:fallen", "start:fallen@0.0", "complete:fallen", "script"), port.events)
        assertFalse(port.events.any { it.startsWith("checkpoint:") })

        port.scriptComplete = true
        timeline.driveScriptBarrier()
        assertEquals("checkpoint:ROUND_START", port.events.last())
    }

    private fun death(id: String, showMessage: Boolean) = BattleDeathPresentationTimeline.DeathUnit(
        unitId = id,
        direction = 2,
        sourceAction = 1,
        duration = 1f,
        originalHp = 0,
        showRetireMessage = showMessage,
        dialogueCharacterId = id,
        retireMessage = if (showMessage) "retire" else null,
    )

    private class RecordingPort(
        val deaths: List<BattleDeathPresentationTimeline.DeathUnit>,
    ) : BattleDeathPresentationTimeline.Port {
        override var now: Float = 0f
        override var scriptComplete: Boolean = false
        override var dialogueActive: Boolean = true
        val events = mutableListOf<String>()

        override fun collectDyingUnits() = deaths
        override fun runScript() {
            events += "script"
            scriptComplete = false
        }
        override fun focusUnit(unitId: String) { events += "focus:$unitId" }
        override fun presentRetireDialogue(unit: BattleDeathPresentationTimeline.DeathUnit) {
            events += "dialogue:${unit.unitId}"
        }
        override fun startDeathAnimation(
            unit: BattleDeathPresentationTimeline.DeathUnit,
            startsAt: Float,
            endsAt: Float,
        ) {
            events += "start:${unit.unitId}@$startsAt"
        }
        override fun completeDeathAnimation(unit: BattleDeathPresentationTimeline.DeathUnit) {
            events += "complete:${unit.unitId}"
        }
        override fun completeCheckpoint(checkpoint: BattleDeathPresentationTimeline.Checkpoint) {
            events += "checkpoint:$checkpoint"
        }
    }
}
