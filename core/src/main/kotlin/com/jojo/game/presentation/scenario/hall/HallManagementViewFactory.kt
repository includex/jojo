package com.jojo.game.presentation.scenario.hall

import com.jojo.game.GameDataCatalog
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.application.runtime.RuntimeScenarioOverlay

/**
 * Creates the Hall management screens' immutable display projections from the
 * campaign read model. Interaction state stays with ScenarioScreen and is
 * supplied only as the tab, selected unit, or transient notice it controls.
 */
internal class HallManagementViewFactory(
    private val campaign: CampaignState,
    private val catalog: GameDataCatalog,
    moduleName: String,
    private val overlayVariant: RuntimeScenarioOverlay?,
) {
    private val stageIndex = moduleName.substringAfter('_').toIntOrNull() ?: 0
    private val equipProjector = HallEquipViewProjector(campaign, catalog)
    private val propertyProjector = HallPropertyViewProjector(campaign, catalog)

    fun equip(unitId: Int, selectedTab: Int, notice: String?): HallEquipView =
        equipProjector.project(unitId, selectedTab, notice)

    fun property(selectedTab: Int): HallPropertyView = propertyProjector.project(selectedTab)

    fun propertyItemIds(selectedTab: Int): List<Int> = propertyProjector.itemIds(selectedTab)

    fun unitRoster(unitIds: List<Int>): HallUnitRosterView = HallUnitRosterView(
        unitIds.take(6).map { id ->
            val unit = catalog.unitProfile(id)
            HallUnitRosterRowView(
                name = campaign.unitNames[id] ?: if (id == 181) "병사 " else unit?.name ?: "무장",
                postName = catalog.postsName(campaign.unitAttribute(id, 17, unit?.posts ?: 0)),
            )
        },
    )

    fun buyCandidates(): List<GameDataCatalog.EquipmentProfile> {
        // The isolated source fixture feeds 0..itemCount (255 is its sentinel).
        // Cocos' vertical layout leaves the tail at the top of the viewport.
        if (overlayVariant == RuntimeScenarioOverlay.BUY) return catalog.allEquipmentProfiles()
            .asReversed()
            .filter { it.id != 255 && catalog.equipmentCategory(it) <= 2 && it.price != 255 }
        return catalog.hallBuyProfiles(stageIndex, campaign.averageJoinedLevel())
            .filter { catalog.equipmentCategory(it) <= 2 }
    }

    fun buyProperties(): List<GameDataCatalog.EquipmentProfile> =
        (if (overlayVariant == RuntimeScenarioOverlay.BUY) catalog.allEquipmentProfiles()
        else catalog.hallBuyProfiles(stageIndex, campaign.averageJoinedLevel()))
            .filter { catalog.equipmentCategory(it) == 3 && it.price != 255 }
            .sortedBy { it.id }

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

    fun forces(): HallForcesView = HallForcesView(
        rows = campaign.joinedUnits.take(7).mapNotNull { id ->
            val unit = catalog.unitProfile(id) ?: return@mapNotNull null
            val level = campaign.unitAttribute(id, 18, unit.level)
            val profile = catalog.battleProfile(id, (level - 1).coerceAtLeast(0), campaign.unitAttribute(id, 17, unit.posts))
                ?: return@mapNotNull null
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

    private fun matchesEquipTab(itemType: Int?, equipTabIndex: Int): Boolean = when (equipTabIndex) {
        0 -> itemType != null && itemType < 150
        1 -> itemType != null && itemType in 0..19
        2 -> itemType != null && itemType in 20..25
        3 -> itemType != null && itemType in 26..149
        else -> false
    }

    private fun portraitId(unitId: Int): Int {
        val face = catalog.unitProfile(unitId)?.face ?: return unitId
        return if (unitId == 0 && face <= 3) face + 1 else face + 8
    }
}

/** Immutable item/count projection retained for Hall interaction hit testing. */
internal data class HallInventoryItemView(val itemId: Int, val count: Int)
