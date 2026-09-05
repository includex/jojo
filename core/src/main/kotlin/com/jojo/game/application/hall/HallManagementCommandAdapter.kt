package com.jojo.game.application.hall

import com.jojo.game.GameDataCatalog
import com.jojo.game.domain.campaign.CampaignState

/** Application boundary for Hall purchase, sale, and ItemLayer discard mutations. */
class HallManagementCommandAdapter(
    private val campaign: CampaignState,
    private val catalog: GameDataCatalog,
) {
    sealed interface Result {
        val message: String

        data class Success(override val message: String) : Result
        data class Rejected(override val message: String) : Result
    }

    fun buy(itemId: Int): Result {
        val item = catalog.equipmentProfile(itemId) ?: return Result.Rejected("구매할 수 없는 물품입니다.")
        val price = catalog.purchasePrice(item)
        if (price == 255) return Result.Rejected("값으로 매길 수 없는 보물이므로 구매할 수 없습니다.")
        if (campaign.money < price) return Result.Rejected("금화가 부족하여 구매할 수 없습니다")
        campaign.addMoney(-price)
        campaign.inventory.addItem(itemId)
        return Result.Success("${item.name} 구매")
    }

    fun sell(itemId: Int): Result {
        val item = catalog.equipmentProfile(itemId) ?: return Result.Rejected("판매할 수 없는 물품입니다.")
        if (item.price == 255) return Result.Rejected("판매할 수 없는 물품입니다.")
        if (!campaign.inventory.consumeItem(itemId)) return Result.Rejected("판매할 물품이 없습니다.")
        campaign.addMoney(catalog.sellingPrice(item))
        return Result.Success("${item.name} 판매")
    }

    fun discard(itemId: Int): Boolean = campaign.inventory.consumeItem(itemId)

    fun unequipAll(): Int = campaign.inventory.unequipAllEquipment()
}
