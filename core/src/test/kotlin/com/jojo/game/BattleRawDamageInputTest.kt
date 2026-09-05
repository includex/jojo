package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * class  `BattleRawDamageInputTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleRawDamageInputTest {
    private fun attackDamage(attacker: BattleUnit, target: BattleUnit): Int {
        val battle = Battle(listOf(attacker, target), events = emptyList())
        return assertIs<TacticalActionResult.Attack>(battle.attack(attacker.id, target.id)).damage
    }

    @Test
    fun `QXJD uses raw martial value instead of final attack ability`() {
/**
 * 공개 메서드 `attacker`
 *
 * ### 파라미터
- `qxjd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

        fun attacker(qxjd: Boolean) = BattleUnit(
            "a", "a", Faction.PLAYER, 0, 0, attack = 300, martial = 50,
            critical = 100, morale = 100, skills = buildMap { put(92, 0); if (qxjd) put(183, 3) },
        )
/**
 * 공개 메서드 `target`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

        fun target() = BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 1, critical = 1, morale = 1)

        val without = attackDamage(attacker(false), target())
        val with = attackDamage(attacker(true), target())
        // floor(Unit.wuwei(WL) * 3 * 10 / 100) = 15, then source applies
        // this seeded attack's source JS-truthy 180% multiplier to the whole harm.
        assertEquals(27, with - without)
    }

    @Test
    fun `KZQB reduces only attacks whose source arm move sound is zero`() {
/**
 * 공개 메서드 `attacker`
 *
 * ### 파라미터
- `moveSound` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

        fun attacker(moveSound: Int) = BattleUnit(
            "a", "a", Faction.PLAYER, 0, 0, attack = 300, critical = 100, morale = 100,
            armMoveSound = moveSound, skills = mapOf(92 to 0),
        )
/**
 * 공개 메서드 `target`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

        fun target() = BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 1, critical = 1, morale = 1, skills = mapOf(139 to 11))

        val horseLike = attackDamage(attacker(0), target())
        val other = attackDamage(attacker(1), target())
        // KZQB is an 11 percentage-point rate reduction before the source
        // critical multiplier and final integer truncation.
        assertEquals(36, other - horseLike)
    }
}
