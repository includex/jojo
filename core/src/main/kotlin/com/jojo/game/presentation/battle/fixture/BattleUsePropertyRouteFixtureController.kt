// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute

/** 소비 아이템 경로 fixture 조정기: 캡처에 필요한 인벤토리와 선택·상세·취소 전환을 한 번만 지시한다. */
internal class BattleUsePropertyRouteFixtureController {
    /** 설치 여부: 같은 캡처 화면에서 인벤토리와 레이어 상태가 반복 초기화되는 것을 막는다. */
    private var installed = false

    /** 경로 설치: 허용된 소비 아이템 경로에서만 공통 준비와 경로별 화면 동작을 순서대로 요청한다. */
    fun install(route: RuntimeBattleRoute?, commands: Commands): Boolean {
        if (route !in SUPPORTED_ROUTES || installed) return false
        installed = true
        commands.seedInventory()
        commands.selectPlayerUnit()
        commands.openPropertyLayer()
        when (route) {
            RuntimeBattleRoute.USE_PROPERTY_DETAIL -> commands.inspectFirstProperty()
            RuntimeBattleRoute.USE_PROPERTY_SELECT -> commands.selectFirstProperty()
            RuntimeBattleRoute.USE_PROPERTY_CANCEL -> commands.cancelPropertyLayer()
            RuntimeBattleRoute.USE_PROPERTY_LIST -> Unit
            else -> error("지원하지 않는 소비 아이템 fixture 경로: $route")
        }
        return true
    }

    /** 화면 명령: 조정기가 실제 렌더링·입력 구현에 의존하지 않고 fixture 상태 변경만 요청하는 경계다. */
    internal interface Commands {
        /** 인벤토리 준비: 캡처가 표시할 소비 아이템 수량을 초기화한다. */
        fun seedInventory()

        /** 사용 유닛 선택: 소비 아이템 목록을 열 기준 아군을 선택한다. */
        fun selectPlayerUnit()

        /** 목록 열기: 화면이 실제 목록 레이어와 콜백을 생성한다. */
        fun openPropertyLayer()

        /** 상세 표시: 첫 행을 길게 눌러 상세 레이어를 표시한다. */
        fun inspectFirstProperty()

        /** 항목 선택: 첫 행을 짧게 눌러 대상 선택 상태로 전환한다. */
        fun selectFirstProperty()

        /** 목록 취소: 취소 입력을 적용하고 목록 레이어를 닫는다. */
        fun cancelPropertyLayer()
    }

    private companion object {
        /** 지원 경로: 소비 아이템 fixture가 실제로 구성할 수 있는 자동 캡처 경로다. */
        val SUPPORTED_ROUTES = setOf(
            RuntimeBattleRoute.USE_PROPERTY_LIST,
            RuntimeBattleRoute.USE_PROPERTY_DETAIL,
            RuntimeBattleRoute.USE_PROPERTY_SELECT,
            RuntimeBattleRoute.USE_PROPERTY_CANCEL,
        )
    }
}
