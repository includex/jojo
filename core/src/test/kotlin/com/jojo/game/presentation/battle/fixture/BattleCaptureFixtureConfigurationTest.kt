// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleActionSample
import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 검증 전용 runtime route가 capture 설정으로만 해석되는지 검증한다. */
class BattleCaptureFixtureConfigurationTest {
    @Test
    fun `action and dialogue fixture values stay outside the UI route policy`() {
        val configuration = BattleCaptureFixtureConfiguration(
            RuntimeBattlePresentation(
                route = RuntimeBattleRoute.CUTSCENE_477,
                actionSample = RuntimeBattleActionSample(25, 12f / 24f),
                dialogueStep = 2,
            ),
        )

        assertEquals(RuntimeBattleActionSample(25, 12f / 24f), configuration.actionSample)
        assertTrue(configuration.actionSampleMode)
        assertTrue(configuration.cutsceneCapture)
        assertTrue(configuration.cutscene477Capture)
        assertEquals(2, configuration.dialogueStepCapture)
        assertFalse(configuration.mapOnlyCapture)
    }

    @Test
    fun `map selection and modal routes are capture-only flags`() {
        assertTrue(BattleCaptureFixtureConfiguration(RuntimeBattlePresentation(RuntimeBattleRoute.MAP_ONLY)).mapOnlyCapture)
        assertTrue(BattleCaptureFixtureConfiguration(RuntimeBattlePresentation(RuntimeBattleRoute.SELECTION)).selectionOverlayCapture)

        val modal = BattleCaptureFixtureConfiguration(RuntimeBattlePresentation(RuntimeBattleRoute.MODAL_SAVE))
        assertEquals(RuntimeBattleRoute.MODAL_SAVE, modal.initialModalRoute)
        assertTrue(modal.modalRenderCapture)
    }
}
