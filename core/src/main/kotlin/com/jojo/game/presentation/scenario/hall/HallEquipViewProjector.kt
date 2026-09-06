// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.campaign.CampaignState

/** HallEquipViewProjector: 거점 Equip 표시 정보 변환기이며, 도메인 데이터를 화면에 바로 쓸 수 있는 표시 모델로 변환한다. */
internal class HallEquipViewProjector(
    /** `campaign` (CampaignState): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val campaign: CampaignState,
    /** `catalog` (GameDataCatalog): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val catalog: GameDataCatalog,
) {
    /**
     * `project`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun project(unitId: Int, selectedTab: Int, notice: String?): HallEquipView {
        val unit = catalog.unitProfile(unitId) ?: catalog.unitProfile(0)
        val level = campaign.unitAttribute(unitId, 18, unit?.level ?: 1)
        val profile = unit?.let {
            catalog.battleProfile(it.id, (level - 1).coerceAtLeast(0), campaign.unitAttribute(it.id, 17, it.posts))
        }
        val bonus = campaign.inventory.equipment[unitId]
            ?.let { catalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1) }
            ?: GameDataCatalog.EquipmentBonus()
        return HallEquipView(
            selectedTab = selectedTab,
            unit = HallEquipUnitView(
                portraitId = portraitId(unitId),
                name = campaign.unitNames[unitId] ?: unit?.name ?: "조조",
                armName = if (unitId == 0) "군웅" else profile?.arm?.name ?: "군웅",
                level = (profile?.level ?: 1).toString(),
                stats = listOf(
                    HallEquipStatView("HP", (profile?.maxHitPoints ?: 0).toString()),
                    HallEquipStatView("MP", (profile?.maxMagicPoints ?: 0).toString()),
                    HallEquipStatView("공격력", ((profile?.attack ?: 0) + bonus.attack).toString()),
                    HallEquipStatView("정신력", ((profile?.spirit ?: 0) + bonus.spirit).toString()),
                    HallEquipStatView("방어력", ((profile?.defense ?: 0) + bonus.defense).toString()),
                    HallEquipStatView("폭발력", (profile?.critical ?: 0).toString()),
                    HallEquipStatView("사기", (profile?.morale ?: 0).toString()),
                    HallEquipStatView("이동력", (profile?.movement ?: 0).toString()),
                ),
                slots = slots(unitId),
            ),
            inventoryRows = inventoryRows(selectedTab),
            notice = notice,
        )
    }

    /**
     * `inventoryRows`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun inventoryRows(selectedTab: Int): List<HallEquipInventoryRowView> = campaign.inventory.items.entries
        .asSequence()
        .filter { (id, _) -> matchesTab(catalog.equipmentProfile(id)?.itemType, selectedTab) }
        .sortedWith(compareBy<Map.Entry<Int, Int>> {
            if (catalog.equipmentProfile(it.key)?.price == 255) 0 else 1
        }.thenBy {
            catalog.equipmentProfile(it.key)?.itemType ?: 255
        }.thenByDescending { it.key })
        .mapNotNull { (itemId, count) ->
            /**
             * `item` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val item = catalog.equipmentProfile(itemId) ?: return@mapNotNull null
            /**
             * `level` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val level = campaign.inventory.itemLevels(itemId).firstOrNull() ?: 1
            /**
             * `experience` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val experience = campaign.inventory.itemExperiences(itemId).firstOrNull() ?: 0
            HallEquipInventoryRowView(
                name = item.name + if (count > 1) "  ×$count" else "",
                icon = item.icon,
                typeName = catalog.equipmentTypeName(item.itemType),
                level = level.toString(),
                experience = if (experience >= catalog.equipmentExperienceLimit(itemId, level)) "MAX" else experience.toString(),
            )
        }
        .take(6)
        .toList()

    /**
     * `slots`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun slots(unitId: Int): List<HallEquipSlotView> {
        val equipped = campaign.inventory.equippedItems().filter { it.unitId == unitId }
        return listOf(
            "무기:" to equipped.firstOrNull { catalog.equipmentProfile(it.itemId)?.itemType?.let { type -> type < 20 } == true },
            "보구: " to equipped.firstOrNull { catalog.equipmentProfile(it.itemId)?.itemType?.let { type -> type in 20..25 } == true },
            "보조: " to equipped.firstOrNull { catalog.equipmentProfile(it.itemId)?.itemType?.let { type -> type > 25 } == true },
        ).mapIndexedNotNull { index, (label, equippedItem) ->
            if (index == 2 && equippedItem == null) return@mapIndexedNotNull null
            val item = equippedItem?.let { catalog.equipmentProfile(it.itemId) }
            HallEquipSlotView(
                index = index,
                label = label,
                name = item?.name ?: "없음",
                icon = item?.icon,
                level = if (index == 0) (equippedItem?.level ?: 1).toString() else null,
                experience = if (index == 0) "${equippedItem?.experience ?: 0}/100" else null,
            )
        }
    }

    /**
     * `matchesTab`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun matchesTab(itemType: Int?, selectedTab: Int): Boolean = when (selectedTab) {
        0 -> itemType != null && itemType < 150
        1 -> itemType != null && itemType in 0..19
        2 -> itemType != null && itemType in 20..25
        3 -> itemType != null && itemType in 26..149
        else -> false
    }

    /**
     * `portraitId`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun portraitId(unitId: Int): Int {
        val face = catalog.unitProfile(unitId)?.face ?: return unitId
        return if (unitId == 0 && face <= 3) face + 1 else face + 8
    }
}
