// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute

/** 기기 목록 캡처 fixture 조정기: 유닛 정보에서 기기 목록으로 이어지는 상태 전환을 한 번만 지시한다. */
internal class BattleJiqiRouteFixtureController {
    /** 설치 여부: 같은 캡처 화면에서 기기 목록을 반복해서 열지 않도록 보관한다. */
    private var installed = false

    /** 경로 설치: 기기 목록 경로일 때만 실제 화면에 열기·전환·닫기 명령을 전달한다. */
    fun install(route: RuntimeBattleRoute?, characterId: Int, commands: Commands): Boolean {
        if (route != RuntimeBattleRoute.JIQI || installed) return false
        installed = true
        commands.openUnitInfo(characterId)
        commands.openJiqi()
        commands.dismissUnitInfo()
        return true
    }

    /** 화면 명령: fixture가 화면 렌더링과 입력 구현에 직접 의존하지 않도록 상태 전환만 노출한다. */
    internal interface Commands {
        /** 유닛 정보 열기: 기기 목록의 원본 레이어를 준비한다. */
        fun openUnitInfo(characterId: Int)

        /** 기기 목록 열기: 원본 레이어의 기기 버튼 전환과 결과 반영을 요청한다. */
        fun openJiqi()

        /** 유닛 정보 닫기: 기기 목록을 표시한 뒤 원본 정보 레이어를 정리한다. */
        fun dismissUnitInfo()
    }
}
