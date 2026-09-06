// Test
package com.jojo.game.verification.preparation

import com.jojo.game.presentation.battle.preparation.BattlePreparationUnitView
import com.jojo.game.presentation.battle.preparation.BattlePreparationViewState

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** BattlePreparationTraceRecorderTest: BattlePreparationTraceRecorder의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

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
        val lines = recorder.renderEvents(state(), "battle-view-fixture")
            .lineSequence().filter(String::isNotBlank).toList()

        assertEquals(16, lines.size)
        assertTrue(lines[3].contains("box6"))
        assertTrue(lines[14].contains("\"text\":\"4\""))
        assertTrue(lines.last().contains("Canvas/Layer/bg/box3"))
    }

    @Test fun `open sort fixture appends menu after preparation events`() {
        val log = recorder.renderEvents(state(), "start-battle-sort-open-fixture")

        assertTrue(log.indexOf("BattleSortLayer") < log.indexOf("Canvas/Layer/menu"))
        assertTrue(log.contains("button1_4/Background/Label"))
    }

    private fun state() =
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
            battleViewMarkerCount = 4,
        )
}
