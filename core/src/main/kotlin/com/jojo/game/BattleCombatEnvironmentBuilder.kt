package com.jojo.game
import com.jojo.game.domain.battle.magic.MagicEnvironment
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge

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
 * Constructs resolution environments for magic casting, physical targeting, and combat interactions.
 */
internal object BattleCombatEnvironmentBuilder {

    /**
     * 공개 메서드 `statusDuration`
     *
     * ### 파라미터
    - `status` (`BattleStatus`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `unit` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `statusRoundFor` (`(BattleStatus`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun statusDuration(status: BattleStatus, unit: BattleUnit, statusRoundFor: (BattleStatus) -> Int): Int = when {
        !unit.isPlayerSide() && status == BattleStatus.CONFUSION -> 1
        !unit.isPlayerSide() && status == BattleStatus.PARALYSIS -> 2
        else -> statusRoundFor(status)
    }.coerceIn(0, 3)

    fun resolveCriticalSpeech(
        unit: BattleUnit,
        criticalFlag: Boolean,
        probabilityResolver: BattleProbabilityResolver,
    ): String? {
        if (!criticalFlag) return null
        val show = unit.criticalSpeechChecks % 2 == 0
        unit.criticalSpeechChecks++
        if (!show) return null
        val speech = unit.criticalSpeech
        if (speech.texts.isEmpty()) return null
        val index = when {
            !speech.randomized || speech.texts.size == 1 -> 0
            speech.flagRandom -> probabilityResolver.flagRandom(0, speech.texts.lastIndex)
            else -> probabilityResolver.defaultRandom(0, speech.texts.lastIndex)
        }
        return speech.texts[index]
    }

    /**
     * 공개 메서드 `buildMagicEnvironment`
     *
     * ### 파라미터
    - `ctx` (`BattleCombatEnvironmentContext`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `MagicEnvironment`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `buildPhysicalTargetEnvironment`
     *
     * ### 파라미터
    - `ctx` (`BattleCombatEnvironmentContext`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `PhysicalTargetEnvironment`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `buildPhysicalCombatEnvironment`
     *
     * ### 파라미터
    - `ctx` (`BattleCombatEnvironmentContext`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `PhysicalCombatEnvironment`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
