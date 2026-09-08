// Test
package com.jojo.game.verification

import com.jojo.game.application.runtime.GameEntryPoint
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VerificationDesktopLaunchOptionsTest {
    @Test
    fun `capture and scripted scenario flags remain verification-owned`() {
        val options = VerificationDesktopLaunchOptions.parse(
            arrayOf(
                "--scenario=R_03", "--verify-scene=scene2", "--verify-globals=7:8",
                "--verify-random=10,90", "--capture-state=hall-menu-fixture",
                "--render-event-log=build/evidence.jsonl",
            ),
        )

        assertEquals("R_03", options.scenario)
        assertEquals("scene2", options.scenarioRun.startScene)
        assertEquals(mapOf(7 to 8), options.scenarioRun.globals)
        assertEquals(listOf(10, 90), options.scenarioRun.randomSequence)
        assertEquals("hall-menu-fixture", options.capture.state)
        assertEquals(GameEntryPoint.SCENARIO, options.toGameConfiguration().entryPoint)
        assertTrue(options.toGameConfiguration().automatedRun)
    }

    @Test
    fun `attack capture states sample the ticks the source harness reports`() {
        // 원본 하네스 `captureBattleActionFrames`가 세 동작을 재는 시점이다. 동작마다
        // 첫 구간 길이가 달라(6·9·18틱) 표본 시각도 다르다. 이 상태들은 화면 캡처로만
        // 쓰여서 이름이 조용히 어긋나도 아무 데서도 드러나지 않았다.
        val expected = mapOf(
            6 to listOf(1, 7, 9, 11), 25 to listOf(1, 10, 12, 14), 48 to listOf(1, 19, 21, 23),
        )

        expected.forEach { (action, ticks) ->
            ticks.forEachIndexed { index, tick ->
                val sample = VerificationBattlePresentation.from("attack$action-f$index").actionSample
                assertEquals(action, sample?.action, message = "attack$action-f$index")
                assertEquals(tick / 24f, sample?.sample, message = "attack$action-f$index")
            }
        }
        assertNull(VerificationBattlePresentation.from("attack6-f4").actionSample)
    }
}
