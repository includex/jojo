// Campaign
package com.jojo.game.domain.campaign
import com.jojo.game.domain.campaign.CampaignInventory

import com.jojo.game.*

internal object CampaignInventoryEquipmentView {
    fun equippedItems(loadouts: Map<Int, CampaignEquipment>): List<CampaignEquippedItem> =
        loadouts.flatMap { (unitId, value) ->
            buildList {
                compactToItemId(value.weapon, WEAPON_OFFSET)?.let {
                    add(CampaignEquippedItem(unitId, it, value.weaponLevel, value.weaponExperience))
                }
                compactToItemId(value.armor, ARMOR_OFFSET)?.let {
                    add(CampaignEquippedItem(unitId, it, value.armorLevel, value.armorExperience))
                }
                compactToItemId(value.auxiliary, AUXILIARY_OFFSET)?.let {
                    add(CampaignEquippedItem(unitId, it, 1, 0))
                }
            }
        }

    private fun compactToItemId(compact: Int, offset: Int): Int? {
        if (compact <= 1) return null
        val id = compact - 2 + offset
        return if (id >= ITEM_PROPERTY_FIRST) id + 105 else id
    }

    private const val ITEM_PROPERTY_FIRST = 150
    private const val WEAPON_OFFSET = 0
    private const val ARMOR_OFFSET = 70
    private const val AUXILIARY_OFFSET = 109
}
