// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.overlay.AutoBattleFlow

/** 자동 전투 경로 fixture 조정기: 캡처 경로별 확인 창, 위임 선택, 진행 상태를 실제 입력 순서로 구성한다. */
internal class BattleAutoBattleRouteFixtureController {
    /** 설치 여부: 같은 캡처 프레임에서 메뉴 입력과 자동 전투 상태 전환이 반복되지 않도록 보관한다. */
    private var installed = false

    /** 경로 설치: 메뉴의 자동 전투 항목을 누른 뒤 경로에 필요한 토글과 확인 입력만 순서대로 전달한다. */
    fun install(route: RuntimeBattleRoute?, commands: Commands): Boolean {
        if (route !in SUPPORTED_ROUTES || installed) return false
        installed = true
        commands.openBattleMenu()
        commands.tapAutoBattleMenu()
        check(commands.view().overlay == AutoBattleFlow.Overlay.PROMPT) {
            "MenuLayer.HHJS did not dispatch END_ROUND to MsgBox4"
        }
        val checked = route != RuntimeBattleRoute.AUTO_PROMPT_OFF
        if (commands.view().checked != checked) commands.togglePrompt()
        if (route == RuntimeBattleRoute.AUTO_ACTIVE) commands.confirmPrompt()
        return true
    }

    /** 화면 명령: fixture가 화면 상태와 실제 입력 콜백을 직접 구현하지 않도록 노출하는 연결 경계다. */
    internal interface Commands {
        /** 전투 메뉴 열기: 자동 전투 메뉴 항목을 누를 기반 레이어를 준비한다. */
        fun openBattleMenu()

        /** 자동 전투 메뉴 누르기: 메뉴의 HHJS 입력을 통해 위임 확인 창을 연다. */
        fun tapAutoBattleMenu()

        /** 자동 전투 상태 조회: 현재 확인 창과 체크 상태를 경로 정책에 비교한다. */
        fun view(): AutoBattleFlow.View

        /** 확인 창 토글: 저장할 위임 여부를 경로가 요구한 체크 상태로 맞춘다. */
        fun togglePrompt()

        /** 확인 입력: 위임 진행 경로에서 확인 버튼을 눌러 실제 자동 전투 상태를 시작한다. */
        fun confirmPrompt()
    }

    private companion object {
        /** 지원 경로: 자동 전투 확인 창의 해제·선택과 위임 진행 캡처를 구성한다. */
        val SUPPORTED_ROUTES = setOf(
            RuntimeBattleRoute.AUTO_PROMPT_OFF,
            RuntimeBattleRoute.AUTO_PROMPT_ON,
            RuntimeBattleRoute.AUTO_ACTIVE,
        )
    }
}
