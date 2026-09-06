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
    /** `campaign` (CampaignState): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val campaign: CampaignState,
    /** `catalog` (GameDataCatalog): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val catalog: GameDataCatalog,
    /** `commands` (HallManagementCommandAdapter): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val commands: HallManagementCommandAdapter,
    /** `views` (HallManagementViewFactory): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val views: HallManagementViewFactory,
    /** `equipUnitIds` (() -> List<Int>): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val equipUnitIds: () -> List<Int>,
) {
    /**
     * `overlayInput` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val overlayInput = HallOverlayInteractionController()
    /**
     * `itemInput` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val itemInput = HallManagementInteractionController()

    /**
     * `info` (HallInfo?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var info: HallInfo? = null
    /**
     * `propertyTab` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var propertyTab = HallPropertyTab.WEAPON
    /**
     * `terrainTab` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var terrainTab = TerrainLayer.Tab.RISE
    /**
     * `itemDetail` (HallItemDetail?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var itemDetail: HallItemDetail? = null
    /**
     * `itemLayer` (ItemLayer?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var itemLayer: ItemLayer? = null
    /**
     * `magicLayer` (MagicInfoLayer?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var magicLayer: MagicInfoLayer? = null
    /**
     * `unitInfoLayer` (UnitInfoLayer?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var unitInfoLayer: UnitInfoLayer? = null
    /**
     * `featsLayer` (FeatsLayer?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var featsLayer: FeatsLayer? = null
    /**
     * `featsHelpOpen` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var featsHelpOpen = false

    /**
     * `openItem`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openItem(itemId: Int, level: String, experience: Int, canDrop: Boolean) {
        val profile = catalog.equipmentProfile(itemId) ?: return
        itemDetail = HallItemDetail(
            itemId, level, experience,
            catalog.equipmentExperienceLimit(itemId, level.toIntOrNull() ?: 1),
        )
        itemLayer = ItemLayer(itemId, profile.name, canDrop, object : ItemLayer.Repository {
            /**
             * `discard`: 조건과 입력 상태를 검증한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
             */

            override fun discard(itemId: Int): Boolean = commands.discard(itemId)
        })
    }

    /**
     * `handleInfoTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `handleItemTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `handleMagicTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun handleMagicTap(x: Float, y: Float) {
        val layer = magicLayer ?: return
        if (overlayInput.magicTap(x, y) == HallLayerTapIntent.CLOSE) layer.close(UnitInfoLayer.TOUCH_END)
        if (!layer.attached) magicLayer = null
    }

    /**
     * `openUnitInfo`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `handleUnitInfoTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun handleUnitInfoTap(x: Float, y: Float) {
        val layer = unitInfoLayer ?: return
        when (overlayInput.unitInfoTap(x, y)) {
            HallLayerTapIntent.PRIMARY -> openFeatsFromUnitInfo()
            HallLayerTapIntent.CLOSE -> layer.onCancel(UnitInfoLayer.TOUCH_END)
            else -> Unit
        }
        if (!layer.ref().attached) unitInfoLayer = null
    }

    /**
     * `handleFeatsTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `openFeatsFromUnitInfo`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openFeatsFromUnitInfo() {
        val unitInfo = unitInfoLayer ?: return
        if (!unitInfo.onButton(8, UnitInfoLayer.TOUCH_END)) return
        if (unitInfo.takeRoutes().none { it.route == UnitInfoLayer.Route.FEATS }) return
        featsLayer = FeatsLayer(featsRows(unitInfo.ref().unit))
    }

    /**
     * `openFeatsHelp`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openFeatsHelp() {
        val layer = featsLayer ?: return
        if (layer.onButton(1, FeatsLayer.TOUCH_END) && layer.consumeRoute() == FeatsLayer.Route.HELP) featsHelpOpen = true
    }

    /**
     * `propertyItemIds`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun propertyItemIds(): List<Int> = views.propertyItemIds(propertyTab.ordinal)

    /**
     * `featsRows`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun featsRows(unit: UnitInfoLayer.Unit): List<FeatsLayer.Row> {
        val abilities = if (unit.id == 0) listOf(41, 49, 46, 40, 42)
        else listOf(unit.attack, unit.defense, unit.spirit, unit.critical, unit.morale)
        return FeatsLayer.TITLES.mapIndexed { index, title -> FeatsLayer.Row(title, abilities[index], 0, 100, 127) }
    }
}
