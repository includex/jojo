package com.jojo.game.domain.campaign

enum class CampaignEquipmentSlot { WEAPON, ARMOR, AUXILIARY }

data class CampaignEquippedItem(
    val unitId: Int,
    val itemId: Int,
    val level: Int,
    val experience: Int,
)

data class CampaignEquipPreview(val itemName: String, val values: List<Int>)

/** Narrow repository used by equipment progression and implemented by the inventory aggregate. */
internal interface CampaignEquipmentRepository {
    fun equipmentFor(unitId: Int): CampaignEquipment?
    fun storeEquipment(unitId: Int, equipment: CampaignEquipment)
}
