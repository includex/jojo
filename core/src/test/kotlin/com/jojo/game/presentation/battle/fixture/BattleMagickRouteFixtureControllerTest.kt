// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.shared.overlay.MagicUiList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** 마법 경로 fixture 조정기 검증: 경로별 목록과 상세 상태가 기존 캡처 순서대로 생성되는지 확인한다. */
class BattleMagickRouteFixtureControllerTest {
    /** 목록 경로: 고정 MP와 최대 MP를 가진 마법 목록만 생성한다. */
    @Test
    fun `마법 목록 경로를 설치한다`() {
        val state = BattleMagickRouteFixtureController().install(RuntimeBattleRoute.MAGICK_LIST, ::magics)

        assertNotNull(state)
        assertEquals(24, state.list.mp)
        assertEquals(58, state.list.maxMp)
        assertNull(state.info)
    }

    /** 상세 경로: 첫 번째 마법을 길게 누른 뒤 상세 레이어를 생성한다. */
    @Test
    fun `마법 상세 경로를 설치한다`() {
        val state = BattleMagickRouteFixtureController().install(RuntimeBattleRoute.MAGICK_DETAIL, ::magics)

        assertNotNull(state)
        assertEquals(1, state.info?.magic?.id)
        assertEquals(24f / 58f, state.list.preview)
    }

    /** 중복 설치 방지: 최초 상태를 만든 후에는 다시 초기화하지 않는다. */
    @Test
    fun `마법 fixture를 한 번만 설치한다`() {
        val controller = BattleMagickRouteFixtureController()

        assertNotNull(controller.install(RuntimeBattleRoute.MAGICK_LIST, ::magics))
        assertNull(controller.install(RuntimeBattleRoute.MAGICK_DETAIL, ::magics))
    }

    /** 마법 행: fixture가 목록과 상세 전환을 검증하는 최소 표시 데이터다. */
    private fun magics() = listOf(
        MagicUiList.Magic(1, "화염", 8, 10, 0, 0, 0, "설명"),
    )
}
