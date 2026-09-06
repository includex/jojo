// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.isPlayerSide
import com.jojo.game.domain.battle.settlement.BattleUnitTurnChange
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.settlement.CampSettlementStage
import com.jojo.game.domain.battle.settlement.RestoreGrowthResolution
import com.jojo.game.domain.battle.settlement.SettlementGrowthGrant
import com.jojo.game.domain.battle.settlement.SettlementGrowthKind
import com.jojo.game.domain.battle.settlement.SettlementSubflow
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.campaign.CampaignExperienceResult

/**
 * `BattleTurnSettlementEnvironment` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class BattleTurnSettlementEnvironment(
    val units: () -> Collection<BattleUnit>,
    val presentationUnit: (String) -> BattleUnit?,
    val defeatUnit: (String) -> Unit,
    val terrain: BattleTerrainGrid?,
    val terrainResumeRates: Map<Int, Int>,
    val terrainResumeMp: Map<Int, Int>,
    val weather: () -> BattleWeather,
    val enabledFeatures: () -> Int,
    val infantryOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    val statusRoundFor: (BattleStatus) -> Int,
    val attributeStatusRoundFor: (BattleAttribute) -> Int,
    val onRestoreUnitExperience: (BattleUnit, Int) -> RestoreGrowthResolution<CampaignExperienceResult>,
    val onRestoreEquipmentExperience: (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<CampaignEquipmentExperienceResult>,
    val onEquipmentUpgrade: (CampaignEquipmentExperienceResult) -> Unit,
)

/** BattleTurnSettlementService: 실제 전투 유닛을 갱신하면서, 표현과 기록에 쓸 정산 DTO를 함께 만든다. */
object BattleTurnSettlementService {
    /**
     * `ENABLED_FEATURE_ZDBHSW` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private const val ENABLED_FEATURE_ZDBHSW = 32

    /**
     * `UnitTurnSnapshot` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class UnitTurnSnapshot(
        /**
         * `hp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hp: Int,
        /**
         * `mp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mp: Int,
        /**
         * `statuses` (Map<BattleStatus, Int>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val statuses: Map<BattleStatus, Int>,
        /**
         * `lifts` (Map<BattleAttribute, Int>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val lifts: Map<BattleAttribute, Int>,
        /**
         * `actionComplete` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val actionComplete: Boolean,
        /**
         * `actionStatusRound` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val actionStatusRound: Int,
    )

    /**
     * `turnSnapshot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun turnSnapshot(units: Collection<BattleUnit>): Map<String, UnitTurnSnapshot> = units.associate { unit ->
        unit.id to UnitTurnSnapshot(
            unit.hitPoints, unit.magicPoints, unit.statuses.toMap(), unit.attributeLifts.toMap(),
            unit.hasActed, unit.actionStatusRound,
        )
    }

    /**
     * `turnChanges`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun turnChanges(
        before: Map<String, UnitTurnSnapshot>,
        presentationUnit: (String) -> BattleUnit?,
    ): List<BattleUnitTurnChange> = before.mapNotNull { (id, old) ->
        /**
         * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unit = presentationUnit(id) ?: return@mapNotNull null
        if (old.hp == unit.hitPoints && old.mp == unit.magicPoints && old.statuses == unit.statuses &&
            old.lifts == unit.attributeLifts && old.actionComplete == unit.hasActed &&
            old.actionStatusRound == unit.actionStatusRound
        ) return@mapNotNull null
        BattleUnitTurnChange(
            id, old.hp, unit.hitPoints, old.mp, unit.magicPoints,
            old.statuses, unit.statuses.toMap(), old.lifts, unit.attributeLifts.toMap(),
            old.actionComplete, unit.hasActed, old.actionStatusRound, unit.actionStatusRound,
        )
    }

    /**
     * `captureSettlement`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun captureSettlement(
        stage: CampSettlementStage,
        faction: Faction,
        env: BattleTurnSettlementEnvironment,
        settle: (MutableList<SettlementSubflow>) -> List<BattleUnitTurnChange>?,
    ): CampSettlement {
        /**
         * `before` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val before = turnSnapshot(env.units())
        /**
         * `subflows` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val subflows = mutableListOf<SettlementSubflow>()
        return CampSettlement(stage, faction, settle(subflows) ?: turnChanges(before, env.presentationUnit), subflows, true)
    }

    /**
     * `settleCampStart`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun settleCampStart(faction: Faction, env: BattleTurnSettlementEnvironment): CampSettlement =
        captureSettlement(CampSettlementStage.START_STATE, faction, env) { processStartOfTurn(faction, it, env) }

    /**
     * `settleCampEnd`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun settleCampEnd(faction: Faction, env: BattleTurnSettlementEnvironment): CampSettlement =
        captureSettlement(CampSettlementStage.END_RESTORE, faction, env) { subflows ->
            if (faction == Faction.FRIEND || faction == Faction.REINFORCEMENTS) {
                /**
                 * `side` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val side = faction.isPlayerSide()
                env.units().filter { it.effectiveFaction().isPlayerSide() == side }.forEach { it.hasActed = false }
            }
            processEndOfTurn(faction, subflows, env)
        }

    /**
     * `processStartOfTurn`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun processStartOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
        env: BattleTurnSettlementEnvironment,
    ): List<BattleUnitTurnChange> {
        /**
         * `processedSide` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val processedSide = when (faction) {
            Faction.PLAYER -> true
            Faction.ENEMY -> false
            Faction.FRIEND, Faction.REINFORCEMENTS -> null
        }
        /**
         * `allUnits` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val allUnits = env.units()
        if (processedSide == null) return emptyList()
        /**
         * `changes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val changes = mutableListOf<BattleUnitTurnChange>()
        allUnits.filter { it.effectiveFaction().isPlayerSide() == processedSide }
            .sortedWith(compareBy<BattleUnit> { it.tileY }.thenBy { it.tileX })
            .forEach { caster ->
                /**
                 * `before` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val before = turnSnapshot(allUnits)
                caster.statuses.entries.toList().forEach { (status, rounds) ->
                    if (rounds <= 1) caster.statuses.remove(status) else caster.statuses[status] = rounds - 1
                }
                caster.attributeLifts.keys.toList().forEach { attribute ->
                    /**
                     * `rounds` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val rounds = caster.attributeLiftRounds[attribute] ?: 0
                    if (rounds <= 1) {
                        caster.attributeLifts.remove(attribute)
                        caster.attributeLiftRounds[attribute] = env.attributeStatusRoundFor(attribute)
                    } else caster.attributeLiftRounds[attribute] = rounds - 1
                }
                env.terrain?.terrainAt(caster.tileX, caster.tileY)?.let { terrainId ->
                    env.terrainResumeRates[terrainId]?.takeIf { it != 0 }?.let { caster.addHpcur(caster.maxHitPoints * it / 100) }
                    env.terrainResumeMp[terrainId]?.takeIf { it != 0 }?.let { caster.addMpcur(it) }
                }
                if (caster.hitPoints <= 0) env.defeatUnit(caster.id)
                changes += turnChanges(before, env.presentationUnit)
                if (caster.hitPoints <= 0) return@forEach

                /**
                 * `nearby`: 타입의 핵심 동작을 수행한다.
                 * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
                 */

