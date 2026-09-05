package com.jojo.game

/** Applies battle and scripted EXP gains to equipment owned by an inventory repository. */
class CampaignEquipmentProgression internal constructor(
    private val equipmentRepository: CampaignEquipmentRepository,
) {
    fun grantBattleExperience(
        unitId: Int,
        ownLevel: Int,
        opponentLevel: Int,
        dealtDamage: Int,
        attacking: Boolean,
        data: GameDataCatalog,
    ): CampaignEquipmentExperienceResult? {
        val current = equipmentRepository.equipmentFor(unitId) ?: return null
        val gain = if (attacking) {
            if (dealtDamage != 0) if (ownLevel <= opponentLevel) 3 else 2 else 1
        } else if (dealtDamage != 0) {
            if (ownLevel <= opponentLevel) 4 else 3
        } else 1
        val slot = if (attacking) CampaignEquipmentSlot.WEAPON else CampaignEquipmentSlot.ARMOR
        return applyExperience(unitId, current, gain, slot, data, limitGainToRemaining = false)
    }

    /** Applies the skill amount passed by the battle restore commands. */
    fun grantExperienceAmount(
        unitId: Int,
        amount: Int,
        slot: CampaignEquipmentSlot,
        data: GameDataCatalog,
    ): CampaignEquipmentExperienceResult? {
        require(slot != CampaignEquipmentSlot.AUXILIARY) { "auxiliary equipment has no EXP track" }
        val current = equipmentRepository.equipmentFor(unitId) ?: return null
        return applyExperience(unitId, current, amount.coerceAtLeast(0), slot, data, limitGainToRemaining = true)
    }

    private fun applyExperience(
        unitId: Int,
        current: CampaignEquipment,
        requestedGain: Int,
        slot: CampaignEquipmentSlot,
        data: GameDataCatalog,
        limitGainToRemaining: Boolean,
    ): CampaignEquipmentExperienceResult? {
        val weapon = slot == CampaignEquipmentSlot.WEAPON
        val itemId = compactToItemId(
            if (weapon) current.weapon else current.armor,
            if (weapon) WEAPON_OFFSET else ARMOR_OFFSET,
        ) ?: return null
        val oldLevel = if (weapon) current.weaponLevel else current.armorLevel
        val oldExperience = if (weapon) current.weaponExperience else current.armorExperience
        val limit = data.equipmentExperienceLimit(itemId, oldLevel)
        val gain = if (limitGainToRemaining) {
            requestedGain.coerceAtMost((limit - oldExperience).coerceAtLeast(0))
        } else {
            requestedGain
        }
        val filled = if (limitGainToRemaining) oldExperience + gain else (oldExperience + gain).coerceAtMost(limit)
        val leveledUp = filled >= limit && oldLevel < data.equipmentLevelLimit(itemId)
        val newLevel = if (leveledUp) oldLevel + 1 else oldLevel
        // At maximum level the filled bar remains visible; otherwise a level-up resets it.
        val newExperience = if (leveledUp && newLevel < data.equipmentLevelLimit(itemId)) 0 else filled
        equipmentRepository.storeEquipment(
            unitId,
            if (weapon) current.copy(weaponLevel = newLevel, weaponExperience = newExperience)
            else current.copy(armorLevel = newLevel, armorExperience = newExperience),
        )
        val profile = data.equipmentProfile(itemId) ?: return null
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
        const val ITEM_PROPERTY_FIRST = 150
        const val WEAPON_OFFSET = 0
        const val ARMOR_OFFSET = 70

        fun compactToItemId(compact: Int, offset: Int): Int? {
            if (compact <= 1) return null
            val id = compact - 2 + offset
            return if (id >= ITEM_PROPERTY_FIRST) id + 105 else id
        }
    }
}
