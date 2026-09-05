package com.jojo.game
import com.jojo.game.presentation.battle.timeline.*

import com.jojo.game.domain.battle.PhysicalTarget

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattlePhysicalPresentationTimelineTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
        // Source anime21: hit=22, duration=40; anime25: hit=11, duration=29.
        // playAtkAnime resumes at hit and waits anime32's 14 ticks, so both
        // attacker clips are replaced by default four ticks before FINISHED.
        assertEquals(36f / 24f, BattlePhysicalPresentationTimeline.scriptedAttackDuration(1))
        assertEquals(25f / 24f, BattlePhysicalPresentationTimeline.scriptedAttackDuration(0))

        // ATTACK_FLAG bit 2 selects anime26 guard, whose duration is 17 ticks.
        assertEquals(28f / 24f, BattlePhysicalPresentationTimeline.scriptedAttackDuration(2))
        assertEquals(39f / 24f, BattlePhysicalPresentationTimeline.scriptedAttackDuration(3))
    }
}
