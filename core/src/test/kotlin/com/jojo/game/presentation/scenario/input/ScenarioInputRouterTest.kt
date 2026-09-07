// Test
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
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(ScenarioInputRouter.Touch.SelectAndConfirm(1), ScenarioInputRouter.choiceTouch(true, 2, 700f, 320f))
    }

    @Test fun `scrolled choice window maps rows back to their source option`() {
        // 원본 ChooseLayer의 view에는 항목이 세 개만 들어가고, 네 번째부터는 스크롤로 닿는다.
        assertEquals(0, ScenarioInputRouter.firstVisibleChoiceIndex(4, 0))
        assertEquals(0, ScenarioInputRouter.firstVisibleChoiceIndex(4, 2))
        assertEquals(1, ScenarioInputRouter.firstVisibleChoiceIndex(4, 3))
        assertEquals(0, ScenarioInputRouter.firstVisibleChoiceIndex(3, 2))
        assertEquals(3, ScenarioInputRouter.firstVisibleChoiceIndex(6, 5))
        // 창이 한 칸 밀리면 첫 줄을 눌러도 두 번째 선택지가 확정되어야 한다.
        assertEquals(
            ScenarioInputRouter.Touch.SelectAndConfirm(1),
            ScenarioInputRouter.choiceTouch(false, 4, 600f, 395f, firstVisibleIndex = 1),
        )
        assertEquals(
            ScenarioInputRouter.Touch.SelectAndConfirm(3),
            ScenarioInputRouter.choiceTouch(false, 4, 600f, 310f, firstVisibleIndex = 1),
        )
        // 창 아래로는 항목이 없으므로 아무것도 고르지 않는다.
        assertEquals(
            ScenarioInputRouter.Touch.None,
            ScenarioInputRouter.choiceTouch(false, 4, 600f, 265f, firstVisibleIndex = 1),
        )
    }
}
