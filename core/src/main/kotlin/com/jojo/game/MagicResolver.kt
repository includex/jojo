package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.magic.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge



/**
 * Pure Kotlin resolution of tactical magic/strategy actions, including area targeting,
 * status effects, healing, multi-pass resolution (CLLJ), and weather transformations.
 */
internal object MagicResolver {

    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
        reaction: Boolean = false,
        bypassCondition: Boolean = false,
        env: MagicEnvironment,
    ): TacticalActionResult {
        if (env.isBattleEnded()) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = env.units().firstOrNull { it.id == attackerId }
            ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = env.units().firstOrNull { it.id == targetId }
            ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
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
        val targetsAllies = magic.target == 1
        val targetsAny = magic.target == 3
        if (!targetsAny && ((targetsAllies && !env.areAllied(attacker, target)) || (!targetsAllies && env.areAllied(
                attacker,
                target
            )))
        ) {
            return TacticalActionResult.Rejected(if (targetsAllies) "아군만 대상으로 할 수 있는 전략입니다." else "적군만 대상으로 할 수 있는 전략입니다.")
        }
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

        val magicCritical = magic.harmType != 4 && if (attacker.skills[269]?.and(255)?.let { it != 255 } == true) {
            true
        } else {
            env.probabilityResolver.criticalHit(attacker, target)
        }
        val offsets = magic.effectOffsets + (0 to 0)
        val experienceTargets = (env.units() + env.pendingPresentationUnits()).associateBy { it.id }

        /**
         * data class  `MagicEquipmentRecord`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class MagicEquipmentRecord(
            val recipient: BattleUnit,
            val opponent: BattleUnit,
            val kind: BattleEquipmentExperienceKind,
            val amount: Int,
        )

        val magicEquipmentByRecipient = linkedMapOf<Pair<String, BattleEquipmentExperienceKind>, MagicEquipmentRecord>()

        /**
         * 공개 메서드 `recordMagicEquipment`
         *
         * ### 파라미터
        - `recipient` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `opponent` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `resolvedHarm` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `kind` (`BattleEquipmentExperienceKind`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun recordMagicEquipment(
            recipient: BattleUnit,
            opponent: BattleUnit,
            resolvedHarm: Int,
            kind: BattleEquipmentExperienceKind
        ) {
            val amount = env.equipmentExperienceAmount(recipient, opponent, resolvedHarm, kind)
            val key = recipient.id to kind
            if (amount > (magicEquipmentByRecipient[key]?.amount ?: 0)) {
                magicEquipmentByRecipient[key] = MagicEquipmentRecord(recipient, opponent, kind, amount)
            }
        }

        val effectCandidates = env.units().filter { unit ->
            unit.visible && MagicDamageCalculator.magicTerrainAllowed(magic, unit) &&
                    (targetsAny || env.areAllied(unit, attacker) == targetsAllies) &&
                    (unit.tileX - target.tileX to (unit.tileY - target.tileY)) in offsets
        }.toList()

        val repeatCount = if (attacker.skills[16]?.and(255)?.let { it != 255 } == true) 2 else 1
        val criticalSpeeches = mutableListOf<String?>()
        val localSettlements = mutableListOf<MagicLocalSettlement>()
        val resultPasses = buildList {
            repeat(repeatCount) { pass ->
                criticalSpeeches += env.resolveCriticalSpeech(attacker, magicCritical)
                val affectedUnits = when (magic.category) {
                    1, 29 -> env.units().filter { unit ->
                        unit.visible && (targetsAny || env.areAllied(unit, attacker) == targetsAllies)
                    }.toList()

                    26 -> if (effectCandidates.isEmpty()) emptyList() else List(5) {
                        effectCandidates[env.probabilityResolver.defaultRandom(0, effectCandidates.lastIndex)]
                    }

                    else -> effectCandidates
                }
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
        val results = resultPasses.flatten()
        val reward = results.mapNotNull { result ->
            experienceTargets[result.targetId]?.let { victim ->
                env.battleExperience(attacker, victim, false)
            }
        }.maxOrNull()
        if (reward != null) env.notifyBattleExperience(attacker, reward)
        results.forEach { result ->
            val victim = experienceTargets[result.targetId] ?: return@forEach
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

    fun castMagicAt(
        attackerId: String,
        targetX: Int,
        targetY: Int,
        magicId: Int,
        env: MagicEnvironment,
    ): TacticalActionResult {
        if (env.isBattleEnded()) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = env.units().firstOrNull { it.id == attackerId }
            ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
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
