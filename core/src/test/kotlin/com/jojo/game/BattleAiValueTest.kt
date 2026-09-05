package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattleAiValueTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleAiValueTest {
    @Test
    fun `turn entry clears source AIValue only for the active camp`() {
        val player = BattleUnit("p", "player", Faction.PLAYER, 0, 0, aiValue = 99)
        val enemy = BattleUnit("e", "enemy", Faction.ENEMY, 1, 0, aiValue = 77)
        val battle = Battle(listOf(player, enemy), emptyList())

        // Player -> enemy.  BattleScreen's turn init clears the camp that is
        // about to act; the inactive unit's AIValue remains available until
        // its own next turn entry.
        battle.roundLifecycle.endTurn()

        assertEquals(99, player.aiValue)
        assertEquals(0, enemy.aiValue)
    }

    @Test
    fun `only active and hold Control subclasses persist selected action score`() {
/**
 * 공개 메서드 `run`
 *
 * ### 파라미터
- `ai` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `BattleUnit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

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

        // CtrlBDCJ extends base Control whose _AIProcess4 is empty. It can
        // attack but must not retain the transient action score.
        assertEquals(0, run(ControlAi.PASSIVE).aiValue)
        // CtrlZDCJ overrides _AIProcess4 and writes info.value.
        kotlin.test.assertTrue(run(ControlAi.ACTIVE).aiValue > 0)
        // CtrlJSYD uses the same override on its current-tile decision.
        kotlin.test.assertTrue(run(ControlAi.HOLD).aiValue > 0)
    }
}
