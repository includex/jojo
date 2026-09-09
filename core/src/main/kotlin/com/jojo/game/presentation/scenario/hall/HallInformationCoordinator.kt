// Scenario
package com.jojo.game.presentation.scenario.hall
import com.jojo.game.infrastructure.audio.UiSound
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

    fun handleInfoTap(kind: HallInfo, x: Float, y: Float): UiSound? {
        // 원본 `InfoLayer`의 뒤쪽 막에는 깃발이 없어 닫을 때 소리가 나지 않는다.
        // 안쪽의 목록 줄과 갈피는 각자의 창에서 깃발 1이다.
        var sound: UiSound? = UiSound.CLICK
        when (val intent = overlayInput.infoTap(HallInfoInputKind.valueOf(kind.name), x, y)) {
            HallInfoInputIntent.None -> sound = null
            HallInfoInputIntent.Close -> { info = null; sound = null }
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
        return sound
    }

    /**
     * `handleItemTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun handleItemTap(x: Float, y: Float): UiSound? {
        val layer = itemLayer ?: return null
        // 원본 `ItemLayer`는 뒤쪽 막이 깃발 2이고 단추와 확인창은 깃발 1이다.
        val sound = when (itemInput.itemTap(layer.discardConfirmationOpen, x, y)) {
            HallItemInputIntent.DISCARD_YES -> { layer.onDiscardAnswer(1); UiSound.CLICK }
            HallItemInputIntent.DISCARD_NO -> { layer.onDiscardAnswer(0); UiSound.CLICK }
            HallItemInputIntent.CLOSE -> { layer.onButton(0, ItemLayer.TOUCH_END); UiSound.CANCEL }
            HallItemInputIntent.REQUEST_DISCARD -> { layer.onButton(1, ItemLayer.TOUCH_END); UiSound.CLICK }
            HallItemInputIntent.NONE -> null
        }
        if (!layer.attached) {
            itemLayer = null
            itemDetail = null
        }
        return sound
    }

    /**
     * `handleMagicTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun handleMagicTap(x: Float, y: Float): UiSound? {
        val layer = magicLayer ?: return null
        // 원본 `MagicLayer`는 뒤쪽 막이 깃발 2다.
        var sound: UiSound? = null
        if (overlayInput.magicTap(x, y) == HallLayerTapIntent.CLOSE) {
            layer.close(UnitInfoLayer.TOUCH_END); sound = UiSound.CANCEL
        }
        if (!layer.attached) magicLayer = null
        return sound
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

    fun handleUnitInfoTap(x: Float, y: Float): UiSound? {
        val layer = unitInfoLayer ?: return null
        // 원본 `UnitInfoLayer`는 단추가 깃발 1, 뒤쪽 막이 깃발 2다.
        val sound = when (overlayInput.unitInfoTap(x, y)) {
            HallLayerTapIntent.PRIMARY -> { openFeatsFromUnitInfo(); UiSound.CLICK }
            HallLayerTapIntent.CLOSE -> { layer.onCancel(UnitInfoLayer.TOUCH_END); UiSound.CANCEL }
            else -> null
        }
        if (!layer.ref().attached) unitInfoLayer = null
        return sound
    }

    /**
     * `handleFeatsTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun handleFeatsTap(x: Float, y: Float): UiSound? {
        val layer = featsLayer ?: return null
        // 원본 `FeatsLayer`는 뒤쪽 막까지 깃발 1이라 닫을 때도 클릭음이 난다.
        var sound: UiSound? = UiSound.CLICK
        when (overlayInput.featsTap(x, y, featsHelpOpen)) {
            HallLayerTapIntent.PRIMARY -> if (featsHelpOpen) featsHelpOpen = false
            HallLayerTapIntent.SECONDARY -> openFeatsHelp()
            HallLayerTapIntent.CLOSE -> layer.onButton(0, FeatsLayer.TOUCH_END)
            HallLayerTapIntent.CANCEL -> layer.onCancel(FeatsLayer.TOUCH_END)
            HallLayerTapIntent.NONE -> sound = null
        }
        if (!layer.attached) featsLayer = null
        return sound
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
        // 원본은 다섯 줄을 모두 무장의 적성과 모은 공훈에서 계산한다. 예전에는 조조의
        // 적성 다섯을 적어 두고 나머지 세 값은 0·100·127로 고정해 두었는데, 그 값은 갓
        // 시작한 조조에게만 맞는다.
        val progress = catalog.featsProgress(unit.id, campaign)
        return FeatsLayer.TITLES.mapIndexed { index, title ->
            val row = progress.getOrNull(index)
            FeatsLayer.Row(
                title,
                row?.aptitude ?: 0,
                row?.progress ?: 0,
                row?.nextProgress ?: 0,
                row?.nextPhase ?: 0,
            )
        }
    }
}
