package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.campaign.CampaignEquipmentSlot

/**
 * data class  `BattleTurnSettlementEnvironment`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
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

/**
 * object  `BattleTurnSettlementService`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object BattleTurnSettlementService {
    private const val ENABLED_FEATURE_ZDBHSW = 32

    /**
     * data class  `UnitTurnSnapshot`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class UnitTurnSnapshot(
        val hp: Int,
        val mp: Int,
        val statuses: Map<BattleStatus, Int>,
        val lifts: Map<BattleAttribute, Int>,
        val actionComplete: Boolean,
        val actionStatusRound: Int,
    )

    /**
     * 공개 메서드 `turnSnapshot`
     *
     * ### 파라미터
    - `units` (`Collection<BattleUnit>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Map<String, UnitTurnSnapshot>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun turnSnapshot(units: Collection<BattleUnit>): Map<String, UnitTurnSnapshot> =
        units.associate { unit ->
            unit.id to UnitTurnSnapshot(
                hp = unit.hitPoints,
                mp = unit.magicPoints,
                statuses = unit.statuses.toMap(),
                lifts = unit.attributeLifts.toMap(),
                actionComplete = unit.hasActed,
                actionStatusRound = unit.actionStatusRound,
            )
        }

    fun turnChanges(
        before: Map<String, UnitTurnSnapshot>,
        presentationUnit: (String) -> BattleUnit?,
    ): List<BattleUnitTurnChange> =
        before.mapNotNull { (id, old) ->
            val unit = presentationUnit(id) ?: return@mapNotNull null
            val changed = old.hp != unit.hitPoints || old.mp != unit.magicPoints ||
                    old.statuses != unit.statuses || old.lifts != unit.attributeLifts ||
                    old.actionComplete != unit.hasActed || old.actionStatusRound != unit.actionStatusRound
            if (!changed) return@mapNotNull null
            BattleUnitTurnChange(
                unitId = id,
                hitPointsBefore = old.hp,
                hitPointsAfter = unit.hitPoints,
                magicPointsBefore = old.mp,
                magicPointsAfter = unit.magicPoints,
                statusesBefore = old.statuses,
                statusesAfter = unit.statuses.toMap(),
                attributeLiftsBefore = old.lifts,
                attributeLiftsAfter = unit.attributeLifts.toMap(),
                actionCompleteBefore = old.actionComplete,
                actionCompleteAfter = unit.hasActed,
                actionStatusRoundBefore = old.actionStatusRound,
                actionStatusRoundAfter = unit.actionStatusRound,
            )
        }

    fun captureSettlement(
        stage: CampSettlementStage,
        faction: Faction,
        env: BattleTurnSettlementEnvironment,
        settle: (MutableList<SettlementSubflow>) -> List<BattleUnitTurnChange>?,
    ): CampSettlement {
        val before = turnSnapshot(env.units())
        val subflows = mutableListOf<SettlementSubflow>()
        val primaryChanges = settle(subflows)
        val changes = primaryChanges ?: turnChanges(before, env.presentationUnit)
        return CampSettlement(stage, faction, changes, subflows, subflowsCaptured = true)
    }

    fun settleCampStart(
        faction: Faction,
        env: BattleTurnSettlementEnvironment,
    ): CampSettlement = captureSettlement(CampSettlementStage.START_STATE, faction, env) { subflows ->
        processStartOfTurn(faction, subflows, env)
    }

    fun settleCampEnd(
        faction: Faction,
        env: BattleTurnSettlementEnvironment,
    ): CampSettlement = captureSettlement(CampSettlementStage.END_RESTORE, faction, env) { subflows ->
        if (faction == Faction.FRIEND || faction == Faction.REINFORCEMENTS) {
            val side = faction.isPlayerSide()
            env.units().filter { it.effectiveFaction().isPlayerSide() == side }.forEach {
                it.hasActed = false
                it.presentation.refreshStatus(it.statuses, it.attributeLifts)
            }
        }
        processEndOfTurn(faction, subflows, env)
    }

    fun processStartOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
        env: BattleTurnSettlementEnvironment,
    ): List<BattleUnitTurnChange> {
        val processedSide = when (faction) {
            Faction.PLAYER -> true
            Faction.ENEMY -> false
            Faction.FRIEND, Faction.REINFORCEMENTS -> null
        }
        val allUnits = env.units()
        val orderedUnits = allUnits.filter {
            processedSide != null && it.effectiveFaction().isPlayerSide() == processedSide
        }.sortedWith(compareBy<BattleUnit> { it.tileY }.thenBy { it.tileX })
        if (processedSide == null) return emptyList()
        val primaryChanges = mutableListOf<BattleUnitTurnChange>()
        orderedUnits.forEach { unit ->
            val ordinaryBefore = turnSnapshot(allUnits)
            unit.statuses.entries.toList().forEach { (status, rounds) ->
                if (rounds <= 1) unit.statuses.remove(status) else unit.statuses[status] = rounds - 1
            }
            unit.presentation.refreshStatus(unit.statuses, unit.attributeLifts)
            unit.attributeLifts.keys.toList().forEach { attribute ->
                val rounds = unit.attributeLiftRounds[attribute] ?: 0
                if (rounds <= 1) {
                    unit.attributeLifts.remove(attribute)
                    unit.attributeLiftRounds[attribute] = env.attributeStatusRoundFor(attribute)
                } else unit.attributeLiftRounds[attribute] = rounds - 1
            }
            unit.presentation.refreshAttributeStatusIcons(unit.attributeLifts)
            val terrainId = env.terrain?.terrainAt(unit.tileX, unit.tileY)
            if (unit.hitPoints < unit.maxHitPoints) {
                val resumeHp = env.terrainResumeRates[terrainId] ?: 0
                if (resumeHp != 0) unit.addHpcur(unit.maxHitPoints * resumeHp / 100)
            }
            if (unit.magicPoints < unit.maxMagicPoints) {
                val resumeMp = env.terrainResumeMp[terrainId] ?: 0
                if (resumeMp != 0) unit.addMpcur(resumeMp)
            }
            if (unit.hitPoints <= 0) env.defeatUnit(unit.id)
            primaryChanges += turnChanges(ordinaryBefore, env.presentationUnit)
            if (unit.hitPoints <= 0) return@forEach

            val caster = unit

            /**
             * 공개 메서드 `effect`
             *
             * ### 파라미터
            - `skillId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun effect(skillId: Int) = caster.skills[skillId]?.and(255)?.takeIf { it != 255 }

            /**
             * 공개 메서드 `nearby`
             *
             * ### 파라미터
            - 입력 파라미터: 없음
             *
             * ### 응답 스펙
             * - 반환 타입: `List<BattleUnit>`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun nearby(): List<BattleUnit> = env.infantryOffsets.mapNotNull { (dx, dy) ->
                allUnits.firstOrNull { target ->
                    target.tileX == caster.tileX + dx && target.tileY == caster.tileY + dy
                }
            }.filter { it.isPlayerSide() == processedSide }.distinctBy { it.id }

            fun record(
                skillId: Int,
                value: Int,
                meffName: String? = null,
                targetOrder: List<BattleUnit>,
                mutate: () -> Unit,
            ) {
                val before = turnSnapshot(allUnits)
                mutate()
                val order = targetOrder.mapIndexed { index, target -> target.id to index }.toMap()
                val nested = turnChanges(before, env.presentationUnit).sortedBy { order[it.unitId] ?: Int.MAX_VALUE }
                if (targetOrder.isNotEmpty()) subflows += SettlementSubflow.LocalAura(
                    casterId = caster.id,
                    skillId = skillId,
                    skillValue = value,
                    meffName = meffName,
                    targets = targetOrder.map { it.id },
                    nestedChanges = nested,
                )
            }

            effect(103)?.let { value ->
                val targets = nearby().filter { target ->
                    listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                        .any(target.statuses::containsKey)
                }
                record(103, value, targetOrder = targets) {
                    targets.forEach { target ->
                        listOf(
                            BattleStatus.PARALYSIS,
                            BattleStatus.SILENCE,
                            BattleStatus.CONFUSION,
                            BattleStatus.POISON
                        )
                            .forEach(target.statuses::remove)
                    }
                }
            }
            effect(208)?.let { value ->
                val targets = nearby().filter { it.hitPoints < it.maxHitPoints }
                record(208, value, "resume_hp", targets) {
                    targets.forEach { target ->
                        target.addHpcur(target.maxHitPoints * value / 100)
                    }
                }
            }
            effect(209)?.let { value ->
                val targets = nearby().filter { it.magicPoints < it.maxMagicPoints }
                record(209, value, "resume_mp", targets) {
                    targets.forEach { target ->
                        val addition = if (value == 0) (caster.level + 10) / 10
                        else target.maxMagicPoints * value / 100
                        target.addMpcur(addition)
                    }
                }
            }
            effect(210)?.takeIf { it and 31 != 0 }?.let { mask ->
                val targets = nearby()
                record(210, mask, targetOrder = targets) {
                    targets.forEach { target ->
                        BattleAttribute.entries.take(5).forEachIndexed { index, attribute ->
                            if (mask and (1 shl index) != 0) {
                                target.applyAttributeLift(attribute, 1, 3)
                            }
                        }
                    }
                }
            }
        }
        return primaryChanges
    }

    fun processEndOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
        env: BattleTurnSettlementEnvironment,
    ): List<BattleUnitTurnChange> {
        val allUnits = env.units()
        allUnits.filter { it.effectiveFaction() == faction }.forEach { unit ->
            val grants = buildList {
                unit.skills[149]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = env.onRestoreUnitExperience(unit, amount)) {
                        RestoreGrowthResolution.Unavailable -> add(
                            SettlementGrowthGrant(
                                SettlementGrowthKind.UNIT_EXP,
                                amount
                            )
                        )

                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            unit.level = resolution.value.level
                            if (resolution.value.gained > 0) add(
                                SettlementGrowthGrant(
                                    SettlementGrowthKind.UNIT_EXP,
                                    amount,
                                    unitResult = resolution.value
                                )
                            )
                        }
                    }
                }
                unit.skills[150]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution =
                        env.onRestoreEquipmentExperience(unit, amount, CampaignEquipmentSlot.WEAPON)) {
                        RestoreGrowthResolution.Unavailable -> add(
                            SettlementGrowthGrant(
                                SettlementGrowthKind.WEAPON_EXP,
                                amount
                            )
                        )

                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            val result = resolution.value
                            if (result.gained > 0) add(
                                SettlementGrowthGrant(
                                    SettlementGrowthKind.WEAPON_EXP,
                                    amount,
                                    equipmentResult = result
                                )
                            )
                            if (result.leveledUp) env.onEquipmentUpgrade(result)
                        }
                    }
                }
                unit.skills[151]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution =
                        env.onRestoreEquipmentExperience(unit, amount, CampaignEquipmentSlot.ARMOR)) {
                        RestoreGrowthResolution.Unavailable -> add(
                            SettlementGrowthGrant(
                                SettlementGrowthKind.ARMOR_EXP,
                                amount
                            )
                        )

                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            val result = resolution.value
                            if (result.gained > 0) add(
                                SettlementGrowthGrant(
                                    SettlementGrowthKind.ARMOR_EXP,
                                    amount,
                                    equipmentResult = result
                                )
                            )
                            if (result.leveledUp) env.onEquipmentUpgrade(result)
                        }
                    }
                }
            }
            if (grants.isNotEmpty()) subflows += SettlementSubflow.Growth(unit.id, grants)
        }
        val poisonBefore = turnSnapshot(allUnits)
        val lethalPoison = env.enabledFeatures() and ENABLED_FEATURE_ZDBHSW != 0
        allUnits.filter { it.effectiveFaction() == faction && BattleStatus.POISON in it.statuses }
            .toList()
            .forEach { unit ->
                if (!lethalPoison && unit.hitPoints < 2) return@forEach
                val rate = if (env.weather() == BattleWeather.CLOUDY) 15 else 10
                var damage = unit.maxHitPoints * rate / 100
                if (!lethalPoison) damage = minOf(unit.hitPoints - 1, damage)
                unit.addHpcur(-damage)
                if (unit.hitPoints <= 0) env.defeatUnit(unit.id)
            }
        return turnChanges(poisonBefore, env.presentationUnit)
    }
}
