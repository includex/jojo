package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattleUnitMoveTimelineTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleUnitMoveTimelineTest {
    @Test
    fun `move2 groups same-direction edges and starts a new movement action on turn`() {
        val timeline = BattleUnitMoveTimeline.schedule(
            path = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2),
            fastMove = true,
        )
        assertEquals(.08f, timeline.secondsPerTile)
        assertEquals(
            listOf(
                BattleUnitMoveTimeline.Segment(2, 0, 2, 0f, .16f),
                BattleUnitMoveTimeline.Segment(1, 2, 4, .16f, .16f),
            ),
            timeline.segments,
        )
        assertEquals(.42f, timeline.idleAt)
        assertEquals(listOf(.08f, .16f, .24f, .32f), timeline.movementTicks)
    }

    @Test
    fun `slow move doubles only source movement duration and cadence`() {
        val timeline = BattleUnitMoveTimeline.schedule(listOf(0 to 0, 1 to 0, 2 to 0), fastMove = false)
        assertEquals(.16f, timeline.secondsPerTile)
        assertEquals(listOf(BattleUnitMoveTimeline.Segment(1, 0, 2, 0f, .32f)), timeline.segments)
        assertEquals(.42f, timeline.idleAt)
        assertEquals(listOf(.16f, .32f), timeline.movementTicks)
    }

    @Test
    fun `move2 sample holds each direction segment then destination delay`() {
        val path = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2)
        val timeline = BattleUnitMoveTimeline.schedule(path, fastMove = true)

        val first = BattleUnitMoveTimeline.sample(path, timeline, .08f)
        assertEquals(0f, first.x, .00001f); assertEquals(1f, first.y, .00001f)
        assertEquals(2, first.direction); assertEquals(true, first.moving)
        val turned = BattleUnitMoveTimeline.sample(path, timeline, .20f)
        assertEquals(.5f, turned.x, .00001f); assertEquals(2f, turned.y, .00001f)
        assertEquals(1, turned.direction); assertEquals(true, turned.moving)
        val delay = BattleUnitMoveTimeline.sample(path, timeline, .35f)
        assertEquals(2f, delay.x, .00001f); assertEquals(2f, delay.y, .00001f)
        assertEquals(1, delay.direction); assertEquals(false, delay.moving)
    }
}
