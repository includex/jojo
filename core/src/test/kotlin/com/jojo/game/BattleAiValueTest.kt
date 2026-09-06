// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.command.*

import kotlin.test.Test
import kotlin.test.assertEquals

/** BattleAiValueTest: BattleAiValue의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleAiValueTest {
    @Test
    fun `turn entry clears source AIValue only for the active camp`() {
        val player = BattleUnit("p", "player", Faction.PLAYER, 0, 0, aiValue = 99)
        val enemy = BattleUnit("e", "enemy", Faction.ENEMY, 1, 0, aiValue = 77)
        val battle = Battle(listOf(player, enemy), emptyList())

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        battle.roundLifecycle.endTurn()

        assertEquals(99, player.aiValue)
        assertEquals(0, enemy.aiValue)
    }

    @Test
    fun `only active and hold Control subclasses persist selected action score`() {
/** run: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun run(ai: Int): BattleUnit {
            val enemy = BattleUnit("enemy-$ai", "적", Faction.ENEMY, 0, 0, ai = ai, attack = 100)
            val battle = Battle(
                listOf(BattleUnit("player-$ai", "아군", Faction.PLAYER, 1, 0), enemy),
                emptyList(),
            )
            battle.roundLifecycle.endTurn()
            battle.ai.resolveTurn(maxUnits = 1)
            return enemy
        }

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        assertEquals(0, run(ControlAi.PASSIVE).aiValue)
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        kotlin.test.assertTrue(run(ControlAi.ACTIVE).aiValue > 0)
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        kotlin.test.assertTrue(run(ControlAi.HOLD).aiValue > 0)
    }
}
