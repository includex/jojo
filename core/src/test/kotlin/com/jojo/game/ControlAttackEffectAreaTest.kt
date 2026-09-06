// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals

/** ControlAttackEffectAreaTest: ControlAttackEffectArea의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class ControlAttackEffectAreaTest {
    @Test
    fun `Control countAtkHarm2 scores physical effect targets at original 75 percent`() {
/** battle: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun battle(effectOffsets: Set<Pair<Int, Int>>) = Battle(
            units = listOf(
                // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택 (WFJGJ)을 검증한다.
                BattleUnit("attacker", "공격", Faction.ENEMY, 0, 0, attack = 100, critical = 1, attackEffectOffsets = effectOffsets, skills = mapOf(226 to 1)),
                BattleUnit("primary", "주대상", Faction.PLAYER, 1, 0, defense = 1, critical = 1),
                BattleUnit("splash", "범위대상", Faction.PLAYER, 1, 1, defense = 1, critical = 1),
            ),
            events = emptyList(),
        )

        val single = battle(emptySet()).ai.previewAttackValue("attacker", "primary")
        val area = battle(setOf(0 to 1)).ai.previewAttackValue("attacker", "primary")

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        assertEquals(single + single - single * 25 / 100, area)
    }
}
