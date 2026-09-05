package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.magic.BattleMagicHitArea
import com.jojo.game.domain.battle.magic.BattleMagicProfile
import com.jojo.game.domain.campaign.*

/**
 * Read-only domain view of decoded game-data tables. Numeric property names
 * deliberately mirror the authored UNIT_ATTR_NAME schema.
 */
/**
 * class  `GameDataCatalog`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class GameDataCatalog private constructor(
    tables: GameDataTableBundle,
) {
    private val combat = GameDataCatalogCombatDomain(tables)
    private val units = GameDataCatalogUnitDomain(tables, combat)
    private val equipment = GameDataCatalogEquipmentDomain(tables)

    /**
     * data class  `UnitProfile`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class UnitProfile(
        val id: Int,
        val name: String,
        /** UNIT_ATTR_NAME2.FACE; DialogueLayer converts this to a Head asset. */
        val face: Int,
        /** UNIT_ATTR_NAME.RAVATAR, used by HallUnit/Pmapobj2. */
        val mapAvatar: Int,
        /** UNIT_ATTR_NAME.SAVATAR, used by BattleUnit/Model.fAvatarGroup. */
        val battleAvatar: Int,
        /** UNIT_ATTR_NAME.SAVATAR_TYPE; gates battle-avatar compatibility. */
        val battleAvatarType: Int,
        /** UNIT_ATTR_NAME.FAMOUS.  BattleUnit uses this for its enemy HP bar. */
        val famous: Boolean,
        val posts: Int,
        val level: Int,
        val attack: Int,
        val defense: Int,
        val spirit: Int,
        val critical: Int,
        val morale: Int,
        val maxHitPoints: Int,
        val maxMagicPoints: Int,
        /** Unit.getCritTxt(): exact source text pool and RNG stream selection. */
        val criticalSpeech: CriticalSpeechProfile,
    ) {
        /** Model.postsToArm() from the original client. */
        val armId: Int get() = if (posts < 60) posts / 3 else posts - 40
    }

    /**
     * data class  `CriticalSpeechProfile`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class CriticalSpeechProfile(
        val texts: List<String>,
        /** Named criIds entries contain one fixed line and consume no Tool.random call. */
        val randomized: Boolean,
        /** Only the source's final hard-coded fallback uses Tool.random flag=1. */
        val flagRandom: Boolean = false,
    )

    /**
     * data class  `ArmProfile`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ArmProfile(
        val id: Int,
        val name: String,
        /** ARM_ATTR_NAME.TYPE: 0 all-rounder, 1 civil, 2 martial. */
        val type: Int,
        val remote: Boolean,
        /** ARM_ATTR_NAME.ATTACKDELAY (arms[6]). */
        val attackDelay: Boolean,
        val magicHarmRate: Int,
        /** ARM_ATTR_NAME.SAVATAR_TYPE. */
        val battleAvatarType: Int,
        private val restraints: Map<Int, Int>,
        private val terrainExpend: Map<Int, Int>,
        private val terrainRise: Map<Int, Int>,
        /** ARM_ATTR_NAME.MOVESPEED. Zero selects BattleUnit.move2's .08s step. */
        val fastMove: Boolean = true,
        /** ARM_ATTR_NAME.MOVESOUND, used by defender KZQB's horse-only reduction. */
        val moveSound: Int = 0,
    ) {
        /** Original Model.armRestraintAttr(): unspecified pairs are exactly 100%. */
        fun restraintAgainst(defenderArmId: Int): Int = restraints[defenderArmId] ?: 100

        /** Original BattleUnit.terrainImpact() baseline before skills. */
        fun terrainImpact(terrainId: Int): Int = terrainRise[terrainId] ?: 100

        /** BattleUnit.getArmTerrain(terrain, 1): absent terrain is impassable (255). */
        fun terrainMoveCost(terrainId: Int): Int = terrainExpend[terrainId] ?: 255

        /** TerrainLayer._initPanel0 distinguishes an absent rise entry from its 100% default. */
        fun terrainRiseForDisplay(terrainId: Int): Int? = terrainRise[terrainId]

        /** TerrainLayer._initPanel1 renders an absent/over-200 expenditure as `--`. */
        fun terrainExpendForDisplay(terrainId: Int): Int? = terrainExpend[terrainId]
    }

    /**
     * data class  `BattleProfile`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class BattleProfile(
        val unit: UnitProfile,
        val level: Int,
        val posts: Int,
        val movement: Int,
        val attack: Int,
        val defense: Int,
        val spirit: Int,
        val critical: Int,
        val morale: Int,
        val maxHitPoints: Int,
        val maxMagicPoints: Int,
        val arm: ArmProfile,
        val hitArea: HitAreaProfile,
        val magic: List<MagicProfile>,
    )

    /**
     * data class  `HitAreaProfile`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class HitAreaProfile(
        override val id: Int,
        override val offsets: Set<Pair<Int, Int>>,
        override val allScreen: Boolean = false,
        /** Model.hitareaUpgrade(id), or this ID when the source has no upgrade. */
        override val upgradeId: Int = id,
    ) : BattleMagicHitArea

    /** Original MAGIC_ATTR_NAME2 fields needed by the tactical resolver. */
    data class MagicProfile(
        override val id: Int,
        override val name: String,
        override val type: Int,
        override val target: Int,
        override val hitArea: BattleMagicHitArea,
        override val effectAreaId: Int,
        override val effectOffsets: Set<Pair<Int, Int>>,
        override val expendMp: Int,
        override val power: Int,
        override val harmType: Int,
        override val category: Int,
        /** MAGIC_ATTR_NAME2.MEFF: source target-effect index, 255 for none. */
        override val effectId: Int = 255,
        /** Original MAGIC_ATTR_NAME.CONDITION (magicConditionTest). */
        override val condition: Int = -1,
        /** MAGIC_ATTR_NAME.AIUSE; 13 bypasses magicConditionTest in Control._AIProcess. */
        override val aiUse: Int = 0,
        /** Original MAGIC_ATTR_NAME.HITRATELIMIT. */
        override val hitRateLimit: Int = 0,
        /** MAGIC_ATTR_NAME.ICON (raw field 6), used by Global108 MagicLayer. */
        override val icon: Int = 0,
        /** MAGIC_ATTR_NAME.INTRO (raw field 7), used by Global108 MagicLayer. */
        override val intro: String = "",
    ) : BattleMagicProfile

    /** Original ITEM_ATTR_NAME values used by battle-script equipment. */
    data class EquipmentProfile(
        val id: Int,
        val name: String,
        val itemType: Int,
        /** ITEM_ATTR_NAME.PRICE (raw item-table field 2). */
        val price: Int,
        val specialType: Int,
        val value: Int,
        val effectValue: Int,
        val upgradePerLevel: Int,
        /** Item.icon(): ITEM_ATTR_NAME2.ICON + 1; source path is Item/<icon>-1. */
        val icon: Int,
        /** ITEM_ATTR_NAME.TREASURE (raw item-table field 9). */
        val treasure: Boolean,
        /** ITEM_ATTR_NAME.INTRO (raw item-table field 10). */
        val intro: String = "",
    )

    /**
     * data class  `EquipmentBonus`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class EquipmentBonus(val attack: Int = 0, val defense: Int = 0, val spirit: Int = 0)

    /** Decoded UNIT_POSTS_SKILL entry after original Tianfu overrides. */
    data class SkillProfile(val index: Int, val skillId: Int, val effect: Int, val name: String)

    fun skillsForUnit(characterId: Int, postsId: Int, campaign: CampaignState? = null) =
        units.skillsForUnit(characterId, postsId, campaign)

    fun unitProfile(id: Int) = units.unitProfile(id)
    fun allUnitNames() = units.allUnitNames()
    fun allUnitIds() = units.allUnitIds()
    fun retreatText(unitId: Int) = units.retreatText(unitId)
    fun allRetreatTexts() = units.allRetreatTexts()
    fun battleName(stageIndex: Int) = units.battleName(stageIndex)
    fun allBattleNames() = units.allBattleNames()
    fun terrainLayer() = units.terrainLayer()
    fun armProfile(id: Int) = units.armProfile(id)
    fun battleProfile(unitId: Int, scriptLevel: Int, postsOverride: Int? = null) =
        units.battleProfile(unitId, scriptLevel, postsOverride)

    fun magicProfile(id: Int) = combat.magicProfile(id)
    fun allMagicProfiles() = combat.allMagicProfiles()
    fun terrainMagicFlag(terrainId: Int) = combat.terrainMagicFlag(terrainId)
    fun terrainResumeHp(terrainId: Int) = combat.terrainResumeHp(terrainId)
    fun terrainResumeMp(terrainId: Int) = combat.terrainResumeMp(terrainId)
    fun statusRound(status: BattleStatus, fallback: Int = 3) = combat.statusRound(status, fallback)
    fun attributeStatusRound(attribute: BattleAttribute, fallback: Int = 3) =
        combat.attributeStatusRound(attribute, fallback)

    fun statusMeff(sourceStatusIndex: Int, meffSlot: Int) = combat.statusMeff(sourceStatusIndex, meffSlot)
    fun namedMeff(name: String) = combat.namedMeff(name)
    fun skillName(skillId: Int) = combat.skillName(skillId)
    fun configTopLevelKeys() = units.configTopLevelKeys()
    fun unitExperienceLimit(level: Int) = units.unitExperienceLimit(level)
    fun unitLevelLimit() = units.unitLevelLimit()
    fun unitLevelGrowth(unitId: Int, postsId: Int, campaign: CampaignState? = null) =
        units.unitLevelGrowth(unitId, postsId, campaign)

    fun unitLevelDerivedAttributes(
        unitId: Int,
        postsId: Int,
        level: Int,
        mine: Boolean,
        campaign: CampaignState? = null
    ) = units.unitLevelDerivedAttributes(unitId, postsId, level, mine, campaign)

    fun promotionTarget(postsId: Int, level: Int) = units.promotionTarget(postsId, level)
    fun equipmentExperienceLimit(itemId: Int, level: Int) = equipment.equipmentExperienceLimit(itemId, level)
    fun equipmentLevelLimit(itemId: Int) = equipment.equipmentLevelLimit(itemId)
    fun equipmentProfile(id: Int) = equipment.equipmentProfile(id)
    fun allEquipmentProfiles() = equipment.allEquipmentProfiles()
    fun postsName(postsId: Int) = units.postsName(postsId)
    fun hallBuyProfiles(stageIndex: Int, averageLevel: Int) = equipment.hallBuyProfiles(stageIndex, averageLevel)
    fun equipmentCategory(item: EquipmentProfile) = equipment.equipmentCategory(item)
    fun purchasePrice(item: EquipmentProfile) = equipment.purchasePrice(item)
    fun sellingPrice(item: EquipmentProfile) = equipment.sellingPrice(item)
    fun equipmentTypeName(itemType: Int) = equipment.equipmentTypeName(itemType)
    fun treasureProfiles() = equipment.treasureProfiles()
    fun battlePropertyItems() = equipment.battlePropertyItems()
    fun equipmentBonus(scriptValues: List<Int>, unitLevel: Int) = equipment.equipmentBonus(scriptValues, unitLevel)
    fun defaultEquipmentBonus(postsId: Int, unitLevel: Int) = equipment.defaultEquipmentBonus(postsId, unitLevel)
    fun defaultEquipment(postsId: Int, unitLevel: Int) = equipment.defaultEquipment(postsId, unitLevel)
    fun equipmentSkills(scriptValues: List<Int>, unitLevel: Int) = equipment.equipmentSkills(scriptValues, unitLevel)
    fun mergeSkills(vararg layers: Map<Int, Int>) = combat.mergeSkills(*layers)
    fun passiveAbility(base: Int, skillId: Int, skills: Map<Int, Int>) = combat.passiveAbility(base, skillId, skills)
    fun learnedMagicIds(postsId: Int, level: Int) = combat.learnedMagicIds(postsId, level)
    fun effectAreaOffsets(id: Int) = combat.effectAreaOffsets(id)
    fun upgradedEffectArea(id: Int) = combat.upgradedEffectArea(id)
    fun hitAreaProfile(id: Int) = combat.hitAreaProfile(id)

    companion object {
        fun sayLayerUnitName(rawName: String): String = rawName.takeWhile { !it.isDigit() }
        fun load(): GameDataCatalog = load(ClasspathThenGdxGameDataResourceSource())
        internal fun load(source: GameDataResourceSource): GameDataCatalog =
            GameDataCatalog(GameDataRepository(source).load())
    }
}
