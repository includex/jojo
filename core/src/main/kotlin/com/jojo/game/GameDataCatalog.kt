package com.jojo.game

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.magic.BattleMagicHitArea
import com.jojo.game.domain.battle.magic.BattleMagicProfile
import com.jojo.game.domain.campaign.*

/** 복호화된 게임 데이터 테이블을 읽기 전용 도메인 모델로 제공한다. */

class GameDataCatalog private constructor(
    tables: GameDataTableBundle,
) {
    private val combat = GameDataCatalogCombatDomain(tables)
    private val units = GameDataCatalogUnitDomain(tables, combat)
    private val equipment = GameDataCatalogEquipmentDomain(tables)


    data class UnitProfile(
        val id: Int,
        val name: String,
        /** 대화창 인물 초상으로 변환할 얼굴 식별자이다. */
        val face: Int,
        /** 홀 화면에 표시할 유닛 아바타 식별자이다. */
        val mapAvatar: Int,
        /** 전투 화면에 표시할 유닛 아바타 식별자이다. */
        val battleAvatar: Int,
        /** 전투 아바타 호환성을 판단하는 유형이다. */
        val battleAvatarType: Int,
        /** 적 체력 바 표시 여부에 쓰는 유명 인물 표식이다. */
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
        /** 치명타 대사 목록과 난수 선택 규칙이다. */
        val criticalSpeech: CriticalSpeechProfile,
    ) {
        /** 직위에서 계산한 병과 식별자이다. */
        val armId: Int get() = if (posts < 60) posts / 3 else posts - 40
    }


    data class CriticalSpeechProfile(
        val texts: List<String>,
        /** 고정 치명타 대사인지 나타낸다. */
        val randomized: Boolean,
        /** 특수 난수 경로를 사용하는 마지막 기본 대사인지 나타낸다. */
        val flagRandom: Boolean = false,
    )


    data class ArmProfile(
        val id: Int,
        val name: String,
        /** 병과 유형을 나타내는 원본 값이다. */
        val type: Int,
        val remote: Boolean,
        /** 공격 지연 여부를 나타낸다. */
        val attackDelay: Boolean,
        val magicHarmRate: Int,
        /** 전투 아바타 유형이다. */
        val battleAvatarType: Int,
        private val restraints: Map<Int, Int>,
        private val terrainExpend: Map<Int, Int>,
        private val terrainRise: Map<Int, Int>,
        /** 빠른 이동 여부를 나타낸다. */
        val fastMove: Boolean = true,
        /** 이동 효과음 유형을 나타낸다. */
        val moveSound: Int = 0,
    ) {
        /** 상대 병과에 대한 상성 수치를 반환한다. */
        fun restraintAgainst(defenderArmId: Int): Int = restraints[defenderArmId] ?: 100

        /** 스킬 적용 전 지형 영향 수치를 반환한다. */
        fun terrainImpact(terrainId: Int): Int = terrainRise[terrainId] ?: 100

        /** 지형 이동 비용을 반환하며 미정 지형은 이동 불가로 처리한다. */
        fun terrainMoveCost(terrainId: Int): Int = terrainExpend[terrainId] ?: 255

        /** 표시용 지형 영향값을 반환한다. */
        fun terrainRiseForDisplay(terrainId: Int): Int? = terrainRise[terrainId]

        /** 표시용 지형 이동 비용을 반환한다. */
        fun terrainExpendForDisplay(terrainId: Int): Int? = terrainExpend[terrainId]
    }


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


    data class HitAreaProfile(
        override val id: Int,
        override val offsets: Set<Pair<Int, Int>>,
        override val allScreen: Boolean = false,
        /** 강화 범위가 없으면 현재 범위 식별자를 사용한다. */
        override val upgradeId: Int = id,
    ) : BattleMagicHitArea

    /** 전술 마법 계산에 필요한 마법 정보이다. */
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
        /** 대상 효과 식별자이며 값 255는 효과 없음을 뜻한다. */
        override val effectId: Int = 255,
        /** 마법 사용 조건 식별자이다. */
        override val condition: Int = -1,
        /** AI의 마법 사용 규칙을 나타낸다. */
        override val aiUse: Int = 0,
        /** 명중률 하한을 나타낸다. */
        override val hitRateLimit: Int = 0,
        /** 마법 목록에 표시할 아이콘 식별자이다. */
        override val icon: Int = 0,
        /** 마법 목록에 표시할 설명이다. */
        override val intro: String = "",
    ) : BattleMagicProfile

    /** 전투 스크립트 장비가 사용하는 아이템 정보이다. */
    data class EquipmentProfile(
        val id: Int,
        val name: String,
        val itemType: Int,
        /** 아이템 가격이다. */
        val price: Int,
        val specialType: Int,
        val value: Int,
        val effectValue: Int,
        val upgradePerLevel: Int,
        /** 아이템 목록에 표시할 아이콘 식별자이다. */
        val icon: Int,
        /** 보물 아이템 여부를 나타낸다. */
        val treasure: Boolean,
        /** 아이템 설명이다. */
        val intro: String = "",
    )


    data class EquipmentBonus(val attack: Int = 0, val defense: Int = 0, val spirit: Int = 0)

    /** 특성 보정을 적용한 유닛 직위 스킬 정보이다. */
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
