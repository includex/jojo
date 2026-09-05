package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * class  `FightSpriteTimelineTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class FightSpriteTimelineTest {
    @Test
    fun `animeFR sprite rows and linear child transforms are sampled from source data`() {
        val timeline = FightSpriteTimeline.fromJson(
            """{"anime2":[
              {"frame":4,"sprite":{"t":1,"idx":4},"props":{"position":[[0,0,0],1]}},
              {"frame":6,"sprite":{"t":1,"idx":5},"props":{"position":[[-50,10,0],1],"scaleX":[-1],"opacity":[127]}}
            ]}""",
        )

        val halfway = requireNotNull(timeline.frame(2, 2f / 24f))
        assertEquals(UnitSpriteSource.MOVEMENT, halfway.source)
        assertEquals(201, halfway.sourceY)
        assertClose(-25f, halfway.pose.childX)
        assertClose(5f, halfway.pose.childY)

        val final = requireNotNull(timeline.frame(2, 9f / 24f))
        assertEquals(251, final.sourceY)
        assertClose(-50f, final.pose.childX)
        assertClose(-1f, final.pose.childScaleX)
        assertClose(127f, final.pose.opacity)
        assertTrue(timeline.duration(2) == 10f / 24f)
    }

    @Test
    fun `callback1 sound events cross their authored frame exactly once`() {
        val timeline = FightSpriteTimeline.fromJson(
            """{"anime9":[
              {"frame":2,"events":{"1":["yidong",8]}},
              {"frame":3,"events":{"1":[7]}},
              {"frame":1}
            ]}""",
        )

        assertEquals(
            listOf("yidong", "8"),
            timeline.soundEventsCrossed(9, 0f, 0f, includeStart = true).map { it.value },
        )
        assertTrue(timeline.soundEventsCrossed(9, 0f, 1f / 24f).isEmpty())
        assertEquals(
            listOf("7"),
            timeline.soundEventsCrossed(9, 1f / 24f, 2f / 24f).map { it.value },
        )
        assertTrue(timeline.soundEventsCrossed(9, 2f / 24f, 5f / 24f).isEmpty())
    }

    @Test
    fun `callback2 selects exact default highlight value and gray material`() {
        val timeline = FightSpriteTimeline.fromJson(
            """{"anime28":[
              {"frame":1,"sprite":{"t":1,"idx":0},"events":{"2":[110]}},
              {"frame":1,"events":{"2":[0]}},
              {"frame":1,"events":{"2":[201]}}
            ]}""",
        )

        val highlighted = requireNotNull(timeline.frame(28, 0f))
        assertEquals(BattleCharacterMaterial.HIGHLIGHT, highlighted.material)
        assertClose(1f, highlighted.materialValue)
        assertEquals(BattleCharacterMaterial.DEFAULT, requireNotNull(timeline.frame(28, 1f / 24f)).material)
        assertEquals(BattleCharacterMaterial.GRAY, requireNotNull(timeline.frame(28, 2f / 24f)).material)
    }

    @Test
    fun `bare opacity values are discrete source keyframes`() {
        val timeline = FightSpriteTimeline.fromJson(
            """{"anime23":[
              {"frame":4,"sprite":{"t":2,"idx":2},"props":{"opacity":127}},
              {"frame":4,"sprite":{"t":2,"idx":2},"props":{"opacity":255}}
            ]}""",
        )

        assertClose(127f, requireNotNull(timeline.frame(23, 3f / 24f)).pose.opacity)
        assertClose(255f, requireNotNull(timeline.frame(23, 4f / 24f)).pose.opacity)
    }

    private fun assertClose(expected: Float, actual: Float) {
        assertTrue(kotlin.math.abs(expected - actual) < 0.0001f, "expected=$expected actual=$actual")
    }
}
