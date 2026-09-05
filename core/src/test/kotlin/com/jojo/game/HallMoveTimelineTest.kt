package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class HallMoveTimelineTest {
    private fun close(expected: Float, actual: Float) = assertEquals(expected, actual, .0001f)

    @Test fun `straight source moveTo interpolates continuously between forty millisecond grid steps`() {
        val path = (45..52).map { it to 48 }
        val sample = HallMoveTimeline.sample(path, .02f)
        close(45.5f, sample.x)
        close(48f, sample.y)
        assertEquals(1, sample.direction)
        // _moveDuring has not fired yet: z remains at the origin.
        close(-52f, sample.zIndex)
    }

    @Test fun `source direction run includes the first corner point in the preceding moveTo`() {
        val path = listOf(45 to 48, 46 to 48, 47 to 48, 47 to 49, 47 to 50)
        val segments = HallMoveTimeline.segments(path)
        assertEquals(2, segments.size)
        close(.08f, segments[0].duration)
        close(47f, segments[0].toX)
        close(49f, segments[0].toY)
        assertEquals(1, HallMoveTimeline.sample(path, .079f).direction)
        assertEquals(2, HallMoveTimeline.sample(path, .081f).direction)
    }

    @Test fun `z order advances only on source forty millisecond scheduler ticks`() {
        val path = (45..52).map { it to 48 }
        val before = HallMoveTimeline.sample(path, .039f)
        val after = HallMoveTimeline.sample(path, .04f)
        close(-52f, before.zIndex)
        close(-48f, after.zIndex)
        close(45.975f, before.x)
    }
}
