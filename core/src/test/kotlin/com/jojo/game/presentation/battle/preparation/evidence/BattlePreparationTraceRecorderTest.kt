package com.jojo.game.presentation.battle.preparation.evidence

import com.jojo.game.presentation.battle.preparation.BattlePreparationFixture
import com.jojo.game.presentation.battle.preparation.BattlePreparationUnitView
import com.jojo.game.presentation.battle.preparation.BattlePreparationViewState

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * class  `BattlePreparationTraceRecorderTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattlePreparationTraceRecorderTest {
    private val unit = BattlePreparationUnitView(
        id = 7,
        name = "허자장",
        armName = "병사",
        level = 3,
        experience = 0,
        maxHitPoints = 100,
        maxMagicPoints = 20,
        traits = emptyList(),
        avatarId = 7,
        headId = 15,
    )
    private val recorder = BattlePreparationTraceRecorder()

    @Test fun `composition records immutable selection geometry`() {
        val trace = recorder.composition(state())

        assertTrue(trace.contains("\"selectedUnitId\":7"))
        assertTrue(trace.contains("\"kind\":\"required\",\"id\":7"))
        assertTrue(trace.contains("\"avatarRect\":[151.434,537.158,99.072,99.072]"))
    }

    @Test fun `battle view fixture retains four ordered marker triples`() {
        val lines = recorder.renderEvents(state(fixture = BattlePreparationFixture.BattleView))
            .lineSequence().filter(String::isNotBlank).toList()

        assertEquals(16, lines.size)
        assertTrue(lines[3].contains("box6"))
        assertTrue(lines[14].contains("\"text\":\"4\""))
        assertTrue(lines.last().contains("Canvas/Layer/bg/box3"))
    }

    @Test fun `open sort fixture appends menu after preparation events`() {
        val log = recorder.renderEvents(state(fixture = BattlePreparationFixture.BattleSort("start-battle-sort-open")))

        assertTrue(log.indexOf("BattleSortLayer") < log.indexOf("Canvas/Layer/menu"))
        assertTrue(log.contains("button1_4/Background/Label"))
    }

    private fun state(fixture: BattlePreparationFixture = BattlePreparationFixture.Standard) =
        BattlePreparationViewState(
            backgroundId = 71,
            availableIds = listOf(7),
            units = listOf(unit),
            selectedIds = listOf(7),
            requiredIds = listOf(7),
            requiredSlotCount = 1,
            minimum = 1,
            maximum = 2,
            cursorId = 7,
            canStart = true,
            fixture = fixture,
            battleViewMarkerCount = 4,
        )
}
