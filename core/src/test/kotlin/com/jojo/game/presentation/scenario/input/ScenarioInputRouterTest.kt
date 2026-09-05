package com.jojo.game.presentation.scenario.input

import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioInputRouterTest {
    private fun hallState(
        feats: Boolean = false,
        save: Boolean = false,
        exclusive: Boolean = false,
        management: ScenarioInputRouter.Management? = null,
        unitListOpen: Boolean = false,
    ) = ScenarioInputRouter.HallState(
        completeMenu = true, feats = feats, unitInfo = false, magic = false, item = false,
        save = save, info = false, exclusive = exclusive, management = management, unitListOpen = unitListOpen,
    )

    @Test fun `hall layers keep source priority over management and main menu`() {
        assertEquals(
            ScenarioInputRouter.Touch.Hall(ScenarioInputRouter.HallLayer.FEATS),
            ScenarioInputRouter.hallTouch(hallState(feats = true, management = ScenarioInputRouter.Management.BUY), 600f, 60f),
        )
        assertEquals(
            ScenarioInputRouter.Touch.Hall(ScenarioInputRouter.HallLayer.SAVE),
            ScenarioInputRouter.hallTouch(hallState(save = true, exclusive = true), 700f, 300f),
        )
    }

    @Test fun `unit list consumes equip close target before underlying equip controls`() {
        assertEquals(
            ScenarioInputRouter.Touch.Hall(ScenarioInputRouter.HallLayer.MANAGEMENT),
            ScenarioInputRouter.hallTouch(hallState(management = ScenarioInputRouter.Management.EQUIP, unitListOpen = true), 680f, 50f),
        )
    }

    @Test fun `choice rows and ask buttons preserve selection bounds`() {
        assertEquals(ScenarioInputRouter.Touch.SelectAndConfirm(1), ScenarioInputRouter.choiceTouch(false, 3, 600f, 350f))
        assertEquals(ScenarioInputRouter.Touch.SelectAndConfirm(0), ScenarioInputRouter.choiceTouch(true, 2, 500f, 320f))
        // 700 is inside the source ask dialog's second-button hitbox.
        assertEquals(ScenarioInputRouter.Touch.SelectAndConfirm(1), ScenarioInputRouter.choiceTouch(true, 2, 700f, 320f))
    }
}
