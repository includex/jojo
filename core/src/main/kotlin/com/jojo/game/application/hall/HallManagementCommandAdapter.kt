// Campaign
package com.jojo.game.application.hall

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.campaign.CampaignState

/** HallManagementCommandAdapter: 거점 구매·판매와 아이템 폐기 변경을 처리하는 애플리케이션 경계입니다. */
class HallManagementCommandAdapter(
    /** campaign: 구매·판매 결과를 반영할 진행 상태이다. */
    private val campaign: CampaignState,
    /** catalog: 물품 가격과 장비 정보를 조회할 게임 데이터 표이다. */
    private val catalog: GameDataCatalog,
) {
    /** Result: 거점 관리 명령의 성공 여부와 사용자 알림 문구를 표현한다. */
    sealed interface Result {
        val message: String

        data class Success(override val message: String) : Result
        data class Rejected(override val message: String) : Result
    }

    /** buy: 금화와 가격을 확인한 뒤 물품을 구매해 소지품에 추가한다. */
    fun buy(itemId: Int): Result {
        val item = catalog.equipmentProfile(itemId) ?: return Result.Rejected("구매할 수 없는 물품입니다.")
        val price = catalog.purchasePrice(item)
        if (price == 255) return Result.Rejected("값으로 매길 수 없는 보물이므로 구매할 수 없습니다.")
        if (campaign.money < price) return Result.Rejected("금화가 부족하여 구매할 수 없습니다")
        campaign.addMoney(-price)
        campaign.inventory.addItem(itemId)
        return Result.Success("${item.name} 구매")
    }

    /** sell: 판매 가능한 소지품을 제거하고 판매 금액을 지급한다. */
    fun sell(itemId: Int): Result {
        val item = catalog.equipmentProfile(itemId) ?: return Result.Rejected("판매할 수 없는 물품입니다.")
        if (item.price == 255) return Result.Rejected("판매할 수 없는 물품입니다.")
        if (!campaign.inventory.consumeItem(itemId)) return Result.Rejected("판매할 물품이 없습니다.")
        campaign.addMoney(catalog.sellingPrice(item))
        return Result.Success("${item.name} 판매")
    }

    /** discard: 지정한 소지품 하나를 폐기하고 성공 여부를 반환한다. */
    fun discard(itemId: Int): Boolean = campaign.inventory.consumeItem(itemId)

    /** unequipAll: 장착 중인 모든 장비를 해제하고 해제한 개수를 반환한다. */
    fun unequipAll(): Int = campaign.inventory.unequipAllEquipment()
}
