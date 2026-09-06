// Battle
package com.jojo.game.domain.battle.magic

import com.jojo.game.domain.battle.*

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleAttributeCalculator

/** MagicTargetResolver: 마법 대상 판별기이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
internal object MagicTargetResolver {

    /**
     * `resolveTarget`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun resolveTarget(
        pass: Int,
        attacker: BattleUnit,
        victim: BattleUnit,
        magic: BattleMagicProfile,
        magicCritical: Boolean,
        env: MagicEnvironment,
    ): Pair<MagicTarget, MagicLocalSettlementEntry?> {
        /**
         * `statusesBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val statusesBefore = victim.statuses.toMap()
        /**
         * `liftsBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val liftsBefore = victim.attributeLifts.toMap()
        /**
         * `liftRoundsBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val liftRoundsBefore = victim.attributeLiftRounds.toMap()

        /**
         * `magicHarm`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun magicHarm(value: Int): Int {
            var result = if (pass > 0) kotlin.math.floor(value * .9).toInt() else value
            if (magicCritical) result += kotlin.math.floor(result * .5).toInt()
            return result
        }

        /**
         * `wrap`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun wrap(result: MagicTarget): Pair<MagicTarget, MagicLocalSettlementEntry?> {
            val entry = if (result.hit) MagicLocalSettlementEntry(
                victim.id,
                statusesBefore,
                victim.statuses.toMap(),
                liftsBefore,
                victim.attributeLifts.toMap(),
                hasStatesPayload = true,
                attributeLiftRoundsBefore = liftRoundsBefore,
                attributeLiftRoundsAfter = victim.attributeLiftRounds.toMap(),
            ) else null
            return result to entry
        }

        if (magic.type == 22) { // HUIGUI
            victim.hasActed = false
            attacker.ai = 0
            return wrap(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false))
        }
        if (magic.type == 25 && magic.category == 29) { // SISHEN / BH
            /**
             * `healing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val healing = victim.maxHitPoints - victim.hitPoints
            victim.setCurHp(victim.maxHitPoints)
            victim.statuses.clear()
            return wrap(
                MagicTarget(
                    victim.id,
                    damage = 0,
                    healing = healing,
                    hitRate = 100,
                    hit = true,
                    defeated = false
                )
            )
        }
        if (magic.type == 26 || magic.type == 28) { // BAQI / SHUAIQI
            /**
             * `lift` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val lift = if (magic.type == 26) 1 else -1
            /**
             * `attributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val attributes = listOf(
                BattleAttribute.ATTACK,
                BattleAttribute.DEFENSE,
                BattleAttribute.SPIRIT,
                BattleAttribute.CRITICAL,
                BattleAttribute.MORALE
            )
                .associateWith { attribute -> victim.applyAttributeLift(attribute, lift, 3) }
            return wrap(
                MagicTarget(
                    victim.id,
                    damage = 0,
                    hitRate = 100,
                    hit = true,
                    defeated = false,
                    attributes = attributes
                )
            )
        }
        if (magic.type == 27) { // QIANGXING
            /**
             * `applied` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val applied = victim.applyAttributeLift(BattleAttribute.MOVEMENT, 1, 3)
            return wrap(
                MagicTarget(
                    victim.id,
                    damage = 0,
                    hitRate = 100,
                    hit = true,
                    defeated = false,
                    attribute = BattleAttribute.MOVEMENT,
                    lift = applied
                )
            )
        }
        if (magic.type == 6) { // XISHOU_MP
            /**
             * `hitRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val hitRate = env.probabilityResolver.magicHitRate(attacker, victim, magic)
            /**
             * `hit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val hit = env.probabilityResolver.magicHit(attacker, victim, hitRate)
            /**
             * `base` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val base = maxOf(
                1,
                (BattleAttributeCalculator.effective(
                    attacker,
                    BattleAttribute.SPIRIT
                ) - BattleAttributeCalculator.effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + attacker.level
            )
            /**
             * `drained` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val drained = if (hit) minOf(victim.magicPoints, maxOf(1, magicHarm(base * magic.power / 100))) else 0
            victim.addMpcur(-drained)
            /**
             * `recovered` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val recovered = minOf(attacker.maxMagicPoints - attacker.magicPoints, drained)
            attacker.addMpcur(recovered)
            return wrap(
                MagicTarget(
                    victim.id,
                    damage = 0,
                    magicRecovery = recovered,
                    magicDrain = drained,
                    hitRate = hitRate,
                    hit = hit,
                    defeated = false
                )
            )
        }
        /**
         * `status` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val status = MagicDamageCalculator.statusEffect(magic.category)
        /**
         * `appliedStatus` (BattleStatus?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var appliedStatus: BattleStatus? = null
        if (status != null) {
            /**
             * `hitRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val hitRate = env.probabilityResolver.magicHitRate(attacker, victim, magic)
            /**
             * `hit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val hit = env.probabilityResolver.magicHit(attacker, victim, hitRate)
            if (hit) {
                victim.statuses[status] = env.statusDuration(status, victim)
                appliedStatus = status
            }
            if (magic.harmType == 4) {
                return wrap(
                    MagicTarget(
                        victim.id,
                        damage = 0,
                        status = appliedStatus,
                        hitRate = hitRate,
                        hit = hit,
                        defeated = false
                    )
                )
            }
        }
        /**
         * `attributeChange` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attributeChange = MagicDamageCalculator.attributeChange(magic.category)
        if (magic.type == 21) { // JUEXING
            /**
             * `hadStatus` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val hadStatus = victim.statuses.isNotEmpty()
            victim.statuses.clear()
            return wrap(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = hadStatus, defeated = false))
        }
        if (magic.type == 7 || magic.type == 11) { // NLXJ / TSNL
            /**
             * `lift` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val lift = if (magic.type == 7) -1 else 1
            /**
             * `attributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val attributes = when (victim.armType) {
                1 -> mapOf(BattleAttribute.SPIRIT to lift)
                2 -> mapOf(BattleAttribute.ATTACK to lift)
                else -> mapOf(BattleAttribute.ATTACK to lift, BattleAttribute.SPIRIT to lift)
            }.mapValues { (attribute, value) -> victim.applyAttributeLift(attribute, value, 3) }
            return wrap(
                MagicTarget(
                    victim.id,
                    damage = 0,
                    hitRate = 100,
                    hit = true,
                    defeated = false,
                    attributes = attributes
                )
            )
        }
        if (attributeChange != null) {
            val (attribute, lift) = attributeChange
            /**
             * `hitRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val hitRate = env.probabilityResolver.magicHitRate(attacker, victim, magic)
            /**
             * `hit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val hit = env.probabilityResolver.magicHit(attacker, victim, hitRate)
            /**
             * `appliedLift` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            var appliedLift = 0
            if (hit) {
                appliedLift = victim.applyAttributeLift(attribute, lift, 3)
            }
            return wrap(
                MagicTarget(
                    targetId = victim.id, damage = 0, hitRate = hitRate, hit = hit, defeated = false,
                    attribute = attribute.takeIf { hit }, lift = appliedLift,
                )
            )
        }
        if (magic.type == 19) {
            /**
             * `base` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val base =
                attacker.hitPoints * magic.power / 100 + if (magic.id == 39 || magic.id == 41) attacker.spirit / 10 else attacker.spirit / 2
            /**
             * `healingRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val healingRate =
                MagicDamageCalculator.healingTerrainRate(attacker, magic, env.terrain, env.terrainMagicFlags)
            /**
             * `healing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val healing = minOf(victim.maxHitPoints - victim.hitPoints, maxOf(0, magicHarm(base * healingRate / 100)))
            victim.addHpcur(healing)
            return wrap(
                MagicTarget(
                    victim.id,
                    damage = 0,
                    healing = healing,
                    hitRate = 100,
                    hit = true,
                    defeated = false
                )
            )
        }
        if (magic.type == 20 && magic.category == 24) { // MX
            /**
             * `transferred` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val transferred = minOf(40, maxOf(0, victim.hitPoints - 1))
            if (transferred > 0 && attacker.magicPoints < attacker.maxMagicPoints) {
                victim.addHpcur(-transferred, keepAlive = true)
                /**
                 * `recovered` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val recovered = minOf(attacker.maxMagicPoints - attacker.magicPoints, transferred * 5 / 8)
                attacker.addMpcur(recovered)
                return wrap(
                    MagicTarget(
                        victim.id,
                        damage = transferred,
                        magicRecovery = recovered,
                        hitRate = 100,
                        hit = true,
                        defeated = false
                    )
                )
            }
            return wrap(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = false, defeated = false))
        }
        if (magic.type == 20) { // JMP
            /**
             * `healing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val healing = minOf(victim.maxMagicPoints - victim.magicPoints, magicHarm(magic.expendMp))
            victim.addMpcur(healing)
            return wrap(
                MagicTarget(
                    victim.id,
                    damage = 0,
                    magicRecovery = healing,
                    hitRate = 100,
                    hit = true,
                    defeated = false
                )
            )
        }
        /**
         * `hitRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitRate = env.probabilityResolver.magicHitRate(attacker, victim, magic)
        /**
         * `hit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hit = env.probabilityResolver.magicHit(attacker, victim, hitRate)
        /**
         * `assassination` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val assassination = magic.type == 4 && magic.category == 2
        /**
         * `base` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val base = if (assassination) {
            victim.maxHitPoints * magic.power / 100
        } else {
            maxOf(
                1,
                (BattleAttributeCalculator.effective(
                    attacker,
                    BattleAttribute.SPIRIT
                ) - BattleAttributeCalculator.effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + attacker.level
            )
        }
        /**
         * `damage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val damage = if (hit) {
            if (assassination) maxOf(1, magicHarm(base))
            else {
                /**
                 * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                var value = maxOf(1, base * magic.power / 100 * victim.magicHarmRate / 100)
                value += MagicDamageCalculator.magicFlatSkillDamage(attacker, magic)
                /**
                 * `flagBonus` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val flagBonus = attacker.skills[292]?.and(255)?.takeIf { it != 255 }
                    ?.let { env.probabilityResolver.flagRandom(0, 5) } ?: 0
                value = maxOf(
                    1,
                    value * MagicDamageCalculator.magicSkillDamageRate(attacker, victim, magic, flagBonus) / 100
                )
                value = value * MagicDamageCalculator.magicWeatherRate(magic, env.weather()) / 100
                value = value * MagicDamageCalculator.offensiveMagicTerrainRate(
                    victim,
                    magic,
                    env.terrain,
                    env.terrainMagicFlags
                ) / 100
                /**
                 * `enemyMinimum` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val enemyMinimum = if (!attacker.isPlayerSide()) {
                    maxOf(
                        1,
                        (minOf(
                            7,
                            env.units().count { it.visible && it.isPlayerSide() }) * attacker.maxMagicPoints) / 100
                    )
                } else 1
                magicHarm(maxOf(enemyMinimum, value))
            }
        } else 0
        victim.addHpcur(-damage)
        /**
         * `casterHealing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val casterHealing = if (magic.type == 5 && damage > 0) {
            minOf(attacker.maxHitPoints - attacker.hitPoints, damage).also { attacker.addHpcur(it) }
        } else 0
        /**
         * `defeated` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val defeated = victim.hitPoints <= 0
        if (defeated) {
            env.onDefeat(victim.id)
            env.notifyUnitDefeated(attacker, victim)
        }
        return wrap(
            MagicTarget(
                targetId = victim.id,
                damage = damage,
                healing = 0,
                hitRate = hitRate,
                hit = hit,
                defeated = defeated,
                casterHealing = casterHealing,
                status = appliedStatus,
            )
        )
    }
}
