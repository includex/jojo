package com.jojo.game.presentation.scenario.hall

import com.jojo.game.domain.campaign.CampaignEquipmentSlot

/** The mutually exclusive command panels displayed by the Hall command bar. */
enum class HallManagement { EQUIP, BUY, SELL }

/** Static information panels entered from the Hall menu. */
enum class HallInfo { FORCES, PROPERTY, TERRAIN, TREASURE, HELPER }

enum class HallPropertyTab { WEAPON, ARMOR, AUXILIARY, PROPERTY }

/** A pending equipment change; applying it remains an explicit user action. */
data class HallEquipConfirmation(
    val values: List<Int>,
    val actionLabel: String,
    val itemId: Int? = null,
    val unequipSlot: CampaignEquipmentSlot? = null,
)

/** The item values that are stable while its detail layer is open. */
data class HallItemDetail(
    val itemId: Int,
    val level: String,
    val experience: Int,
    val experienceLimit: Int,
)
