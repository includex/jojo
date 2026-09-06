// Scenario
package com.jojo.game.presentation.scenario.hall
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.*
import com.jojo.game.application.hall.HallManagementCommandAdapter
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.campaign.CampaignState

/** HallManagementCoordinator: 거점 Management 조정기이며, 사용자 입력과 런타임 상태를 해석해 화면 전환과 오버레이 처리를 조정한다. */
internal class HallManagementCoordinator(
    /** `campaign` (CampaignState): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val campaign: CampaignState,
    /** `catalog` (GameDataCatalog): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val catalog: GameDataCatalog,
    /** `interaction` (HallInteractionController): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val interaction: HallInteractionController,
    /** `commands` (HallManagementCommandAdapter): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val commands: HallManagementCommandAdapter,
    val views: HallManagementViewFactory,
) {
    /**
     * `input` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val input = HallManagementInteractionController()
    /**
     * `confirmations` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val confirmations = EquipConfirmationFlow(campaign, catalog)

    /**
     * `management` (HallManagement?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var management: HallManagement? = null
    /**
     * `notice` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var notice: String? = null
    /**
     * `equipUnitIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var equipUnitIndex = 0
    /**
     * `unequipConfirmationOpen` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var unequipConfirmationOpen = false
    /**
     * `unitListLayer` (HallUnitListLayer?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var unitListLayer: HallUnitListLayer? = null
    /**
     * `equipConfirmation` (HallEquipConfirmation?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var equipConfirmation: HallEquipConfirmation? = null
    /**
     * `exclusiveLayer` (ExclusiveLayer?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var exclusiveLayer: ExclusiveLayer? = null

    /**
     * `equipUnitIds`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun equipUnitIds(): List<Int> = campaign.joinedUnits.toList().ifEmpty { listOf(0) }

    /**
     * `equipUnitId`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun equipUnitId(): Int {
        val units = equipUnitIds()
        equipUnitIndex = ((equipUnitIndex % units.size) + units.size) % units.size
        return units[equipUnitIndex]
    }

    /**
     * `prepareDefaultEquipment`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun prepareDefaultEquipment(kind: HallManagement) {
        val unitId = if (kind == HallManagement.EQUIP) equipUnitId() else campaign.joinedUnits.firstOrNull() ?: 0
        campaign.inventory.ensureDefaultEquipment(unitId, catalog)
    }

    /**
     * `prepareForcesDefaultEquipment`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun prepareForcesDefaultEquipment() {
        campaign.joinedUnits.forEach { campaign.inventory.ensureDefaultEquipment(it, catalog) }
    }

    /**
     * `open`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun open(kind: HallManagement) {
        management = kind
        prepareDefaultEquipment(kind)
    }

    /**
     * `close`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun close() {
        management = null
        notice = null
        unequipConfirmationOpen = false
        unitListLayer = null
    }

    /**
     * `handleTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun handleTap(kind: HallManagement, x: Float, y: Float) {
        val unitId = if (kind == HallManagement.EQUIP) equipUnitId() else campaign.joinedUnits.firstOrNull() ?: 0
        when (kind) {
            HallManagement.EQUIP -> handleEquipTap(unitId, x, y)
            HallManagement.BUY -> handleBuyTap(x, y)
            HallManagement.SELL -> handleSellTap(x, y)
        }
    }

    /**
     * `handleEquipTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun handleEquipTap(unitId: Int, x: Float, y: Float) {
        equipConfirmation?.let { confirmation ->
            when (input.equipConfirmationTap(x, y)) {
                HallEquipConfirmationInputIntent.CONFIRM -> {
                    val changed = if (confirmation.itemId != null || confirmation.unequipSlot != null)
                        confirmations.answer(unitId, accept = true) else false
                    if (changed) notice = if (confirmation.actionLabel == "해제") "장비를 해제했습니다." else "장비를 변경했습니다."
                }
                HallEquipConfirmationInputIntent.CANCEL -> confirmations.cancel()
            }
            equipConfirmation = null
            return
        }
        if (unequipConfirmationOpen) {
            when (input.unequipConfirmationTap(x, y)) {
                HallUnequipConfirmationInputIntent.CONFIRM -> {
                    val count = commands.unequipAll()
                    unequipConfirmationOpen = false
                    notice = if (count == 0) "해제할 장비가 없습니다." else "장비 ${count}개를 모두 해제했습니다."
                }
                HallUnequipConfirmationInputIntent.CANCEL -> unequipConfirmationOpen = false
                HallUnequipConfirmationInputIntent.NONE -> Unit
            }
            return
        }
        unitListLayer?.let { unitList ->
            val row = if (x in (924.186f * .86f)..(1284.186f * .86f)) {
                (0 until unitList.rows.size.coerceAtMost(6)).firstOrNull { index ->
                    y in ((607f - index * 52f) * .86f)..((657f - index * 52f) * .86f)
                }
            } else null
            row?.let { unitList.onRow(it, HallUnitListLayer.TOUCH_END) }?.let { selectedId ->
                equipUnitIndex = equipUnitIds().indexOf(selectedId)
                prepareDefaultEquipment(HallManagement.EQUIP)
            }
            if (unitList.attached) unitList.onCancel(HallUnitListLayer.TOUCH_END)
            unitListLayer = null
            notice = null
            return
        }
        when (val intent = input.equipTap(x, y)) {
            is HallEquipInputIntent.SelectTab -> {
                interaction.selectEquipTab(intent.index)
                notice = null
            }
            HallEquipInputIntent.OpenExclusive -> {
                exclusiveLayer = EquipExclusiveRoute.openFromInformationButton(ExclusiveLayer.TOUCH_END)
                notice = null
            }
            HallEquipInputIntent.RequestUnequipConfirmation -> {
                unequipConfirmationOpen = true
                notice = null
            }
            HallEquipInputIntent.PreviousUnit -> {
                equipUnitIndex--
                prepareDefaultEquipment(HallManagement.EQUIP)
                notice = null
            }
            HallEquipInputIntent.NextUnit -> {
                equipUnitIndex++
                prepareDefaultEquipment(HallManagement.EQUIP)
                notice = null
            }
            HallEquipInputIntent.OpenUnitList -> {
                unitListLayer = HallUnitListLayer(equipUnitIds())
                notice = null
            }
            HallEquipInputIntent.RequestWeaponUnequip -> confirmations.requestUnequip(unitId, CampaignEquipmentSlot.WEAPON)?.let { preview ->
                equipConfirmation = HallEquipConfirmation(preview.values, preview.actionLabel, unequipSlot = preview.unequipSlot)
            }
            is HallEquipInputIntent.RequestEquipmentRow -> {
                val itemId = views.equipInventory(interaction.view.equipTabIndex).getOrNull(intent.row)?.itemId ?: return
                val preview = confirmations.requestEquip(unitId, itemId)
                if (preview == null) notice = "이 물품은 장착할 수 없습니다."
                else {
                    equipConfirmation = HallEquipConfirmation(preview.values, preview.actionLabel, itemId = preview.itemId)
                    notice = null
                }
            }
            HallEquipInputIntent.None -> Unit
        }
    }

    /**
     * `handleBuyTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun handleBuyTap(x: Float, y: Float) {
        when (val intent = input.buyTap(x, y, interaction.view.buyTabIndex)) {
            is HallBuyInputIntent.SelectTab -> {
                interaction.selectBuyTab(intent.index)
                notice = null
            }
            is HallBuyInputIntent.Row -> {
                val item = if (interaction.view.buyTabIndex == 0) views.buyCandidates().getOrNull(intent.index)
                else views.buyProperties().getOrNull(intent.index)
                item?.let { notice = commands.buy(it.id).message }
            }
            HallBuyInputIntent.None -> Unit
        }
    }

    /**
     * `handleSellTap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun handleSellTap(x: Float, y: Float) {
        when (val intent = input.sellTap(x, y)) {
            is HallSellInputIntent.SelectTab -> {
                interaction.selectSellTab(intent.index)
                notice = null
            }
            is HallSellInputIntent.Cell -> views.sellCandidates(interaction.view.sellTabIndex)
                .getOrNull(intent.row * 2 + intent.column)
                ?.let { notice = commands.sell(it.itemId).message }
            HallSellInputIntent.None -> Unit
        }
    }
}
