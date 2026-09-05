package com.jojo.port

/** Testable route boundary between EquipLayer and EquipConfirmLayer. */
class EquipConfirmationFlow(
    private val campaign: CampaignState,
    private val data: OriginalGameData,
) {
    data class Request(
        val values: List<Int>,
        val actionLabel: String,
        val itemId: Int? = null,
        val unequipSlot: CampaignState.EquipmentSlot? = null,
    )

    var pending: Request? = null
        private set

    fun requestEquip(unitId: Int, itemId: Int): Request? =
        campaign.previewEquipInventoryItem(unitId, itemId, data)?.let { preview ->
            Request(preview.values, "장비", itemId = itemId).also { pending = it }
        }

    fun requestUnequip(unitId: Int, slot: CampaignState.EquipmentSlot): Request? =
        campaign.previewUnequipInventorySlot(unitId, slot, data)?.let { preview ->
            Request(preview.values, "해제", unequipSlot = slot).also { pending = it }
        }

    fun answer(unitId: Int, accept: Boolean): Boolean {
        val request = pending ?: return false
        pending = null
        if (!accept) return false
        return when {
            request.itemId != null -> campaign.equipInventoryItem(unitId, request.itemId, data) != null
            request.unequipSlot != null -> campaign.unequipInventorySlot(unitId, request.unequipSlot)
            else -> false
        }
    }

    fun cancel() { pending = null }
}
