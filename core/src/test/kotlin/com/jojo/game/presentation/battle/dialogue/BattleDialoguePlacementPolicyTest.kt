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

        // 원본이 옮기는 노드는 bg0(높이 260)이고 패널 bg2 는 그 안에서 y=-12 에 있다.
        assertEquals(392f, placement.panelY)
        assertEquals(390f, placement.portraitY)
        assertEquals(581.4f, placement.speakerDrawY)
        assertEquals(526.501f, placement.textDrawY)
    }

    /** 화면 위 화자는 원본 `_resetPos` 규칙에 따라 대화창을 화자 아래에 둔다. */
    @Test
    fun `upper screen speaker places panel below the unit`() {
        val placement = BattleDialoguePlacementPolicy.place(speakerScreenCenterY = 500f, viewportHeight = 800f)

        assertEquals(172f, placement.panelY)
        assertEquals(170f, placement.portraitY)
        assertEquals(361.4f, placement.speakerDrawY)
        assertEquals(306.501f, placement.textDrawY)
    }

    /** 화자가 없으면 기존 원본 캡처와 같은 기본 대화창 좌표를 유지한다. */
    @Test
    fun `missing speaker retains legacy capture position`() {
        val placement = BattleDialoguePlacementPolicy.place(speakerScreenCenterY = null, viewportHeight = 800f)

        assertEquals(245.65f, placement.panelX)
        assertEquals(282f, placement.panelY)
    }
    @Test
    fun `authored fixed placement controls all dialogue components`() {
        val layout = com.jojo.game.presentation.shared.dialogue.DialogueRenderLayout(
            panelRightX = 100f, panelY = 80f, panelWidth = 700f, panelHeight = 220f,
            portraitRightX = 900f, portraitOffsetY = 12f, speakerRightX = 150f,
            speakerOffsetY = 190f, textRightX = 160f, textOffsetY = 140f, textWidth = 600f,
        )
        val result = BattleDialoguePlacementPolicy.place(500f, 800f, layout, false)
        assertEquals(100f, result.panelX)
        assertEquals(80f, result.panelY)
        assertEquals(900f, result.portraitX)
        assertEquals(92f, result.portraitY)
        assertEquals(150f, result.speakerX)
        assertEquals(270f, result.speakerDrawY)
        assertEquals(160f, result.textX)
        assertEquals(220f, result.textDrawY)
        assertEquals(600f, result.textWidth)
    }
}
