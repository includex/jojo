// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 소비 아이템 경로 fixture 조정기 검증: 공통 준비와 경로별 입력 지시가 캡처 순서대로 한 번만 실행되는지 확인한다. */
class BattleUsePropertyRouteFixtureControllerTest {
    /** 상세 경로: 인벤토리와 아군 선택 후 목록을 열고 첫 항목의 길게 누르기를 요청한다. */
    @Test
    fun `상세 경로에 필요한 준비와 길게 누르기를 지시한다`() {
        val commands = RecordingCommands()

        assertTrue(BattleUsePropertyRouteFixtureController().install(RuntimeBattleRoute.USE_PROPERTY_DETAIL, commands))

        assertEquals(listOf("seed", "select-unit", "open", "inspect-first"), commands.calls)
    }

    /** 선택과 취소 경로: 각각 짧은 누름과 취소 동작만 마지막 단계로 지시한다. */
    @Test
    fun `선택과 취소 경로에 맞는 마지막 동작을 지시한다`() {
        val selectCommands = RecordingCommands()
        val cancelCommands = RecordingCommands()

        BattleUsePropertyRouteFixtureController().install(RuntimeBattleRoute.USE_PROPERTY_SELECT, selectCommands)
        BattleUsePropertyRouteFixtureController().install(RuntimeBattleRoute.USE_PROPERTY_CANCEL, cancelCommands)

        assertEquals("select-first", selectCommands.calls.last())
        assertEquals("cancel", cancelCommands.calls.last())
    }

    /** 중복·비지원 경로 방지: 이미 설치했거나 다른 경로이면 inventory를 다시 바꾸지 않는다. */
    @Test
    fun `허용된 fixture를 한 번만 설치한다`() {
        val controller = BattleUsePropertyRouteFixtureController()
        val commands = RecordingCommands()

        assertFalse(controller.install(RuntimeBattleRoute.MAGICK_LIST, commands))
        assertTrue(controller.install(RuntimeBattleRoute.USE_PROPERTY_LIST, commands))
        assertFalse(controller.install(RuntimeBattleRoute.USE_PROPERTY_DETAIL, commands))
        assertEquals(listOf("seed", "select-unit", "open"), commands.calls)
    }

    /** 명령 기록기: 화면 구현 대신 조정기가 요청한 상태 전환 순서만 보관한다. */
    private class RecordingCommands : BattleUsePropertyRouteFixtureController.Commands {
        /** 호출 순서: fixture가 화면에 전달한 준비와 입력 지시 목록이다. */
        val calls = mutableListOf<String>()

        override fun seedInventory() = calls.add("seed").let { Unit }
        override fun selectPlayerUnit() = calls.add("select-unit").let { Unit }
        override fun openPropertyLayer() = calls.add("open").let { Unit }
        override fun inspectFirstProperty() = calls.add("inspect-first").let { Unit }
        override fun selectFirstProperty() = calls.add("select-first").let { Unit }
        override fun cancelPropertyLayer() = calls.add("cancel").let { Unit }
    }
}
