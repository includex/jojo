// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** BattleRawDamageInputTest: BattleRawDamageInput의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleRawDamageInputTest {
    private fun attackDamage(attacker: BattleUnit, target: BattleUnit): Int {
        val battle = Battle(listOf(attacker, target), events = emptyList())
        return assertIs<TacticalActionResult.Attack>(battle.combat.attack(attacker.id, target.id)).damage
    }

    @Test
    fun `QXJD uses raw martial value instead of final attack ability`() {
/** attacker: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun attacker(qxjd: Boolean) = BattleUnit(
            "a", "a", Faction.PLAYER, 0, 0, attack = 300, martial = 50,
            critical = 100, morale = 100, skills = buildMap { put(92, 0); if (qxjd) put(183, 3) },
        )
/** target: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun target() = BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 1, critical = 1, morale = 1)

        val without = attackDamage(attacker(false), target())
        val with = attackDamage(attacker(true), target())
        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        assertEquals(27, with - without)
    }

    @Test
    fun `KZQB reduces only attacks whose source arm move sound is zero`() {
/** attacker: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun attacker(moveSound: Int) = BattleUnit(
            "a", "a", Faction.PLAYER, 0, 0, attack = 300, critical = 100, morale = 100,
            armMoveSound = moveSound, skills = mapOf(92 to 0),
        )
/** target: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun target() = BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 1, critical = 1, morale = 1, skills = mapOf(139 to 11))

        val horseLike = attackDamage(attacker(0), target())
        val other = attackDamage(attacker(1), target())
        // 테스트 근거: 전투 계산·난수 소비·경계값 (KZQB)을 검증한다.
        assertEquals(36, other - horseLike)
    }
}
