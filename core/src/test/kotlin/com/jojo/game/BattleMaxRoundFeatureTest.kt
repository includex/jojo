// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals

/** BattleMaxRoundFeatureTest: BattleMaxRoundFeature의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleMaxRoundFeatureTest {
    @Test
    fun `BattleScreen setMaxRound adds original ZJHH four-turn feature only when enabled`() {
/** battle: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

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

        // 테스트 근거: 전투 계산·난수 소비·경계값 (S_12)을 검증한다.
        assertEquals(null, battle.outcome())
        battle.syncScriptedOutcome(BattleOutcome.PLAYER_VICTORY)
        assertEquals(BattleOutcome.PLAYER_VICTORY, battle.outcome())

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        battle.syncScriptedOutcome(null)
        assertEquals(BattleOutcome.PLAYER_VICTORY, battle.outcome())
    }
}
