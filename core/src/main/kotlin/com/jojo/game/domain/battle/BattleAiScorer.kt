package com.jojo.game.domain.battle
import com.jojo.game.domain.battle.BattleTerrainGrid

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge
import com.jojo.game.domain.battle.BattleAttributeCalculator

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

/**
 * Evaluates tactical AI scoring for physical and magic candidate actions,
 * converting domain BattleUnit models into ControlScoring adapters.
 */
internal object BattleAiScorer {

    /**
     * 공개 메서드 `canAttack`
     *
     * ### 파라미터
    - `attacker` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `target` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canAttack(attacker: BattleUnit, target: BattleUnit): Boolean =
        attacker.attackAllScreen || ((target.tileX - attacker.tileX) to (target.tileY - attacker.tileY)) in attacker.attackOffsets

    /**
     * 공개 메서드 `canAttackFrom`
     *
     * ### 파라미터
    - `attacker` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `target` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canAttackFrom(attacker: BattleUnit, x: Int, y: Int, target: BattleUnit): Boolean =
        attacker.attackAllScreen || ((target.tileX - x) to (target.tileY - y)) in attacker.attackOffsets

    fun aiSortValue(
        unit: BattleUnit,
        terrain: BattleTerrainGrid?,
        terrainResumeRates: Map<Int, Int>,
    ): Double {
        val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
        val resumeHp = terrainResumeRates[terrain?.terrainAt(unit.tileX, unit.tileY)] ?: 0
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

    fun cocosAiBaseValueAt(
        unit: BattleUnit,
        x: Int,
        y: Int,
        units: Collection<BattleUnit>,
        terrain: BattleTerrainGrid?,
        terrainResumeRates: Map<Int, Int>,
        areAllied: (BattleUnit, BattleUnit) -> Boolean,
    ): Int {
        var value = unit.terrainImpacts[terrain?.terrainAt(x, y)] ?: 100
        val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
        if (unit.armType == 1 || unit.remoteAttack || wounded) {
            val originalX = unit.tileX
            val originalY = unit.tileY
            unit.tileX = x
            unit.tileY = y
            units.filter { it.visible && it !== unit }.forEach { other ->
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

    fun estimatedAttackValue(
        attacker: BattleUnit,
        target: BattleUnit,
        env: BattleAiScoringEnvironment,
    ): Int = ControlScoring.attackValue(
        AiScoringUnit(attacker, env),
        AiScoringUnit(target, env),
        counter = true,
    )

    fun estimatedMagicValue(
        attacker: BattleUnit,
        target: BattleUnit,
        magic: GameDataCatalog.MagicProfile,
        cache: MutableMap<String, Int>,
        env: BattleAiScoringEnvironment,
    ): Int = ControlScoring.magicValue(
        AiMagic(magic),
        AiScoringUnit(attacker, env),
        AiScoringUnit(target, env),
        cache,
        hitRate = { _, _, _ -> env.probabilityResolver.magicHitRate(attacker, target, magic) },
    )

    private data class AiMagic(val source: GameDataCatalog.MagicProfile) : ControlScoring.Magic {
        override val id get() = source.id
        override val category get() = source.category
        override val type get() = source.type
        override val harmType get() = source.harmType
        override val expendMp get() = source.expendMp
    }

    private class AiScoringUnit(
        val source: BattleUnit,
        val env: BattleAiScoringEnvironment,
    ) : ControlScoring.Unit {
        override val index: Int get() = source.characterId ?: source.id.hashCode()
        override val hp: Int get() = source.maxHitPoints
        override val hpCur: Int get() = source.hitPoints
        override val mp: Int get() = source.maxMagicPoints
        override val mpCur: Int get() = source.magicPoints
        override val armType: Int get() = source.armType
        override val isRemote: Boolean get() = source.remoteAttack
        override val famous: Boolean get() = source.famous
        override val mine: Boolean get() = source.isPlayerSide()
        override val ai: Int get() = source.ai
        override val aiValue: Int get() = source.aiValue
        override fun skill(id: Int): Int = source.skills[id]?.and(255) ?: 255
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

        override fun isCanXue(): Boolean = source.hitPoints < source.maxHitPoints * (if (source.famous) 4 else 2) / 10
        override fun isCanLan(): Boolean =
            source.magicPoints < source.maxMagicPoints * (if (source.famous) 4 else 2) / 10

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

        private fun offensiveMagicHarm(base: Int, magic: GameDataCatalog.MagicProfile, victim: BattleUnit): Int {
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
