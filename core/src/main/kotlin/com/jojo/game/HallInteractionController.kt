package com.jojo.game

/** Immutable hall-input projection for rendering and evidence consumers. */
internal data class HallInteractionView(
    val menuOpen: Boolean,
    val equipTabIndex: Int,
    val buyTabIndex: Int,
    val sellTabIndex: Int,
)

internal sealed interface HallInteractionIntent {
    data object None : HallInteractionIntent
    data object MenuClosed : HallInteractionIntent
    data object OpenMenu : HallInteractionIntent
    data object StartBattle : HallInteractionIntent
    data class OpenManagement(val kind: ManagementKind) : HallInteractionIntent
    data class MenuSelection(val index: Int) : HallInteractionIntent

    enum class ManagementKind { EQUIP, BUY, SELL }
}

/** Owns hall menu/tab selection and translates raw coordinates into intents. */
internal class HallInteractionController {
    private var menuOpen = false
    private var equipTabIndex = 1
    private var buyTabIndex = 0
    private var sellTabIndex = 0

    val view: HallInteractionView
        get() = HallInteractionView(menuOpen, equipTabIndex, buyTabIndex, sellTabIndex)

    fun openMenu() {
        menuOpen = true
    }

    fun closeMenu(): Boolean {
        if (!menuOpen) return false
        menuOpen = false
        return true
    }

    fun mainTap(x: Float, y: Float): HallInteractionIntent {
        if (menuOpen) {
            if (y > MENU_BOTTOM) return HallInteractionIntent.MenuClosed.also { menuOpen = false }
            return menuSelection(x, y)
        }
        return when {
            x in 31f..82.6f && y in 318.2f..369.8f -> HallInteractionIntent.OpenMenu.also { menuOpen = true }
            x in 895.58f..978.14f && y in 1.72f..84.28f -> HallInteractionIntent.StartBattle
            x in 978.14f..1060.70f && y in 1.72f..84.28f -> HallInteractionIntent.OpenManagement(HallInteractionIntent.ManagementKind.EQUIP)
            x in 1060.70f..1143.26f && y in 1.72f..84.28f -> HallInteractionIntent.OpenManagement(HallInteractionIntent.ManagementKind.BUY)
            x in 1143.26f..1225.82f && y in 1.72f..84.28f -> HallInteractionIntent.OpenManagement(HallInteractionIntent.ManagementKind.SELL)
            else -> HallInteractionIntent.None
        }
    }

    fun selectEquipTabAt(x: Float, y: Float): Boolean {
        if (y !in 566f..610f || x !in 123f..639f) return false
        val index = ((x - 123f) / 129f).toInt()
        if (index in 0 until EQUIP_TAB_COUNT) equipTabIndex = index
        return true
    }

    fun selectBuyTabAt(x: Float, y: Float): Boolean {
        if (y !in 521f..566f) return false
        when (x) {
            in 183f..338f -> 0
            in 338f..493f -> 1
            else -> null
        }?.let { buyTabIndex = it }
        return true
    }

    fun selectSellTabAt(x: Float, y: Float): Boolean {
        if (y !in 75f..128f) return false
        when (x) {
            in 523f..695f -> 0
            in 695f..867f -> 1
            else -> null
        }?.let { sellTabIndex = it }
        return true
    }

    private fun menuSelection(x: Float, y: Float): HallInteractionIntent {
        if (y !in 44.3f..120f) return HallInteractionIntent.None
        val centers = floatArrayOf(47.39f, 123.29f, 199.39f, 275.84f, 364.05f, 439.95f, 516.05f, 593.78f, 678.92f)
        val index = centers.indexOfFirst { kotlin.math.abs(x - it) <= 37.84f }
        if (index < 0) return HallInteractionIntent.None
        menuOpen = false
        return HallInteractionIntent.MenuSelection(index)
    }

    private companion object {
        const val MENU_BOTTOM = 125.56f
        const val EQUIP_TAB_COUNT = 4
    }
}
