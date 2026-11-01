package com.jojo.game

import com.jojo.game.presentation.battle.overlay.BattleForcesOverlayController
import com.jojo.game.presentation.battle.overlay.BattleHelperOverlayController
import com.jojo.game.presentation.shared.overlay.HelperLayer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BattleOverlayCancellationTest {
    @Test fun `keyboard dismissal closes forces without selecting a unit`() {
        val overlay = BattleForcesOverlayController()
        overlay.open(emptyList(), emptyList(), 0)
        assertTrue(overlay.isVisible())
        val result = overlay.dispatch(BattleForcesOverlayController.Intent.Dismiss)
        assertTrue(result.effect is BattleForcesOverlayController.Effect.Closed)
        assertFalse(overlay.isVisible())
        assertFalse(overlay.dispatch(BattleForcesOverlayController.Intent.Dismiss).consumed)
    }

    @Test fun `keyboard dismissal closes helper using its button lifecycle`() {
        val overlay = BattleHelperOverlayController()
        overlay.open(object : HelperLayer.Model {
            override fun getInfo() = emptyList<HelperLayer.Info>()
            override fun replaceSpeInfo(text: String, flags: Int) = text
        })
        assertTrue(overlay.dispatch(BattleHelperOverlayController.Intent.Dismiss))
        assertNull(overlay.view())
        assertFalse(overlay.dispatch(BattleHelperOverlayController.Intent.Dismiss))
    }
}
