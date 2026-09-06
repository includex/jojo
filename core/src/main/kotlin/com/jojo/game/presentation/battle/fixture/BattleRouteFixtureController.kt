// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.overlay.MiniMapLayer

/** 전투 경로 fixture 조정기: 캡처 경로가 요구하는 라운드 카드와 미니맵 상태 전환을 화면 밖에서 재현한다. */
internal object BattleRouteFixtureController {
    /** 라운드 카드 요청: 경로 종류와 최대 라운드에 맞는 표시 숫자 및 완료 콜백 여부를 결정한다. */
    fun roundCard(route: RuntimeBattleRoute?, maxRounds: Int): RoundCard = when (route) {
        RuntimeBattleRoute.ROUND_FINAL -> RoundCard(maxRounds + 1, maxRounds)
        RuntimeBattleRoute.ROUND_ENEMY -> RoundCard(null, maxRounds)
        else -> RoundCard(3.coerceAtMost(maxRounds), maxRounds)
    }

    /** 미니맵 적용: 원본 터치와 슬라이드 완료 순서를 사용해 선택한 캡처 상태까지 전환한다. */
    fun applyMiniMap(route: RuntimeBattleRoute?, miniMap: MiniMapLayer) {
        if (miniMap.shown) {
            miniMap.touch(MiniMapLayer.TOUCH_END)
            miniMap.advance(MiniMapLayer.SLIDE_SECONDS)
        }
        miniMap.touch(MiniMapLayer.TOUCH_END)
        miniMap.advance(MiniMapLayer.SLIDE_SECONDS)
        if (route == RuntimeBattleRoute.MINI_MAP_HIDDEN) {
            miniMap.touch(MiniMapLayer.TOUCH_END)
            miniMap.advance(MiniMapLayer.SLIDE_SECONDS)
        }
    }

    /** 라운드 카드: 화면이 실제 RoundLayer를 생성하는 데 필요한 표시 값 묶음이다. */
    data class RoundCard(val round: Int?, val maxRounds: Int)
}
