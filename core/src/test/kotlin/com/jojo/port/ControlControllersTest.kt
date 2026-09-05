package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ControlControllersTest {
    private class Port : ControlControllerPort {
        var here = Control.Point(2, 2)
        var paralyzed = false
        var surrounded = false
        var mine = false
        var retreat: ControlTransition? = null
        var attack = false
        var nearest: ControlTarget? = null
        var targetValue: ControlTarget? = null
        var centre: Control.Point? = null
        var destination: Control.Point? = null
        var near: Control.Point? = null
        var blocked: Int? = null
        var choice: Control.Result? = null
        var choiceMode = -1
        var persisted = -1
        override fun currentPoint() = here
        override fun isParalyzed() = paralyzed
        override fun isSurrounded() = surrounded
        override fun isMine() = mine
        override fun setPersistentAi(ai: Int) { persisted = ai }
        override fun target(index: Int) = targetValue?.takeIf { it.index == index }
        override fun hasAttackTargets(targetIndex: Int?) = attack
        override fun exhaustedRetreat() = retreat
        override fun nearestOpponent() = nearest
        override fun winRectCentre() = centre
        override fun destinationPoint(target: Control.Point) = destination
        override fun nearPoint(target: Control.Point) = near
        override fun blockingEnemy(target: Control.Point) = blocked
        override fun chooseAi(mode: Int): Control.Result? { choiceMode = mode; return choice }
    }

    @Test fun `factory preserves BattleLayer controls array order`() {
        assertIs<CtrlBDCJ>(ControlControllerFactory.create(0))
        assertIs<CtrlZDCJ>(ControlControllerFactory.create(1))
        assertIs<CtrlJSYD>(ControlControllerFactory.create(2))
        assertIs<CtrlGJWJ>(ControlControllerFactory.create(3))
        assertIs<CtrlDZDD>(ControlControllerFactory.create(4))
        assertIs<CtrlGSWJ>(ControlControllerFactory.create(5))
        assertIs<CtrlTZZDD>(ControlControllerFactory.create(6))
        assertIs<CtrlYDDZDDJS>(ControlControllerFactory.create(7))
        assertIs<CtrlYDDZDDBM>(ControlControllerFactory.create(8))
        assertIs<CtrlYDDZDDGJ>(ControlControllerFactory.create(9))
    }

    @Test fun `base process1 re-enters hold before derived AI except hold`() {
        val port = Port().apply { paralyzed = true }
        val step = CtrlBDCJ().step(port, ControlData())
        assertEquals(1, step.status)
        assertEquals(ControlAi.HOLD, step.transition!!.ai)
        assertEquals(Control.Result(2, 2), step.result)
        assertEquals(0, CtrlJSYD().step(port, ControlData()).status)
    }

    @Test fun `passive stops only when no attack target after retreat check`() {
        val port = Port()
        assertEquals(2, CtrlBDCJ().step(port, ControlData()).status)
        port.retreat = ControlTransition(ControlAi.RETREAT_TO, ControlData(target = Control.Point(9, 9)))
        assertEquals(ControlAi.RETREAT_TO, CtrlBDCJ().step(port, ControlData()).transition!!.ai)
    }

    @Test fun `active seeks nearest enemy via attack move and objective centre`() {
        val port = Port().apply { nearest = ControlTarget(8, Control.Point(7, 5), false, 8); near = Control.Point(5, 4) }
        val seek = CtrlZDCJ().step(port, ControlData())
        assertEquals(ControlAi.MOVE_ATTACK, seek.transition!!.ai)
        assertEquals(Control.Point(5, 4), seek.transition!!.data.target)
        port.nearest = null; port.near = null; port.mine = true; port.centre = Control.Point(11, 12)
        val objective = CtrlZDCJ().step(port, ControlData())
        assertEquals(ControlAi.GO_TO, objective.transition!!.ai)
        assertEquals(Control.Point(11, 12), objective.transition!!.data.target)
    }

    @Test fun `go and retreat controllers persist passive at destination`() {
        val port = Port()
        val at = ControlData(target = Control.Point(2, 2))
        listOf(CtrlDZDD(), CtrlTZZDD()).forEach { controller ->
            val step = controller.step(port, at)
            assertEquals(1, step.status)
            assertEquals(ControlAi.PASSIVE, step.transition!!.ai)
            assertEquals(ControlAi.PASSIVE, port.persisted)
        }
    }

    @Test fun `go uses attack destination but retreat uses magic destination`() {
        val port = Port().apply { destination = Control.Point(4, 4) }
        val go = CtrlDZDD().step(port, ControlData(target = Control.Point(8, 8)))
        assertEquals(ControlAi.MOVE_ATTACK, go.transition!!.ai)
        val retreat = CtrlTZZDD().step(port, ControlData(target = Control.Point(8, 8)))
        assertEquals(ControlAi.MOVE_MAGIC, retreat.transition!!.ai)
    }

    @Test fun `retreat falls through to source ganlu route when remote target is not directly reachable`() {
        val port = Port().apply {
            destination = null
            near = Control.Point(5, 4)
        }

        val retreat = CtrlTZZDD().step(port, ControlData(target = Control.Point(0, 2)))

        assertEquals(ControlAi.MOVE_MAGIC, retreat.transition!!.ai)
        assertEquals(Control.Point(5, 4), retreat.transition!!.data.target)
    }

    @Test fun `attack unit resets missing target and holds nearby friend`() {
        val port = Port()
        val missing = CtrlGJWJ().step(port, ControlData(4))
        assertEquals(ControlAi.ACTIVE, missing.transition!!.ai)
        assertEquals(ControlAi.ACTIVE, port.persisted)
        port.mine = true
        port.targetValue = ControlTarget(4, Control.Point(3, 2), true, 2)
        assertEquals(ControlAi.PASSIVE, CtrlGJWJ().step(port, ControlData(4)).transition!!.ai)
    }

    @Test fun `follow resets missing target and otherwise approaches with attack move`() {
        val port = Port().apply { targetValue = ControlTarget(4, Control.Point(8, 2), true, 6); near = Control.Point(6, 2) }
        val step = CtrlGSWJ().step(port, ControlData(4))
        assertEquals(ControlAi.MOVE_ATTACK, step.transition!!.ai)
        assertEquals(Control.Point(6, 2), step.transition!!.data.target)
    }

    @Test fun `magic controller passes source flag 2 and target score only matches`() {
        val port = Port().apply { choice = Control.Result(3, 3) }
        assertEquals(0, CtrlYDDZDDBM().step(port, ControlData()).status)
        assertEquals(2, port.choiceMode)
        val controller = CtrlYDDZDDGJ()
        assertEquals(30, controller.targetScore(5, ControlData(5), 30))
        assertEquals(0, controller.targetScore(4, ControlData(5), 30))
        assertNull(CtrlYDDZDDJS().step(Port(), ControlData()).result)
    }
}
