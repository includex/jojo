package com.jojo.game

import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * class  `BattleCommandFlowTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleCommandFlowTest {
    private val before = BattleCommandFlow.UnitPose(3, 4, 2)
    private val after = BattleCommandFlow.UnitPose(5, 4, 1)

    @Test
    fun `move callback stage end blocks CommandLayer with or without an outcome`() {
        assertFalse(BattleMoveScriptContinuation.shouldOpenCommand(PlaybackState.COMPLETE, battleEndedByScript = true))
        assertTrue(BattleMoveScriptContinuation.shouldOpenCommand(PlaybackState.COMPLETE, battleEndedByScript = false))

        val flow = BattleCommandFlow()
        flow.beginMove("mine", before)
        flow.abandonMoveForScriptEnd()

        assertEquals(BattleCommandFlow.Phase.IDLE, flow.phase)
        assertIs<BattleCommandFlow.Result.Ignored>(flow.touch(5, BattleCommandFlow.TOUCH_END))
    }

    @Test
    fun `command opens only after movement and applies five-bit capability mask`() {
        val flow = BattleCommandFlow()
        flow.beginMove("cao-cao", before)
        assertEquals(BattleCommandFlow.Phase.MOVING, flow.phase)
        flow.movementCompleted(after, BattleCommandFlow.ATTACK_BIT or BattleCommandFlow.PROPERTY_BIT)

        val buttons = flow.view()
        assertEquals(7, buttons.size)
        assertTrue(buttons[0].interactable)
        assertFalse(buttons[1].interactable)
        assertTrue(buttons[2].interactable)
        assertFalse(buttons[3].interactable)
        assertFalse(buttons[4].interactable)
        assertTrue(buttons[5].interactable)
        assertTrue(buttons[6].interactable)
        assertTrue(buttons[1].grayscale)
    }

    @Test
    fun `magic and property use command child route and cancellation reopens command`() {
        val flow = BattleCommandFlow()
        flow.beginMove("cao-cao", before)
        flow.movementCompleted(after, BattleCommandFlow.MAGICK_BIT or BattleCommandFlow.PROPERTY_BIT)

        assertEquals(
            BattleCommandFlow.Result.OpenChild(BattleCommandFlow.Command.MAGICK),
            flow.touch(1, BattleCommandFlow.TOUCH_END),
        )
        flow.childCancelled()
        assertEquals(BattleCommandFlow.Phase.COMMAND, flow.phase)
        assertEquals(
            BattleCommandFlow.Result.OpenChild(BattleCommandFlow.Command.PROPERTY),
            flow.touch(2, BattleCommandFlow.TOUCH_END),
        )
    }

    @Test
    fun `physical target completion consumes attack command child`() {
        val flow = BattleCommandFlow()
        flow.beginMove("cao-cao", before)
        flow.movementCompleted(after, BattleCommandFlow.ATTACK_BIT)

        assertEquals(
            BattleCommandFlow.Result.OpenChild(BattleCommandFlow.Command.ATTACK),
            flow.touch(0, BattleCommandFlow.TOUCH_END),
        )
        assertEquals(
            BattleCommandFlow.Result.Commit(BattleCommandFlow.Command.ATTACK),
            flow.childCompleted(consumesAction = true),
        )
        assertEquals(BattleCommandFlow.Phase.COMMITTED, flow.phase)
    }

    @Test
    fun `disabled and non-end touches do not dispatch while wait commits`() {
        val flow = BattleCommandFlow()
        flow.beginMove("cao-cao", before)
        flow.movementCompleted(after, 0)
        assertIs<BattleCommandFlow.Result.Ignored>(flow.touch(0, BattleCommandFlow.TOUCH_END))
        assertIs<BattleCommandFlow.Result.Ignored>(flow.touch(5, 1))
        assertEquals(
            BattleCommandFlow.Result.Commit(BattleCommandFlow.Command.WAIT),
            flow.touch(5, BattleCommandFlow.TOUCH_END),
        )
        assertEquals(BattleCommandFlow.Phase.COMMITTED, flow.phase)
    }

    @Test
    fun `button6 and panel cancel restore exact pre-move pose`() {
        val flow = BattleCommandFlow()
        flow.beginMove("cao-cao", before)
        flow.movementCompleted(after, 0x1f)
        val result = assertIs<BattleCommandFlow.Result.Rollback>(
            flow.touch(6, BattleCommandFlow.TOUCH_END),
        )
        assertEquals("cao-cao", result.unitId)
        assertEquals(before, result.pose)
        assertEquals(BattleCommandFlow.Phase.ROLLED_BACK, flow.phase)

        val nodes = BattleCommandRenderModel.nodes(flow.view())
        assertEquals(2, nodes.last().listenerPriority)
        assertEquals("Canvas/Layer/Panel_cancel", nodes.last().path)
    }

    @Test
    fun `command renderer preserves authored dual icons and trimmed source extents`() {
        assertEquals(200f / 255f, BattleCommandRenderModel.PANEL_OPACITY)
        assertEquals(10f / 255f, BattleCommandRenderModel.DISMISS_DIM_OPACITY)
        assertEquals(160f / 255f, BattleCommandRenderModel.DISABLED_COMPONENT)

        val attack = BattleCommandRenderModel.visuals[0]
        assertEquals(listOf("command1", "command1"), attack.icons.map { it.asset })
        assertEquals(749.6f, attack.icons[0].x)
        assertEquals(825.6f, attack.icons[1].x)

        val item = BattleCommandRenderModel.visuals[2]
        assertEquals(listOf(30f, 30f), item.icons.map { it.width })
        val swap = BattleCommandRenderModel.visuals[3]
        assertEquals(listOf(28f, 28f), swap.icons.map { it.height })
        assertTrue(BattleCommandRenderModel.visuals[6].icons.isEmpty())
    }
}
