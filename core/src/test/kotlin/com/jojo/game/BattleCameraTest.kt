// Test
package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.presentation.battle.render.*
import com.jojo.game.domain.battle.BattleUnitMoveTimeline

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** BattleCameraTest: BattleCamera의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleCameraTest {
    @Test fun `loaded map geometry preserves S00 coordinates and expands later maps`() {
        // 테스트 근거: 저장·추적 자료의 순서와 직렬화 규칙 (JSON, S_00)을 검증한다.
        assertEquals(-320f, SourceBattleMapGeometry.boardLeft(20, 0f), .0001f)
        assertEquals(1728f, SourceBattleMapGeometry.boardBottom(20, 0f), .0001f)
        assertEquals(-96f, SourceBattleMapGeometry.mapBottom(20, 0f), .0001f)
        assertEquals(-272f to 1776f, SourceBattleMapGeometry.tileCenter(0f, 0f, 20, 20, 0f, 0f))

        // 테스트 근거: 연출 프레임과 콜백 처리 순서 (S52, S57)을 검증한다.
        assertEquals(1920f, SourceBattleMapGeometry.boardBottom(24, 0f), .0001f)
        assertEquals(-288f, SourceBattleMapGeometry.mapBottom(24, 0f), .0001f)
        assertEquals(-1280f, SourceBattleMapGeometry.boardLeft(40, 0f), .0001f)
        assertEquals(2688f, SourceBattleMapGeometry.boardBottom(40, 0f), .0001f)
        assertEquals(-1056f, SourceBattleMapGeometry.mapBottom(40, 0f), .0001f)
        assertEquals(-1232f to 2736f, SourceBattleMapGeometry.tileCenter(0f, 0f, 40, 40, 0f, 0f))
    }

    @Test fun `first unit probes source global slot ranges regardless of live order or visibility`() {
        val later = BattleUnit("enemy-2", "later", Faction.ENEMY, 13, 13)
        val firstSlot = BattleUnit("enemy-0", "first", Faction.ENEMY, 5, 10, visible = false, hitPoints = 0)
        assertEquals(firstSlot, firstCampCameraUnit(listOf(later, firstSlot), Faction.ENEMY))

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (LOST)을 검증한다.
        firstSlot.statuses[BattleStatus.LOST] = 1
        assertEquals(firstSlot, firstCampCameraUnit(listOf(later, firstSlot), Faction.REINFORCEMENTS))
    }

    @Test fun `source authored slot offsets come from Config battle partitions`() {
        assertEquals(0, battleSlotIndexFor(BattleUnit("mine-0", "mine", Faction.PLAYER, 0, 0)))
        assertEquals(40, battleSlotIndexFor(BattleUnit("friend-0", "friend", Faction.FRIEND, 0, 0)))
        assertEquals(60, battleSlotIndexFor(BattleUnit("enemy-0", "enemy", Faction.ENEMY, 0, 0)))
        assertEquals(60, battleSlotIndexFor(BattleUnit("enemy-0", "reinforcement", Faction.REINFORCEMENTS, 0, 0)))
    }

    @Test fun `first friend probe preserves source disjoint range bug`() {
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (FRIEND, BATTLE_MINE_N, BATTLE_FRIEND_N)을 검증한다.
        val friend = BattleUnit("friend-0", "friend", Faction.FRIEND, 0, 0)
        assertEquals(null, firstCampCameraUnit(listOf(friend), Faction.FRIEND))
    }

    @Test fun `node anchor point retains prefab zero when authored y is missing`() {
        val c = BattleCamera()
        val (_, nodeY) = c.sourceNodeScreenPoint(7, 0, authoredX = true, authoredY = false)
        assertEquals(864f, nodeY, .0001f)
    }

    @Test fun `node anchor point retains prefab zero on both omitted axes`() {
        val c = BattleCamera()
        val (nodeX, nodeY) = c.sourceNodeScreenPoint(7, 9, authoredX = false, authoredY = false)
        assertEquals(640f, nodeX, .0001f)
        assertEquals(864f, nodeY, .0001f)
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        val (movedX, movedY) = c.sourceNodeScreenPoint(7, 9, authoredX = true, authoredY = true)
        assertEquals(400f, movedX, .0001f)
        assertEquals(912f, movedY, .0001f)
    }

    @Test fun `source contains includes unit half size outside ninety six pixel edge band`() {
        val c = BattleCamera()
        assertTrue(c.ensureVisible(30f, 400f))
        assertEquals(114f, c.x)
        c.reset()
        assertTrue(c.ensureVisible(1450f, 400f))
        assertEquals(-105.62793f, c.x, .0001f)
    }

    @Test fun `source contains does not create a camera transition for an in-bounds action`() {
        val c = BattleCamera()
        assertEquals(false, c.ensureVisible(744f, 400f))
        assertEquals(-104.18605f, c.contentX, .0001f)
        assertEquals(464f, c.contentY, .0001f)
        assertEquals(0, c.mapScrollingDispatchCount)
    }

    @Test fun `source contains dispatches scrolling even when edge clamp prevents coordinate change`() {
        val c = BattleCamera()
        c.pan(10000f, 0f)
        val clamped = c.contentX

        // 테스트 근거: 전투 계산·난수 소비·경계값 (MAP_SCROLLING)을 검증한다.
        assertEquals(false, c.ensureVisible(0f, 400f))
        assertEquals(clamped, c.contentX, .0001f)
        assertEquals(1, c.mapScrollingDispatchCount)
    }

    @Test fun `authored zero row show establishes camp camera before move callbacks`() {
        val c = BattleCamera()
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        c.pan(320f, -640f)
        val shown = c.sourceNodeScreenPoint(6, 0, authoredX = true, authoredY = true)
        assertTrue(c.ensureVisible(shown.first, shown.second))
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        assertEquals(215.81395f, c.contentX, .0001f)
        assertEquals(-560f, c.contentY, .0001f)
    }

    @Test fun `camera delta clamps through original asymmetric content position`() {
        val c = BattleCamera()
        c.pan(-10000f, -10000f)
        assertEquals(-111.62793f, c.x, .0001f)
        assertEquals(-1024f, c.y)
        c.pan(20000f, 20000f)
        assertEquals(320f, c.x)
        assertEquals(96f, c.y)
    }

    @Test fun `runtime viewport update preserves source content clamp formula`() {
        val c = BattleCamera()
        c.configureViewport(1280f, 800f)
        c.pan(-10000f, 10000f)
        assertEquals(-320f, c.x)
        assertEquals(96f, c.y)
    }

    @Test fun `loaded map dimensions define production drag limits before first authored center`() {
        val s52 = BattleCamera(mapWidth = 20 * 96f, mapHeight = 24 * 96f)
        s52.pan(0f, 10000f)
        assertEquals(752f, s52.contentY, .0001f)

        val s57 = BattleCamera(mapWidth = 40 * 96f, mapHeight = 40 * 96f)
        s57.pan(10000f, -10000f)
        assertEquals(1175.81395f, s57.contentX, .0001f)
        assertEquals(-1520f, s57.contentY, .0001f)
    }

    @Test fun `forced center moves an already visible unit to viewport center`() {
        val c = BattleCamera()
        c.forceCenter(700f, 400f)
        assertEquals(44.18605f, c.x, .0001f)
        assertEquals(0f, c.y)
    }

    @Test fun `source force center uses tile position rather than unit node half tile`() {
        val c = BattleCamera()
        val tileX = 9f
        val tileY = 14f
        val nodeCenterX = -272f + tileX * 96f + c.x
        val nodeCenterY = 1776f - tileY * 96f + c.y
        c.forceCenter(nodeCenterX - 48f, nodeCenterY + 48f)
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(96f, c.contentX, .0001f)
        assertEquals(384f, c.contentY, .0001f)
    }

    @Test fun `trace coordinates expose source ScrollView content position`() {
        val c = BattleCamera()
        assertEquals(-104.18605f, c.contentX, .0001f)
        assertEquals(464f, c.contentY, .0001f)
        c.pan(104.18605f, -160f)
        assertEquals(0f, c.contentX, .0001f)
        assertEquals(304f, c.contentY, .0001f)
    }

    @Test fun `scripted movement camera follows current interpolation with cocos timer cadence`() {
        val path = listOf(6 to 16, 6 to 17, 7 to 17, 8 to 17)
        val timeline = BattleUnitMoveTimeline.schedule(path, fastMove = true)
        val cursor = MovementCameraTickCursor()
        assertEquals(emptyList(), cursor.crossed(path, timeline, 0f))
        assertEquals(emptyList(), cursor.crossed(path, timeline, .079f))
        assertEquals(listOf(6f to 17f), cursor.crossed(path, timeline, .08f).map { it.x to it.y })
        assertEquals(emptyList(), cursor.crossed(path, timeline, .08f))
        assertEquals(listOf(7.5f to 17f), cursor.crossed(path, timeline, .20f).map { it.x to it.y })
        // 테스트 근거: 연출 프레임과 콜백 처리 순서을 검증한다.
        assertEquals(emptyList(), cursor.crossed(path, timeline, .24f))
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        assertEquals(emptyList(), cursor.crossed(path, timeline, .28f))
    }

    @Test fun `completion render callback samples preceding interpolation instead of endpoint`() {
        val path = listOf(8 to 10, 8 to 9, 9 to 9, 9 to 8, 9 to 7, 9 to 6)
        val timeline = BattleUnitMoveTimeline.schedule(path, fastMove = true)
        val cursor = MovementCameraTickCursor()
        assertEquals(emptyList(), cursor.crossed(path, timeline, 0f))
        assertEquals(listOf(8f to 10f), cursor.crossed(path, timeline, .41f).map { it.x to it.y })
        assertEquals(emptyList(), cursor.crossed(path, timeline, .41f))
    }

    @Test fun `yingchuan completion callback retains pre-final actor258 position once`() {
        val path = listOf(7 to 2, 7 to 3, 7 to 4, 8 to 4, 8 to 5, 9 to 5, 9 to 6, 9 to 7)
        val timeline = BattleUnitMoveTimeline.schedule(path, fastMove = true)
        val cursor = MovementCameraTickCursor()
        assertEquals(emptyList(), cursor.crossed(path, timeline, 0f))
        val inFlight = cursor.crossed(path, timeline, .526f).single()
        assertEquals(9f, inFlight.x, .0001f)
        assertEquals(6.575f, inFlight.y, .0001f)
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        val completion = cursor.crossed(path, timeline, .66f).single()
        assertEquals(9f, completion.x, .0001f)
        assertEquals(6.575f, completion.y, .0001f)
        assertEquals(emptyList(), cursor.crossed(path, timeline, .66f))
    }

    @Test fun `source actor258 callback sample produces retained minus 537 point 28 camera`() {
        val path = listOf(7 to 2, 7 to 3, 7 to 4, 8 to 4, 8 to 5, 9 to 5, 9 to 6, 9 to 7)
        val timeline = BattleUnitMoveTimeline.schedule(path, fastMove = true)
        val cursor = MovementCameraTickCursor()
        cursor.crossed(path, timeline, 0f)
        cursor.crossed(path, timeline, .5256f)
        val sample = cursor.crossed(path, timeline, .66f).single()
        assertEquals(9f, sample.x, .0001f)
        assertEquals(6.57f, sample.y, .0001f)

        val camera = BattleCamera()
        camera.pan(320f, -1024f) // source pre-reinforcement content=(215.814,-560)
        val screenX = -272f + sample.x * 96f + camera.x
        val screenY = 1776f - sample.y * 96f + camera.y
        camera.ensureVisible(screenX, screenY)
        assertEquals(215.81395f, camera.contentX, .0001f)
        assertEquals(-537.28f, camera.contentY, .001f)
    }

    @Test fun `coarse callback observes current in-flight node position once`() {
        val path = listOf(7 to 2, 7 to 3, 7 to 4, 8 to 4, 8 to 5, 9 to 5, 9 to 6, 9 to 7)
        val timeline = BattleUnitMoveTimeline.schedule(path, fastMove = true)
        val cursor = MovementCameraTickCursor()
        assertEquals(emptyList(), cursor.crossed(path, timeline, 0f))
        val sample = cursor.crossed(path, timeline, .53f).single()
        assertEquals(9f, sample.x, .0001f)
        assertEquals(6.625f, sample.y, .0001f)
        assertEquals(emptyList(), cursor.crossed(path, timeline, .53f))
    }

    @Test fun `same tile scripted path has no source camera schedule and resets cursor safely`() {
        val movingPath = listOf(8 to 10, 8 to 9)
        val movingTimeline = BattleUnitMoveTimeline.schedule(movingPath, fastMove = true)
        val cursor = MovementCameraTickCursor()
        assertEquals(emptyList(), cursor.crossed(movingPath, movingTimeline, 0f))
        assertEquals(listOf(8f to 9f), cursor.crossed(movingPath, movingTimeline, .08f).map { it.x to it.y })

        assertEquals(emptyList(), cursor.crossed(listOf(8 to 9), null, 10f))

        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        assertEquals(emptyList(), cursor.crossed(movingPath, movingTimeline, 0f))
        assertEquals(listOf(8f to 9f), cursor.crossed(movingPath, movingTimeline, .08f).map { it.x to it.y })
    }

    @Test fun `slow movement camera keeps source point 16 second cadence through final delay`() {
        val path = listOf(0 to 0, 1 to 0, 2 to 0)
        val timeline = BattleUnitMoveTimeline.schedule(path, fastMove = false)
        val cursor = MovementCameraTickCursor()

        assertEquals(emptyList(), cursor.crossed(path, timeline, 0f))
        assertEquals(emptyList(), cursor.crossed(path, timeline, .08f))
        assertEquals(listOf(1f to 0f), cursor.crossed(path, timeline, .16f).map { it.x to it.y })
        assertEquals(listOf(2f to 0f), cursor.crossed(path, timeline, .32f).map { it.x to it.y })
        assertEquals(emptyList(), cursor.crossed(path, timeline, .42f), "the final .1 second idle delay has no extra camera tick")
    }

    @Test fun `stage center uses dynamic map clamp and dispatches repeated equal positions`() {
        val c = BattleCamera()
        c.configureViewport(1488.3721f, 800f)

        c.centerTile(5, 20, 40, 40)
        assertEquals(1175.81396f, c.contentX, .0001f)
        assertEquals(0f, c.contentY, .0001f)
        c.centerTile(11, 20, 40, 40)
        assertEquals(864f, c.contentX, .0001f)
        c.centerTile(13, 20, 40, 40)
        assertEquals(672f, c.contentX, .0001f)
        c.centerTile(13, 20, 40, 40)
        assertEquals(672f, c.contentX, .0001f)
        assertEquals(4, c.mapScrollingDispatchCount)
    }
}
