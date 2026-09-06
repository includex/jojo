// Game
package com.jojo.game.infrastructure.data

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.magic.BattleMagicHitArea
import com.jojo.game.domain.battle.magic.BattleMagicProfile
import com.jojo.game.domain.campaign.*

/** GameDataCatalog: 복호화한 원본 테이블을 유닛·전투·장비 조회용 읽기 전용 도메인 모델로 제공한다. */

class GameDataCatalog private constructor(
    tables: GameDataTableBundle,
) {
    /**
     * `combat` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val combat = GameDataCatalogCombatDomain(tables)
    /**
     * `units` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val units = GameDataCatalogUnitDomain(tables, combat)
    /**
     * `equipment` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val equipment = GameDataCatalogEquipmentDomain(tables)


    /**
     * `UnitProfile` 클래스: data 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class UnitProfile(
        /**
         * `id` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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
        /**
         * `posts` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val posts: Int,
        /**
         * `level` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val level: Int,
        /**
         * `attack` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attack: Int,
        /**
         * `defense` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val defense: Int,
        /**
         * `spirit` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val spirit: Int,
        /**
         * `critical` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val critical: Int,
        /**
         * `morale` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val morale: Int,
        /**
         * `maxHitPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxHitPoints: Int,
        /**
         * `maxMagicPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxMagicPoints: Int,
        /** 치명타 대사 목록과 난수 선택 규칙이다. */
        val criticalSpeech: CriticalSpeechProfile,
    ) {
        /** 직위에서 계산한 병과 식별자이다. */
        val armId: Int get() = if (posts < 60) posts / 3 else posts - 40
    }


    /**
     * `CriticalSpeechProfile` 클래스: data 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class CriticalSpeechProfile(
        /**
         * `texts` (List<String>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val texts: List<String>,
        /** 고정 치명타 대사인지 나타낸다. */
        val randomized: Boolean,
        /** 특수 난수 경로를 사용하는 마지막 기본 대사인지 나타낸다. */
        val flagRandom: Boolean = false,
    )


    /**
     * `ArmProfile` 클래스: data 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class ArmProfile(
        /**
         * `id` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /** 병과 유형을 나타내는 원본 값이다. */
        val type: Int,
        /**
         * `remote` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val remote: Boolean,
        /** 공격 지연 여부를 나타낸다. */
        val attackDelay: Boolean,
        /**
         * `magicHarmRate` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magicHarmRate: Int,
        /** 전투 아바타 유형이다. */
        val battleAvatarType: Int,
        /**
         * `restraints` (Map<Int, Int>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        private val restraints: Map<Int, Int>,
        /**
         * `terrainExpend` (Map<Int, Int>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        private val terrainExpend: Map<Int, Int>,
        /**
         * `terrainRise` (Map<Int, Int>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

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


    /**
     * `BattleProfile` 클래스: data 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class BattleProfile(
        /**
         * `unit` (UnitProfile,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unit: UnitProfile,
        /**
         * `level` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val level: Int,
        /**
         * `posts` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val posts: Int,
        /**
         * `movement` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val movement: Int,
        /**
         * `attack` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attack: Int,
        /**
         * `defense` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val defense: Int,
        /**
         * `spirit` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val spirit: Int,
        /**
         * `critical` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val critical: Int,
        /**
         * `morale` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val morale: Int,
        /**
         * `maxHitPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxHitPoints: Int,
        /**
         * `maxMagicPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxMagicPoints: Int,
        /**
         * `arm` (ArmProfile,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val arm: ArmProfile,
        /**
         * `hitArea` (HitAreaProfile,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitArea: HitAreaProfile,
        /**
         * `magic` (List<MagicProfile>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magic: List<MagicProfile>,
    )


    /**
     * `HitAreaProfile` 클래스: data 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class HitAreaProfile(
        /**
         * `id` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val id: Int,
        /**
         * `offsets` (Set<Pair<Int, Int>>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val offsets: Set<Pair<Int, Int>>,
        /**
         * `allScreen` (Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val allScreen: Boolean = false,
        /** 강화 범위가 없으면 현재 범위 식별자를 사용한다. */
        override val upgradeId: Int = id,
    ) : BattleMagicHitArea

    /** 전술 마법 계산에 필요한 마법 정보이다. */
    data class MagicProfile(
        /**
         * `id` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val id: Int,
        /**
         * `name` (String,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val name: String,
        /**
         * `type` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val type: Int,
        /**
         * `target` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val target: Int,
        /**
         * `hitArea` (BattleMagicHitArea,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val hitArea: BattleMagicHitArea,
        /**
         * `effectAreaId` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val effectAreaId: Int,
        /**
         * `effectOffsets` (Set<Pair<Int, Int>>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val effectOffsets: Set<Pair<Int, Int>>,
        /**
         * `expendMp` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val expendMp: Int,
        /**
         * `power` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val power: Int,
        /**
         * `harmType` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val harmType: Int,
        /**
         * `category` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

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
        /**
         * `id` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /**
         * `itemType` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val itemType: Int,
        /** 아이템 가격이다. */
        val price: Int,
        /**
         * `specialType` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val specialType: Int,
        /**
         * `value` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Int,
        /**
         * `effectValue` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val effectValue: Int,
        /**
         * `upgradePerLevel` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val upgradePerLevel: Int,
        /** 아이템 목록에 표시할 아이콘 식별자이다. */
        val icon: Int,
        /** 보물 아이템 여부를 나타낸다. */
        val treasure: Boolean,
        /** 아이템 설명이다. */
        val intro: String = "",
    )


    /**
     * `EquipmentBonus` 클래스: data 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class EquipmentBonus(val attack: Int = 0, val defense: Int = 0, val spirit: Int = 0)

    /** 특성 보정을 적용한 유닛 직위 스킬 정보이다. */
    data class SkillProfile(val index: Int, val skillId: Int, val effect: Int, val name: String)

    /**
     * `skillsForUnit`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun skillsForUnit(characterId: Int, postsId: Int, campaign: CampaignState? = null) =
        units.skillsForUnit(characterId, postsId, campaign)

    /**
     * `unitProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitProfile(id: Int) = units.unitProfile(id)
    /**
     * `allUnitNames`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allUnitNames() = units.allUnitNames()
    /**
     * `allUnitIds`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allUnitIds() = units.allUnitIds()
    /**
     * `retreatText`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun retreatText(unitId: Int) = units.retreatText(unitId)
    /**
     * `allRetreatTexts`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allRetreatTexts() = units.allRetreatTexts()
    /**
     * `battleName`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleName(stageIndex: Int) = units.battleName(stageIndex)
    /**
     * `allBattleNames`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allBattleNames() = units.allBattleNames()
    /**
     * `terrainLayer`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun terrainLayer() = units.terrainLayer()
    /**
     * `armProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun armProfile(id: Int) = units.armProfile(id)
    /**
     * `battleProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleProfile(unitId: Int, scriptLevel: Int, postsOverride: Int? = null) =
        units.battleProfile(unitId, scriptLevel, postsOverride)

    /**
     * `magicProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun magicProfile(id: Int) = combat.magicProfile(id)
    /**
     * `allMagicProfiles`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allMagicProfiles() = combat.allMagicProfiles()
    /**
     * `terrainMagicFlag`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun terrainMagicFlag(terrainId: Int) = combat.terrainMagicFlag(terrainId)
    /**
     * `terrainResumeHp`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun terrainResumeHp(terrainId: Int) = combat.terrainResumeHp(terrainId)
    /**
     * `terrainResumeMp`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun terrainResumeMp(terrainId: Int) = combat.terrainResumeMp(terrainId)
    /**
     * `statusRound`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun statusRound(status: BattleStatus, fallback: Int = 3) = combat.statusRound(status, fallback)
    /**
     * `attributeStatusRound`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun attributeStatusRound(attribute: BattleAttribute, fallback: Int = 3) =
        combat.attributeStatusRound(attribute, fallback)

    /**
     * `statusMeff`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun statusMeff(sourceStatusIndex: Int, meffSlot: Int) = combat.statusMeff(sourceStatusIndex, meffSlot)
    /**
     * `namedMeff`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun namedMeff(name: String) = combat.namedMeff(name)
    /**
     * `skillName`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun skillName(skillId: Int) = combat.skillName(skillId)
    /**
     * `configTopLevelKeys`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun configTopLevelKeys() = units.configTopLevelKeys()
    /**
     * `unitExperienceLimit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitExperienceLimit(level: Int) = units.unitExperienceLimit(level)
    /**
     * `unitLevelLimit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitLevelLimit() = units.unitLevelLimit()
    /**
     * `unitLevelGrowth`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitLevelGrowth(unitId: Int, postsId: Int, campaign: CampaignState? = null) =
        units.unitLevelGrowth(unitId, postsId, campaign)

    /**
     * `unitLevelDerivedAttributes`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun unitLevelDerivedAttributes(
        unitId: Int,
        postsId: Int,
        level: Int,
        mine: Boolean,
        campaign: CampaignState? = null
    ) = units.unitLevelDerivedAttributes(unitId, postsId, level, mine, campaign)

    /**
     * `promotionTarget`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun promotionTarget(postsId: Int, level: Int) = units.promotionTarget(postsId, level)
    /**
     * `equipmentExperienceLimit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipmentExperienceLimit(itemId: Int, level: Int) = equipment.equipmentExperienceLimit(itemId, level)
    /**
     * `equipmentLevelLimit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipmentLevelLimit(itemId: Int) = equipment.equipmentLevelLimit(itemId)
    /**
     * `equipmentProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipmentProfile(id: Int) = equipment.equipmentProfile(id)
    /**
     * `allEquipmentProfiles`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allEquipmentProfiles() = equipment.allEquipmentProfiles()
    /**
     * `postsName`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun postsName(postsId: Int) = units.postsName(postsId)
    /**
     * `hallBuyProfiles`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hallBuyProfiles(stageIndex: Int, averageLevel: Int) = equipment.hallBuyProfiles(stageIndex, averageLevel)
    /**
     * `equipmentCategory`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipmentCategory(item: EquipmentProfile) = equipment.equipmentCategory(item)
    /**
     * `purchasePrice`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun purchasePrice(item: EquipmentProfile) = equipment.purchasePrice(item)
    /**
     * `sellingPrice`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun sellingPrice(item: EquipmentProfile) = equipment.sellingPrice(item)
    /**
     * `equipmentTypeName`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipmentTypeName(itemType: Int) = equipment.equipmentTypeName(itemType)
    /**
     * `treasureProfiles`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun treasureProfiles() = equipment.treasureProfiles()
    /**
     * `battlePropertyItems`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battlePropertyItems() = equipment.battlePropertyItems()
    /**
     * `equipmentBonus`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipmentBonus(scriptValues: List<Int>, unitLevel: Int) = equipment.equipmentBonus(scriptValues, unitLevel)
    /**
     * `defaultEquipmentBonus`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun defaultEquipmentBonus(postsId: Int, unitLevel: Int) = equipment.defaultEquipmentBonus(postsId, unitLevel)
    /**
     * `defaultEquipment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun defaultEquipment(postsId: Int, unitLevel: Int) = equipment.defaultEquipment(postsId, unitLevel)
    /**
     * `equipmentSkills`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipmentSkills(scriptValues: List<Int>, unitLevel: Int) = equipment.equipmentSkills(scriptValues, unitLevel)
    /**
     * `mergeSkills`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun mergeSkills(vararg layers: Map<Int, Int>) = combat.mergeSkills(*layers)
    /**
     * `passiveAbility`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun passiveAbility(base: Int, skillId: Int, skills: Map<Int, Int>) = combat.passiveAbility(base, skillId, skills)
    /**
     * `learnedMagicIds`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun learnedMagicIds(postsId: Int, level: Int) = combat.learnedMagicIds(postsId, level)
    /**
     * `effectAreaOffsets`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun effectAreaOffsets(id: Int) = combat.effectAreaOffsets(id)
    /**
     * `upgradedEffectArea`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun upgradedEffectArea(id: Int) = combat.upgradedEffectArea(id)
    /**
     * `hitAreaProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hitAreaProfile(id: Int) = combat.hitAreaProfile(id)

    companion object {
        /**
         * `sayLayerUnitName`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun sayLayerUnitName(rawName: String): String = rawName.takeWhile { !it.isDigit() }
        /**
         * `load`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun load(): GameDataCatalog = load(ClasspathThenGdxGameDataResourceSource())
        /**
         * `load`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        internal fun load(source: GameDataResourceSource): GameDataCatalog =
            GameDataCatalog(GameDataRepository(source).load())
    }
}
