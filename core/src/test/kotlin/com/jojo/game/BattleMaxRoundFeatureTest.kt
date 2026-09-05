package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattleMaxRoundFeatureTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleMaxRoundFeatureTest {
    @Test
    fun `BattleScreen setMaxRound adds original ZJHH four-turn feature only when enabled`() {
/**
 * 공개 메서드 `battle`
 *
 * ### 파라미터
- `features` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

        fun battle(features: Int) = Battle(
            units = listOf(BattleUnit("u", "u", Faction.PLAYER, 0, 0)),
            events = emptyList(), enabledFeatures = features,
        )

        battle(0).also { it.setMaxRounds(12); assertEquals(12, it.maxRounds) }
        battle(8).also { it.setMaxRounds(12); assertEquals(16, it.maxRounds) }
        battle(16).also { it.setMaxRounds(12); assertEquals(12, it.maxRounds) }
    }

    @Test
    fun `resumed scene1 non annihilation result is mirrored into tactical outcome`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("mine", "mine", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "enemy", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
        )

        // S_12's escape branch ends while enemies still exist, so the
        // ordinary annihilation predicate deliberately remains unresolved.
        assertEquals(null, battle.outcome())
        battle.syncScriptedOutcome(BattleOutcome.PLAYER_VICTORY)
        assertEquals(BattleOutcome.PLAYER_VICTORY, battle.outcome())

        // A later ordinary scene1 pass carries no new result and must not
        // erase the authored terminal state.
        battle.syncScriptedOutcome(null)
        assertEquals(BattleOutcome.PLAYER_VICTORY, battle.outcome())
    }
}
