// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.application.runtime.RuntimeScenarioOverlay

/** HallManagementViewFactory: 거점 Management 표시 정보 생성기이며, 도메인 데이터를 화면에 바로 쓸 수 있는 표시 모델로 변환한다. */
internal class HallManagementViewFactory(
    /** `campaign` (CampaignState): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val campaign: CampaignState,
    /** `catalog` (GameDataCatalog): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val catalog: GameDataCatalog,
    moduleName: String,
    /** `overlayVariant` (RuntimeScenarioOverlay?): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val overlayVariant: RuntimeScenarioOverlay?,
) {
    /**
     * `stageIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val stageIndex = moduleName.substringAfter('_').toIntOrNull() ?: 0
    /**
     * `equipProjector` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val equipProjector = HallEquipViewProjector(campaign, catalog)
    /**
     * `propertyProjector` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val propertyProjector = HallPropertyViewProjector(campaign, catalog)

    /**
     * `equip`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun equip(unitId: Int, selectedTab: Int, notice: String?): HallEquipView =
        equipProjector.project(unitId, selectedTab, notice)

    /**
     * `property`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun property(selectedTab: Int): HallPropertyView = propertyProjector.project(selectedTab)

    /**
     * `propertyItemIds`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun propertyItemIds(selectedTab: Int): List<Int> = propertyProjector.itemIds(selectedTab)

    /**
     * `unitRoster`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun unitRoster(unitIds: List<Int>): HallUnitRosterView = HallUnitRosterView(
        unitIds.take(6).map { id ->
            /**
             * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val unit = catalog.unitProfile(id)
            HallUnitRosterRowView(
                name = campaign.unitNames[id] ?: if (id == 181) "병사 " else unit?.name ?: "무장",
                postName = catalog.postsName(campaign.unitAttribute(id, 17, unit?.posts ?: 0)),
            )
        },
    )

    /**
     * `buyCandidates`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun buyCandidates(): List<GameDataCatalog.EquipmentProfile> {
        if (overlayVariant == RuntimeScenarioOverlay.BUY) return catalog.allEquipmentProfiles()
            .asReversed()
            .filter { it.id != 255 && catalog.equipmentCategory(it) <= 2 && it.price != 255 }
        return catalog.hallBuyProfiles(stageIndex, campaign.averageJoinedLevel())
            .filter { catalog.equipmentCategory(it) <= 2 }
    }

    /**
     * `buyProperties`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun buyProperties(): List<GameDataCatalog.EquipmentProfile> =
        (if (overlayVariant == RuntimeScenarioOverlay.BUY) catalog.allEquipmentProfiles()
        else catalog.hallBuyProfiles(stageIndex, campaign.averageJoinedLevel()))
            .filter { catalog.equipmentCategory(it) == 3 && it.price != 255 }
            .sortedBy { it.id }

    /**
     * `buyCatalog`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun buyCatalog(buyTabIndex: Int): HallBuyCatalogView {
        val propertyTab = buyTabIndex != 0
        val profiles = if (propertyTab) buyProperties().take(4) else buyCandidates().take(3)
        return HallBuyCatalogView(
            propertyTab = propertyTab,
            rows = profiles.map { item ->
                val inventory = campaign.inventory.items[item.id] ?: 0
                HallBuyCatalogRowView(
                    name = item.name,
                    icon = item.icon,
                    typeName = catalog.equipmentTypeName(item.itemType),
                    inventory = inventory,
                    total = inventory + campaign.inventory.equippedItems().count { it.itemId == item.id },
                    price = catalog.purchasePrice(item).let { price -> if (item.price == 255) "---" else price.toString() },
                )
            },
        )
    }

    /**
     * `buyUnitSummary`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun buyUnitSummary(unitId: Int): HallBuyUnitSummaryView {
        val unit = catalog.unitProfile(unitId) ?: catalog.unitProfile(0)
        val zeroBasedLevel = (campaign.unitAttribute(unitId, 18, unit?.level ?: 1) - 1).coerceAtLeast(0)
        val profile = unit?.let {
            catalog.battleProfile(it.id, zeroBasedLevel, campaign.unitAttribute(it.id, 17, it.posts))
        }
        val bonus = campaign.inventory.equipment[unitId]
            ?.let { catalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1) }
            ?: GameDataCatalog.EquipmentBonus()
        return HallBuyUnitSummaryView(
            portraitId = portraitId(unitId),
            name = campaign.unitNames[unitId] ?: unit?.name ?: "조조",
            postName = catalog.postsName(campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)).ifEmpty { "군웅" },
            level = profile?.level ?: 1,
            hitPoints = profile?.maxHitPoints ?: 0,
            magicPoints = profile?.maxMagicPoints ?: 0,
            stats = listOf(
                HallBuyUnitSummaryStat("공격력", (profile?.attack ?: 0) + bonus.attack),
                HallBuyUnitSummaryStat("정신력", (profile?.spirit ?: 0) + bonus.spirit),
                HallBuyUnitSummaryStat("방어력", (profile?.defense ?: 0) + bonus.defense),
                HallBuyUnitSummaryStat("폭발력", profile?.critical ?: 0),
                HallBuyUnitSummaryStat("사기", profile?.morale ?: 0),
                HallBuyUnitSummaryStat("이동력", profile?.movement ?: 0),
            ),
        )
    }

    /**
     * `forces`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun forces(): HallForcesView = HallForcesView(
        rows = campaign.joinedUnits.take(7).mapNotNull { id ->
            /**
             * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val unit = catalog.unitProfile(id) ?: return@mapNotNull null
            /**
             * `level` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val level = campaign.unitAttribute(id, 18, unit.level)
            /**
             * `profile` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val profile = catalog.battleProfile(id, (level - 1).coerceAtLeast(0), campaign.unitAttribute(id, 17, unit.posts))
                ?: return@mapNotNull null
            /**
             * `bonus` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val bonus = campaign.inventory.equipment[id]?.let {
                catalog.equipmentBonus(it.asScriptValues(), profile.level)
            } ?: GameDataCatalog.EquipmentBonus()
            HallForcesRowView(
                values = listOf(
                    campaign.unitNames[id] ?: GameDataCatalog.sayLayerUnitName(unit.name),
                    profile.arm.name, profile.level.toString(),
                    "${profile.maxHitPoints}/${profile.maxHitPoints}",
                    "${profile.maxMagicPoints}/${profile.maxMagicPoints}",
                    (profile.attack + bonus.attack).toString(), (profile.defense + bonus.defense).toString(),
                    (profile.spirit + bonus.spirit).toString(), profile.critical.toString(), profile.morale.toString(),
                ),
            )
        },
    )

    /**
     * `sellCandidates`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun sellCandidates(sellTabIndex: Int): List<HallInventoryItemView> = campaign.inventory.items.entries
        .asSequence()
        .filter { (_, count) -> count > 0 }
        .filter { (id, _) ->
            catalog.equipmentProfile(id)?.let(catalog::equipmentCategory)?.let { category ->
                if (sellTabIndex == 0) category <= 2 else category == 3
            } == true
        }
        .sortedBy { it.key }
        .map { (itemId, count) -> HallInventoryItemView(itemId, count) }
        .toList()

    /**
     * `sell`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun sell(sellTabIndex: Int, notice: String?): HallSellView {
        val equipmentTab = sellTabIndex == 0
        return HallSellView(
            rows = sellCandidates(sellTabIndex).take(5).mapNotNull { (itemId, count) ->
                val item = catalog.equipmentProfile(itemId) ?: return@mapNotNull null
                HallSellRowView(
                    name = item.name,
                    icon = item.icon,
                    primaryDetail = if (equipmentTab) "Lv: ${campaign.inventory.itemLevels(itemId).firstOrNull() ?: 1}" else "인벤토리: $count",
                    secondaryDetail = if (equipmentTab) "Exp: 0" else null,
                    salePrice = if (item.price == 255) "---" else catalog.sellingPrice(item).toString(),
                )
            },
            money = campaign.money,
            notice = notice,
        )
    }

    /**
     * `equipInventory`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun equipInventory(equipTabIndex: Int): List<HallInventoryItemView> = campaign.inventory.items.entries
        .asSequence()
        .filter { (itemId, _) -> matchesEquipTab(catalog.equipmentProfile(itemId)?.itemType, equipTabIndex) }
        .sortedWith(compareBy<Map.Entry<Int, Int>> {
            if (catalog.equipmentProfile(it.key)?.price == 255) 0 else 1
        }.thenBy {
            catalog.equipmentProfile(it.key)?.itemType ?: 255
        }.thenByDescending { it.key })
        .map { (itemId, count) -> HallInventoryItemView(itemId, count) }
        .toList()

    /**
     * `matchesEquipTab`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun matchesEquipTab(itemType: Int?, equipTabIndex: Int): Boolean = when (equipTabIndex) {
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

/** HallInventoryItemView: 거점 소지품 Item 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallInventoryItemView(val itemId: Int, val count: Int)
