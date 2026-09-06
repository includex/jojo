// Battle
package com.jojo.game.application.battle.combat

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.magic.MagicEnvironment
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge

/**
 * `BattleCombatEnvironmentContext` 클래스: combat 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class BattleCombatEnvironmentContext(
    val units: () -> Collection<BattleUnit>,
    val pendingPresentationUnits: () -> Collection<BattleUnit>,
    val unitAt: (Int, Int) -> BattleUnit?,
    val areAllied: (BattleUnit, BattleUnit) -> Boolean,
    val weather: () -> BattleWeather,
    val setWeather: (BattleWeather) -> Unit,
    val terrain: BattleTerrainGrid?,
    val terrainMagicFlags: Map<Int, Int>,
    val activeFaction: () -> Faction,
    val isBattleEnded: () -> Boolean,
    val statusRoundFor: (BattleStatus) -> Int,
    val probabilityResolver: BattleProbabilityResolver,
    val battleExperience: (BattleUnit, BattleUnit, Boolean) -> Int,
    val equipmentExperienceAmount: (BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> Int,
    val notifyBattleExperience: (BattleUnit, Int) -> Unit,
    val notifyEquipmentExperienceAward: (BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> Unit,
    val notifyPhysicalDamage: (BattleUnit, BattleUnit, Int) -> Unit,
    val notifyUnitDefeated: (BattleUnit, BattleUnit) -> Unit,
    val onDefeat: (String) -> Unit,
    val canAttack: (BattleUnit, BattleUnit) -> Boolean,
    val backPosition: (BattleUnit, BattleUnit) -> Pair<Int, Int>?,
    val facingDirection: (Int, Int, Int, Int) -> Int,
    val getPlayerMoney: () -> Int,
    val setPlayerMoney: (Int) -> Unit,
    val getEnemyMoney: () -> Int,
    val setEnemyMoney: (Int) -> Unit,
    val propertyItem: (Int) -> BattlePropertyItem?,
    val zdsyGlobalValue: Int,
    val notifyConsumeAutomaticProperty: (Int) -> Unit,
    val incSkillTemp: (String, Int) -> Int,
    val applyProperty: (BattlePropertyItem, BattleUnit, () -> Boolean) -> TacticalActionResult.Item?,
    val visibleFamousPlayerCount: () -> Int,
    val basePhysicalDamageContext: (BattleUnit, BattleUnit, Boolean, PhysicalDefenseRule) -> BasePhysicalDamageContext,
    val physicalDamageRateContext: (BattleUnit, BattleUnit) -> PhysicalDamageRateContext,
    val physicalCriticalRateContext: (BattleUnit, BattleUnit, Boolean, Boolean, Boolean, Boolean) -> PhysicalCriticalRateContext,
    val flatPhysicalDamageContext: (BattleUnit, Boolean) -> FlatPhysicalDamageContext,
    val castReactionMagic: (BattleUnit, BattleUnit, Int) -> TacticalActionResult.Magic?,
    val consumeXuShiDamage: (BattleUnit) -> Int,
    val consumeMpAttackSkill: (BattleUnit) -> Unit,
    val mrspDamage: (BattleUnit, BattleUnit) -> Int?,
)
/**
 * `BattleCombatEnvironmentBuilder` 싱글턴 객체: combat 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object BattleCombatEnvironmentBuilder {


    /**
     * `statusDuration`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun statusDuration(status: BattleStatus, unit: BattleUnit, statusRoundFor: (BattleStatus) -> Int): Int = when {
        !unit.isPlayerSide() && status == BattleStatus.CONFUSION -> 1
        !unit.isPlayerSide() && status == BattleStatus.PARALYSIS -> 2
        else -> statusRoundFor(status)
    }.coerceIn(0, 3)

    /**
     * `resolveCriticalSpeech`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun resolveCriticalSpeech(
        unit: BattleUnit,
        criticalFlag: Boolean,
        probabilityResolver: BattleProbabilityResolver,
    ): String? {
        if (!criticalFlag) return null
        /**
         * `show` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val show = unit.criticalSpeechChecks % 2 == 0
        unit.criticalSpeechChecks++
        if (!show) return null
        /**
         * `speech` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val speech = unit.criticalSpeech
        if (speech.texts.isEmpty()) return null
        /**
         * `index` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val index = when {
            !speech.randomized || speech.texts.size == 1 -> 0
            speech.flagRandom -> probabilityResolver.flagRandom(0, speech.texts.lastIndex)
            else -> probabilityResolver.defaultRandom(0, speech.texts.lastIndex)
        }
        return speech.texts[index]
    }


    /**
     * `buildMagicEnvironment`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun buildMagicEnvironment(ctx: BattleCombatEnvironmentContext): MagicEnvironment = MagicEnvironment(
        probabilityResolver = ctx.probabilityResolver,
        units = ctx.units,
        pendingPresentationUnits = ctx.pendingPresentationUnits,
        unitAt = ctx.unitAt,
        areAllied = ctx.areAllied,
        weather = ctx.weather,
        setWeather = ctx.setWeather,
        terrain = ctx.terrain,
        terrainMagicFlags = ctx.terrainMagicFlags,
        activeFaction = ctx.activeFaction,
        isBattleEnded = ctx.isBattleEnded,
        statusDuration = { status, unit -> statusDuration(status, unit, ctx.statusRoundFor) },
        resolveCriticalSpeech = { unit, crit -> resolveCriticalSpeech(unit, crit, ctx.probabilityResolver) },
        battleExperience = ctx.battleExperience,
        equipmentExperienceAmount = ctx.equipmentExperienceAmount,
        notifyBattleExperience = ctx.notifyBattleExperience,
        notifyEquipmentExperienceAward = ctx.notifyEquipmentExperienceAward,
        notifyUnitDefeated = ctx.notifyUnitDefeated,
        onDefeat = ctx.onDefeat,
    )


    /**
     * `buildPhysicalTargetEnvironment`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun buildPhysicalTargetEnvironment(ctx: BattleCombatEnvironmentContext): PhysicalTargetEnvironment =
        PhysicalTargetEnvironment(
            random100 = { ctx.probabilityResolver.random100() },
            statusDuration = { status, unit -> statusDuration(status, unit, ctx.statusRoundFor) },
            canAttack = ctx.canAttack,
            backPosition = ctx.backPosition,
            activeFaction = ctx.activeFaction(),
            getPlayerMoney = ctx.getPlayerMoney,
            setPlayerMoney = ctx.setPlayerMoney,
            getEnemyMoney = ctx.getEnemyMoney,
            setEnemyMoney = ctx.setEnemyMoney,
            propertyItem = ctx.propertyItem,
            zdsyGlobalValue = ctx.zdsyGlobalValue,
            notifyPhysicalDamage = ctx.notifyPhysicalDamage,
            notifyConsumeAutomaticProperty = ctx.notifyConsumeAutomaticProperty,
            notifyUnitDefeated = ctx.notifyUnitDefeated,
            onDefeat = ctx.onDefeat,
            incSkillTemp = ctx.incSkillTemp,
            applyProperty = ctx.applyProperty,
        )


    /**
     * `buildPhysicalCombatEnvironment`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun buildPhysicalCombatEnvironment(ctx: BattleCombatEnvironmentContext): PhysicalCombatEnvironment {
        val targetEnv = buildPhysicalTargetEnvironment(ctx)
        return PhysicalCombatEnvironment(
            probabilityResolver = ctx.probabilityResolver,
            units = ctx.units,
            unitAt = ctx.unitAt,
            areAllied = ctx.areAllied,
            canAttack = ctx.canAttack,
            facingDirection = ctx.facingDirection,
            visibleFamousPlayerCount = ctx.visibleFamousPlayerCount,
            basePhysicalDamageContext = ctx.basePhysicalDamageContext,
            physicalDamageRateContext = ctx.physicalDamageRateContext,
            physicalCriticalRateContext = ctx.physicalCriticalRateContext,
            flatPhysicalDamageContext = ctx.flatPhysicalDamageContext,
            rollAttackStatusBatch = { attacker ->
                PhysicalTargetResolver.rollAttackStatusBatch(attacker) { ctx.probabilityResolver.random100() }
            },
            resolvePhysicalTarget = { attacker, target, harm, statuses, active ->
                PhysicalTargetResolver.resolve(attacker, target, harm, statuses, active, targetEnv)
            },
            resolveCriticalSpeech = { unit, crit -> resolveCriticalSpeech(unit, crit, ctx.probabilityResolver) },
            castReactionMagic = ctx.castReactionMagic,
            battleExperience = ctx.battleExperience,
            equipmentExperienceAmount = ctx.equipmentExperienceAmount,
            notifyBattleExperience = ctx.notifyBattleExperience,
            notifyEquipmentExperienceAward = ctx.notifyEquipmentExperienceAward,
            notifyPhysicalDamage = ctx.notifyPhysicalDamage,
            notifyUnitDefeated = ctx.notifyUnitDefeated,
            onDefeat = ctx.onDefeat,
            consumeXuShiDamage = ctx.consumeXuShiDamage,
            consumeMpAttackSkill = ctx.consumeMpAttackSkill,
            mrspDamage = ctx.mrspDamage,
        )
    }
}
