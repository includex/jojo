package com.jojo.game
import com.jojo.game.presentation.battle.UnitSpriteSource
import com.jojo.game.presentation.battle.unit.BattleSpriteTimeline
import com.jojo.game.presentation.battle.timeline.*

import com.badlogic.gdx.utils.JsonReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleSpriteTimelineTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleSpriteTimelineTest {
    private fun originalTimeline(): BattleSpriteTimeline {
        val json = requireNotNull(javaClass.classLoader.getResourceAsStream("maps/battle-anime.json")) {
            "원본 battle-anime.json 리소스가 없습니다"
        }.bufferedReader().use { it.readText() }
        return BattleSpriteTimeline.fromJson(json)
    }

    @Test
    fun `every original combat attack and hit direction has an authored frame and hit event`() {
        val timeline = originalTimeline()
        listOf(21, 25, 48, 49).forEach { action ->
            (0..3).forEach { direction ->
                assertTrue(timeline.duration(action, direction) > 0f, "anime${action}_$direction duration")
                assertTrue(timeline.frame(action, direction, 0f) != null, "anime${action}_$direction frame")
                assertTrue(timeline.hitTime(action, direction) != null, "anime${action}_$direction hit")
            }
        }
        (0..3).forEach { direction ->
            assertTrue(timeline.duration(32, direction) > 0f, "anime32_$direction duration")
            assertTrue(timeline.frame(32, direction, 0f) != null, "anime32_$direction frame")
        }
    }

    @Test
    fun `every original BRAnime clip has evaluable opening and final frames`() {
        val timeline = originalTimeline()
        val keyPattern = Regex("anime(\\d+)(?:_(\\d+))?")
        timeline.clipNames().forEach { key ->
            val match = requireNotNull(keyPattern.matchEntire(key)) { "알 수 없는 원본 BRAnime 키: $key" }
            val action = match.groupValues[1].toInt()
            val direction = match.groupValues[2].toIntOrNull() ?: 2
            val duration = timeline.duration(action, direction)
            assertTrue(duration > 0f, "$key duration")
            assertTrue(timeline.frame(action, direction, 0f) != null, "$key opening frame")
            assertTrue(timeline.frame(action, direction, duration, loop = false) != null, "$key final frame")
        }
    }

    @Test
    fun `every original BRAnime tick retains a concrete sprite frame`() {
        val timeline = originalTimeline()
        val keyPattern = Regex("anime(\\d+)(?:_(\\d+))?")
        timeline.clipNames().forEach { key ->
            val match = requireNotNull(keyPattern.matchEntire(key))
            val action = match.groupValues[1].toInt()
            val direction = match.groupValues[2].toIntOrNull() ?: 2
            val ticks = (timeline.duration(action, direction) * 24f).toInt()
            for (tick in 0 until ticks) {
                assertTrue(
                    timeline.frame(action, direction, tick / 24f, loop = false) != null,
                    "$key tick=$tick must inherit an authored sprite",
                )
            }
        }
    }

    @Test
    fun `every authored hit event is exposed at its exact source tick`() {
        val raw = requireNotNull(javaClass.classLoader.getResourceAsStream("maps/battle-anime.json"))
            .bufferedReader().use { it.readText() }
        val timeline = BattleSpriteTimeline.fromJson(raw)
        val keyPattern = Regex("anime(\\d+)(?:_(\\d+))?")
        var clip = JsonReader().parse(raw).child
        while (clip != null) {
            val match = requireNotNull(keyPattern.matchEntire(clip.name))
            val action = match.groupValues[1].toInt()
            val direction = match.groupValues[2].toIntOrNull() ?: 2
            var ticks = 0
            var expected: Float? = null
            var key = clip.child
            while (key != null) {
                val events = key.get("events")?.get("0")
                if (events?.child?.asString() == "hit") expected = ticks / 24f
                ticks += key.getInt("frame", 1)
                key = key.next
            }
            if (expected != null) assertEquals(expected, timeline.hitTime(action, direction), clip.name)
            clip = clip.next
        }
    }

    @Test
    fun `games CreateAnime bottom origin rows and generated right mirror`() {
        val timeline = BattleSpriteTimeline.fromJson(
            """{
              "anime0_2":[{"frame":8,"sprite":{"t":1,"idx":0}},{"frame":8,"sprite":{"t":1,"idx":1}}],
              "anime0_3":[{"frame":8,"sprite":{"t":1,"idx":4}},{"frame":8,"sprite":{"t":1,"idx":5}}]
            }"""
        )

        val down0 = requireNotNull(timeline.frame(0, 2, 0f, loop = true))
        val down1 = requireNotNull(timeline.frame(0, 2, 8f / 24f, loop = true))
        val right = requireNotNull(timeline.frame(0, 1, 0f, loop = true))
        val left = requireNotNull(timeline.frame(0, 3, 0f, loop = true))

        assertEquals(UnitSpriteSource.MOVEMENT, down0.source)
        assertEquals(1, down0.sourceY)
        assertEquals(51, down1.sourceY)
        assertEquals(201, right.sourceY)
        assertTrue(right.flipX)
        assertFalse(left.flipX)
    }

    @Test
    fun `preserves action offsets and sprite changes at authored ticks`() {
        val timeline = BattleSpriteTimeline.fromJson(
            """{
              "anime6_2":[
                {"frame":6,"sprite":{"t":0,"idx":0},"props":{"position":[[0,-8,0]]}},
                {"frame":2,"sprite":{"t":0,"idx":1}}
              ]
            }"""
        )

        val opening = requireNotNull(timeline.frame(6, 2, 0f))
        val followUp = requireNotNull(timeline.frame(6, 2, 6f / 24f))
        assertEquals(1, opening.sourceY)
        assertEquals(-16f, opening.offsetY)
        assertEquals(67, followUp.sourceY)
        assertEquals(8f / 24f, timeline.duration(6, 2))
    }

    @Test
    fun `uses the source hit animation event rather than click time`() {
        val timeline = BattleSpriteTimeline.fromJson(
            """{
              "anime25_2":[
                {"frame":9,"sprite":{"t":0,"idx":0}},
                {"frame":2,"sprite":{"t":0,"idx":1}},
                {"frame":2,"sprite":{"t":0,"idx":2},"events":{"0":["hit"]}},
                {"frame":16,"sprite":{"t":0,"idx":3}}
              ]
            }"""
        )

        assertEquals(11f / 24f, timeline.hitTime(25, 2))
    }

    @Test
    fun `script attack callback edge matches live BRAnime hit plus target reaction`() {
        val timeline = originalTimeline()
        assertEquals(22f / 24f, timeline.hitTime(21, 2))
        assertEquals(11f / 24f, timeline.hitTime(25, 2))
        assertEquals(14f / 24f, timeline.duration(32, 2))
        assertEquals(17f / 24f, timeline.duration(26, 2))

        assertEquals(
            timeline.hitTime(21, 2)!! + timeline.duration(32, 2),
            BattlePhysicalPresentationTimeline.scriptedAttackDuration(1),
        )
        assertEquals(
            timeline.hitTime(25, 2)!! + timeline.duration(32, 2),
            BattlePhysicalPresentationTimeline.scriptedAttackDuration(0),
        )
        assertEquals(
            timeline.hitTime(25, 2)!! + timeline.duration(26, 2),
            BattlePhysicalPresentationTimeline.scriptedAttackDuration(2),
        )
    }
}
