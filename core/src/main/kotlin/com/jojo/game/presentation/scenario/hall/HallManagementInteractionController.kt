package com.jojo.game.presentation.scenario.hall

internal sealed interface HallEquipInputIntent {
    data object None : HallEquipInputIntent
    data class SelectTab(val index: Int) : HallEquipInputIntent
    data object OpenExclusive : HallEquipInputIntent
    data object RequestUnequipConfirmation : HallEquipInputIntent
    data object PreviousUnit : HallEquipInputIntent
    data object NextUnit : HallEquipInputIntent
    data object OpenUnitList : HallEquipInputIntent
    data object RequestWeaponUnequip : HallEquipInputIntent
    data class RequestEquipmentRow(val row: Int) : HallEquipInputIntent
}

internal sealed interface HallBuyInputIntent {
    data object None : HallBuyInputIntent
    data class SelectTab(val index: Int) : HallBuyInputIntent
    data class Row(val index: Int) : HallBuyInputIntent
}

internal sealed interface HallSellInputIntent {
    data object None : HallSellInputIntent
    data class SelectTab(val index: Int) : HallSellInputIntent
    data class Cell(val row: Int, val column: Int) : HallSellInputIntent
}

internal enum class HallEquipConfirmationInputIntent { CONFIRM, CANCEL }
internal enum class HallUnequipConfirmationInputIntent { NONE, CONFIRM, CANCEL }
internal enum class HallItemInputIntent { NONE, CLOSE, REQUEST_DISCARD, DISCARD_YES, DISCARD_NO }

/**
 * Owns Hall-management and item-layer hit testing. The returned intents deliberately
 * contain no screen, campaign, rendering, or layer references.
 */
internal class HallManagementInteractionController {
    fun equipTap(x: Float, y: Float): HallEquipInputIntent = when {
        y in 566f..610f && x in 123f..639f -> HallEquipInputIntent.SelectTab(((x - 123f) / 129f).toInt())
        x in 125f..212f && y in 37f..82f -> HallEquipInputIntent.OpenExclusive
        x in 493f..642f && y in 37f..82f -> HallEquipInputIntent.RequestUnequipConfirmation
        x in 842f..995f && y in 37f..82f -> HallEquipInputIntent.PreviousUnit
        x in 995f..1148f && y in 37f..82f -> HallEquipInputIntent.NextUnit
        x in 820f..1125f && y in 566f..610f -> HallEquipInputIntent.OpenUnitList
        x in 745f..1149f && y in 85f..151f -> HallEquipInputIntent.RequestWeaponUnequip
        x in 124f..729f -> HallEquipInputIntent.RequestEquipmentRow(((529f - y) / 68f).toInt())
        else -> HallEquipInputIntent.None
    }

    fun buyTap(x: Float, y: Float, buyTab: Int): HallBuyInputIntent = when {
        y in 521f..566f && x in 183f..338f -> HallBuyInputIntent.SelectTab(0)
        y in 521f..566f && x in 338f..493f -> HallBuyInputIntent.SelectTab(1)
        y in 521f..566f -> HallBuyInputIntent.None
        x !in 176f..657f -> HallBuyInputIntent.None
        buyTab == 0 && y in 118f..577f -> HallBuyInputIntent.Row(((522.16f - y) / 153.08f).toInt())
        buyTab != 0 && y in 132f..563f -> HallBuyInputIntent.Row(((562.64f - y) / 108f).toInt())
        else -> HallBuyInputIntent.None
    }

    fun sellTap(x: Float, y: Float): HallSellInputIntent = when {
        y in 75f..128f && x in 523f..695f -> HallSellInputIntent.SelectTab(0)
        y in 75f..128f && x in 695f..867f -> HallSellInputIntent.SelectTab(1)
        x in 271f..1009f && y in 182f..495f -> HallSellInputIntent.Cell(
            row = ((495f - y) / 157f).toInt(),
            column = if (x >= 636f) 1 else 0,
        )
        else -> HallSellInputIntent.None
    }

    fun unequipConfirmationTap(x: Float, y: Float): HallUnequipConfirmationInputIntent = when {
        x in 439f..623f && y in 291f..334f -> HallUnequipConfirmationInputIntent.CONFIRM
        x in 657f..841f && y in 291f..334f -> HallUnequipConfirmationInputIntent.CANCEL
        else -> HallUnequipConfirmationInputIntent.NONE
    }

    fun equipConfirmationTap(x: Float, y: Float): HallEquipConfirmationInputIntent =
        if (x in 472.30f..601.30f && y in 216.63f..259.63f) HallEquipConfirmationInputIntent.CONFIRM
        else HallEquipConfirmationInputIntent.CANCEL

    fun itemTap(discardOpen: Boolean, x: Float, y: Float): HallItemInputIntent {
        val sourceX = x / .86f
        val sourceY = y / .86f
        if (discardOpen) {
            return when {
                sourceX in 554.186f..734.186f && sourceY in 271.285f..321.285f -> HallItemInputIntent.DISCARD_YES
                sourceX in 754.186f..934.186f && sourceY in 271.285f..321.285f -> HallItemInputIntent.DISCARD_NO
                else -> HallItemInputIntent.NONE
            }
        }
        return when {
            sourceX in 1065.827f..1215.827f && sourceY in 97.824f..147.824f -> HallItemInputIntent.CLOSE
            sourceX in 901.312f..1051.312f && sourceY in 97.824f..147.824f -> HallItemInputIntent.REQUEST_DISCARD
            else -> HallItemInputIntent.NONE
        }
    }
}
