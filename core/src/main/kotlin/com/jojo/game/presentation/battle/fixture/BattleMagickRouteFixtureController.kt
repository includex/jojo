// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.shared.overlay.MagicInfoLayer
import com.jojo.game.presentation.shared.overlay.MagicUiList

/** 마법 경로 fixture 조정기: 캡처용 마법 목록을 만들고 상세 경로에 필요한 길게 누르기 전환 상태를 구성한다. */
internal class BattleMagickRouteFixtureController {
    /** 설치 여부: 같은 화면 프레임에서 fixture가 반복 초기화되는 것을 막는다. */
    private var installed = false

    /** 경로 설치: 최초 요청에서만 목록 및 상세 레이어 상태를 생성해 화면에 전달한다. */
    fun install(
        route: RuntimeBattleRoute?,
        magics: () -> List<MagicUiList.Magic>,
    ): State? {
        if (route == null || installed) return null
        installed = true
        /**
         * `list` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val list = MagicUiList(MP, MAX_MP, magics(), emptyMap())
        /**
         * `info` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val info = if (route == RuntimeBattleRoute.MAGICK_DETAIL) {
            list.start(DETAIL_INDEX)
            list.tick()?.let(::MagicInfoLayer)
        } else {
            null
        }
        return State(list, info)
    }

    /** 마법 fixture 화면 상태: 실제 렌더링과 입력 처리가 이어서 사용할 목록 및 상세 레이어다. */
    data class State(
        /**
         * `list` (MagicUiList,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val list: MagicUiList,
        /**
         * `info` (MagicInfoLayer?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val info: MagicInfoLayer?,
    )

    private companion object {
        /** 현재 MP: 원본 캡처 경로가 표시하던 마나 수치다. */
        const val MP = 24

        /** 최대 MP: 마나 막대 비율 계산에 사용하는 기준 수치다. */
        const val MAX_MP = 58

        /** 상세 선택 행: 상세 캡처에서 길게 누르는 첫 번째 마법 행이다. */
        const val DETAIL_INDEX = 0
    }
}
