// Battle Dialogue
package com.jojo.game.presentation.battle.dialogue

import kotlin.test.Test
import kotlin.test.assertEquals

/** 전투 SayLayer 배치 정책: 원본 화자 기준 상·하단 배치와 구성 요소 동행 이동을 검증한다. */
class BattleDialoguePlacementPolicyTest {
    /** 화면 아래 화자는 원본 `_resetPos` 규칙에 따라 대화창을 화자 위에 둔다. */
    @Test
    fun `lower screen speaker places panel above the unit`() {
        val placement = BattleDialoguePlacementPolicy.place(speakerScreenCenterY = 300f, viewportHeight = 800f)

        assertEquals(380f, placement.panelY)
        assertEquals(378f, placement.portraitY)
        assertEquals(569.4f, placement.speakerBaselineY)
        assertEquals(479.814f, placement.textBaselineY)
    }

    /** 화면 위 화자는 원본 `_resetPos` 규칙에 따라 대화창을 화자 아래에 둔다. */
    @Test
    fun `upper screen speaker places panel below the unit`() {
        val placement = BattleDialoguePlacementPolicy.place(speakerScreenCenterY = 500f, viewportHeight = 800f)

        assertEquals(208f, placement.panelY)
        assertEquals(206f, placement.portraitY)
        assertEquals(397.4f, placement.speakerBaselineY)
        assertEquals(307.814f, placement.textBaselineY)
    }

    /** 화자가 없으면 기존 원본 캡처와 같은 기본 대화창 좌표를 유지한다. */
    @Test
    fun `missing speaker retains legacy capture position`() {
        val placement = BattleDialoguePlacementPolicy.place(speakerScreenCenterY = null, viewportHeight = 800f)

        assertEquals(245.65f, placement.panelX)
        assertEquals(282f, placement.panelY)
    }
}
