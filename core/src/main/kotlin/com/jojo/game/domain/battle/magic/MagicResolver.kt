// Battle
package com.jojo.game.domain.battle.magic

import com.jojo.game.domain.battle.*


/** MagicResolver: 마법 판별기이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
internal object MagicResolver {

    /**
     * `castMagic`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
        reaction: Boolean = false,
        bypassCondition: Boolean = false,
        env: MagicEnvironment,
    ): TacticalActionResult {
        if (env.isBattleEnded()) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        /**
         * `attacker` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attacker = env.units().firstOrNull { it.id == attackerId }
            ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        /**
         * `target` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val target = env.units().firstOrNull { it.id == targetId }
            ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        /**
         * `magic` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magic = attacker.magic.firstOrNull { it.id == magicId }
            ?: return TacticalActionResult.Rejected("사용할 수 없는 전략입니다.")
        if (!attacker.visible || !target.visible || (!reaction && (attacker.effectiveFaction() != env.activeFaction() || attacker.hasActed))) {
            return TacticalActionResult.Rejected("현재 유닛은 전략을 사용할 수 없습니다.")
        }
        if (BattleStatus.PARALYSIS in attacker.statuses || BattleStatus.CONFUSION in attacker.statuses || BattleStatus.SILENCE in attacker.statuses) {
            return TacticalActionResult.Rejected("현재 상태에서는 전략을 사용할 수 없습니다.")
        }
        if (magic.target == 2) {
            if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
            attacker.addMpcur(-magic.expendMp)
            if (!reaction) attacker.markActionComplete()
            when (magic.id) {
                58 -> env.setWeather(BattleWeather.HEAVY_RAIN)
                59 -> env.setWeather(BattleWeather.CLEAR)
                60 -> env.setWeather(BattleWeather.CLOUDY)
            }
            return TacticalActionResult.Magic(magic.name, magic.expendMp, emptyList())
        }
        /**
         * `targetsAllies` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetsAllies = magic.target == 1
        /**
         * `targetsAny` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetsAny = magic.target == 3
        if (!targetsAny && ((targetsAllies && !env.areAllied(attacker, target)) || (!targetsAllies && env.areAllied(
                attacker,
                target
            )))
        ) {
            return TacticalActionResult.Rejected(if (targetsAllies) "아군만 대상으로 할 수 있는 전략입니다." else "적군만 대상으로 할 수 있는 전략입니다.")
        }
        /**
         * `offset` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offset = target.tileX - attacker.tileX to target.tileY - attacker.tileY
        if (magic.category !in setOf(1, 29) && !magic.hitArea.allScreen && offset !in magic.hitArea.offsets) {
            return TacticalActionResult.Rejected("전략 범위를 벗어났습니다.")
        }
        if (!MagicDamageCalculator.magicTerrainAllowed(magic, target)) {
            return TacticalActionResult.Rejected("이 지형에서는 사용할 수 없는 전략입니다.")
        }
        if (!bypassCondition) {
            MagicDamageCalculator.magicConditionReason(attacker, magic, env.weather())?.let {
                return TacticalActionResult.Rejected(it)
            }
        }
        if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
        attacker.addMpcur(-magic.expendMp)
        if (!reaction) attacker.markActionComplete()

        /**
         * `magicCritical` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magicCritical = magic.harmType != 4 && if (attacker.skills[269]?.and(255)?.let { it != 255 } == true) {
            true
        } else {
            env.probabilityResolver.criticalHit(attacker, target)
        }
        /**
         * `offsets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offsets = magic.effectOffsets + (0 to 0)
        /**
         * `experienceTargets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val experienceTargets = (env.units() + env.pendingPresentationUnits()).associateBy { it.id }


        /**
         * `MagicEquipmentRecord` 클래스: magic 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class MagicEquipmentRecord(
            /**
             * `recipient` (BattleUnit,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val recipient: BattleUnit,
            /**
             * `opponent` (BattleUnit,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val opponent: BattleUnit,
            /**
             * `kind` (BattleEquipmentExperienceKind,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val kind: BattleEquipmentExperienceKind,
            /**
             * `amount` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val amount: Int,
        )

        /**
         * `magicEquipmentByRecipient` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magicEquipmentByRecipient = linkedMapOf<Pair<String, BattleEquipmentExperienceKind>, MagicEquipmentRecord>()


        /**
         * `recordMagicEquipment`: 타입의 핵심 동작을 수행한다.
         * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun recordMagicEquipment(
            recipient: BattleUnit,
            opponent: BattleUnit,
            resolvedHarm: Int,
            kind: BattleEquipmentExperienceKind
        ) {
            /**
             * `amount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val amount = env.equipmentExperienceAmount(recipient, opponent, resolvedHarm, kind)
            /**
             * `key` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val key = recipient.id to kind
            if (amount > (magicEquipmentByRecipient[key]?.amount ?: 0)) {
                magicEquipmentByRecipient[key] = MagicEquipmentRecord(recipient, opponent, kind, amount)
            }
        }

        /**
         * `effectCandidates` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val effectCandidates = env.units().filter { unit ->
            unit.visible && MagicDamageCalculator.magicTerrainAllowed(magic, unit) &&
                    (targetsAny || env.areAllied(unit, attacker) == targetsAllies) &&
                    (unit.tileX - target.tileX to (unit.tileY - target.tileY)) in offsets
        }.toList()

        /**
         * `repeatCount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val repeatCount = if (attacker.skills[16]?.and(255)?.let { it != 255 } == true) 2 else 1
        /**
         * `criticalSpeeches` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val criticalSpeeches = mutableListOf<String?>()
        /**
         * `localSettlements` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val localSettlements = mutableListOf<MagicLocalSettlement>()
        /**
         * `resultPasses` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val resultPasses = buildList {
            repeat(repeatCount) { pass ->
                criticalSpeeches += env.resolveCriticalSpeech(attacker, magicCritical)
                /**
                 * `affectedUnits` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val affectedUnits = when (magic.category) {
                    1, 29 -> env.units().filter { unit ->
                        unit.visible && (targetsAny || env.areAllied(unit, attacker) == targetsAllies)
                    }.toList()

                    26 -> if (effectCandidates.isEmpty()) emptyList() else List(5) {
                        effectCandidates[env.probabilityResolver.defaultRandom(0, effectCandidates.lastIndex)]
                    }

                    else -> effectCandidates
                }
                /**
                 * `localEntries` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val localEntries = mutableListOf<MagicLocalSettlementEntry>()
                add(affectedUnits.map { victim ->
                    val (result, entry) = MagicTargetResolver.resolveTarget(
                        pass,
                        attacker,
                        victim,
                        magic,
                        magicCritical,
                        env
                    )
                    if (entry != null) localEntries += entry
                    result
                })
                localSettlements += MagicLocalSettlement(localEntries)
            }
        }
        /**
         * `results` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val results = resultPasses.flatten()
        /**
         * `reward` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val reward = results.mapNotNull { result ->
            experienceTargets[result.targetId]?.let { victim ->
                env.battleExperience(attacker, victim, false)
            }
        }.maxOrNull()
        if (reward != null) env.notifyBattleExperience(attacker, reward)
        results.forEach { result ->
            /**
             * `victim` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val victim = experienceTargets[result.targetId] ?: return@forEach
            /**
             * `resolvedHarm` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val resolvedHarm = result.magicDrain.takeIf { it > 0 } ?: result.damage
            if (magic.harmType != 4) {
                recordMagicEquipment(victim, attacker, resolvedHarm, BattleEquipmentExperienceKind.ARMOR)
            }
            if (attacker.armType != 2) {
                recordMagicEquipment(attacker, victim, resolvedHarm, BattleEquipmentExperienceKind.WEAPON)
            }
        }
        magicEquipmentByRecipient.values.forEach { record ->
            env.notifyEquipmentExperienceAward(record.recipient, record.opponent, record.amount, record.kind)
        }
        return TacticalActionResult.Magic(
            magic.name, magic.expendMp, results, resultPasses,
            critical = magicCritical, criticalSpeeches = criticalSpeeches,
            localSettlements = localSettlements,
        )
    }

    /**
     * `castMagicAt`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun castMagicAt(
        attackerId: String,
        targetX: Int,
        targetY: Int,
        magicId: Int,
        env: MagicEnvironment,
    ): TacticalActionResult {
        if (env.isBattleEnded()) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        /**
         * `attacker` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attacker = env.units().firstOrNull { it.id == attackerId }
            ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        /**
         * `magic` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magic = attacker.magic.firstOrNull { it.id == magicId }
            ?: return TacticalActionResult.Rejected("사용할 수 없는 전략입니다.")
        if (magic.type != 37) return TacticalActionResult.Rejected("좌표를 대상으로 할 수 없는 전략입니다.")
        if (!attacker.visible || attacker.effectiveFaction() != env.activeFaction() || attacker.hasActed) {
            return TacticalActionResult.Rejected("현재 유닛은 전략을 사용할 수 없습니다.")
        }
        if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
        if (env.unitAt(targetX, targetY) != null || targetX < 0 || targetY < 0 ||
            env.terrain?.let { targetX >= it.width || targetY >= it.height } == true
        ) {
            return TacticalActionResult.Rejected("이동할 수 없는 칸입니다.")
        }
        /**
         * `offset` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offset = targetX - attacker.tileX to targetY - attacker.tileY
        if (!magic.hitArea.allScreen && offset !in magic.hitArea.offsets) {
            return TacticalActionResult.Rejected("전략 범위를 벗어났습니다.")
        }
        attacker.addMpcur(-magic.expendMp)
        attacker.tileX = targetX
        attacker.tileY = targetY
        attacker.markActionComplete()
        return TacticalActionResult.Magic(magic.name, magic.expendMp, emptyList())
    }
}
