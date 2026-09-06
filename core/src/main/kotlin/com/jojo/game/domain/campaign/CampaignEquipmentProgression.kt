// Campaign
package com.jojo.game.domain.campaign

import com.jojo.game.infrastructure.data.GameDataCatalog

/** CampaignEquipmentProgression: 전투·스크립트 보상 경험치를 장비 등급과 능력치 변화로 환산한다. */
class CampaignEquipmentProgression internal constructor(
    /**
     * `equipmentRepository` (CampaignEquipmentRepository,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val equipmentRepository: CampaignEquipmentRepository,
) {
    /**
     * `grantBattleExperience`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun grantBattleExperience(
        unitId: Int,
        ownLevel: Int,
        opponentLevel: Int,
        dealtDamage: Int,
        attacking: Boolean,
        data: GameDataCatalog,
    ): CampaignEquipmentExperienceResult? {
        /**
         * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val current = equipmentRepository.equipmentFor(unitId) ?: return null
        /**
         * `gain` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val gain = if (attacking) {
            if (dealtDamage != 0) if (ownLevel <= opponentLevel) 3 else 2 else 1
        } else if (dealtDamage != 0) {
            if (ownLevel <= opponentLevel) 4 else 3
        } else 1
        /**
         * `slot` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val slot = if (attacking) CampaignEquipmentSlot.WEAPON else CampaignEquipmentSlot.ARMOR
        return applyExperience(unitId, current, gain, slot, data, limitGainToRemaining = false)
    }

    /** grantExperienceAmount: 복원된 경험치 수치를 지정 장비에 적용하고 등급 상승 결과를 계산한다. */
    fun grantExperienceAmount(
        unitId: Int,
        amount: Int,
        slot: CampaignEquipmentSlot,
        data: GameDataCatalog,
    ): CampaignEquipmentExperienceResult? {
        require(slot != CampaignEquipmentSlot.AUXILIARY) { "auxiliary equipment has no EXP track" }
        /**
         * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val current = equipmentRepository.equipmentFor(unitId) ?: return null
        return applyExperience(unitId, current, amount.coerceAtLeast(0), slot, data, limitGainToRemaining = true)
    }

    /**
     * `applyExperience`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun applyExperience(
        unitId: Int,
        current: CampaignEquipment,
        requestedGain: Int,
        slot: CampaignEquipmentSlot,
        data: GameDataCatalog,
        limitGainToRemaining: Boolean,
    ): CampaignEquipmentExperienceResult? {
        /**
         * `weapon` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val weapon = slot == CampaignEquipmentSlot.WEAPON
        /**
         * `itemId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val itemId = compactToItemId(
            if (weapon) current.weapon else current.armor,
            if (weapon) WEAPON_OFFSET else ARMOR_OFFSET,
        ) ?: return null
        /**
         * `oldLevel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val oldLevel = if (weapon) current.weaponLevel else current.armorLevel
        /**
         * `oldExperience` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val oldExperience = if (weapon) current.weaponExperience else current.armorExperience
        /**
         * `limit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val limit = data.equipmentExperienceLimit(itemId, oldLevel)
        /**
         * `gain` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val gain = if (limitGainToRemaining) {
            requestedGain.coerceAtMost((limit - oldExperience).coerceAtLeast(0))
        } else {
            requestedGain
        }
        /**
         * `filled` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val filled = if (limitGainToRemaining) oldExperience + gain else (oldExperience + gain).coerceAtMost(limit)
        /**
         * `leveledUp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val leveledUp = filled >= limit && oldLevel < data.equipmentLevelLimit(itemId)
        /**
         * `newLevel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val newLevel = if (leveledUp) oldLevel + 1 else oldLevel
        // 최고 레벨에서는 누적 경험치를 유지하고, 그 외 레벨업 시에는 초기화한다.
        val newExperience = if (leveledUp && newLevel < data.equipmentLevelLimit(itemId)) 0 else filled
        equipmentRepository.storeEquipment(
            unitId,
            if (weapon) current.copy(weaponLevel = newLevel, weaponExperience = newExperience)
            else current.copy(armorLevel = newLevel, armorExperience = newExperience),
        )
        /**
         * `profile` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val profile = data.equipmentProfile(itemId) ?: return null
        /**
         * `valueAt`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun valueAt(level: Int) = profile.value + profile.upgradePerLevel * (level - 1).coerceAtLeast(0)
        return CampaignEquipmentExperienceResult(
            unitId = unitId,
            slot = slot,
            itemId = itemId,
            gained = gain,
            oldLevel = oldLevel,
            newLevel = newLevel,
            oldExperience = oldExperience,
            newExperience = newExperience,
            oldValue = valueAt(oldLevel),
            newValue = valueAt(newLevel),
        )
    }

    private companion object {
        /**
         * `ITEM_PROPERTY_FIRST` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ITEM_PROPERTY_FIRST = 150
        /**
         * `WEAPON_OFFSET` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val WEAPON_OFFSET = 0
        /**
         * `ARMOR_OFFSET` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ARMOR_OFFSET = 70

        /**
         * `compactToItemId`: 입력을 규칙에 따라 계산·변환한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun compactToItemId(compact: Int, offset: Int): Int? {
            if (compact <= 1) return null
            val id = compact - 2 + offset
            return if (id >= ITEM_PROPERTY_FIRST) id + 105 else id
        }
    }
}
