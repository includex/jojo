// Test
package com.jojo.game

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.application.battle.*

import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** BattleCommandFlowTest: BattleCommandFlow의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleCommandFlowTest {
    @Test
    fun `exchange requires same effective camp rather than same weapon or allied side`() {
        val actor = BattleUnit("cao", "조조", Faction.PLAYER, 10, 5, armId = 0)
        val friend = BattleUnit("liu", "유비", Faction.FRIEND, 9, 5, armId = 0)
        val mine = BattleUnit("mine", "아군", Faction.PLAYER, 11, 5, armId = 21)
        val offsets = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
        assertFalse(BattleCommandFlow.canSwap(actor, listOf(actor, friend), offsets))
        assertTrue(BattleCommandFlow.canSwap(actor, listOf(actor, mine), offsets))
        mine.statuses[BattleStatus.LOST] = 1
        assertFalse(BattleCommandFlow.canSwap(actor, listOf(mine), offsets))
        mine.statuses.clear()
        mine.hitPoints = 0
        assertFalse(BattleCommandFlow.canSwap(actor, listOf(mine), offsets))
    }

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
        assertEquals(8, buttons.size)
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
    fun `command cards have one bounded icon and distinct info hit target`() {
        assertEquals(8, BattleCommandRenderModel.visuals.size)
        BattleCommandRenderModel.visuals.forEachIndexed { tag, visual ->
            assertTrue(visual.icons.size <= 1)
            visual.icons.forEach { icon ->
                assertTrue(icon.x >= visual.x && icon.x + icon.width < visual.labelX)
                assertTrue(icon.y >= visual.y && icon.y + icon.height <= visual.y + visual.height)
            }
            val center=BattleCommandRenderModel.buttonCenter(tag,0f to 0f)
            assertEquals(tag,BattleCommandRenderModel.tagAt(center.first,center.second))
        }
    }
    @Test
    fun `command panel stays inside viewport at each corner`() {
        for (x in listOf(0f,1488f)) for(y in listOf(0f,800f)) {
            val offset=BattleCommandRenderModel.placementOffset(x,y,1488f,800f)
            val left=BattleCommandRenderModel.PANEL_LEFT+offset.first
            val bottom=BattleCommandRenderModel.PANEL_BOTTOM+offset.second
            assertTrue(left>=0f && bottom>=0f)
            assertTrue(left+BattleCommandRenderModel.PANEL_WIDTH<=1488f)
            assertTrue(bottom+BattleCommandRenderModel.PANEL_HEIGHT<=800f)
        }
    }
    @Test
    fun `viewing info preserves pending move and returns to command`() {
        val flow=BattleCommandFlow()
        flow.beginMove("lin",before)
        flow.movementCompleted(after,0)
        assertEquals(BattleCommandFlow.Result.OpenChild(BattleCommandFlow.Command.INFO),flow.touch(7,BattleCommandFlow.TOUCH_END))
        flow.childCancelled()
        assertEquals(BattleCommandFlow.Phase.COMMAND,flow.phase)
        assertEquals(before,assertIs<BattleCommandFlow.Result.Rollback>(flow.touch(6,BattleCommandFlow.TOUCH_END)).pose)
    }

    @Test
    fun `translated command centers and hit testing use the same placement`() {
        val offset = 500f to 200f
        val attack = BattleCommandRenderModel.buttonCenter(0, offset)
        val wait = BattleCommandRenderModel.buttonCenter(5, offset)

        assertEquals(0, BattleCommandRenderModel.tagAt(attack.first, attack.second, offset))
        assertEquals(5, BattleCommandRenderModel.tagAt(wait.first, wait.second, offset))
        assertNull(BattleCommandRenderModel.tagAt(attack.first, attack.second))
    }

}
