// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 기기 목록 fixture 검증: 지원 경로의 상태 전환 순서와 중복 설치 방지를 확인한다. */
class BattleJiqiRouteFixtureControllerTest {
    /** 기기 목록 경로: 유닛 정보 열기부터 기기 목록 전환과 원본 닫기까지 순서대로 지시한다. */
    @Test
    fun `기기 목록 경로의 상태 전환을 한 번 지시한다`() {
        val commands = RecordingCommands()

        assertTrue(BattleJiqiRouteFixtureController().install(RuntimeBattleRoute.JIQI, 7, commands))

        assertEquals(listOf("open-unit:7", "open-jiqi", "dismiss-unit"), commands.calls)
    }

    /** 중복·비지원 경로 방지: 다른 경로와 두 번째 설치에서는 화면 명령을 전달하지 않는다. */
    @Test
    fun `기기 목록 fixture를 한 번만 설치한다`() {
        val controller = BattleJiqiRouteFixtureController()
        val commands = RecordingCommands()

        assertFalse(controller.install(RuntimeBattleRoute.MAGICK_LIST, 7, commands))
        assertTrue(controller.install(RuntimeBattleRoute.JIQI, 7, commands))
        assertFalse(controller.install(RuntimeBattleRoute.JIQI, 8, commands))

        assertEquals(listOf("open-unit:7", "open-jiqi", "dismiss-unit"), commands.calls)
    }

    /** 명령 기록기: 실제 화면 대신 fixture가 요청한 상태 전환만 기록한다. */
    private class RecordingCommands : BattleJiqiRouteFixtureController.Commands {
        /** 호출 순서: fixture 설치가 전달한 세부 명령 목록이다. */
        val calls = mutableListOf<String>()

        override fun openUnitInfo(characterId: Int) {
            calls += "open-unit:$characterId"
        }

        override fun openJiqi() {
            calls += "open-jiqi"
        }

        override fun dismissUnitInfo() {
            calls += "dismiss-unit"
        }
    }
}
