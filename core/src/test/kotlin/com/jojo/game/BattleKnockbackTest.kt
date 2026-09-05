package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * class  `BattleKnockbackTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleKnockbackTest {
    @Test
    fun `TPGJ pushes a defender one passable tile directly away`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(92 to 0, 221 to 5)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, defense = 20, hitPoints = 500, maxHitPoints = 500, attackOffsets = emptySet()),
            ), events = emptyList(),
        )
        assertIs<TacticalActionResult.Attack>(battle.combat.attack("a", "t"))
        assertEquals(2 to 0, battle.units.getValue("t").let { it.tileX to it.tileY })
    }

    @Test
    fun `TPGJ and YI_BU use source canBack result for blocked retreat`() {
/**
 * 공개 메서드 `battle`
 *
 * ### 파라미터
- `blocked` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

        fun battle(blocked: Boolean) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(92 to 0, 221 to 5)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, defense = 20, hitPoints = 500, maxHitPoints = 500, attackOffsets = emptySet(), skills = mapOf(250 to 10)),
            ), events = emptyList(), blockedTiles = if (blocked) setOf(2 to 0) else emptySet(),
        )
        val open = battle(false)
        val blocked = battle(true)
        val openResult = assertIs<TacticalActionResult.Attack>(open.combat.attack("a", "t"))
        val blockedResult = assertIs<TacticalActionResult.Attack>(blocked.combat.attack("a", "t"))
        // These are _countAttackHarmRate percentage points, not fixed harm:
        // blocked = +5(TPGJ)-5(YI_BU), open = -10(YI_BU).
        assertEquals(6, blockedResult.damage - openResult.damage)
        assertEquals(1 to 0, blocked.units.getValue("t").let { it.tileX to it.tileY })
    }
}
