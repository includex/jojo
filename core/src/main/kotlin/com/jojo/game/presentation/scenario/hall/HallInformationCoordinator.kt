// Scenario
package com.jojo.game.presentation.scenario.hall
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.*
import com.jojo.game.application.hall.HallManagementCommandAdapter
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.presentation.shared.overlay.UnitInfoLayer
import com.jojo.game.presentation.shared.overlay.MagicInfoLayer
import com.jojo.game.presentation.shared.overlay.TerrainLayer

/** HallInformationCoordinator: 거점 정보 조정기이며, 사용자 입력과 런타임 상태를 해석해 화면 전환과 오버레이 처리를 조정한다. */
internal class HallInformationCoordinator(
    private val campaign: CampaignState,
    private val catalog: GameDataCatalog,
    private val commands: HallManagementCommandAdapter,
    private val views: HallManagementViewFactory,
    private val equipUnitIds: () -> List<Int>,
) {
    private val overlayInput = HallOverlayInteractionController()
    private val itemInput = HallManagementInteractionController()

    var info: HallInfo? = null
    var propertyTab = HallPropertyTab.WEAPON
    var terrainTab = TerrainLayer.Tab.RISE
    var itemDetail: HallItemDetail? = null
    var itemLayer: ItemLayer? = null
    var magicLayer: MagicInfoLayer? = null
    var unitInfoLayer: UnitInfoLayer? = null
    var featsLayer: FeatsLayer? = null
    var featsHelpOpen = false

    fun openItem(itemId: Int, level: String, experience: Int, canDrop: Boolean) {
        val profile = catalog.equipmentProfile(itemId) ?: return
        itemDetail = HallItemDetail(
            itemId, level, experience,
            catalog.equipmentExperienceLimit(itemId, level.toIntOrNull() ?: 1),
        )
        itemLayer = ItemLayer(itemId, profile.name, canDrop, object : ItemLayer.Repository {
            override fun discard(itemId: Int): Boolean = commands.discard(itemId)
        })
    }

    fun handleInfoTap(kind: HallInfo, x: Float, y: Float) {
        when (val intent = overlayInput.infoTap(HallInfoInputKind.valueOf(kind.name), x, y)) {
            HallInfoInputIntent.None -> Unit
            HallInfoInputIntent.Close -> info = null
            is HallInfoInputIntent.OpenForcesRow -> equipUnitIds().sorted().getOrNull(intent.row)?.let(::openUnitInfo)
            is HallInfoInputIntent.SelectPropertyTab -> HallPropertyTab.entries.getOrNull(intent.tab)?.let { propertyTab = it }
            is HallInfoInputIntent.OpenPropertyRow -> propertyItemIds().getOrNull(intent.row)?.let { itemId ->
                val level = if (propertyTab >= HallPropertyTab.AUXILIARY) "---" else (campaign.inventory.itemLevels(itemId).firstOrNull() ?: 1).toString()
                val experience = if (propertyTab >= HallPropertyTab.AUXILIARY) 0 else campaign.inventory.itemExperiences(itemId).firstOrNull() ?: 0
                val profile = catalog.equipmentProfile(itemId) ?: return@let
                openItem(itemId, level, experience, campaign.inventory.items[itemId]?.let { it > 0 } == true && catalog.equipmentCategory(profile) != 3)
            }
            is HallInfoInputIntent.SelectTerrainTab -> terrainTab = if (intent.index == 0) TerrainLayer.Tab.RISE else TerrainLayer.Tab.EXPEND
            is HallInfoInputIntent.OpenTreasureRow -> catalog.treasureProfiles().take(6).getOrNull(intent.row)
                ?.takeIf { it.id in campaign.inventory.discoveredTreasures }
                ?.let { openItem(it.id, "1", 0, false) }
        }
    }

    fun handleItemTap(x: Float, y: Float) {
        val layer = itemLayer ?: return
        when (itemInput.itemTap(layer.discardConfirmationOpen, x, y)) {
            HallItemInputIntent.DISCARD_YES -> layer.onDiscardAnswer(1)
            HallItemInputIntent.DISCARD_NO -> layer.onDiscardAnswer(0)
            HallItemInputIntent.CLOSE -> layer.onButton(0, ItemLayer.TOUCH_END)
            HallItemInputIntent.REQUEST_DISCARD -> layer.onButton(1, ItemLayer.TOUCH_END)
            HallItemInputIntent.NONE -> Unit
        }
        if (!layer.attached) {
            itemLayer = null
            itemDetail = null
        }
    }

    fun handleMagicTap(x: Float, y: Float) {
        val layer = magicLayer ?: return
        if (overlayInput.magicTap(x, y) == HallLayerTapIntent.CLOSE) layer.close(UnitInfoLayer.TOUCH_END)
        if (!layer.attached) magicLayer = null
    }

    fun openUnitInfo(selectedUnitId: Int) {
        val rows = equipUnitIds().sorted().mapNotNull { id ->
            val unit = catalog.unitProfile(id) ?: return@mapNotNull null
            val level = campaign.unitAttribute(id, 18, unit.level)
            val battle = catalog.battleProfile(id, (level - 1).coerceAtLeast(0), campaign.unitAttribute(id, 17, unit.posts))
            UnitInfoLayer.Unit(
                id, campaign.unitNames[id] ?: if (id == 181) "병사 " else unit.name,
                catalog.postsName(campaign.unitAttribute(id, 17, unit.posts)), level,
                battle?.maxHitPoints ?: unit.maxHitPoints, battle?.maxHitPoints ?: unit.maxHitPoints,
                battle?.maxMagicPoints ?: unit.maxMagicPoints, battle?.maxMagicPoints ?: unit.maxMagicPoints,
                battle?.attack ?: unit.attack, battle?.defense ?: unit.defense, battle?.spirit ?: unit.spirit,
                battle?.critical ?: unit.critical, battle?.morale ?: unit.morale,
            )
        }
        if (rows.isEmpty()) return
        unitInfoLayer = UnitInfoLayer(rows, featsEnabled = campaign.globalVariables[4074].toString().toIntOrNull() != 0)
            .also { it.onCreate(rows.indexOfFirst { row -> row.id == selectedUnitId }.coerceAtLeast(0)) }
    }

    fun handleUnitInfoTap(x: Float, y: Float) {
        val layer = unitInfoLayer ?: return
        when (overlayInput.unitInfoTap(x, y)) {
            HallLayerTapIntent.PRIMARY -> openFeatsFromUnitInfo()
            HallLayerTapIntent.CLOSE -> layer.onCancel(UnitInfoLayer.TOUCH_END)
            else -> Unit
        }
        if (!layer.ref().attached) unitInfoLayer = null
    }

    fun handleFeatsTap(x: Float, y: Float) {
        val layer = featsLayer ?: return
        when (overlayInput.featsTap(x, y, featsHelpOpen)) {
            HallLayerTapIntent.PRIMARY -> if (featsHelpOpen) featsHelpOpen = false
            HallLayerTapIntent.SECONDARY -> openFeatsHelp()
            HallLayerTapIntent.CLOSE -> layer.onButton(0, FeatsLayer.TOUCH_END)
            HallLayerTapIntent.CANCEL -> layer.onCancel(FeatsLayer.TOUCH_END)
            HallLayerTapIntent.NONE -> Unit
        }
        if (!layer.attached) featsLayer = null
    }

    fun openFeatsFromUnitInfo() {
        val unitInfo = unitInfoLayer ?: return
        if (!unitInfo.onButton(8, UnitInfoLayer.TOUCH_END)) return
        if (unitInfo.takeRoutes().none { it.route == UnitInfoLayer.Route.FEATS }) return
        featsLayer = FeatsLayer(featsRows(unitInfo.ref().unit))
    }

    fun openFeatsHelp() {
        val layer = featsLayer ?: return
        if (layer.onButton(1, FeatsLayer.TOUCH_END) && layer.consumeRoute() == FeatsLayer.Route.HELP) featsHelpOpen = true
    }

    private fun propertyItemIds(): List<Int> = views.propertyItemIds(propertyTab.ordinal)

    private fun featsRows(unit: UnitInfoLayer.Unit): List<FeatsLayer.Row> {
        val abilities = if (unit.id == 0) listOf(41, 49, 46, 40, 42)
        else listOf(unit.attack, unit.defense, unit.spirit, unit.critical, unit.morale)
        return FeatsLayer.TITLES.mapIndexed { index, title -> FeatsLayer.Row(title, abilities[index], 0, 100, 127) }
    }
}
