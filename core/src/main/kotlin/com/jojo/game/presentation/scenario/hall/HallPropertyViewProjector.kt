// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.campaign.CampaignEquippedItem
import com.jojo.game.domain.campaign.CampaignState

/** HallPropertyViewProjector: 거점 속성 표시 정보 변환기이며, 도메인 데이터를 화면에 바로 쓸 수 있는 표시 모델로 변환한다. */
internal class HallPropertyViewProjector(
    private val campaign: CampaignState,
    private val catalog: GameDataCatalog,
) {
    fun project(selectedTab: Int): HallPropertyView = HallPropertyView(
        selectedTab = selectedTab,
        rows = rows(selectedTab).take(7),
    )

    fun itemIds(selectedTab: Int): List<Int> = rows(selectedTab).map(HallPropertyRowView::itemId)

    private fun rows(selectedTab: Int): List<HallPropertyRowView> {
        campaign.joinedUnits.forEach { campaign.inventory.ensureDefaultEquipment(it, catalog) }
        val equipped = if (selectedTab == PROPERTY_TAB) emptyList() else campaign.inventory.equippedItems()
            .filter { accepts(it.itemId, selectedTab) }
            .sortedWith(compareBy<CampaignEquippedItem> { it.itemId }.thenBy { it.unitId })
            .mapNotNull { equippedItem -> equippedRow(equippedItem, selectedTab) }
        val inventory = campaign.inventory.items.entries
            .sortedBy { it.key }
            .filter { accepts(it.key, selectedTab) }
            .mapNotNull { (itemId, count) -> inventoryRow(itemId, count, selectedTab) }
        return equipped + inventory
    }

    private fun equippedRow(item: CampaignEquippedItem, selectedTab: Int): HallPropertyRowView? {
        val profile = catalog.equipmentProfile(item.itemId) ?: return null
        val hiddenProgress = selectedTab == AUXILIARY_TAB
        return HallPropertyRowView(
            itemId = item.itemId,
            name = profile.name,
            icon = profile.icon,
            typeName = catalog.equipmentTypeName(profile.itemType),
            level = if (hiddenProgress) "---" else item.level.toString(),
            experience = if (hiddenProgress) "---" else item.experience.toString(),
            owner = campaign.unitNames[item.unitId] ?: catalog.unitProfile(item.unitId)?.name.orEmpty(),
        )
    }

    private fun inventoryRow(itemId: Int, count: Int, selectedTab: Int): HallPropertyRowView? {
        val profile = catalog.equipmentProfile(itemId) ?: return null
        val hiddenProgress = selectedTab >= AUXILIARY_TAB
        return HallPropertyRowView(
            itemId = itemId,
            name = profile.name + if (count > 1) " ×$count" else "",
            icon = profile.icon,
            typeName = catalog.equipmentTypeName(profile.itemType),
            level = if (hiddenProgress) "---" else (campaign.inventory.itemLevels(itemId).firstOrNull() ?: 1).toString(),
            experience = if (hiddenProgress) "---" else (campaign.inventory.itemExperiences(itemId).firstOrNull() ?: 0).toString(),
            owner = "창고",
        )
    }

    private fun accepts(itemId: Int, selectedTab: Int): Boolean {
        val item = catalog.equipmentProfile(itemId) ?: return false
        return when (selectedTab) {
            0 -> item.itemType < 20
            1 -> item.itemType in 20..25
            2 -> item.itemType > 45 && itemId < 150
            else -> itemId >= 150 || item.itemType in 26..45
        }
    }

    private companion object {
        const val AUXILIARY_TAB = 2
        const val PROPERTY_TAB = 3
    }
}
