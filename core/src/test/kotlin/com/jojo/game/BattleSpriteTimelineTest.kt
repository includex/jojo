// Test
package com.jojo.game

import com.jojo.game.presentation.battle.unit.UnitSpriteSource
import com.jojo.game.presentation.battle.unit.BattleSpriteTimeline
import com.jojo.game.presentation.battle.timeline.*

import com.badlogic.gdx.utils.JsonReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** BattleSpriteTimelineTest: BattleSpriteTimeline의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleSpriteTimelineTest {
    private fun originalTimeline(): BattleSpriteTimeline {
        val json = requireNotNull(javaClass.classLoader.getResourceAsStream("maps/battle-anime.json")) {
            "원본 battle-anime.json 리소스가 없습니다"
        }.bufferedReader().use { it.readText() }
        return BattleSpriteTimeline.fromJson(json)
    }

    @Test
    fun `retreat and death clips blink and fade through the original opacity channel`() {
        val timeline = originalTimeline()
        // anime23은 5틱마다 255/0을 세 번 반복한다.
        listOf(0f, 10f, 20f).forEach { tick ->
            assertEquals(1f, timeline.frame(23, 0, tick / 24f)?.opacity, "anime23 visible at $tick")
        }
        listOf(5f, 15f, 25f).forEach { tick ->
            assertEquals(0f, timeline.frame(23, 0, tick / 24f)?.opacity, "anime23 hidden at $tick")
        }
        // anime24는 깜빡인 뒤 흰색 점등을 거쳐 200/150/100/50/0으로 사라진다.
        assertEquals(1f, timeline.frame(24, 0, 0f)?.opacity, "anime24 onset")
        assertEquals(0f, timeline.frame(24, 0, 6f / 24f)?.opacity, "anime24 first blink")
        assertEquals(0.1f, timeline.materialValue(24, 0, 75f / 24f), "anime24 highlight onset")
        assertEquals(1f, timeline.materialValue(24, 0, 91f / 24f), "anime24 highlight peak")
        val fade = listOf(107, 111, 115, 119, 123).map { timeline.frame(24, 0, it / 24f)?.opacity }
        assertEquals(listOf(200f, 150f, 100f, 50f, 0f).map { it / 255f }, fade, "anime24 fade")
    }

    @Test
    fun `critical attack ramps the original white highlight and clears it before the clip ends`() {
        val timeline = originalTimeline()
        // anime21은 4틱에 101(점등, 0.1)로 켜지고 10(1.0)까지 오른 뒤 17틱에 0으로 꺼진다.
        (0..3).forEach { direction ->
            assertEquals(null, timeline.materialValue(21, direction, 3f / 24f), "anime21_$direction before")
            assertEquals(0.1f, timeline.materialValue(21, direction, 4f / 24f), "anime21_$direction onset")
            assertEquals(1.0f, timeline.materialValue(21, direction, 13f / 24f), "anime21_$direction peak")
            assertEquals(null, timeline.materialValue(21, direction, 17f / 24f), "anime21_$direction cleared")
        }
    }

    @Test
    fun `hit reaction flashes white from the first frame and clears partway through`() {
        val timeline = originalTimeline()
        // anime32는 0틱에 110(점등, 1.0)으로 켜지고 7틱에 0으로 꺼진다.
        (0..3).forEach { direction ->
            assertEquals(1.0f, timeline.materialValue(32, direction, 0f), "anime32_$direction onset")
            assertEquals(1.0f, timeline.materialValue(32, direction, 6f / 24f), "anime32_$direction held")
            assertEquals(null, timeline.materialValue(32, direction, 7f / 24f), "anime32_$direction cleared")
        }
    }

    @Test
    fun `actions without an authored material channel never highlight`() {
        val timeline = originalTimeline()
        // 원본은 크리티컬이 아닌 공격(25)과 대기(0)에 점등 채널을 두지 않는다.
        listOf(0, 25).forEach { action ->
            (0..3).forEach { direction ->
                assertEquals(null, timeline.materialValue(action, direction, 0f), "anime${action}_$direction")
                assertEquals(null, timeline.materialValue(action, direction, .25f), "anime${action}_$direction late")
            }
        }
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
