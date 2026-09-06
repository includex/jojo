// Presentation
package com.jojo.game.presentation.title
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.SettingLayer

/** TitleInteraction: 타이틀 메뉴 입력을 해석해 시작·불러오기·설정·종료 화면 동작으로 변환한다. */
object TitleInteraction {

    /**
     * `MainAction`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class MainAction { NEW_GAME, LOAD, SETTINGS, EXIT }
    /**
     * `LoadAction`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    sealed interface LoadAction {
        /**
         * `CloseOverlay`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object CloseOverlay : LoadAction
        /**
         * `ConfirmLoad`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object ConfirmLoad : LoadAction
        /**
         * `CancelConfirmation`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object CancelConfirmation : LoadAction


        /**
         * `SelectVisualRow`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class SelectVisualRow(val index: Int) : LoadAction
    }

    /**
     * `SettingAction`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    sealed interface SettingAction {

        /**
         * `Toggle`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Toggle(val bit: Int) : SettingAction


        /**
         * `TextSpeed`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class TextSpeed(val index: Int) : SettingAction


        /**
         * `NotifyLevel`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class NotifyLevel(val index: Int) : SettingAction


        /**
         * `Background`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Background(val index: Int) : SettingAction


        /**
         * `GameSpeed`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class GameSpeed(val progress: Float) : SettingAction
        /**
         * `Confirm`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Confirm : SettingAction
    }


    /**
     * `Bounds`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Bounds(val left: Int, val bottom: Int, val right: Int, val top: Int) {

        /**
         * `contains`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun contains(x: Int, y: Int) = x in left..right && y in bottom..top
    }

    /**
     * `mainButtons` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val mainButtons = listOf(
        Bounds(945, 462, 1249, 539) to MainAction.NEW_GAME,
        Bounds(945, 354, 1249, 430) to MainAction.LOAD,
        Bounds(945, 245, 1249, 322) to MainAction.SETTINGS,
        Bounds(945, 136, 1249, 213) to MainAction.EXIT,
    )
    /**
     * `loadCancel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val loadCancel = Bounds(897, 87, 1040, 152)
    /**
     * `confirmCancel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val confirmCancel = Bounds(475, 227, 632, 282)
    /**
     * `confirmLoad` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val confirmLoad = Bounds(645, 227, 807, 282)


    /**
     * `mainActionAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun mainActionAt(x: Int, y: Int): MainAction? = mainButtons.firstOrNull { it.first.contains(x, y) }?.second


    /**
     * `loadActionAt`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun loadActionAt(x: Int, y: Int, confirmationOpen: Boolean): LoadAction? {
        if (confirmationOpen) return when {
            confirmLoad.contains(x, y) -> LoadAction.ConfirmLoad
            confirmCancel.contains(x, y) -> LoadAction.CancelConfirmation
            else -> null
        }
        if (loadCancel.contains(x, y)) return LoadAction.CloseOverlay
        if (x !in 245..1035 || y !in 157..517) return null
        return LoadAction.SelectVisualRow(((517 - y) / 45).coerceAtLeast(0))
    }

    /** settingActionAt: 설정 메뉴에서 선택 위치에 대응하는 변경 동작을 반환한다. */
    fun settingActionAt(x: Int, y: Int): SettingAction? {
        if (x in 965..1112 && y in 37..93) return SettingAction.Confirm
        if (x in 180..630) {
            val bit = when (y) {
                in 530..570 -> 0
                in 475..514 -> 1
                in 420..459 -> 4
                in 365..404 -> 5
                in 310..349 -> 6
                else -> null
            }
            if (bit != null) return SettingAction.Toggle(bit)
        }
        if (y in 460..507 && x in 685..1025) return SettingAction.TextSpeed(((x - 685) / 112).coerceIn(0, 2))
        if (y in 235..277 && x in 685..1025) return SettingAction.NotifyLevel(((x - 685) / 112).coerceIn(0, 2))
        if (y in 85..175 && x in 700..1075) return SettingAction.Background(((x - 700) / 94).coerceIn(0, 3))
        if (y in 345..395 && x in 685..1085) return SettingAction.GameSpeed(((x - 700f) / 375f).coerceIn(0f, 1f))
        return null
    }

    /** applySetting: 전달한 설정 변경 동작을 타이틀 화면 상태에 적용한다. */
    fun applySetting(action: SettingAction, layer: SettingLayer): Boolean = when (action) {
        SettingAction.Confirm -> layer.close(SettingLayer.TOUCH_END)
        is SettingAction.Toggle -> {
            val enabled = layer.view().flags and (1 shl action.bit) != 0
            layer.check(action.bit, !enabled)
            false
        }

        is SettingAction.TextSpeed -> {
            layer.check2(0, action.index); false
        }

        is SettingAction.NotifyLevel -> {
            layer.check2(2, action.index); false
        }

        is SettingAction.Background -> {
            layer.selectBackground(action.index); false
        }

        is SettingAction.GameSpeed -> {
            layer.onSlider(action.progress); false
        }
    }


    /**
     * `MainRoutes`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    interface MainRoutes {

        /**
         * `newGame`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun newGame(moduleName: String)


        /**
         * `openLoad`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun openLoad()


        /**
         * `openSettings`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun openSettings()


        /**
         * `requestExit`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun requestExit()
    }

    /** dispatch: 타이틀 입력을 메뉴 선택·설정·장면 전환 결과로 변환한다. */
    fun dispatch(action: MainAction, routes: MainRoutes) = when (action) {
        MainAction.NEW_GAME -> routes.newGame("R_00")
        MainAction.LOAD -> routes.openLoad()
        MainAction.SETTINGS -> routes.openSettings()
        MainAction.EXIT -> routes.requestExit()
    }
}