                fun nearby() = env.infantryOffsets.mapNotNull { (dx, dy) ->
                    allUnits.firstOrNull { it.tileX == caster.tileX + dx && it.tileY == caster.tileY + dy }
                }.filter { it.isPlayerSide() == processedSide }.distinctBy { it.id }
                /**
                 * `record`: 타입의 핵심 동작을 수행한다.
                 * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
                 */

                fun record(skillId: Int, value: Int, meffName: String? = null, targets: List<BattleUnit>, mutate: () -> Unit) {
                    val nestedBefore = turnSnapshot(allUnits)
                    mutate()
                    val order = targets.mapIndexed { index, target -> target.id to index }.toMap()
                    val nested = turnChanges(nestedBefore, env.presentationUnit).sortedBy { order[it.unitId] ?: Int.MAX_VALUE }
                    if (targets.isNotEmpty()) subflows += SettlementSubflow.LocalAura(
                        caster.id, skillId, value, meffName = meffName, targets = targets.map { it.id }, nestedChanges = nested,
                    )
                }
                /**
                 * `effect`: 타입의 핵심 동작을 수행한다.
                 * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
                 */

                fun effect(id: Int) = caster.skills[id]?.and(255)?.takeIf { it != 255 }
                effect(103)?.let { value ->
                    /**
                     * `targets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val targets = nearby().filter { target ->
                        listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                            .any(target.statuses::containsKey)
                    }
                    record(103, value, targets = targets) { targets.forEach { target ->
                        listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                            .forEach(target.statuses::remove)
                    } }
                }
                effect(208)?.let { value ->
                    /**
                     * `targets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val targets = nearby().filter { it.hitPoints < it.maxHitPoints }
                    record(208, value, "resume_hp", targets) { targets.forEach { it.addHpcur(it.maxHitPoints * value / 100) } }
                }
                effect(209)?.let { value ->
                    /**
                     * `targets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val targets = nearby().filter { it.magicPoints < it.maxMagicPoints }
                    record(209, value, "resume_mp", targets) { targets.forEach { target ->
                        target.addMpcur(if (value == 0) (caster.level + 10) / 10 else target.maxMagicPoints * value / 100)
                    } }
                }
                effect(210)?.takeIf { it and 31 != 0 }?.let { mask ->
                    /**
                     * `targets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val targets = nearby()
                    record(210, mask, targets = targets) { targets.forEach { target ->
                        BattleAttribute.entries.take(5).forEachIndexed { index, attribute ->
                            if (mask and (1 shl index) != 0) target.applyAttributeLift(attribute, 1, 3)
                        }
                    } }
                }
            }
        return changes
    }

    /**
     * `processEndOfTurn`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun processEndOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
        env: BattleTurnSettlementEnvironment,
    ): List<BattleUnitTurnChange> {
        /**
         * `allUnits` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val allUnits = env.units()
        allUnits.filter { it.effectiveFaction() == faction }.forEach { unit ->
            /**
             * `grants` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val grants = buildList {
                unit.skills[149]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = env.onRestoreUnitExperience(unit, amount)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.UNIT_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            unit.level = resolution.value.level
                            if (resolution.value.gained > 0) add(
                                SettlementGrowthGrant(SettlementGrowthKind.UNIT_EXP, amount, unitResult = resolution.value)
                            )
                        }
                    }
                }
                unit.skills[150]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = env.onRestoreEquipmentExperience(unit, amount, CampaignEquipmentSlot.WEAPON)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.WEAPON_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            /**
                             * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                             */

                            val result = resolution.value
                            if (result.gained > 0) add(
                                SettlementGrowthGrant(SettlementGrowthKind.WEAPON_EXP, amount, equipmentResult = result)
                            )
                            if (result.leveledUp) env.onEquipmentUpgrade(result)
                        }
                    }
                }
                unit.skills[151]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = env.onRestoreEquipmentExperience(unit, amount, CampaignEquipmentSlot.ARMOR)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.ARMOR_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            /**
                             * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                             */

                            val result = resolution.value
                            if (result.gained > 0) add(
                                SettlementGrowthGrant(SettlementGrowthKind.ARMOR_EXP, amount, equipmentResult = result)
                            )
                            if (result.leveledUp) env.onEquipmentUpgrade(result)
                        }
                    }
                }
            }
            if (grants.isNotEmpty()) subflows += SettlementSubflow.Growth(unit.id, grants)
        }
        /**
         * `poisonBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val poisonBefore = turnSnapshot(allUnits)
        /**
         * `lethalPoison` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val lethalPoison = env.enabledFeatures() and ENABLED_FEATURE_ZDBHSW != 0
        allUnits.filter { it.effectiveFaction() == faction && BattleStatus.POISON in it.statuses }.forEach { unit ->
            if (!lethalPoison && unit.hitPoints < 2) return@forEach
            /**
             * `rate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val rate = if (env.weather() == BattleWeather.CLOUDY) 15 else 10
            /**
             * `damage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            var damage = unit.maxHitPoints * rate / 100
            if (!lethalPoison) damage = minOf(unit.hitPoints - 1, damage)
            unit.addHpcur(-damage)
            if (unit.hitPoints <= 0) env.defeatUnit(unit.id)
        }
        return turnChanges(poisonBefore, env.presentationUnit)
    }

}
