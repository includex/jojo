package com.jojo.game.presentation.scenario.hall

import com.jojo.game.*
import com.jojo.game.application.hall.HallManagementCommandAdapter
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.campaign.CampaignState

/** Owns Hall equipment/shop state and translates their pointer intents to campaign commands. */
internal class HallManagementCoordinator(
    private val campaign: CampaignState,
    private val catalog: GameDataCatalog,
    private val interaction: HallInteractionController,
    private val commands: HallManagementCommandAdapter,
    val views: HallManagementViewFactory,
) {
    private val input = HallManagementInteractionController()
    private val confirmations = EquipConfirmationFlow(campaign, catalog)

    var management: HallManagement? = null
    var notice: String? = null
    var equipUnitIndex = 0
    var unequipConfirmationOpen = false
    var unitListLayer: HallUnitListLayer? = null
    var equipConfirmation: HallEquipConfirmation? = null
    var exclusiveLayer: ExclusiveLayer? = null

    fun equipUnitIds(): List<Int> = campaign.joinedUnits.toList().ifEmpty { listOf(0) }

    fun equipUnitId(): Int {
        val units = equipUnitIds()
        equipUnitIndex = ((equipUnitIndex % units.size) + units.size) % units.size
        return units[equipUnitIndex]
    }

    fun prepareDefaultEquipment(kind: HallManagement) {
        val unitId = if (kind == HallManagement.EQUIP) equipUnitId() else campaign.joinedUnits.firstOrNull() ?: 0
        campaign.inventory.ensureDefaultEquipment(unitId, catalog)
    }

    fun prepareForcesDefaultEquipment() {
        campaign.joinedUnits.forEach { campaign.inventory.ensureDefaultEquipment(it, catalog) }
    }

    fun open(kind: HallManagement) {
        management = kind
        prepareDefaultEquipment(kind)
    }

    fun close() {
        management = null
        notice = null
        unequipConfirmationOpen = false
        unitListLayer = null
    }

    fun handleTap(kind: HallManagement, x: Float, y: Float) {
        val unitId = if (kind == HallManagement.EQUIP) equipUnitId() else campaign.joinedUnits.firstOrNull() ?: 0
        when (kind) {
            HallManagement.EQUIP -> handleEquipTap(unitId, x, y)
            HallManagement.BUY -> handleBuyTap(x, y)
            HallManagement.SELL -> handleSellTap(x, y)
        }
    }

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
