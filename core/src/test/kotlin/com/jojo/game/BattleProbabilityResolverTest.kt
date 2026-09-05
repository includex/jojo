package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge
import com.jojo.game.application.runtime.BattleTraceRandomStreams

import java.util.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleProbabilityResolverTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleProbabilityResolverTest {
    private fun unit(
        id: String,
        critical: Int = 35,
        morale: Int = 35,
        remoteAttack: Boolean = false,
        skills: Map<Int, Int> = emptyMap(),
        gauges: Map<Int, Int> = emptyMap(),
    ) = BattleUnit(
        id = id,
        name = id,
        faction = Faction.PLAYER,
        tileX = 0,
        tileY = 0,
        critical = critical,
        morale = morale,
        remoteAttack = remoteAttack,
        skills = skills,
        rateAccumulators = gauges.toMutableMap(),
    )

    private fun resolver(random: Random = Random(0)) = BattleProbabilityResolver(random, null)

    @Test
    fun `opposed gauges accumulate wrap and double only the skill 111 incoming rate`() {
        val ordinaryAttacker = unit("ordinary", gauges = mapOf(BattleRateGauge.PHYSICAL_HIT.index to 75))
        val ordinaryDefender = unit("ordinary-target")
        assertTrue(
            resolver().countRate(
                ordinaryAttacker,
                ordinaryDefender,
                BattleRateGauge.PHYSICAL_HIT,
                BattleRateGauge.PHYSICAL_GUARD,
                25,
            ),
        )
        assertEquals(0, ordinaryAttacker.rateAccumulators[BattleRateGauge.PHYSICAL_HIT.index])
        assertEquals(75, ordinaryDefender.rateAccumulators[BattleRateGauge.PHYSICAL_GUARD.index])

        val boostedAttacker = unit("boosted", skills = mapOf(111 to 0))
        val boostedDefender = unit("boosted-target")
        assertTrue(
            resolver().countRate(
                boostedAttacker,
                boostedDefender,
                BattleRateGauge.PHYSICAL_HIT,
                BattleRateGauge.PHYSICAL_GUARD,
                40,
            ),
        )
        assertEquals(0, boostedAttacker.rateAccumulators[BattleRateGauge.PHYSICAL_HIT.index])
        assertEquals(60, boostedDefender.rateAccumulators[BattleRateGauge.PHYSICAL_GUARD.index])
    }

    @Test
    fun `physical critical and magic rate formulas retain their boundary truncation`() {
        val resolver = resolver()
        assertEquals(100, resolver.physicalHitRate(100, 50))
        assertEquals(96, resolver.physicalHitRate(80, 50))
        assertEquals(90, resolver.physicalHitRate(50, 50))
        assertEquals(60, resolver.physicalHitRate(25, 50))
        assertEquals(25, resolver.physicalHitRate(1, 100))

        assertEquals(100, resolver.criticalRate(150, 50))
        assertEquals(20, resolver.criticalRate(100, 50))
        assertEquals(12, resolver.criticalRate(80, 50))
        assertEquals(0, resolver.criticalRate(49, 50))

        assertEquals(96, resolver.magicHitRate(40, 40, 25, 25))
        assertEquals(60, resolver.magicHitRate(10, 15, 25, 25))
        assertEquals(25, resolver.magicHitRate(1, 0, 50, 50))
    }

    @Test
    fun `forced and immune outcomes are applied after their opposed gauges mutate`() {
        val resolver = resolver()
        val continuousAttacker = unit(
            "continuous",
            critical = 80,
            skills = mapOf(197 to 0),
        )
        val continuousTarget = unit("continuous-target", critical = 50)
        assertTrue(resolver.continuousAttack(continuousAttacker, continuousTarget))
        assertEquals(12, continuousAttacker.rateAccumulators[BattleRateGauge.CONTINUOUS_ATTACK.index])
        assertEquals(0, continuousTarget.rateAccumulators[BattleRateGauge.CONTINUOUS_GUARD.index])

        val immuneAttacker = unit(
            "immune-attacker",
            remoteAttack = true,
            skills = mapOf(92 to 0),
        )
        val immuneTarget = unit("immune-target", skills = mapOf(48 to 0))
        assertFalse(resolver.physicalHit(immuneAttacker, immuneTarget, 100))
        assertEquals(0, immuneAttacker.rateAccumulators[BattleRateGauge.PHYSICAL_HIT.index])
        assertEquals(0, immuneTarget.rateAccumulators[BattleRateGauge.PHYSICAL_GUARD.index])

        val guaranteedAttacker = unit("guaranteed", skills = mapOf(92 to 0))
        val guaranteedTarget = unit("guaranteed-target")
        assertTrue(resolver.physicalHit(guaranteedAttacker, guaranteedTarget, 25))
        assertEquals(25, guaranteedAttacker.rateAccumulators[BattleRateGauge.PHYSICAL_HIT.index])

        val magicAttacker = unit(
            "magic",
            gauges = mapOf(BattleRateGauge.MAGIC_HIT.index to 75),
        )
        val magicTarget = unit("magic-target", skills = mapOf(17 to 0))
        assertFalse(resolver.magicHit(magicAttacker, magicTarget, 25))
        assertEquals(0, magicAttacker.rateAccumulators[BattleRateGauge.MAGIC_HIT.index])
        assertEquals(75, magicTarget.rateAccumulators[BattleRateGauge.MAGIC_GUARD.index])

        val criticalAttacker = unit("critical", morale = 1, skills = mapOf(270 to 0))
        val criticalTarget = unit("critical-target", morale = 100)
        assertTrue(resolver.criticalHit(criticalAttacker, criticalTarget))
        assertTrue(BattleRateGauge.CRITICAL_HIT.index in criticalAttacker.rateAccumulators)
        assertTrue(BattleRateGauge.CRITICAL_GUARD.index in criticalTarget.rateAccumulators)
    }

    @Test
    fun `random channels are inclusive and no-stream status duration consumes no fallback draw`() {
/**
 * class  `MaximumRandom`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

        class MaximumRandom : Random() {
            var draws = 0
            override fun nextInt(bound: Int): Int {
                draws++
                return bound - 1
            }
        }

        val fallback = MaximumRandom()
        val fallbackResolver = resolver(fallback)
        assertEquals(4, fallbackResolver.defaultRandom(2, 4))
        assertEquals(1, fallbackResolver.flagRandom(-1, 1))
        assertEquals(100, fallbackResolver.random100())
        assertEquals(3, fallbackResolver.rollStatusDuration())
        assertEquals(3, fallback.draws)

        val streams = BattleTraceRandomStreams(toolSeed = 1_000, mathSeed = 1)
        val streamedResolver = BattleProbabilityResolver(Random(0), streams)
        assertTrue(streamedResolver.defaultRandom(2, 4) in 2..4)
        assertTrue(streamedResolver.flagRandom(5, 6) in 5..6)
        assertTrue(streamedResolver.random100() in 0..100)
        assertTrue(streamedResolver.rollStatusDuration() in 1..3)
        assertEquals(listOf(0, 1, 0, 0), streams.events.map { it.flag })
        assertEquals(listOf(2 to 4, 5 to 6, 0 to 100, 1 to 3), streams.events.map { it.min to it.max })
    }
}
