// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.command.*
import com.jojo.game.domain.battle.BattleTerrainGrid

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.magic.BattleMagicProfile
import com.jojo.game.domain.battle.magic.MagicDamageCalculator
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge
import com.jojo.game.domain.battle.BattleAttributeCalculator

/**
 * `BattleAiScoringEnvironment` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class BattleAiScoringEnvironment(
    val units: () -> Collection<BattleUnit>,
    val unitAt: (Int, Int) -> BattleUnit?,
    val areAllied: (BattleUnit, BattleUnit) -> Boolean,
    val weather: () -> BattleWeather,
    val terrain: BattleTerrainGrid?,
    val terrainMagicFlags: Map<Int, Int>,
    val probabilityResolver: BattleProbabilityResolver,
    val basePhysicalDamageContext: (attacker: BattleUnit, target: BattleUnit, splash: Boolean) -> BasePhysicalDamageContext,
)

/** BattleAiScorer: AI 후보 행동의 공격·안전·목표 기여도를 점수로 계산해 행동 선택에 사용한다. */
internal object BattleAiScorer {


    /**
     * `canAttack`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun canAttack(attacker: BattleUnit, target: BattleUnit): Boolean =
        attacker.attackAllScreen || ((target.tileX - attacker.tileX) to (target.tileY - attacker.tileY)) in attacker.attackOffsets


    /**
     * `canAttackFrom`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun canAttackFrom(attacker: BattleUnit, x: Int, y: Int, target: BattleUnit): Boolean =
        attacker.attackAllScreen || ((target.tileX - x) to (target.tileY - y)) in attacker.attackOffsets

    /**
     * `aiSortValue`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun aiSortValue(
        unit: BattleUnit,
        terrain: BattleTerrainGrid?,
        terrainResumeRates: Map<Int, Int>,
    ): Double {
        /**
         * `wounded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
        /**
         * `resumeHp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val resumeHp = terrainResumeRates[terrain?.terrainAt(unit.tileX, unit.tileY)] ?: 0
        /**
         * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var value = when {
            resumeHp > 0 && !wounded -> 110.0
            wounded -> 30.0
            else -> 0.0
        }
        if (BattleStatus.CONFUSION in unit.statuses) value -= 20.0
        if (BattleStatus.PARALYSIS in unit.statuses) value -= 10.0
        value += when (unit.armType) {
            2 -> (if (unit.remoteAttack) 25 else 10) + 100.0 * unit.hitPoints / unit.maxHitPoints.coerceAtLeast(1)
            0 -> 20 + 100.0 * unit.hitPoints / unit.maxHitPoints.coerceAtLeast(1)
            else -> 30 + 100.0 * (unit.maxHitPoints - unit.hitPoints) / unit.maxHitPoints.coerceAtLeast(1)
        }
        return value + 15 - BattleAttributeCalculator.effectiveMovement(unit)
    }

    /**
     * `cocosAiBaseValueAt`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cocosAiBaseValueAt(
        unit: BattleUnit,
        x: Int,
        y: Int,
        units: Collection<BattleUnit>,
        terrain: BattleTerrainGrid?,
        terrainResumeRates: Map<Int, Int>,
        areAllied: (BattleUnit, BattleUnit) -> Boolean,
    ): Int {
        /**
         * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var value = unit.terrainImpacts[terrain?.terrainAt(x, y)] ?: 100
        /**
         * `wounded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
        if (unit.armType == 1 || unit.remoteAttack || wounded) {
            /**
             * `originalX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val originalX = unit.tileX
            /**
             * `originalY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val originalY = unit.tileY
            unit.tileX = x
            unit.tileY = y
            units.filter { it.visible && it !== unit }.forEach { other ->
                /**
                 * `d` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val d = ControlScoring.coverDistance(
                    unit.tileX, unit.tileY, other.tileX, other.tileY,
                )
                value += ControlScoring.coverPressure(d, areAllied(unit, other))
            }
            unit.tileX = originalX
            unit.tileY = originalY
        }
        if (wounded) value += terrainResumeRates[terrain?.terrainAt(x, y)] ?: 0
        return value
    }

    /**
     * `estimatedAttackValue`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun estimatedAttackValue(
        attacker: BattleUnit,
        target: BattleUnit,
        env: BattleAiScoringEnvironment,
    ): Int = ControlActionScoring.attackValue(
        AiScoringUnit(attacker, env),
        AiScoringUnit(target, env),
        counter = true,
    )

    /**
     * `estimatedMagicValue`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun estimatedMagicValue(
        attacker: BattleUnit,
        target: BattleUnit,
        magic: BattleMagicProfile,
        cache: MutableMap<String, Int>,
        env: BattleAiScoringEnvironment,
    ): Int = ControlActionScoring.magicValue(
        AiMagic(magic),
        AiScoringUnit(attacker, env),
        AiScoringUnit(target, env),
        cache,
        hitRate = { _, _, _ -> env.probabilityResolver.magicHitRate(attacker, target, magic) },
    )

    /**
     * `AiMagic` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    private data class AiMagic(val source: BattleMagicProfile) : ControlScoring.Magic {
        /**
         * `id` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val id get() = source.id
        /**
         * `category` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val category get() = source.category
        /**
         * `type` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val type get() = source.type
        /**
         * `harmType` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val harmType get() = source.harmType
        /**
         * `expendMp` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val expendMp get() = source.expendMp
    }

    /**
     * `AiScoringUnit` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    private class AiScoringUnit(
        /**
         * `source` (BattleUnit,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val source: BattleUnit,
        /**
         * `env` (BattleAiScoringEnvironment,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val env: BattleAiScoringEnvironment,
    ) : ControlScoring.Unit {
        /**
         * `index` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val index: Int get() = source.characterId ?: source.id.hashCode()
        /**
         * `hp` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val hp: Int get() = source.maxHitPoints
        /**
         * `hpCur` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val hpCur: Int get() = source.hitPoints
        /**
         * `mp` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val mp: Int get() = source.maxMagicPoints
        /**
         * `mpCur` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val mpCur: Int get() = source.magicPoints
        /**
         * `armType` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val armType: Int get() = source.armType
        /**
         * `isRemote` (Boolean get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val isRemote: Boolean get() = source.remoteAttack
        /**
         * `famous` (Boolean get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val famous: Boolean get() = source.famous
        /**
         * `mine` (Boolean get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val mine: Boolean get() = source.isPlayerSide()
        /**
         * `ai` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val ai: Int get() = source.ai
        /**
         * `aiValue` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val aiValue: Int get() = source.aiValue
        /**
         * `skill`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun skill(id: Int): Int = source.skills[id]?.and(255) ?: 255
        /**
         * `status`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun status(index: Int): Int = when (index) {
            0, 1, 2, 3, 4, 5 -> when {
                (source.attributeLifts[BattleAttribute.entries[index]] ?: 0) < 0 -> ControlScoring.Lift.DOWN
                (source.attributeLifts[BattleAttribute.entries[index]] ?: 0) > 0 -> ControlScoring.Lift.UP
                else -> ControlScoring.Lift.NORMAL
            }

            7 -> if (BattleStatus.PARALYSIS in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            8 -> if (BattleStatus.SILENCE in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            9 -> if (BattleStatus.CONFUSION in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            10 -> if (BattleStatus.POISON in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            13 -> if (BattleStatus.LOST in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            14 -> if (source.hasActed) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            else -> ControlScoring.Lift.NORMAL
        }

        /**
         * `isCanXue`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun isCanXue(): Boolean = source.hitPoints < source.maxHitPoints * (if (source.famous) 4 else 2) / 10
        /**
         * `isCanLan`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun isCanLan(): Boolean =
            source.magicPoints < source.maxMagicPoints * (if (source.famous) 4 else 2) / 10

        /**
         * `attackHarms`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun attackHarms(target: ControlScoring.Unit): List<ControlScoring.AttackHarm> {
            val primary = (target as? AiScoringUnit)?.source ?: return emptyList()
            if (!source.visible || BattleStatus.CONFUSION in source.statuses ||
                !primary.visible || !canAttack(source, primary)
            ) return emptyList()
            val affected = buildList {
                add(primary to false)
                PhysicalAttackAreaResolver.physicalEffectPositions(source, primary).asSequence()
                    .mapNotNull { (x, y) -> env.unitAt(x, y) }
                    .filter { it !== primary && it.visible && !env.areAllied(source, it) }
                    .forEach { add(it to true) }
            }
            var flag = when {
                primary.skills.keys.any {
                    it in intArrayOf(
                        226,
                        44,
                        251,
                        50
                    ) && primary.skills[it]?.and(255) != 255
                } -> 0

                canAttack(primary, source) -> 1
                else -> 0
            }
            flag = flag or when (source.armType) {
                0 -> 8; 1 -> 16; else -> 0
            }
            return affected.map { (victim, splash) ->
                val harm = PhysicalDamageCalculator.basePhysicalDamage(
                    source,
                    victim,
                    env.basePhysicalDamageContext(source, victim, splash),
                )
                if (victim.famous) flag = flag or 2
                if (harm >= victim.hitPoints) flag = flag or 4
                val hitRate =
                    if (BattleStatus.CONFUSION in victim.statuses) 100 else env.probabilityResolver.physicalHitRate(
                        source,
                        victim
                    )
                ControlScoring.AttackHarm(harm, AiScoringUnit(victim, env), flag, hitRate)
            }
        }

        /**
         * `magicHarm`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun magicHarm(magic: ControlScoring.Magic, target: ControlScoring.Unit): Int {
            val profile = (magic as? AiMagic)?.source ?: return 0
            val victim = (target as? AiScoringUnit)?.source ?: return 0
            val base = maxOf(
                1,
                (BattleAttributeCalculator.effective(
                    source,
                    BattleAttribute.SPIRIT
                ) - BattleAttributeCalculator.effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + source.level
            )
            return when (profile.type) {
                19 -> source.hitPoints * profile.power / 100 + if (profile.id == 39 || profile.id == 41) source.spirit / 10 else source.spirit / 2
                20 -> profile.expendMp
                4 -> if (profile.category == 2) victim.maxHitPoints * profile.power / 100 else offensiveMagicHarm(
                    base,
                    profile,
                    victim
                )

                else -> offensiveMagicHarm(base, profile, victim)
            }
        }

        /**
         * `offensiveMagicHarm`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        private fun offensiveMagicHarm(base: Int, magic: BattleMagicProfile, victim: BattleUnit): Int {
            var value = maxOf(1, base * magic.power / 100 * victim.magicHarmRate / 100)
            value += MagicDamageCalculator.magicFlatSkillDamage(source, magic)
            val flagBonus =
                source.skills[292]?.and(255)?.takeIf { it != 255 }?.let { env.probabilityResolver.flagRandom(0, 5) }
                    ?: 0
            value = maxOf(1, value * MagicDamageCalculator.magicSkillDamageRate(source, victim, magic, flagBonus) / 100)
            value = value * MagicDamageCalculator.magicWeatherRate(magic, env.weather()) / 100
            value = value * MagicDamageCalculator.offensiveMagicTerrainRate(
                victim,
                magic,
                env.terrain,
                env.terrainMagicFlags
            ) / 100
            val minimum = if (!source.isPlayerSide()) {
                maxOf(1, minOf(7, env.units().count { it.visible && it.isPlayerSide() }) * source.maxMagicPoints / 100)
            } else 1
            return maxOf(minimum, value)
        }
    }
}
