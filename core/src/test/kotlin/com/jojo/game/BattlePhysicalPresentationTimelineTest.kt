// Test
package com.jojo.game

import com.jojo.game.presentation.battle.timeline.*

import com.jojo.game.domain.battle.PhysicalTarget

import kotlin.test.Test
import kotlin.test.assertEquals

/** BattlePhysicalPresentationTimelineTest: BattlePhysicalPresentationTimeline의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattlePhysicalPresentationTimelineTest {
    @Test
    fun `BattleScreen attack2 presents primary then every CTGJ target before follow up`() {
        val frames = BattlePhysicalPresentationTimeline.sequence(
            primaryId = "primary", primaryDamage = 50,
            splash = listOf(PhysicalTarget("area-a", 40), PhysicalTarget("area-b", 30)),
            hitAt = 1f,
            durationFor = mapOf("primary" to .2f, "area-a" to .3f, "area-b" to .4f)::getValue,
        )

        assertEquals(
            listOf(
                BattlePhysicalPresentationTimeline.Hit("primary", 50, 1f, 1.2f),
                BattlePhysicalPresentationTimeline.Hit("area-a", 40, 1.2f, 1.5f),
                BattlePhysicalPresentationTimeline.Hit("area-b", 30, 1.5f, 1.9f),
            ),
            frames,
        )
        assertEquals(1.9f, frames.last().endsAt)
    }

    @Test
    fun `scripted attack resumes at hit plus target completion rather than attacker finish`() {
        // 테스트 근거: 전투 계산·난수 소비·경계값 (FINISHED)을 검증한다.
        assertEquals(36f / 24f, BattlePhysicalPresentationTimeline.scriptedAttackDuration(1))
        assertEquals(25f / 24f, BattlePhysicalPresentationTimeline.scriptedAttackDuration(0))

        // 테스트 근거: 전투 계산·난수 소비·경계값 (ATTACK_FLAG)을 검증한다.
        assertEquals(28f / 24f, BattlePhysicalPresentationTimeline.scriptedAttackDuration(2))
        assertEquals(39f / 24f, BattlePhysicalPresentationTimeline.scriptedAttackDuration(3))
    }
}
