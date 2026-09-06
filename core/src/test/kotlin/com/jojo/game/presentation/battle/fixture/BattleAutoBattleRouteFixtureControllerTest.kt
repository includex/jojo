// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.overlay.AutoBattleFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 자동 전투 fixture 검증: 경로별 확인 창·체크 상태·위임 시작 입력이 한 번씩 적용되는지 확인한다. */
class BattleAutoBattleRouteFixtureControllerTest {
    /** 선택 해제 경로: 메뉴 입력으로 확인 창을 열되 체크 상태와 위임 시작 입력은 바꾸지 않는다. */
    @Test
    fun `해제 확인 창 경로는 체크를 해제한 prompt 상태를 남긴다`() {
        val commands = RecordingCommands(initialStored = false)

        assertTrue(BattleAutoBattleRouteFixtureController().install(RuntimeBattleRoute.AUTO_PROMPT_OFF, commands))

        assertEquals(listOf("open-menu", "tap-auto-menu"), commands.calls)
        assertEquals(AutoBattleFlow.Overlay.PROMPT, commands.flow.view().overlay)
        assertFalse(commands.flow.view().checked)
    }

    /** 선택 확인 창 경로: 기존 저장값과 무관하게 토글로 선택 상태를 만들고 확인 창을 유지한다. */
    @Test
    fun `선택 확인 창 경로는 체크 상태만 켠다`() {
        val commands = RecordingCommands(initialStored = false)

        BattleAutoBattleRouteFixtureController().install(RuntimeBattleRoute.AUTO_PROMPT_ON, commands)

        assertEquals(listOf("open-menu", "tap-auto-menu", "toggle"), commands.calls)
        assertEquals(AutoBattleFlow.Overlay.PROMPT, commands.flow.view().overlay)
        assertTrue(commands.flow.view().checked)
    }

    /** 위임 진행 경로: 체크 상태를 만든 후 확인 입력까지 전달해 자동 전투 표시 상태를 시작한다. */
    @Test
    fun `위임 진행 경로는 선택 후 확인을 눌러 tuoguan 상태를 만든다`() {
        val commands = RecordingCommands(initialStored = false)

        BattleAutoBattleRouteFixtureController().install(RuntimeBattleRoute.AUTO_ACTIVE, commands)

        assertEquals(listOf("open-menu", "tap-auto-menu", "toggle", "confirm"), commands.calls)
        assertEquals(AutoBattleFlow.Overlay.TUOGUAN, commands.flow.view().overlay)
        assertTrue(commands.flow.view().checked)
    }

    /** 중복·비지원 경로 방지: 지원하지 않는 상태와 두 번째 설치에서는 화면 입력을 다시 보내지 않는다. */
    @Test
    fun `자동 전투 fixture를 한 번만 설치한다`() {
        val controller = BattleAutoBattleRouteFixtureController()
        val commands = RecordingCommands()

        assertFalse(controller.install(RuntimeBattleRoute.MAGICK_LIST, commands))
        assertTrue(controller.install(RuntimeBattleRoute.AUTO_PROMPT_OFF, commands))
        assertFalse(controller.install(RuntimeBattleRoute.AUTO_ACTIVE, commands))
        assertEquals(listOf("open-menu", "tap-auto-menu"), commands.calls)
    }

    /** 명령 기록기: 실제 화면 대신 AutoBattleFlow와 입력 콜백의 연결 순서를 재현한다. */
    private class RecordingCommands(initialStored: Boolean = false) : BattleAutoBattleRouteFixtureController.Commands {
        /** 자동 전투 흐름: 확인 창과 위임 표시 상태를 실제 로직으로 보관한다. */
        val flow = AutoBattleFlow(initialStored)

        /** 호출 순서: fixture가 화면에 요청한 메뉴·토글·확인 입력 목록이다. */
        val calls = mutableListOf<String>()

        override fun openBattleMenu() {
            calls += "open-menu"
        }

        override fun tapAutoBattleMenu() {
            calls += "tap-auto-menu"
            flow.openEndRoundPrompt()
        }

        override fun view(): AutoBattleFlow.View = flow.view()

        override fun togglePrompt() {
            calls += "toggle"
            flow.toggle()
        }

        override fun confirmPrompt() {
            calls += "confirm"
            flow.answer(0, AutoBattleFlow.TOUCH_END)
        }
    }
}
