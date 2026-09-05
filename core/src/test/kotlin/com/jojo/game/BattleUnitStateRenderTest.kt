package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `BattleUnitStateRenderTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleUnitStateRenderTest {
    @Test fun `render command follows scaled child positions and source draw order`() {
        val effect = BattleUnitStateAnimation.Effect(listOf(0, 2))
        assertEquals(
            BattleUnitStateRender.Command(0, 48f, 96f, 32f, 32f),
            BattleUnitStateRender.command(effect, 0f, 48f, 32f, 96f),
        )
        assertEquals(
            BattleUnitStateRender.Command(2, 112f, 96f, 32f, 32f),
            BattleUnitStateRender.command(effect, 1f / 3f, 48f, 32f, 96f),
        )
    }

    @Test fun `hidden state node emits no render command`() {
        assertNull(BattleUnitStateRender.command(BattleUnitStateAnimation.Effect(listOf(1, 1), active = false), 0f, 0f, 0f, 96f))
        assertNull(BattleUnitStateRender.command(null, 0f, 0f, 0f, 96f))
    }

    @Test fun `render log records source status node asset geometry and order`() {
        val command = BattleUnitStateRender.Command(3, 112f, 96f, 32f, 32f)
        val log = BattleUnitStateRender.jsonl(command, frame = 7)
        assertTrue(log.contains("\"frame\":7"))
        assertTrue(log.contains("\"nodePath\":\"Canvas/Layer/ScrollView/view/content/map/unit/status\""))
        assertTrue(log.contains("\"x\":112.000,\"y\":96.000,\"w\":32.000,\"h\":32.000"))
        assertTrue(log.contains("\"assetId\":\"maps/ui/battle-status/state_3.png\""))
        assertEquals("after-unit-info-before-harm-number", command.order)
    }

    @Test fun `ordinary state animation advances while battle script clock is paused`() {
        val effect = BattleUnitStateAnimation.Effect(listOf(0, 2))
        val pausedBattleClock = 0f
        val first = requireNotNull(BattleUnitStateRender.command(effect, pausedBattleClock, 0f, 0f, 96f))
        // StageLayer.pause only pauses PyManager; the visual elapsed clock
        // still reaches the next cc.Animation frame after one third second.
        val visualElapsedDuringDialogue = 1f / 3f
        val next = requireNotNull(BattleUnitStateRender.command(effect, visualElapsedDuringDialogue, 0f, 0f, 96f))
        assertEquals(0, first.textureIndex)
        assertEquals(2, next.textureIndex)
        assertEquals(0f, first.x)
        assertEquals(64f, next.x)
    }
}
