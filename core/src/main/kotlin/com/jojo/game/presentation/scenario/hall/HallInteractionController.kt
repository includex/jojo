// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallInteractionView: 거점 Interaction 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallInteractionView(
    val menuOpen: Boolean,
    val equipTabIndex: Int,
    val buyTabIndex: Int,
    val sellTabIndex: Int,
)

/**
 * `HallInteractionIntent`: 관련 상태와 동작을 묶는 interface다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal sealed interface HallInteractionIntent {
    /**
     * `None`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object None : HallInteractionIntent
    /**
     * `MenuClosed`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object MenuClosed : HallInteractionIntent
    /**
     * `OpenMenu`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object OpenMenu : HallInteractionIntent
    /**
     * `StartBattle`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object StartBattle : HallInteractionIntent
    /**
     * `OpenManagement`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class OpenManagement(val kind: ManagementKind) : HallInteractionIntent
    /**
     * `MenuSelection`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class MenuSelection(val index: Int) : HallInteractionIntent

    /**
     * `ManagementKind`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class ManagementKind { EQUIP, BUY, SELL }
}

/** HallInteractionController: 거점 Interaction 제어기이며, 사용자 입력과 런타임 상태를 해석해 화면 전환과 오버레이 처리를 조정한다. */
internal class HallInteractionController {
    /**
     * `menuOpen` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var menuOpen = false
    /**
     * `equipTabIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var equipTabIndex = 1
    /**
     * `buyTabIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var buyTabIndex = 0
    /**
     * `sellTabIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var sellTabIndex = 0

    /**
     * `view` (HallInteractionView): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val view: HallInteractionView
        get() = HallInteractionView(menuOpen, equipTabIndex, buyTabIndex, sellTabIndex)

    /**
     * `openMenu`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openMenu() {
        menuOpen = true
    }

    /**
     * `closeMenu`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun closeMenu(): Boolean {
        if (!menuOpen) return false
        menuOpen = false
        return true
    }

    /**
     * `mainTap`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `selectEquipTabAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun selectEquipTabAt(x: Float, y: Float): Boolean {
        if (y !in 566f..610f || x !in 123f..639f) return false
        selectEquipTab(((x - 123f) / 129f).toInt())
        return true
    }

    /**
     * `selectEquipTab`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun selectEquipTab(index: Int) {
        if (index in 0 until EQUIP_TAB_COUNT) equipTabIndex = index
    }

    /**
     * `selectBuyTabAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun selectBuyTabAt(x: Float, y: Float): Boolean {
        if (y !in 521f..566f) return false
        when (x) {
            in 183f..338f -> 0
            in 338f..493f -> 1
            else -> null
        }?.let(::selectBuyTab)
        return true
    }

    /**
     * `selectBuyTab`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun selectBuyTab(index: Int) {
        if (index in 0..1) buyTabIndex = index
    }

    /**
     * `selectSellTabAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun selectSellTabAt(x: Float, y: Float): Boolean {
        if (y !in 75f..128f) return false
        when (x) {
            in 523f..695f -> 0
            in 695f..867f -> 1
            else -> null
        }?.let(::selectSellTab)
        return true
    }

    /**
     * `selectSellTab`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun selectSellTab(index: Int) {
        if (index in 0..1) sellTabIndex = index
    }

    /**
     * `menuSelection`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun menuSelection(x: Float, y: Float): HallInteractionIntent {
        if (y !in 44.3f..120f) return HallInteractionIntent.None
        val centers = floatArrayOf(47.39f, 123.29f, 199.39f, 275.84f, 364.05f, 439.95f, 516.05f, 593.78f, 678.92f)
        val index = centers.indexOfFirst { kotlin.math.abs(x - it) <= 37.84f }
        if (index < 0) return HallInteractionIntent.None
        menuOpen = false
        return HallInteractionIntent.MenuSelection(index)
    }

    private companion object {
        /**
         * `MENU_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MENU_BOTTOM = 125.56f
        /**
         * `EQUIP_TAB_COUNT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val EQUIP_TAB_COUNT = 4
    }
}
