// Test
package com.jojo.game

import com.jojo.game.presentation.battle.*
import com.jojo.game.presentation.battle.fight.*
import com.jojo.game.domain.scenario.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** FightPresentationViewTest: FightPresentationView의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class FightPresentationViewTest {
    @Test
    fun `render snapshot is unaffected by later presentation mutations`() {
        val state = FightPresentationState()
        state.begin(ScenarioFightCommand.Start(7L, 3, 134, 5, 2))
        state.mine.created = true
        state.mine.action = 17
        state.mine.actionElapsedSeconds = 0.125f
        state.mine.parentX = -180f
        state.mineSpeech.active = true
        state.mineSpeech.renderedText = "before"

        val snapshot = state.renderSnapshot()
        state.mine.created = false
        state.mine.action = 21
        state.mine.actionElapsedSeconds = 0.25f
        state.mine.parentX = 80f
        state.mineSpeech.active = false
        state.mineSpeech.renderedText = "after"

        assertTrue(snapshot.mine.created)
        assertEquals(17, snapshot.mine.action)
        assertEquals(0.125f, snapshot.mine.actionElapsedSeconds)
        assertEquals(-180f, snapshot.mine.parentX)
        assertTrue(snapshot.mineSpeech.active)
        assertEquals("before", snapshot.mineSpeech.renderedText)
    }

    @Test
    fun `builder preserves authored slot side identity and resolved presentation values`() {
        val state = FightPresentationState(isMineUnit = { false })
        state.begin(ScenarioFightCommand.Start(9L, 3, 134, 5, 2))
        state.mine.created = true
        state.mine.action = 17
        state.mineSpeech.active = true
        state.mineSpeech.renderedText = "mine"
        val snapshot = state.renderSnapshot()

        val view = FightPresentationViewBuilder.build(
            snapshot,
            mineIdentity = FightUnitRenderIdentity("관우", "관우", portraitFaceId = 40, avatarId = 74),
            enemyIdentity = FightUnitRenderIdentity("화웅", "화웅", portraitFaceId = 90, avatarId = 93),
        )

        assertEquals(FightSide.ENEMY, view.fighterAt(0).side)
        assertEquals("화웅", view.fighterAt(0).name)
        assertEquals(93, view.fighterAt(0).avatarId)
        assertEquals(FightSide.MINE, view.fighterAt(1).side)
        assertEquals("관우", view.fighterAt(1).name)
        assertEquals(40, view.fighterAt(1).portraitFaceId)
        assertTrue(view.fighterAt(1).speech.active)
        assertEquals("mine", view.fighterAt(1).speech.renderedText)
        assertFalse(view.fighterAt(0).speech.active)
        assertFailsWith<IllegalStateException> { view.fighterAt(2) }
    }
}
