// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.campaign.CampaignEquippedItem
import com.jojo.game.domain.campaign.CampaignState

/** HallPropertyViewProjector: 거점 속성 표시 정보 변환기이며, 도메인 데이터를 화면에 바로 쓸 수 있는 표시 모델로 변환한다. */
internal class HallPropertyViewProjector(
    /** `campaign` (CampaignState): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val campaign: CampaignState,
    /** `catalog` (GameDataCatalog): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val catalog: GameDataCatalog,
) {
    /**
     * `project`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun project(selectedTab: Int): HallPropertyView = HallPropertyView(
        selectedTab = selectedTab,
        rows = rows(selectedTab).take(7),
    )

    /**
     * `itemIds`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun itemIds(selectedTab: Int): List<Int> = rows(selectedTab).map(HallPropertyRowView::itemId)

    /**
     * `rows`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `equippedRow`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `inventoryRow`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `accepts`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
        /**
         * `AUXILIARY_TAB` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val AUXILIARY_TAB = 2
        /**
         * `PROPERTY_TAB` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PROPERTY_TAB = 3
    }
}
