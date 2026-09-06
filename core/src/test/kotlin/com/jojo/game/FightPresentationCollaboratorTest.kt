// Test
package com.jojo.game

import com.jojo.game.presentation.battle.fight.FightActionPose
import com.jojo.game.presentation.battle.fight.FightPresentationEvent
import com.jojo.game.presentation.battle.fight.FightSide
import com.jojo.game.presentation.battle.fight.FightUnitPresentation
import com.jojo.game.presentation.battle.fight.FightActionTimeline
import com.jojo.game.presentation.battle.fight.FightSpeechLifecycle
import com.jojo.game.presentation.battle.fight.FightSpriteTimeline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class FightPresentationCollaboratorTest {
    @Test
    fun `action timeline samples pose and emits source sound crossings in order`() {
        val timeline = FightActionTimeline(
            durations = mapOf(4 to 1f),
            hitTimes = emptyMap(),
            poseAt = { _, elapsed -> FightActionPose(childX = elapsed * 10f, childScaleX = -1f) },
            soundsCrossed = { _, from, to, includeStart ->
                listOfNotNull(if (includeStart && from == 0f && to >= 0f) FightSpriteTimeline.SoundEvent(0f, "start") else null,
                    if (to >= .5f) FightSpriteTimeline.SoundEvent(.5f, "hit") else null)
            },
        )
        val fighter = FightUnitPresentation(parentScaleX = -1f, childX = 2f, childScaleX = -1f)

        assertIs<FightPresentationEvent.ActionStarted>(timeline.start(FightSide.MINE, fighter, 0f, 4))
        val sounds = timeline.advance(.5f) { fighter }

        assertEquals(listOf("start", "hit"), sounds.map { it.value })
        assertEquals(5f, fighter.childX)
        assertEquals(-1f, fighter.childScaleX)
        assertEquals(-8f, fighter.parentX, "start folds the previous child transform before the next pose")
    }

    @Test
    fun `speech lifecycle keeps markup grouping and emits rendered updates`() {
        val speech = FightSpeechLifecycle()
        speech.begin(FightSide.ENEMY, "<color=#fff>가</color>")
        val update = speech.applyContent(FightSide.ENEMY, "<color=#fff>가</color>")

        assertEquals(listOf("<color=#fff>", "<color=#fff>가", "<color=#fff>가</color>"),
            FightSpeechLifecycle.typingContents("<color=#fff>가</color>"))
        assertEquals("<color=#fff>가</color></c>", update.renderedText)
        assertEquals(update.renderedText, speech.enemy.renderedText)
    }
}
