package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `ControlAttackEffectAreaTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ControlAttackEffectAreaTest {
    @Test
    fun `Control countAtkHarm2 scores physical effect targets at original 75 percent`() {
/**
 * 공개 메서드 `battle`
 *
 * ### 파라미터
- `effectOffsets` (`Set<Pair<Int, Int>>`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

        fun battle(effectOffsets: Set<Pair<Int, Int>>) = Battle(
            units = listOf(
                // WFJGJ is checked on the attacking unit by
                // Control._countAttackValue; it removes the counter branch
                // so this fixture isolates countAtkHarm2's splash reduction.
                BattleUnit("attacker", "공격", Faction.ENEMY, 0, 0, attack = 100, critical = 1, attackEffectOffsets = effectOffsets, skills = mapOf(226 to 1)),
                BattleUnit("primary", "주대상", Faction.PLAYER, 1, 0, defense = 1, critical = 1),
                BattleUnit("splash", "범위대상", Faction.PLAYER, 1, 1, defense = 1, critical = 1),
            ),
            events = emptyList(),
        )

        val single = battle(emptySet()).ai.previewAttackValue("attacker", "primary")
        val area = battle(setOf(0 to 1)).ai.previewAttackValue("attacker", "primary")

        // countAtkHarm2 deducts floor(25% * raw harm), rather than flooring
        // a 75%-multiplied score.  With matching HP/hit-rate this is
        // single - floor(single * 25 / 100), including its rounding edge.
        assertEquals(single + single - single * 25 / 100, area)
    }
}
